package ivc.fireworks.actions;

import ivc.data.commands.CommandArgs;
import ivc.data.commands.CommitCommand;
import ivc.fireworks.markers.MarkersManager;
import ivc.manager.ProjectsManager;
import ivc.repository.Status;
import ivc.util.Constants;
import ivc.wizards.commit.CommitWizard;
import ivc.wizards.commit.CommitWizardDialog;
import ivc.wizards.commit.pages.CommitWizardPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable.PrintMode;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.wizard.WizardDialog;
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

	private IResource[] findAllResources() throws CoreException {
		List<IResource> resourcesTo = new ArrayList<IResource>();
		for (IResource resource : getSelectedResources()) {
			addResource(resourcesTo, resource);
		}
		return (IResource[]) resourcesTo.toArray();
	}

	private void addResource(List<IResource> resources, IResource resource) throws CoreException {
		if (!resources.contains(resource)) {
			resources.add(resource);
			if (resource instanceof IProject) {
				IProject proj = (IProject) resource;
				for (IResource res : proj.members()) {
					addResource(resources, res);
				}
			} else {
				if (resource instanceof IFolder) {
					IFolder fold = (IFolder) resource;
					for (IResource res : fold.members()) {
						addResource(resources, res);
					}
				}
			}
		}
	}

	@Override
	public void run(IAction action){

		Map<IResource, Status> statusMap = new HashMap<IResource, Status>();
		IResource[] resources = getSelectedResources();

		for (IResource resource : resources) {
			statusMap.put(resource, ProjectsManager.instance().getStatus(resource));
		}
		CommitWizardPage commitPage = new CommitWizardPage(getSelectedResources(), statusMap, false);
		CommitWizard wizard = new CommitWizard(commitPage);
		CommitWizardDialog dialog = new CommitWizardDialog(getShell(), wizard);

		wizard.setParentDialog(dialog);
		boolean commitOK = (dialog.open() == WizardDialog.OK);
		if (commitOK) {
			return;
		}
		List<String> filePaths = new ArrayList<String>();

		for (IResource resource : resources) {
			if (resource.getType() == IResource.PROJECT) {
				break;
			} else {
				filePaths.add(resource.getProjectRelativePath().toOSString());
			}
			// boolean result = MarkersManager.updateMarkers(resource);
			// action.setChecked(result);
		}
		CommitCommand commitCommand = new CommitCommand();
		CommandArgs args = new CommandArgs();
		args.putArgument(Constants.PROJECT_NAME, resources[0].getProject().getName());
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