package ivc.wizards.checkout;

import ivc.commands.CheckoutCommand;
import ivc.commands.CommandArgs;
import ivc.util.Constants;
import ivc.wizards.checkout.pages.CheckoutWizardPage;
import ivc.wizards.checkout.pages.NewProjectWizardPage;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * 
 * @author alexm
 * 
 *         Wizard used to checkout a new project
 */
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

	@Override
	public boolean performFinish() {
		if (!projectPage.testProjectName())
			return false;

		CommandArgs args = new CommandArgs();
		args.putArgument(Constants.PROJECT_NAME, projectPage.getProjectName());
		args.putArgument(Constants.SERVER_ADDRESS, mainPage.getTxtServerURL().getText());
		args.putArgument(Constants.PROJECT_PATH, "\\" + mainPage.getTxtPath().getText());
		CheckoutCommand command = new CheckoutCommand(args);
		try {
			this.getContainer().run(false, true, command);
		} catch (InvocationTargetException e) {
			e.printStackTrace();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (command.getResult().isSuccess())
			return true;
		else {
			projectPage.setErrorMessage(command.getResult().getMessage());
			return false;
		}

	}

	/**
	 * gets the Project wizard page
	 * 
	 * @return the ProjectWizard page
	 */
	public NewProjectWizardPage getProjectWizardPage() {
		return projectPage;
	}

	@Override
	public void init(IWorkbench arg0, IStructuredSelection arg1) {

	}
}
