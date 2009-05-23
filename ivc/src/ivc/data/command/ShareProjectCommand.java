package ivc.data.command;

import ivc.data.BaseVersion;
import ivc.data.Result;
import ivc.data.exception.ServerException;
import ivc.rmi.server.ServerBusiness;
import ivc.rmi.server.ServerIntf;
import ivc.util.ConnectionManager;
import ivc.util.Constants;
import ivc.util.FileHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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

public class ShareProjectCommand implements IRunnableWithProgress {
	private String projectName;
	private String projectPath;
	private String serverAddress;
	private CommandArgs args;
	private BaseVersion bv;
	private Result result;

	public ShareProjectCommand(CommandArgs cArgs) {
		args = cArgs;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {

		// init local properties
		monitor.beginTask("Init local properties", 5);

		projectName = (String) args.getArgumentValue("projectName");
		projectPath = (String) args.getArgumentValue("projectPath");
		serverAddress = (String) args.getArgumentValue("serverAddress");
		bv = new BaseVersion();
		bv.setProjectName(projectName);
		bv.setProjectPath(projectPath);
		monitor.worked(1);

		monitor.setTaskName("Save server address to disk and connect to server");
		// 1.save server address to disk and connect to server
		File svrfile = new File(projectPath + Constants.ServerFile);
		try {
			svrfile.createNewFile();
			FileHandler.writeObjectToFile(svrfile.getAbsolutePath(), serverAddress);
		} catch (IOException e) {
			e.printStackTrace();
			result = new Result(false, e.getMessage(), e);
			return;
		}

		try {
			ConnectionManager.getInstance().connectToServer(serverAddress);
		} catch (ServerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// continue if connection succedded
		ServerIntf server = ConnectionManager.getInstance().getServer();
		if (server != null) {
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
				server.receiveBaseVersion(bv);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 5.update gui ??
			monitor.worked(1);
			monitor.setTaskName("Finished");
			monitor.done();
			// TODO update intf on sharing project
		}
		result = new Result(true, "Success", null);

	}

	private void createLogFiles() {
		try {
			// create document directory
			File ivcfolder = new File(projectPath + Constants.IvcFolder);
			ivcfolder.mkdir();
			// local log file
			File llfile = new File(projectPath + Constants.LocalLog);
			llfile.createNewFile();
			// remote committed log
			File rclfile = new File(projectPath + Constants.RemoteCommitedLog);
			rclfile.createNewFile();
			File cvFile = new File(projectPath + Constants.CurrentVersionFile);
			cvFile.createNewFile();		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void createBaseVersion() {

		// get local workspace
		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		// get the project root
		IWorkspaceRoot root = workspace.getRoot();

		// get a handle to project with name 'projectName'
		IProject project = root.getProject(projectName);

		// add project to ivc repository

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
				HashMap<String,Integer> cv = new HashMap<String,Integer>();
				List<String> files = (List<String>) bv.getFiles().keySet();
				for (Iterator<String> iterator = files.iterator(); iterator.hasNext();) {
					String file =  iterator.next();
					cv.put(file, 0);
				}
				FileHandler.writeObjectToFile(ServerBusiness.PROJECTPATH+Constants.CurrentVersionFile,cv);
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
				StringBuffer sb = FileHandler.InputStreamToStringBuffer(content);
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
