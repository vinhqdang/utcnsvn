package ivc.fireworks.actions;

import ivc.data.IVCProject;
import ivc.data.commands.CommandArgs;
import ivc.data.commands.UpdateCommand;
import ivc.manager.ProjectsManager;
import ivc.util.Constants;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
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
			for (int i = 0; i < resources.length; i++) {
				IResource resource = resources[i];
				String filePath = resource.getProjectRelativePath().toOSString();
				files.add(filePath);
			}
			String projName = resources[0].getProject().getName();
			UpdateCommand uc = new UpdateCommand();
			CommandArgs args = new CommandArgs();
			IVCProject project = ProjectsManager.instance().getIVCProjectByName(projName);
			args.putArgument(Constants.IVCPROJECT, project);
			args.putArgument(Constants.FILE_PATHS, files);
			uc.execute(args);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Update successfull.");
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub
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
