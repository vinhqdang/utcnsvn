package ivc.data.commands;

import ivc.connection.ConnectionManager;
import ivc.data.BaseVersion;
import ivc.data.Peer;
import ivc.data.Transformation;
import ivc.data.TransformationHistory;
import ivc.data.TransformationHistoryList;
import ivc.data.exception.Exceptions;
import ivc.data.exception.IVCException;
import ivc.rmi.client.ClientIntf;
import ivc.rmi.server.ServerIntf;
import ivc.util.Constants;
import ivc.util.FileUtils;
import ivc.util.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * @author danielan
 * 
 */
public class CheckoutCommand implements IRunnableWithProgress {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CommandArgs args;
	private Result result;

	private String serverAddress;
	private String projectPath;
	private String projectName;

	private IProject project;
	private ConnectionManager connectionManager;

	public CheckoutCommand(CommandArgs args) {
		this.args = args;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		// init fields
		monitor.beginTask("Init local properties", 5);
		serverAddress = (String) args.getArgumentValue(Constants.SERVER_ADDRESS);
		projectPath = (String) args.getArgumentValue(Constants.PROJECT_PATH);
		projectName = (String) args.getArgumentValue(Constants.PROJECT_NAME);
		connectionManager = ConnectionManager.getInstance(projectName);

		monitor.worked(1);
		monitor.setTaskName("Establish connections");
		// 1.establish connections: connect to server; expose intf; connect to
		// other peers
		try {
			connectionManager.initiateConnections(serverAddress, projectPath);
		} catch (IVCException e) {
			e.printStackTrace();
			result = new Result(false, "error", e);
			return;
		}

		monitor.worked(1);
		monitor.setTaskName("Establish connections");
		// 2. create IProject
		try {
			createProject(monitor);
		} catch (CoreException e) {
			e.printStackTrace();
			result = new Result(false, Exceptions.COULD_NOT_CREATE_PROJECT, e);
			return;
		}

		// 3. get base version and transformations
		createProjectFiles(monitor);

		// 4. init workspace file
		createLogFiles();

		// 5. create log files on peers
		createPeersRemoteFiles();
		result = new Result(true, "Success", null);
	}

	/**
	 * 
	 */
	private void createLogFiles() {
		try {
			// create document directory
			File ivcfolder = new File(project.getLocation().toOSString() + Constants.IvcFolder);
			ivcfolder.mkdir();
			// local log file
			File llfile = new File(project.getLocation().toOSString() + Constants.IvcFolder + Constants.LocalLog);
			llfile.createNewFile();
			// remote committed log
			File rclfile = new File(project.getLocation().toOSString() + Constants.IvcFolder + Constants.RemoteCommitedLog);
			rclfile.createNewFile();
			List<String> peerHosts = connectionManager.getPeerHosts();
			if (peerHosts != null) {
				Iterator<String> it = peerHosts.iterator();
				while (it.hasNext()) {
					String peerHost = it.next();
					File rlufile = new File(project.getLocation().toOSString() + Constants.RemoteUnCommitedLog + "_" + peerHost);
					rlufile.createNewFile();
				}
			}
			File cvFile = new File(project.getLocation().toOSString() + Constants.IvcFolder + Constants.CurrentVersionFile);
			cvFile.createNewFile();
			HashMap<String, Integer> cv = (HashMap<String, Integer>) connectionManager.getServer().getVersionNumber(projectPath);
			FileUtils.writeObjectToFile(cvFile.getAbsolutePath(), cv);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private void createPeersRemoteFiles() {
		List<ClientIntf> peers = connectionManager.getPeers();
		if (peers != null) {
			Iterator<ClientIntf> it = peers.iterator();
			while (it.hasNext()) {
				ClientIntf peer = it.next();
				try {
					peer.createRULFile(projectPath, NetworkUtils.getHostAddress());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 */
	private void createProjectFiles(IProgressMonitor monitor) {
		ServerIntf server = connectionManager.getServer();
		try {
			BaseVersion bv = server.returnBaseVersion(projectPath);
			TransformationHistoryList thl = server.returnHeadVersion(projectPath);
			// create folder structure
			Iterator<String> itfld = bv.getFolders().iterator();
			while (itfld.hasNext()) {
				File f = new File(project.getLocation().toOSString() + "\\" + itfld.next());
				f.mkdirs();
			}
			// 5. create file structure
			Iterator<String> itFiles = bv.getFiles().keySet().iterator();
			while (itFiles.hasNext()) {
				String filePath = itFiles.next();
				StringBuffer baseContent = bv.getFiles().get(filePath);
				try {
					File f = new File(project.getLocation().toOSString() + "\\" + filePath);
					f.createNewFile();
					FileUtils.writeStringBufferToFile(f.getAbsolutePath(), baseContent);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			thl.applyTransformationHistoryList(project);
			// refresh project
			try {
				project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void createProject(IProgressMonitor progressMonitor) throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		project = root.getProject(projectName);
		project.create(progressMonitor);
		project.open(progressMonitor);

	}

	public Result getResult() {
		return result;
	}

}
