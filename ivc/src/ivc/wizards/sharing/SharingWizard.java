package ivc.wizards.sharing;

import ivc.wizards.sharing.pages.FirstPage;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.team.ui.IConfigurationWizard;
import org.eclipse.ui.IWorkbench;

public class SharingWizard extends Wizard implements IConfigurationWizard {
	IProject project;
	IWorkbench workbench;

	@Override
	public void init(IWorkbench wb, IProject proj) {
		project = proj;
		workbench = wb;
	}

	@Override
	public void addPages() {
		addPage(new FirstPage());

	}

	@Override
	public boolean canFinish() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

}
