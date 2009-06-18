package ivc.actions;

import ivc.commands.CommandArgs;
import ivc.commands.UpdateCommand;
import ivc.data.IVCProject;
import ivc.managers.ProjectsManager;
import ivc.util.Constants;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * The class is used to perform a update action
 * 
 * @author alexm
 * 
 */
public class UpdateAction extends BaseActionDelegate {

	@Override
	public void dispose() {

	}

	@Override
	public void init(IWorkbenchWindow arg0) {
	}

	@Override
	public void run(IAction arg0) {
		try {
			List<String> files = new ArrayList<String>();
			// returning all selected resources for update
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

	@Override
	public boolean menuItemEnabled() {
		return true;
	}

}
