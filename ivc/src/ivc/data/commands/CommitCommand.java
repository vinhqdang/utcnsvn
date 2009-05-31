/**
 * 
 */
package ivc.data.commands;

import ivc.connection.ConnectionManager;
import ivc.data.Peer;
import ivc.data.TransformationHistory;
import ivc.data.TransformationHistoryList;
import ivc.data.exception.Exceptions;
import ivc.manager.ProjectsManager;
import ivc.rmi.client.ClientIntf;
import ivc.util.Constants;
import ivc.util.FileUtils;

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
	private String projectPath;
	private List<String> filePaths;

	private TransformationHistoryList changedFiles;
	private HashMap<String, Integer> currentCommitedVersion;
	private ConnectionManager connectionManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see command.CommandIntf#execute(data.CommandArgs)
	 */
	@Override
	//TODO: 1.update rul ;)
	
	// add version 1; delete version remains
	
	public Result execute(CommandArgs args) {
		// init local fields
		
		projectName = (String) args.getArgumentValue(Constants.PROJECT_NAME);
		projectPath = ProjectsManager.instance().getIVCProjectByName(projectName).getServerPath();
		filePaths = (List<String>) args.getArgumentValue(Constants.FILE_PATHS);
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
connectionManager.getServer().updateHeadVersion(projectPath, changedFiles);
		} catch (RemoteException e) {
			return new Result(false,Exceptions.SERVER_UPDATE_HEADVERSION_FAILED, e);
		}
		updateCurrentVersion();
		updateRCLFiles();
		updateRUL();
		cleanLL();
		return new Result(true,"Success",null);
	}

	private void getChangedFiles() {
		// added files, folders; removed files, folders, modified files
		changedFiles = (TransformationHistoryList) FileUtils.readObjectFromFile(projectPath +Constants.IvcFolder + Constants.LocalLog);
		// TODO: 1.added removed, modified files outside eclipse
	}

	private boolean checkVersion() {
		// rcl must be empty and also version on server same as local version of
		// the file
		List<TransformationHistory> rcl = (List<TransformationHistory>) FileUtils.readObjectFromFile(projectPath + Constants.IvcFolder +Constants.RemoteCommitedLog);
		if (rcl != null || !rcl.isEmpty()) {
			return false;
		}
		try {
			currentCommitedVersion = (HashMap)connectionManager.getServer().getVersionNumber(projectPath);
			Iterator<TransformationHistory> it = changedFiles.iterator();
			while (it.hasNext()) {
				TransformationHistory th = it.next();
				String filePath = th.getFilePath();
				if (!th.getTransformations().isEmpty()) {
					Integer localVersion = th.getTransformations().get(0)
							.getFileVersion();
					Integer commitedVersion = currentCommitedVersion
							.get(filePath);
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
			connectionManager.getServer().updateHeadVersion(projectPath, changedFiles);
			HashMap<String, Integer> localVersion = (HashMap<String, Integer>) FileUtils.readObjectFromFile(projectPath+ Constants.IvcFolder +Constants.CurrentVersionFile);
			Iterator<TransformationHistory> it = changedFiles.iterator();
			// increment version numbers
			while (it.hasNext()) {
				TransformationHistory th = it.next();
				String filePath = th.getFilePath();
				// update current version number
				Integer localNo = localVersion.get(filePath);
				if (localNo == null){
					localNo = 0;
				}
				localNo++;
				Integer serverNo = currentCommitedVersion.get(filePath);
				if (serverNo == null){
					serverNo = 0;
				}
				serverNo++;
				currentCommitedVersion.put(filePath, serverNo);
				localVersion.put(filePath, serverNo);
			}
			// update project version
			Integer localProjNo = localVersion.get(projectPath);
			localProjNo++;
			localVersion.put(projectPath, localProjNo);
			Integer serverProjNo = currentCommitedVersion.get(projectPath);
			serverProjNo++;
			currentCommitedVersion.put(projectPath, serverProjNo);
			
			// save new changes
			FileUtils.writeObjectToFile(projectPath+Constants.IvcFolder +Constants.CurrentVersionFile, localVersion);
			connectionManager.getServer().updateVersionNumber(projectPath, currentCommitedVersion);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	private void updateRCLFiles() {
		List<ClientIntf> peers = connectionManager.getPeers();
		if (peers == null){
			return;
		}
		 Iterator<ClientIntf> it = peers.iterator();
		 while (it.hasNext()){
			 ClientIntf peer = it.next();
			 try {
				peer.updateRCL(projectPath, changedFiles);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		 }
		 // notify peers that are not on line
		 try {
			List<Peer> all =  connectionManager.getServer().getAllClientHosts(projectPath);
			List<String> disconnected =  new ArrayList<String>();
			Iterator<Peer> itp = all.iterator();
			while(itp.hasNext()){
				Peer peer = itp.next();
				if(!connectionManager.getPeerHosts().contains(peer.getHostAddress())){
					disconnected.add(peer.getHostAddress());
				}
			}
			connectionManager.getServer().updatePendingRCL(projectPath, disconnected, changedFiles);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void cleanLL() {
		LinkedList<TransformationHistory> ll = new LinkedList<TransformationHistory>();
		FileUtils.writeObjectToFile(projectPath +Constants.IvcFolder + Constants.LocalLog, ll);
	}

	private void updateRUL(){
		
	}
}
