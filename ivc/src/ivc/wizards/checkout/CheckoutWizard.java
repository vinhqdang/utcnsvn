package ivc.wizards.checkout;

import java.lang.reflect.InvocationTargetException;

import ivc.data.commands.CheckoutCommand;
import ivc.data.commands.CommandArgs;
import ivc.data.commands.ShareProjectCommand;
import ivc.wizards.checkout.pages.CheckoutWizardPage;
import ivc.wizards.checkout.pages.NewProjectWizardPage;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class CheckoutWizard extends Wizard implements INewWizard, IImportWizard {
	private CheckoutWizardPage mainPage;
	private NewProjectWizardPage projectPage;

	@Override
	public void addPages() {
		mainPage = new CheckoutWizardPage("Checkout a new project from IVC repository");
		addPage(mainPage);
		projectPage = new NewProjectWizardPage("Create the project");
		addPage(projectPage);
	}

	@Override
	public boolean canFinish() {
		return getContainer().getCurrentPage() == projectPage;
	}

	// @Override
	// public IWizardPage getNextPage(IWizardPage wizardPage) {
	// if (wizardPage == mainPage) {
	// return projectPage;
	// }
	// return super.getNextPage(wizardPage);
	//
	// }

	@Override
	public boolean performFinish() {
		CommandArgs args = new CommandArgs();
		args.putArgument("projectName", projectPage.getProjectName());
		CheckoutCommand command = new CheckoutCommand(args);
		try {
			this.getContainer().run(false, true, command);
		} catch (InvocationTargetException e) {
			e.printStackTrace();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (command.getResult().isSuccess())
			return true;
		else {
			projectPage.setErrorMessage(command.getResult().getMessage());
			return false;
		}

	}

	public NewProjectWizardPage getProjectWizardPage() {
		return projectPage;
	}

	@Override
	public void init(IWorkbench arg0, IStructuredSelection arg1) {
		// TODO Auto-generated method stub

	}

}
