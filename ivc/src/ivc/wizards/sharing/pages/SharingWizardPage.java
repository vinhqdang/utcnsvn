package ivc.wizards.sharing.pages;

import ivc.plugin.ImageDescriptorManager;
import ivc.wizards.BaseWizardPage;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class SharingWizardPage extends BaseWizardPage {
	private Text txtProjectName;

	public SharingWizardPage() {
		super("Select repository");
		setTitle("Select repository");
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
		txtProjectName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				dialogChanged();
			}
		});
	}

	private void dialogChanged() {
		String status = new TextValidator().isValid(txtProjectName.getText());
		updateStatus(status);
	}


	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
	
}
