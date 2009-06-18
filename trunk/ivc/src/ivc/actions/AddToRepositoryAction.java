package ivc.actions;

import ivc.commands.AddToRepositoryCommand;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * 
 * @author alexm The class is used to mark a resource as added to the repository
 */
public class AddToRepositoryAction extends BaseActionDelegate {

	public void dispose() {
	}

	@Override
	public void init(IWorkbenchWindow arg0) {

	}

	@Override
	public void run(IAction action) {

		for (IResource resource : getSelectedResources()) {
			if (!resourceInRepository(resource)) {
				AddToRepositoryCommand command = new AddToRepositoryCommand(null,
						resource);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.actions.BaseActionDelegate#menuItemEnabled()
	 */
	public boolean menuItemEnabled() {
		for (IResource resource : getSelectedResources()) {
			if (resourceInRepository(resource))
				return false;
		}
		return true;
	}
}
