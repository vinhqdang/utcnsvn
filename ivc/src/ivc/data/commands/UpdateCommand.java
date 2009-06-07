/**
 * 
 */
package ivc.data.commands;

import ivc.connection.ConnectionManager;
import ivc.data.IVCProject;
import ivc.data.Peer;
import ivc.data.operation.Operation;
import ivc.data.operation.OperationHistory;
import ivc.data.operation.OperationHistoryList;
import ivc.listeners.FileModificationManager;
import ivc.rmi.client.ClientIntf;
import ivc.rmi.server.ServerIntf;
import ivc.util.Constants;
import ivc.util.FileUtils;
import ivc.util.NetworkUtils;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

/**
 * @author danielan
 * 
 */
public class UpdateCommand implements CommandIntf {

	private String projectPath;
	private IVCProject ivcProject;
	private List<String> filesToUpdate;

	private boolean updateAll;
	private OperationHistoryList rcl;

	/*
	 * (non-Javadoc)
	 * 
	 * @see command.CommandIntf#execute(data.CommandArgs)
	 */
	@Override
	public Result execute(CommandArgs args) {

		// init local variables
		ivcProject = (IVCProject) args.getArgumentValue(Constants.IVCPROJECT);
		projectPath = ivcProject.getServerPath();
		if (args.getArgumentValue(Constants.FILE_PATHS) != null) {
			filesToUpdate = (List<String>) args.getArgumentValue(Constants.FILE_PATHS);
		}
		if (filesToUpdate == null) {
			updateAll = true;
		}

		// 1. apply rcl and update version
		applyRCL();
		// 2. update ll to include effects of rcl & update rul of others to contain modified version of ll
		updateLL();
		// 3. clean rcl
		cleanRCL();

		return new Result(true, "Success", null);
	}

	private void applyRCL() {
		IProject project = ivcProject.getProject();
		// read current version
		HashMap<String, Integer> currentLocalVersion = ivcProject.getCurrentVersion();
		try {
			ServerIntf server = ConnectionManager.getInstance(project.getName()).getServer();
			HashMap<String, Integer> currentCommitedVersion = (HashMap<String, Integer>) server.getVersionNumber(projectPath);
			rcl = ivcProject.getRemoteCommitedLog();
			Iterator<OperationHistory> it = rcl.iterator();
			while (it.hasNext()) {
				OperationHistory th = it.next();
				String filePath = th.getFilePath();
				if (filesToUpdate == null || filesToUpdate.contains(filePath)) {
					StringBuffer content = new StringBuffer();
					LinkedList<Operation> operations = th.getTransformations();
					if (operations != null) {
						Iterator<Operation> itt = operations.descendingIterator();
						while (itt.hasNext()) {
							Operation tr = itt.next();
							if (tr.getOperationType() == Operation.CHARACTER_ADD || tr.getOperationType() == Operation.CHARACTER_DELETE) {
								// handle file content modifications
								IFile file = (IFile) project.findMember(filePath);
								InputStream contentStream;
								try {
									contentStream = file.getContents(true);
									content = FileUtils.InputStreamToStringBuffer(contentStream);
									content = tr.applyContentTransformation(content);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								// handle project structure modifications
								tr.applyStructureTransformation(project);
							}
						}
						// update file content
						FileUtils.writeStringBufferToFile(project.getLocation().toOSString() + "\\" + filePath, content);
						// update file version
						Integer serverVersion = currentCommitedVersion.get(filePath);
						currentLocalVersion.put(filePath, serverVersion);
					}
				}
			}
			ivcProject.setCurrentVersion(currentLocalVersion);
			try {
				FileModificationManager.ignoreModifications = true;
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				FileModificationManager.ignoreModifications = false;
			}
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void cleanRCL() {
		if (filesToUpdate == null) {
			rcl = new OperationHistoryList();
		} else {
			Iterator<String> it = filesToUpdate.iterator();
			while (it.hasNext()) {
				rcl.removeOperationHistForFile(it.next());
			}
		}
	}

	/**
	 * ll must be transformed to include effects of rcl we say that we apply an inclusion transformation over operations in ll
	 */
	private void updateLL() {
		OperationHistoryList ll = ivcProject.getLocalLog();
		ll = ll.includeOperationHistoryList(rcl);
		ivcProject.setLocalLog(ll);
		ConnectionManager connectionManager = ConnectionManager.getInstance(ivcProject.getName());
		try {
			List<Peer> all = connectionManager.getServer().getAllClientHosts(projectPath);
			List<String> disconnected = new ArrayList<String>();
			Iterator<Peer> itp = all.iterator();
			while (itp.hasNext()) {
				Peer peer = itp.next();
				if (!connectionManager.getPeerHosts().contains(peer.getHostAddress())) {
					disconnected.add(peer.getHostAddress());
				} else {
					ClientIntf client = connectionManager.getPeerByAddress(peer.getHostAddress());
					client.updateRUL(ivcProject.getServerPath(), NetworkUtils.getHostAddress(), ll);
				}
			}
			connectionManager.getServer().updatePendingRUL(projectPath, NetworkUtils.getHostAddress(), disconnected, ll);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
