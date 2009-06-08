package ivc.wizards.sharing.pages;

import ivc.managers.ImageDescriptorManager;
import ivc.wizards.BaseWizardPage;
import ivc.wizards.validation.Validator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class SharingWizardPage extends BaseWizardPage {
	private Text txtServerURL;
	private Text txtProjectPath;
	private Text txtUserName;
	private Text txtPassword;
	private Validator validator;
	public final String ERROR_USER_NULL = "Some fields are not filled";

	public SharingWizardPage() {
		super("Select repository");
		setTitle("Select repository");
		ImageDescriptor img = ImageDescriptorManager.getImageDescriptor(ImageDescriptorManager.SHARE_WIZARD);
		setImageDescriptor(img);
		validator = new Validator() {
			@Override
			public void setError(String error) {
				updateStatus(error);
			}
		};

	}

	public String getServerUrl() {
		return txtServerURL.getText();
	}

	public String getProjectPath() {
		return txtProjectPath.getText();
	}

	public String getUserName() {
		return txtUserName.getText();
	}

	public String getPassword() {
		return txtPassword.getText();
	}

	@Override
	public void createControl(Composite mainControl) {

		Composite composite = createComposite(mainControl, 2);
		setControl(composite);

		createLabel(composite, "Server URL:");

		txtServerURL = createTextField(composite);
		validator.addControl(txtServerURL, "Invalid URL");

		createLabel(composite, "Project Path:");

		txtProjectPath = createTextField(composite);
		validator.addControl(txtProjectPath, "Invalid path");

		createLabel(composite, "UserName:");

		txtUserName = createTextField(composite);
		validator.addControl(txtUserName, "Cannot be null");

		createLabel(composite, "Password:");

		txtPassword = createTextField(composite);
		validator.addControl(txtPassword, "Cannot be null");
	}

	@Override
	public boolean isPageComplete() {
		super.isPageComplete();
		return validator.isValidated();
	}

	private void updateStatus(String message) {

		setErrorMessage(message);
		if (message == null)
			setMessage("Click finish to continue");
	}
}
