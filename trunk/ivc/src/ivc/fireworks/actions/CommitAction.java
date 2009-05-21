package ivc.fireworks.actions;

import java.util.HashMap;
import java.util.Map;

import ivc.fireworks.markers.MarkersManager;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class CommitAction implements IWorkbenchWindowActionDelegate {
	private IResource resource;

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
		try {
			IResource res = ResourcesPlugin.getWorkspace().getRoot().getProjects()[1].members()[2];

			Map map=new HashMap();
			
			 
			 
			MarkersManager.addMarker(res, map, "infoMarker");
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub
	}

}
