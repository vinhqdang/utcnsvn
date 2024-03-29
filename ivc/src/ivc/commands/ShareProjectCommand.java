package ivc.commands;

import ivc.data.BaseVersion;
import ivc.data.exception.Exceptions;
import ivc.data.exception.IVCException;
import ivc.managers.ConnectionManager;
import ivc.managers.ProjectsManager;
import ivc.repository.IVCRepositoryProvider;
import ivc.server.rmi.ServerIntf;
import ivc.util.Constants;
import ivc.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.team.core.RepositoryProvider;
import org.eclipse.team.core.TeamException;

public class ShareProjectCommand implements IRunnableWithProgress {
	private IProject project;
	private String projectPath;
	private String serverAddress;
	private String userName;
	private String pass;

	private CommandArgs args;
	private BaseVersion bv;
	private Result result;
	private ConnectionManager connectionManager;

	public ShareProjectCommand(CommandArgs cArgs) {
		args = cArgs;
	}

	@Override
	/*
	 * * Shares a project from the local host. The class establishes connection with the
	 * server and sends over the base version of the project. This base version is saved
	 * by the server in a specific file, that will from now on available through server
	 * methods invocation.
	 */
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {

		// init local properties
		monitor.beginTask("Init local properties", 5);

		project = (IProject) args.getArgumentValue(Constants.IPROJECT);
		projectPath = (String) args.getArgumentValue(Constants.PROJECT_PATH);
		userName = (String) args.getArgumentValue(Constants.USERNAME);
		pass = (String) args.getArgumentValue(Constants.PASSWORD);
		serverAddress = (String) args.getArgumentValue(Constants.SERVER_ADDRESS);
		connectionManager = ConnectionManager.getInstance(project.getName());

		bv = new BaseVersion();
		bv.setProjectName(project.getName());
		monitor.worked(1);

		monitor.setTaskName("Save server address to disk and connect to server");
		// 1. connect to server

		try {
			ServerIntf server = ConnectionManager.getInstance(project.getName())
					.connectToServer(serverAddress);

			// continue if connection succedded
			if (server != null) {
				// authenticate
				try {
					if (!server.authenticateHost(userName, pass)) {
						result = new Result(false,
								Exceptions.SERVER_AUTHENTICATION_FAILED, null);
						return;
					}
				} catch (RemoteException e1) {
					result = new Result(false, Exceptions.SERVER_AUTHENTICATION_FAILED,
							e1);
					e1.printStackTrace();
					return;
				}

				// 2.expose interface
				monitor.worked(1);

				monitor.setTaskName("Exposing interface");
				projectPath = projectPath.toLowerCase();
				connectionManager.exposeInterface(projectPath);
				monitor.worked(1);
				monitor.setTaskName("Init log files");
				// 3. init log files
				createLogFiles();

				// 4.save base version on server repository
				monitor.worked(1);
				monitor.setTaskName("Saving base version on server repository");
				createBaseVersion();
				try {
					server.receiveBaseVersion(projectPath, bv);
				} catch (RemoteException e) {
					e.printStackTrace();
				}

				// 5. refresh project
				try {
					project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
				} catch (CoreException e) {
					e.printStackTrace();
				}
				try {
					RepositoryProvider.map(project, IVCRepositoryProvider.ID);
				} catch (TeamException e) {
					throw new IVCException(e);
				}
				monitor.worked(1);
				monitor.setTaskName("Finished");
				monitor.done();

			}
		} catch (IVCException e1) {
			result = new Result(false, Exceptions.SERVER_CONNECTION_FAILED, e1);
			e1.printStackTrace();
			return;
		}
		if (result == null)
			result = new Result(true, "Success", null);

	}

	private void createLogFiles() {
		try {
			String localProjPath = project.getLocation().toOSString();
			// create document directory
			File ivcfolder = new File(localProjPath + Constants.IvcFolder);
			ivcfolder.mkdir();
			// svr host file
			File svrfile = new File(localProjPath + Constants.IvcFolder
					+ Constants.ServerFile);
			svrfile.createNewFile();
			FileUtils.writeObjectToFile(svrfile.getAbsolutePath(), serverAddress + "\\"
					+ projectPath);
			// local log file
			File llfile = new File(localProjPath + Constants.IvcFolder
					+ Constants.LocalLog);
			llfile.createNewFile();
			// remote committed log
			File rclfile = new File(localProjPath + Constants.IvcFolder
					+ Constants.RemoteCommitedLog);
			rclfile.createNewFile();
			File cvFile = new File(localProjPath + Constants.IvcFolder
					+ Constants.CurrentVersionFile);
			cvFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void createBaseVersion() {
		// build base revision
		if (project.exists()) {
			try {
				// get project structure
				IResource[] resources = project.members();
				for (int i = 0; i < resources.length; i++) {
					IResource resource = resources[i];
					handleResource(resource);
				}
				// save current version for each file
				HashMap<String, Integer> cv = new HashMap<String, Integer>();
				Set<String> files = bv.getFiles().keySet();
				for (Iterator<String> iterator = files.iterator(); iterator.hasNext();) {
					String file = iterator.next();
					cv.put(file, 1);
				}
				cv.put(project.getName(), 1);
				FileUtils.writeObjectToFile(project.getLocation().toOSString()
						+ Constants.IvcFolder + Constants.CurrentVersionFile, cv);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleResource(IResource resource) {
		int resourceType = resource.getType();
		// found file
		ProjectsManager.instance().setDefaultStatus(resource);
		if (resourceType == IResource.FILE) {
			IFile file = (IFile) resource;
			try {
				InputStream content = file.getContents(true);
				StringBuffer sb = FileUtils.InputStreamToStringBuffer(content);
				IPath relPath = file.getProjectRelativePath();
				bv.addFile(relPath.toOSString(), sb);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		// found folder... need to go deeper
		if (resourceType == IResource.FOLDER) {
			IFolder folder = (IFolder) resource;
			if (folder.getName().equalsIgnoreCase("bin")) {
				return;
			}
			bv.addFolder(folder.getProjectRelativePath().toOSString());
			try {
				IResource[] subfolders = folder.members();
				for (int i = 0; i < subfolders.length; i++) {
					IResource subfolder = subfolders[i];
					handleResource(subfolder);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	public Result getResult() {
		return result;
	}

}
