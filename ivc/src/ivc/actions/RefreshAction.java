package ivc.actions;

import ivc.managers.ProjectsManager;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * Refreshes the status of the first selected resource
 * 
 * @author alexm
 * 
 */
public class RefreshAction extends BaseActionDelegate {

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
		ProjectsManager.instance().setDefaultStatus(getSelectedResources()[0]);
	}

}
