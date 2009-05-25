package ivc.wizards.checkout;

import ivc.wizards.checkout.pages.CheckoutWizardPage;
import ivc.wizards.checkout.pages.NewProjectWizardPage;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class CheckoutWizard extends Wizard implements INewWizard, IImportWizard {
	CheckoutWizardPage mainPage;
	public NewProjectWizardPage projectPage;

	@Override
	public void addPages() {
		mainPage = new CheckoutWizardPage("Checkout a new project from IVC repository");
		addPage(mainPage);
		projectPage = new NewProjectWizardPage("Create the project");
		addPage(mainPage);
	}

//	@Override
//	public IWizardPage getNextPage(IWizardPage wizardPage) {
//		if (wizardPage == mainPage) {
//			return projectPage;
//		}
//		return super.getNextPage(wizardPage);
//
//	}

	@Override
	public boolean performFinish() {

		return false;
	}

	@Override
	public void init(IWorkbench arg0, IStructuredSelection arg1) {
		// TODO Auto-generated method stub

	}

}
