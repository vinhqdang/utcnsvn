package ivc.actions;

import ivc.commands.AddToRepositoryCommand;
import ivc.commands.CommandArgs;
import ivc.commands.CommitCommand;
import ivc.commands.Result;
import ivc.managers.ProjectsManager;
import ivc.repository.CacheManager;
import ivc.repository.Status;
import ivc.util.Constants;
import ivc.wizards.commit.CommitWizard;
import ivc.wizards.commit.CommitWizardDialog;
import ivc.wizards.commit.pages.CommitWizardPage;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;

public class CommitAction extends BaseActionDelegate {

	@Override
	public void dispose() {

	}

	@Override
	public void init(IWorkbenchWindow arg0) {

	}

	@Override
	public void run(IAction action) {

		Map<IResource, Status> statusMap = new HashMap<IResource, Status>();
		IResource[] resources = null;
		try {
			resources = findAllResources(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (resources.length == 0) {
			MessageDialog.openInformation(getShell(), "Cannot commit", "No files were changed");
			return;
		}
		try {
			for (IResource resource : resources) {
				if (!resource.isPhantom())
					statusMap.put(resource, ProjectsManager.instance().getStatus(resource));
				else
					statusMap.put(resource, Status.Deleted);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		CommitWizardPage commitPage = new CommitWizardPage(resources, statusMap, false);
		CommitWizard wizard = new CommitWizard(commitPage);
		CommitWizardDialog dialog = new CommitWizardDialog(getShell(), wizard);

		wizard.setParentDialog(dialog);
		boolean commitOK = (dialog.open() == WizardDialog.OK);
		if (!commitOK) {
			return;
		}
		List<String> filePaths = new ArrayList<String>();
		IResource[] commitedResources = commitPage.getSelectedResources();
		if (commitedResources != null && commitedResources.length > 0) {
			for (IResource resource : commitedResources) {
				if (resource.getType() != IResource.PROJECT) {
					if (ProjectsManager.instance().isManaged(resource)) {
						AddToRepositoryCommand command = new AddToRepositoryCommand(null, resource);

						try {
							command.run();
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
					filePaths.add(resource.getProjectRelativePath().toOSString());
				}
			}
			CommandArgs args = new CommandArgs();
			args.putArgument(Constants.PROJECT_NAME, commitedResources[0].getProject().getName());
			args.putArgument(Constants.FILE_PATHS, filePaths);

			CommitCommand commitCommand = new CommitCommand(null, args);
			try {
				commitCommand.run();
			} catch (Exception e) {
				MessageDialog.openError(getShell(), "Commit failed", e.getMessage());
				return;
			}
			Result result = commitCommand.getResult();
			if (!result.isSuccess()) {
				MessageDialog.openError(getShell(), "Commit failed", result.getMessage());
				return;
			}
			for (IResource resource : commitedResources) {
				try {
					if (resource.isPhantom()) {
						ResourcesPlugin.getWorkspace().getSynchronizer().setSyncInfo(CacheManager.IVC_STATUS_KEY, resource, null);
					}

					ProjectsManager.instance().setCommitedStatus(resource);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			MessageDialog.openInformation(getShell(), "Commit successful", "Commit succeded");
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
