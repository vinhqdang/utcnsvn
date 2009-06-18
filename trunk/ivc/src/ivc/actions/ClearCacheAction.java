package ivc.actions;

import ivc.managers.ProjectsManager;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchWindow;

public class ClearCacheAction extends BaseActionDelegate {

	@Override
	public boolean menuItemEnabled() {
		return true;
	}

	@Override
	public void dispose() {

	}

	@Override
	public void init(IWorkbenchWindow arg0) {

	}

	@Override
	public void run(IAction arg0) {
		ProjectsManager.instance().removeStatus(getSelectedResources()[0]);

	}

}
