package ivc.wizards.sharing;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import ivc.data.commands.CommandArgs;
import ivc.data.commands.Result;
import ivc.data.commands.ShareProjectCommand;
import ivc.manager.ProjectsManager;
import ivc.plugin.IVCPlugin;
import ivc.repository.IVCRepositoryProvider;
import ivc.util.Constants;
import ivc.wizards.sharing.pages.SharingWizardPage;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.team.core.RepositoryProvider;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.ui.IConfigurationWizard;
import org.eclipse.ui.IWorkbench;

public class SharingWizard extends Wizard implements IConfigurationWizard {
	IProject project;
	IWorkbench workbench;
	SharingWizardPage sharingWizardPage;

	@Override
	public void init(IWorkbench wb, IProject proj) {
		project = proj;
		workbench = wb;
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		sharingWizardPage = new SharingWizardPage();
		addPage(sharingWizardPage);

	}

	@Override
	public boolean canFinish() {
		return true;
	}

	boolean result = false;

	@Override
	public boolean performFinish() {
		if (!getPages()[0].isPageComplete())
			return false;
		CommandArgs args = new CommandArgs();
		args.putArgument(Constants.SERVER_ADDRESS, sharingWizardPage.getServerUrl());
		args.putArgument(Constants.PROJECT_PATH,"\\"+ sharingWizardPage.getProjectPath());
		args.putArgument(Constants.IPROJECT, project);
		args.putArgument(Constants.USERNAME, sharingWizardPage.getUserName());
		args.putArgument(Constants.PASSWORD, sharingWizardPage.getPassword());
		ShareProjectCommand command = new ShareProjectCommand(args);
		try {
			// fork, cancelable, process
			this.getContainer().run(false, true, command);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			setErrorMessage(e.getMessage());
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			setErrorMessage(e.getMessage());
			return false;
		}
		if (command.getResult().isSuccess()) {
			return true;
		} else {
			setErrorMessage(command.getResult().getMessage());
			return false;
		}
	}

	private void setErrorMessage(String error) {
		sharingWizardPage.setErrorMessage(error);
	}
}
