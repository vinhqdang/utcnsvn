/**
 * 
 */
package ivc.data.commands;

import ivc.connection.ConnectionManager;
import ivc.data.IVCProject;
import ivc.data.Peer;
import ivc.data.OperationHistory;
import ivc.data.OperationHistoryList;
import ivc.data.exception.Exceptions;
import ivc.manager.ProjectsManager;
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

		// if the user tries to commit the entire project must get the changed files
		if (filePaths == null || filePaths.isEmpty()) {
			getChangedFiles();
			if (filePaths == null || filePaths.isEmpty()) {
				return new Result(true, Exceptions.COMMIT_NOFILE_CHANGED, null);
			}
		}

		if (!checkVersion()) {
			return new Result(false, Exceptions.FILE_OUT_OF_SYNC, null);
		}

		try {
			connectionManager.getServer().updateHeadVersion(ivcProject.getServerPath(), changedFiles);
		} catch (RemoteException e) {
			return new Result(false, Exceptions.SERVER_UPDATE_HEADVERSION_FAILED, e);
		}
		updateCurrentVersion();
		updateRCLFiles();
		updatePendingRUL();
		cleanLL();
		return new Result(true, "Success", null);
	}

	private void getChangedFiles() {
		// added files, folders; removed files, folders, modified files
		changedFiles = ivcProject.getLocalLog();
		// TODO: 1.added removed, modified files outside eclipse
	}

	private boolean checkVersion() {
		// rcl must be empty and also version on server same as local version of
		// the file
		OperationHistoryList rcl = ivcProject.getRemoteCommitedLog();
		if (rcl != null || !rcl.getTransformationHist().isEmpty()) {
			return false;
		}
		try {
			currentCommitedVersion = (HashMap) connectionManager.getServer().getVersionNumber(ivcProject.getServerPath());
			Iterator<OperationHistory> it = changedFiles.iterator();
			while (it.hasNext()) {
				OperationHistory th = it.next();
				String filePath = th.getFilePath();
				if (!th.getTransformations().isEmpty()) {
					Integer localVersion = th.getTransformations().get(0).getFileVersion();
					Integer commitedVersion = currentCommitedVersion.get(filePath);
					if (localVersion < commitedVersion) {
						return false;
					}
				}
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	private void updateCurrentVersion() {
		try {
			// update head version on server
			connectionManager.getServer().updateHeadVersion(ivcProject.getServerPath(), changedFiles);
			HashMap<String, Integer> localVersion = ivcProject.getLocalVersion();
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
			Integer serverProjNo = currentCommitedVersion.get(projectName);
			serverProjNo++;
			currentCommitedVersion.put(projectName, serverProjNo);

			// save new changes
			ivcProject.setCurrentVersion(localVersion);
			connectionManager.getServer().updateVersionNumber(ivcProject.getServerPath(), currentCommitedVersion);

		} catch (RemoteException e) {
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
				if (!connectionManager.getPeerHosts().contains(peer.getHostAddress())) {
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
		OperationHistoryList ll = new OperationHistoryList();
		ivcProject.setLocalLog(ll);
	}

	private void updatePendingRUL() {
		// notify peers that are not on line
		try {
			List<Peer> all = connectionManager.getServer().getAllClientHosts(ivcProject.getServerPath());
			List<String> disconnected = new ArrayList<String>();
			Iterator<Peer> itp = all.iterator();
			while (itp.hasNext()) {
				Peer peer = itp.next();
				if (!connectionManager.getPeerHosts().contains(peer.getHostAddress())) {
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
