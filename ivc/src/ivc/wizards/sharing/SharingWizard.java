package ivc.wizards.sharing;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import ivc.manager.ProjectsManager;
import ivc.plugin.IVCPlugin;
import ivc.wizards.sharing.pages.SharingWizardPage;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
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

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(monitor);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		try {
			getContainer().run(true, true, op);
		} catch (Exception e) {
			e.printStackTrace();

		}

		return false;
	}

	private void doFinish(IProgressMonitor monitor) {
		monitor.beginTask("Verifying project name", 2);
		if (ProjectsManager.instance().projectInRepository(project)) {
			result = false;
			return;
		}
		monitor.worked(1);
		monitor.setTaskName("Adding project to repository");
		for (int i = 0; i < 100000; i++) {
			System.out.print("a");
		}
		if (1 == 1)
			return;
		monitor.worked(1);
		result = true;
	}
}
