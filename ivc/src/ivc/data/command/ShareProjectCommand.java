/**
 * 
 */
package ivc.data.command;

import ivc.data.BaseVersion;
import ivc.data.Result;
import ivc.util.FileHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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

	private String projectName;
	private String projectPath;

	private BaseVersion bv;

	/*
	 * (non-Javadoc)  
	 * 
	 * @see ivc.command.CommandIntf#execute(ivc.command.CommandArgs)
	 */
	@Override
	public Result execute(CommandArgs args) {
		// try {
		
		// init local properties
		projectName = (String) args.getArgumentValue("projectName");
		projectPath = (String) args.getArgumentValue("projectPath");
		bv = new BaseVersion();
		bv.setProjectName(projectName);
		bv.setProjectPath(projectPath);

		// 1.expose interface 
		// ConnectionManager.getInstance().exposeInterface();
	
		// 2.create workspace log files
		// save base version for each file??
		createBaseVersion();
		//init log files
		createLogFiles();
		
		// 3.update gui ??
		//TODO update intf on sharing project
		
		// } catch (ServerException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }
  
		return new Result(true,"Success",null);
	}

	private void createLogFiles(){
		try {
			File llfile = new File(projectPath + "\\.ivc\\.ll");
			llfile.createNewFile();
			File rclfile = new File(projectPath + "\\.ivc\\.rcl");
			rclfile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void createBaseVersion() {

		// create document directory
		File ivcfolder = new File(projectPath + "\\.ivc");
		ivcfolder.mkdir();

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
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// write base version to file 
		FileHandler.writeObjectToFile(projectPath + "\\.ivc\\.bv", bv);
	}	

	private void handleResource(IResource resource){
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
			IFolder folder = (IFolder)resource;
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
