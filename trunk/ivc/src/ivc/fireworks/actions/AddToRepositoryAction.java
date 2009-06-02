package ivc.fireworks.actions;

import ivc.data.Operation;
import ivc.data.commands.CommandArgs;
import ivc.data.commands.HandleOperationCommand;
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
				Operation operation = new Operation();
				if (resource.getType() == IResource.FILE) {
					operation.setOperationType(Operation.ADD_FILE);
					InputStream is;
					try {
						is = ((IFile)resource).getContents();
						operation.setText(FileUtils.InputStreamToStringBuffer(is).toString());
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					operation.setOperationType(Operation.ADD_FOLDER);
				}
				operation.setFilePath(resource.getProjectRelativePath().toOSString());
				operation.setFileVersion(1);
				operation.setDate(new Date());
				HandleOperationCommand command = new HandleOperationCommand();
				CommandArgs args = new CommandArgs();
				args.putArgument(Constants.TRANSFORMATION, operation);
				args.putArgument(Constants.IPROJECT, resource.getProject());
				command.execute(args);
				
				ProjectsManager.instance().setAddedStatus(resource);

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
