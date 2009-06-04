package ivc.fireworks.actions;

import ivc.data.commands.CommandArgs;
import ivc.data.commands.CommitCommand;
import ivc.fireworks.markers.MarkersManager;
import ivc.util.Constants;

import java.util.ArrayList;
import java.util.List;

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
		List<String> filePaths =  new ArrayList<String>();
		IResource[] resources = getSelectedResources();
		for (IResource resource : resources) {
			if (resource.getType() == IResource.PROJECT){
				break;
			}else{
				filePaths.add(resource.getProjectRelativePath().toOSString());
			}
			boolean result = MarkersManager.addMarker(resource, null, "infoMarker");
			action.setChecked(result);
		}
		CommitCommand commitCommand = new CommitCommand();
		CommandArgs args = new CommandArgs();
		args.putArgument(Constants.PROJECT_NAME,resources[0].getProject().getName());
		args.putArgument(Constants.FILE_PATHS, filePaths);
		commitCommand.execute(args);
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
