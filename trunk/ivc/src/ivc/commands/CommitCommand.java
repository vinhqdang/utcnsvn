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
import ivc.util.NetworkUtils;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.team.ui.TeamOperation;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author danielan
 * 
 */
public class CommitCommand extends TeamOperation {

	private String projectName;
	private List<String> filePaths;
	private IVCProject ivcProject;
	private CommandArgs args;
	private OperationHistoryList changedFiles;
	private HashMap<String, Integer> currentCommitedVersion;
	private ConnectionManager connectionManager;
	private Result result;
	private IProgressMonitor monitor;

	public CommitCommand(IWorkbenchPart part, CommandArgs args) {
		super(part);
		this.args = args;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see command.CommandIntf#execute(data.CommandArgs)
	 */
	@SuppressWarnings("unchecked")
	@Override
	// add version 1; delete version remains
	public void run(IProgressMonitor monitor) {
		// init local fields
		this.monitor = monitor;
		monitor.beginTask("Commit", 4);
		projectName = (String) args.getArgumentValue(Constants.PROJECT_NAME);
		filePaths = (List<String>) args.getArgumentValue(Constants.FILE_PATHS);

		ivcProject = ProjectsManager.instance().getIVCProjectByName(projectName);
		connectionManager = ConnectionManager.getInstance(projectName);

		// if the user tries to commit the entire project must get only the changed files
		getChangedFiles();
		if (changedFiles == null || changedFiles.getOperationHist().isEmpty()) {
			result = new Result(true, Exceptions.COMMIT_NOFILE_CHANGED, null);
			return;
		}
		// we must check if what the user commits contains latest commited changes
		if (!checkVersion()) {
			result = new Result(false, Exceptions.FILE_OUT_OF_SYNC, null);
			return;
		}
		try {
			// send commited changes to the server
			// TODO 1. add newly added files to base version
			connectionManager.getServer().updateHeadVersion(ivcProject.getServerPath(), changedFiles);

			// increment file versions
			monitor.setTaskName("Updating current version");
			updateCurrentVersion();
			monitor.internalWorked(1);

			// update RCL for all hosts interested in the project
			monitor.setTaskName("Updating Remote Commited Log");
			updateRCLFiles();
			monitor.internalWorked(1);
			// refresh uncommited changes from the pending rul files from the server
			monitor.setTaskName("Updating Pending Remote Uncommited Log");
			updatePendingRUL();
			monitor.internalWorked(1);
			// clean local log as the operations in it are now commited
			monitor.setTaskName("Cleaning Local Log");
			cleanLL();
			monitor.done();
			// refreshing the decorations
		} catch (Exception e) {
			result = new Result(false, e.getMessage(), e);
			return;
		}
		result = new Result(true, "Success", null);
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
		} else {
			// TODO: 1. handle add and remove files
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
			currentCommitedVersion = (HashMap<String, Integer>) connectionManager.getServer().getVersionNumber(ivcProject.getServerPath());
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
				if (!connectionManager.getPeerHosts().contains(peer.getHostAddress())
						&& !peer.getHostAddress().equalsIgnoreCase(NetworkUtils.getHostAddress())) {
					disconnected.add(peer.getHostAddress());
				}
			}
			connectionManager.getServer().updatePendingRCL(ivcProject.getServerPath(), disconnected, changedFiles);
		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	private void cleanLL() {
		ivcProject.setLocalLog(new OperationHistoryList());
	}

	private void updatePendingRUL() throws RemoteException {
		// notify peers that are not on line

		List<Peer> all = connectionManager.getServer().getAllClientHosts(ivcProject.getServerPath());
		List<String> disconnected = new ArrayList<String>();
		Iterator<Peer> itp = all.iterator();
		while (itp.hasNext()) {
			Peer peer = itp.next();
			if (!connectionManager.getPeerHosts().contains(peer.getHostAddress())
					&& !peer.getHostAddress().equalsIgnoreCase(NetworkUtils.getHostAddress())) {
				disconnected.add(peer.getHostAddress());
			}
		}
		connectionManager.getServer().updatePendingRUL(ivcProject.getServerPath(), NetworkUtils.getHostAddress(), disconnected, changedFiles);

	}

	public Result getResult() {
		return result;
	}
}
