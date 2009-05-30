/**
 * 
 */
package ivc.data.commands;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import ivc.connection.ConnectionManager;
import ivc.data.Transformation;
import ivc.data.TransformationHistory;
import ivc.data.TransformationHistoryList;
import ivc.util.Constants;
import ivc.util.FileUtils;

/**
 * @author danielan
 * 
 */
public class UpdateCommand implements CommandIntf {

	private String projectPath;
	private List<String> filesToUpdate;

	private boolean updateAll;
	private TransformationHistoryList rcl;

	/*
	 * (non-Javadoc)
	 * 
	 * @see command.CommandIntf#execute(data.CommandArgs)
	 */
	@Override
	public Result execute(CommandArgs args) {

		// init local variables
		projectPath = (String) args.getArgumentValue("projectPath");
		if (args.getArgumentValue("filesToUpdate") != null) {
			filesToUpdate = (List<String>) args.getArgumentValue("filesToUpdate");
		}
		if (filesToUpdate == null) {
			updateAll = true;
		}

		// 1. apply rcl and update version
		applyRCL();
		// 2. clean rcl
		cleanRCL();

		return new Result(true, "Success", null);
	}

	private void applyRCL() {
		// get local workspace
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		// get the project root
		IWorkspaceRoot root = workspace.getRoot();
		// get a handle to project with name 'projectName'
		IProject project = root.getProject(projectPath);

		// read current version
		HashMap<String, Integer> currentLocalVersion = (HashMap<String, Integer>) FileUtils.readObjectFromFile(projectPath
				+ Constants.CurrentVersionFile);
		try {
			HashMap<String, Integer> currentCommitedVersion = (HashMap) ConnectionManager.getInstance().getServer().getVersionNumber(projectPath);

			rcl = (TransformationHistoryList) FileUtils.readObjectFromFile(projectPath +Constants.IvcFolder + Constants.RemoteCommitedLog);
			Iterator<TransformationHistory> it = rcl.iterator();
			while (it.hasNext()) {
				TransformationHistory th = it.next();
				String filePath = th.getFilePath();
				if (filesToUpdate == null || filesToUpdate.contains(filePath)) {
					LinkedList<Transformation> transformations = th.getTransformations();
					if (transformations != null) {
						Iterator<Transformation> itt = transformations.descendingIterator();
						while (itt.hasNext()) {
							Transformation tr = itt.next();
							if (tr.getOperationType() == Transformation.CHARACTER_ADD || tr.getOperationType() == Transformation.CHARACTER_DELETE) {
								// handle file content modifications
								IFile file = (IFile) project.findMember(filePath);
								InputStream contentStream;
								try {
									contentStream = file.getContents(true);
									StringBuffer content = FileUtils.InputStreamToStringBuffer(contentStream);
									tr.applyContentTransformation(content);
								} catch (CoreException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								// handle project structure modifications
								tr.applyStructureTransformation();
							}
						}
						// update file version
						Integer serverVersion = currentCommitedVersion.get(filePath);
						currentLocalVersion.put(filePath, serverVersion);
					}
				}
			}
			FileUtils.writeObjectToFile(projectPath + Constants.IvcFolder +Constants.CurrentVersionFile, currentLocalVersion);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void cleanRCL() {
		if (filesToUpdate == null) {
			rcl = new TransformationHistoryList();
		} else {
			Iterator<String> it = filesToUpdate.iterator();
			while (it.hasNext()) {
				rcl.removeTransformationHistForFile(it.next());
			}
		}
	}

}
