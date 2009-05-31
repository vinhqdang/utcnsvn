package ivc.fireworks.actions;

import ivc.fireworks.markers.MarkersManager;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchWindow;

public class CommitAction extends BaseActionDelegate {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IWorkbenchWindow arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(IAction action) {

		IResource[] resources = getSelectedResources();
		for (IResource resource : resources) {

			boolean result = MarkersManager.addMarker(resource, null, "infoMarker");
			action.setChecked(result);
		}

	}

	@Override
	public boolean menuItemEnabled() {
		for (IResource resource : getSelectedResources()) {
			if (!resourceInRepository(resource))
				return false;
		}
		return true;
	}

}
