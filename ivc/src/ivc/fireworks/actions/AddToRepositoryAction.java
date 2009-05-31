package ivc.fireworks.actions;

import ivc.data.Transformation;
import ivc.data.commands.CommandArgs;
import ivc.data.commands.HandleTransformationCommand;
import ivc.manager.ProjectsManager;
import ivc.util.Constants;
import ivc.util.FileUtils;

import java.io.InputStream;
import java.util.Date;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchWindow;

public class AddToRepositoryAction extends BaseActionDelegate {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(IWorkbenchWindow arg0) {

	}

	@Override
	public void run(IAction action) {

		for (IResource resource : getSelectedResources()) {
			if (!resourceInRepository(resource)) {
				// TODO 1. add file to repository
				Transformation transformation = new Transformation();
				if (resource.getType() == IResource.FILE) {
					transformation.setOperationType(Transformation.ADD_FILE);
					InputStream is;
					try {
						is = ((IFile)resource).getContents();
						transformation.setText(FileUtils.InputStreamToStringBuffer(is).toString());
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					transformation.setOperationType(Transformation.ADD_FOLDER);
				}
				transformation.setFilePath(resource.getProjectRelativePath().toOSString());
				transformation.setFileVersion(1);
				transformation.setDate(new Date());
				HandleTransformationCommand command = new HandleTransformationCommand();
				CommandArgs args = new CommandArgs();
				args.putArgument(Constants.TRANSFORMATION, transformation);
				args.putArgument(Constants.IPROJECT, resource.getProject());
				command.execute(args);
				
				ProjectsManager.instance().addDefaultStatus(resource);

			}
		}
	}

	public boolean menuItemEnabled() {
		for (IResource resource : getSelectedResources()) {
			if (resourceInRepository(resource))
				return false;
		}
		return true;
	}
}
