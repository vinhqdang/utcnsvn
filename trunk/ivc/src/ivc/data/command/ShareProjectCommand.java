/**
 * 
 */
package ivc.data.command;

import ivc.data.BaseVersion;
import ivc.data.Result;
import ivc.rmi.server.ServerIntf;
import ivc.util.ConnectionManager;
import ivc.util.Constants;
import ivc.util.FileHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

/**
 * @author danielan
 * 
 */
public class ShareProjectCommand implements CommandIntf {

	private IProject project;
	private String userName;
	private String password;
	private String projectPath;
	private String serverAddress;

	private BaseVersion bv;

	/*
	 * returns:
	 * ConnectionFailed
	 * AuthenticationError
	 * Path already in use
	 * Invalid path
	 * (non-Javadoc)
	 * 
	 * @see ivc.command.CommandIntf#execute(ivc.command.CommandArgs)
	 */
	@Override
	public Result execute(CommandArgs args) {

		// init local properties
		project = (IProject) args.getArgumentValue("project");
		projectPath = (String) args.getArgumentValue("projectPath");
		serverAddress = (String) args.getArgumentValue("serverAddress");
		userName = (String) args.getArgumentValue("userName");
		password = (String) args.getArgumentValue("password");
		bv = new BaseVersion();
		bv.setProjectName(project.getName());
		bv.setProjectPath(projectPath);

		// 1.save server address to disk and connect to server
		File svrfile = new File(projectPath + Constants.ServerFile);
		try {
			svrfile.createNewFile();
			FileHandler.writeObjectToFile(svrfile.getAbsolutePath(),serverAddress);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ConnectionManager.getInstance().connectToServer(serverAddress);

		// continue if connection succedded
		ServerIntf server = ConnectionManager.getInstance().getServer();
		if (server != null) {
			// 2.expose interface
			ConnectionManager.getInstance().exposeInterface();

			// 3. init log files
			createLogFiles();

			//4.save base version on server repository
			createBaseVersion();
			try {
				server.receiveBaseVersion(bv);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// 5.update gui ??
			// TODO update intf on sharing project
		}
		return new Result(true, "Success", null);
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void createBaseVersion() {

		
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

}
