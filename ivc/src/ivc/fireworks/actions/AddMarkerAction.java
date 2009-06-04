package ivc.fireworks.actions;

import ivc.fireworks.markers.MarkersManager;
import ivc.util.WorkspaceUtils;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchWindow;

public class AddMarkerAction extends BaseActionDelegate {

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
		MarkersManager.updateMarkers(WorkspaceUtils.getCurrentFile());
	}

}
