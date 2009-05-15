package ivc.repository;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.team.core.RepositoryProvider;
import org.eclipse.team.core.TeamException;
import org.eclipse.ui.IWorkbench;

public class IVCSharingWizard implements
		org.eclipse.team.ui.IConfigurationWizard {
	IProject project;
	IWorkbench workbench;

	@Override
	public void init(IWorkbench wb, IProject proj) {
		project = proj;
		workbench = wb;
	}

	@Override
	public void addPages() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canFinish() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void createPageControls(Composite arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public IWizardContainer getContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image getDefaultPageImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDialogSettings getDialogSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IWizardPage getPage(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPageCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IWizardPage[] getPages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IWizardPage getPreviousPage(IWizardPage arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IWizardPage getStartingPage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RGB getTitleBarColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWindowTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isHelpAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean needsPreviousAndNextButtons() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean needsProgressMonitor() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean performCancel() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean performFinish() {

		try {
			RepositoryProvider.map(project, "ivc.reprovider");
			System.out.println("loaded project");
		} catch (TeamException te) {
			System.out.println("Team exception");
		}
		return true;
	}

	@Override
	public void setContainer(IWizardContainer arg0) {
		// TODO Auto-generated method stub

	}
}
