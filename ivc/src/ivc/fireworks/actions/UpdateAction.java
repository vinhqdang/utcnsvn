package ivc.fireworks.actions;

import ivc.commands.CommandArgs;
import ivc.commands.UpdateCommand;
import ivc.data.IVCProject;
import ivc.managers.ProjectsManager;
import ivc.util.Constants;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;

public class UpdateAction extends BaseActionDelegate {

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
			List<String> files = new ArrayList<String>();
			IResource[] resources = findAllResources(false);
			if (resources != null) {
				for (int i = 0; i < resources.length; i++) {
					IResource resource = resources[i];
					String filePath = resource.getProjectRelativePath().toOSString();
					files.add(filePath);
				}
			}
			String projName = resources[0].getProject().getName();
			CommandArgs args = new CommandArgs();
			IVCProject project = ProjectsManager.instance().getIVCProjectByName(projName);
			args.putArgument(Constants.IVCPROJECT, project);
			args.putArgument(Constants.FILE_PATHS, files);
			UpdateCommand uc = new UpdateCommand(null, args);
			uc.run();

		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openError(getShell(), "Failed", "Update failed");
			return;
		}
		MessageDialog.openInformation(getShell(), "Success", "Update Successful");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.fireworks.actions.BaseActionDelegate#menuItemEnabled()
	 */
	@Override
	public boolean menuItemEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
