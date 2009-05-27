/**
 * 
 */
package ivc.data.command;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import ivc.data.Result;
import ivc.data.Transformation;
import ivc.data.TransformationHistory;
import ivc.data.TransformationHistoryList;
import ivc.data.exception.Exceptions;
import ivc.rmi.client.ClientIntf;
import ivc.util.ConnectionManager;
import ivc.util.Constants;
import ivc.util.FileHandler;

/**
 * @author danielan
 * 
 */
public class CommitCommand implements CommandIntf {

	private String projectPath;
	private List<String> filePaths;

	TransformationHistoryList changedFiles;
	HashMap<String, Integer> currentCommitedVersion;

	/*
	 * (non-Javadoc)
	 * 
	 * @see command.CommandIntf#execute(data.CommandArgs)
	 */
	@Override
	//TODO: update rul ;)
	
	// add version 1; delete version remains
	
	public Result execute(CommandArgs args) {
		// init local fields
		projectPath = (String) args.getArgumentValue("projectPath");
		filePaths = (List<String>) args.getArgumentValue("filePaths");
		// if the user tries to commit the entire project must get the changed
		// files
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
			ConnectionManager.getInstance().getServer().updateHeadVersion(projectPath, changedFiles);
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
		changedFiles = (TransformationHistoryList) FileHandler.readObjectFromFile(projectPath + Constants.LocalLog);
		// TODO: added removed, modified files outside eclipse
	}

	private boolean checkVersion() {
		// rcl must be empty and also version on server same as local version of
		// the file
		List<TransformationHistory> rcl = (List<TransformationHistory>) FileHandler	.readObjectFromFile(projectPath + Constants.RemoteCommitedLog);
		if (rcl != null || !rcl.isEmpty()) {
			return false;
		}
		try {
			currentCommitedVersion = (HashMap)ConnectionManager.getInstance().getServer().getVersionNumber(projectPath);
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
			ConnectionManager.getInstance().getServer().updateHeadVersion(projectPath, changedFiles);
			HashMap<String, Integer> localVersion = (HashMap<String, Integer>) FileHandler.readObjectFromFile(projectPath+ Constants.CurrentVersionFile);
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
			FileHandler.writeObjectToFile(projectPath+Constants.CurrentVersionFile, localVersion);
			ConnectionManager.getInstance().getServer().updateVersionNumber(projectPath, currentCommitedVersion);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	private void updateRCLFiles() {
		List<ClientIntf> peers = ConnectionManager.getInstance().getPeers();
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
			List<String> disconnected =  ConnectionManager.getInstance().getServer().getAllClientHosts();
			disconnected.removeAll(ConnectionManager.getInstance().getPeerHosts());
			ConnectionManager.getInstance().getServer().updatePendingRCL(projectPath, disconnected, changedFiles);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void cleanLL() {
		LinkedList<TransformationHistory> ll = new LinkedList<TransformationHistory>();
		FileHandler.writeObjectToFile(projectPath + Constants.LocalLog, ll);
	}

	private void updateRUL(){
		
	}
}
