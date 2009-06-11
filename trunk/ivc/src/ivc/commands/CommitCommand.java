/**
 * 
 */
package ivc.commands;

import ivc.data.IVCProject;
import ivc.data.Peer;
import ivc.data.exception.Exceptions;
import ivc.data.operation.OperationHistory;
import ivc.data.operation.OperationHistoryList;
import ivc.managers.ConnectionManager;
import ivc.managers.ProjectsManager;
import ivc.rmi.client.ClientIntf;
import ivc.util.Constants;
import ivc.util.FileUtils;
import ivc.util.NetworkUtils;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author danielan
 * 
 */
public class CommitCommand implements CommandIntf {

	private String projectName;
	private List<String> filePaths;
	private IVCProject ivcProject;

	private OperationHistoryList changedFiles;
	private HashMap<String, Integer> currentCommitedVersion;
	private ConnectionManager connectionManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see command.CommandIntf#execute(data.CommandArgs)
	 */
	@Override
	// add version 1; delete version remains
	public Result execute(CommandArgs args) {
		// init local fields

		projectName = (String) args.getArgumentValue(Constants.PROJECT_NAME);
		filePaths = (List<String>) args.getArgumentValue(Constants.FILE_PATHS);

		ivcProject = ProjectsManager.instance().getIVCProjectByName(projectName);
		connectionManager = ConnectionManager.getInstance(projectName);

		// if the user tries to commit the entire project must get only the changed files
		getChangedFiles();
		if (changedFiles == null || changedFiles.getOperationHist().isEmpty()) {
			return new Result(true, Exceptions.COMMIT_NOFILE_CHANGED, null);
		}
		// we must check if what the user commits contains latest commited changes
		if (!checkVersion()) {
			return new Result(false, Exceptions.FILE_OUT_OF_SYNC, null);
		}
		try {
			// send commited changes to the server
			//TODO 1. add newly added files to base version 
			connectionManager.getServer().updateHeadVersion(ivcProject.getServerPath(), changedFiles);
		} catch (Exception e) {
			return new Result(false, Exceptions.SERVER_UPDATE_HEADVERSION_FAILED, e);
		}
		// increment file versions
		updateCurrentVersion();
		// update RCL for all hosts interested in the project
		updateRCLFiles();
		// refresh uncommited changes from the pending rul files from the server
		updatePendingRUL();
		// clean local log as the operations in it are now commited
		cleanLL();
		//refreshing the decorations
		
		return new Result(true, "Success", null);
	}

	private void getChangedFiles() {
		changedFiles = new OperationHistoryList();
		// added files, folders; removed files, folders, modified files
		OperationHistoryList ll = ivcProject.getLocalLog();
		if (!ll.getOperationHist().isEmpty() && filePaths != null && !filePaths.isEmpty()) {
			Iterator<String> it = filePaths.iterator();
			while (it.hasNext()) {
				String filePath = it.next();
				if (ll.getOperationHistForFile(filePath) != null) {
					OperationHistory oh = ll.getOperationHistForFile(filePath);
					changedFiles.appendOperationHistory(oh);
				}
			}
		}else{
		// TODO: 1. handle add and remove files ca alex nu a avut chef sa faca o transformare
			changedFiles.appendOperationHistoryList(ll);
		}

	}

	private boolean checkVersion() {
		// rcl must be empty and also version on server same as local version of
		// the file
		OperationHistoryList rcl = ivcProject.getRemoteCommitedLog();
		if (rcl != null && !rcl.getOperationHist().isEmpty()) {
			if (filePaths == null || filePaths.isEmpty()) {
				return false;
			}
			Iterator<String> it = filePaths.iterator();
			while (it.hasNext()) {
				String filePath = it.next();
				if (rcl.getOperationHistForFile(filePath) != null && !rcl.getOperationHistForFile(filePath).getOperations().isEmpty()) {
					return false;
				}
			}
		}
		try {
			currentCommitedVersion = (HashMap) connectionManager.getServer().getVersionNumber(ivcProject.getServerPath());
			Iterator<OperationHistory> it = changedFiles.iterator();
			while (it.hasNext()) {
				OperationHistory oh = it.next();
				String filePath = oh.getFilePath();
				if (!oh.getOperations().isEmpty()) {
					Integer localVersion = oh.getOperations().getLast().getFileVersion();
					Integer commitedVersion = currentCommitedVersion.get(filePath);
					if (commitedVersion != null) {
						if (localVersion < commitedVersion) {
							return false;
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	private void updateCurrentVersion() {
		try {
			HashMap<String, Integer> localVersion = ivcProject.getCurrentVersion();
			Iterator<OperationHistory> it = changedFiles.iterator();
			// increment version numbers
			while (it.hasNext()) {
				OperationHistory th = it.next();
				String filePath = th.getFilePath();
				// update current version number
				Integer localNo = localVersion.get(filePath);
				if (localNo == null) {
					localNo = 0;
				}
				localNo++;
				Integer serverNo = currentCommitedVersion.get(filePath);
				if (serverNo == null) {
					serverNo = 0;
				}
				serverNo++;
				currentCommitedVersion.put(filePath, serverNo);
				localVersion.put(filePath, serverNo);
			}
			// update project version
			Integer localProjNo = localVersion.get(projectName);
			localProjNo++;
			localVersion.put(projectName, localProjNo);
			Integer serverProjNo = currentCommitedVersion.get(ivcProject.getServerPath());
			serverProjNo++;
			currentCommitedVersion.put(ivcProject.getServerPath(), serverProjNo);

			// save new changes
			ivcProject.setCurrentVersion(localVersion);
			connectionManager.getServer().updateVersionNumber(ivcProject.getServerPath(), currentCommitedVersion);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void updateRCLFiles() {
		List<ClientIntf> peers = connectionManager.getPeers();
		if (peers == null) {
			return;
		}
		Iterator<ClientIntf> it = peers.iterator();
		while (it.hasNext()) {
			ClientIntf peer = it.next();
			try {
				peer.updateRCL(ivcProject.getServerPath(), NetworkUtils.getHostAddress(), changedFiles);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		// notify peers that are not on line
		try {
			List<Peer> all = connectionManager.getServer().getAllClientHosts(ivcProject.getServerPath());
			List<String> disconnected = new ArrayList<String>();
			Iterator<Peer> itp = all.iterator();
			while (itp.hasNext()) {
				Peer peer = itp.next();
				if (!connectionManager.getPeerHosts().contains(peer.getHostAddress()) && !peer.getHostAddress().equalsIgnoreCase(NetworkUtils.getHostAddress())) {
					disconnected.add(peer.getHostAddress());
				}
			}
			connectionManager.getServer().updatePendingRCL(ivcProject.getServerPath(), disconnected, changedFiles);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void cleanLL() {
		ivcProject.setLocalLog(new OperationHistoryList());
	}

	private void updatePendingRUL() {
		// notify peers that are not on line
		try {
			List<Peer> all = connectionManager.getServer().getAllClientHosts(ivcProject.getServerPath());
			List<String> disconnected = new ArrayList<String>();
			Iterator<Peer> itp = all.iterator();
			while (itp.hasNext()) {
				Peer peer = itp.next();
				if (!connectionManager.getPeerHosts().contains(peer.getHostAddress()) && !peer.getHostAddress().equalsIgnoreCase(NetworkUtils.getHostAddress())) {
					disconnected.add(peer.getHostAddress());
				}
			}
			connectionManager.getServer().updatePendingRUL(ivcProject.getServerPath(), NetworkUtils.getHostAddress(), disconnected, changedFiles);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
