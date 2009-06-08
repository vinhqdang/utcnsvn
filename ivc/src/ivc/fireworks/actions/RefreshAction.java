package ivc.fireworks.actions;

import ivc.managers.ProjectsManager;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchWindow;

public class RefreshAction extends BaseActionDelegate {

	@Override
	public boolean menuItemEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IWorkbenchWindow arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(IAction arg0) {
		ProjectsManager.instance().removeStatus(getSelectedResources()[0]);
		ProjectsManager.instance().setDefaultStatus(getSelectedResources()[0]);
	}

}
