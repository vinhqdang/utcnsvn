package ivc.data.command;

import ivc.connection.ConnectionManager;
import ivc.data.BaseVersion;
import ivc.data.exception.Exceptions;
import ivc.data.exception.IVCException;
import ivc.manager.ProjectsManager;
import ivc.rmi.server.ServerBusiness;
import ivc.rmi.server.ServerIntf;
import ivc.util.Constants;
import ivc.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.internal.ole.win32.CONTROLINFO;
import org.eclipse.swt.internal.ole.win32.COSERVERINFO;

public class ShareProjectCommand implements IRunnableWithProgress {
	private IProject project;
	private String projectPath;
	private String serverAddress;
	private String userName;
	private String pass;

	private CommandArgs args;
	private BaseVersion bv;
	private Result result;

	public ShareProjectCommand(CommandArgs cArgs) {
		args = cArgs;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		// init local properties
		monitor.beginTask("Init local properties", 5);

		project = (IProject) args.getArgumentValue("project");
		projectPath = (String) args.getArgumentValue("projectPath");
		userName = (String) args.getArgumentValue("userName");
		pass = (String) args.getArgumentValue("pass");
		serverAddress = (String) args.getArgumentValue("serverAddress");

		bv = new BaseVersion();
		bv.setProjectName(project.getName());
		monitor.worked(1);

		monitor.setTaskName("Save server address to disk and connect to server");
		// 1. connect to server

		try {
			ServerIntf server = ConnectionManager.getInstance().connectToServer(serverAddress);

			// continue if connection succedded
			if (server != null) {
				// authenticate
				try {
					if (!server.authenticateHost(userName, pass)) {
						result = new Result(false, Exceptions.SERVER_AUTHENTICATION_FAILED, null);
					}
				} catch (RemoteException e1) {
					result = new Result(false, Exceptions.SERVER_AUTHENTICATION_FAILED, e1);
					e1.printStackTrace();
				}

				// 2.expose interface
				monitor.worked(1);

				monitor.setTaskName("Exposing interface");
				ConnectionManager.getInstance().exposeInterface();
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// 5.update gui
				// TODO update intf on sharing project
				ProjectsManager.instance().tryAddProject(project);

				monitor.worked(1);
				monitor.setTaskName("Finished");
				monitor.done();
				
			}
		} catch (IVCException e1) {
			// TODO Auto-generated catch block
			result = new Result(false, Exceptions.SERVER_CONNECTION_FAILED, e1);
			e1.printStackTrace();
		}
		result = new Result(true, "Success", null);

	}

	private void createLogFiles() {
		try {
			String localProjPath = project.getLocation().toOSString();
			// create document directory
			File ivcfolder = new File(localProjPath + Constants.IvcFolder);
			ivcfolder.mkdir();
			// svr host file
			File svrfile = new File(localProjPath + Constants.IvcFolder + Constants.ServerFile);
			svrfile.createNewFile();
			FileUtils.writeObjectToFile(svrfile.getAbsolutePath(), serverAddress + "\\" + projectPath);
			// local log file
			File llfile = new File(localProjPath + Constants.IvcFolder + Constants.LocalLog);
			llfile.createNewFile();
			// remote committed log
			File rclfile = new File(localProjPath + Constants.IvcFolder + Constants.RemoteCommitedLog);
			rclfile.createNewFile();
			File cvFile = new File(localProjPath + Constants.IvcFolder + Constants.CurrentVersionFile);
			cvFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
					cv.put(file, 0);
				}
				FileUtils.writeObjectToFile(project.getLocation().toOSString() + Constants.IvcFolder + Constants.CurrentVersionFile, cv);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void handleResource(IResource resource) {
		int resourceType = resource.getType();
		// found file
		if (resourceType == IResource.FILE) {
			IFile file = (IFile) resource;
			try {
				InputStream content = file.getContents(true);
				StringBuffer sb = FileUtils.InputStreamToStringBuffer(content);
				IPath relPath = file.getProjectRelativePath();
				bv.addFile(relPath.toOSString(), sb);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// found folder... need to go deeper
		if (resourceType == IResource.FOLDER) {
			IFolder folder = (IFolder) resource;
			bv.addFolder(folder.getProjectRelativePath().toOSString());
			try {
				IResource[] subfolders = folder.members();
				for (int i = 0; i < subfolders.length; i++) {
					IResource subfolder = subfolders[i];
					handleResource(subfolder);
				}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Result getResult() {
		return result;
	}

}
