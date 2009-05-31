package ivc.fireworks.actions;

import ivc.manager.ProjectsManager;

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
