package ivc.actions;

import ivc.commands.AddToRepositoryCommand;
import ivc.commands.CommandArgs;
import ivc.commands.HandleOperationCommand;
import ivc.data.operation.Operation;
import ivc.data.operation.OperationHistory;
import ivc.managers.ProjectsManager;
import ivc.util.Constants;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.eclipse.core.resources.IResource;
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
				AddToRepositoryCommand command = new AddToRepositoryCommand(null, resource);
				try {
					
					command.run();
					
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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
