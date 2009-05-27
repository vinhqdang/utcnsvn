package ivc.wizards.checkout.pages;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import ivc.plugin.ImageDescriptorManager;
import ivc.wizards.BaseWizardPage;

public class NewProjectWizardPage extends BaseWizardPage {

	private Text txtProjectName;

	public String getProjectName() {
		return txtProjectName.getText();
	}

	public NewProjectWizardPage(String pageName) {
		super(pageName);
		this.setTitle(pageName);
		ImageDescriptor img = ImageDescriptorManager
				.getImageDescriptor(ImageDescriptorManager.SHARE_WIZARD);
		setImageDescriptor(img);
	}

	@Override
	public void createControl(Composite mainControl) {
		Composite composite = createComposite(mainControl, 2);
		setControl(composite);

		createLabel(composite, "Project Name:");
		txtProjectName = createTextField(composite);
	}

	boolean testProjectName() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] projects = root.getProjects();
		for (int i = 0; i < projects.length; i++) {
			if (projects[i].getName().equals(txtProjectName.getText())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean canFlipToNextPage() {
		return false;
	}

	public boolean createProject() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(txtProjectName.getText());

		// project.create(progressMonitor);
		// project.open(progressMonitor);
		return false;
	}

}
