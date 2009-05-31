package ivc.wizards.checkout.pages;

import java.lang.reflect.InvocationTargetException;

import ivc.data.commands.FindHostProjectCommand;
import ivc.plugin.ImageDescriptorManager;
import ivc.wizards.BaseWizardPage;
import ivc.wizards.checkout.CheckoutWizard;
import ivc.wizards.validation.Validator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class CheckoutWizardPage extends BaseWizardPage {
	public final String ERROR_USER_NULL = "Some fields are not filled";
	private Text txtServerURL;
	private Text txtPath;
	private Validator validator;
	
	
	public CheckoutWizardPage(String pageName) {
		super(pageName);
		setTitle(pageName);
		ImageDescriptor img = ImageDescriptorManager
				.getImageDescriptor(ImageDescriptorManager.SHARE_WIZARD);
		setImageDescriptor(img);
		validator = new Validator() {
			@Override
			public void setError(String error) {
				updateStatus(getValidatorErrorMessage());
			}
		};
	}
	
	

	/**
	 * @return the txtServerURL
	 */
	public Text getTxtServerURL() {
		return txtServerURL;
	}



	/**
	 * @param txtServerURL the txtServerURL to set
	 */
	public void setTxtServerURL(Text txtServerURL) {
		this.txtServerURL = txtServerURL;
	}



	/**
	 * @return the txtPath
	 */
	public Text getTxtPath() {
		return txtPath;
	}



	/**
	 * @param txtPath the txtPath to set
	 */
	public void setTxtPath(Text txtPath) {
		this.txtPath = txtPath;
	}



	@Override
	public void createControl(Composite mainControl) {
		Composite composite = createComposite(mainControl, 2);
		setControl(composite);

		createLabel(composite, "Server URL:");
		txtServerURL = createTextField(composite);
		validator.addControl(txtServerURL, "Url cannot be null");
		
		createLabel(composite, "Project Path:");
		txtPath = createTextField(composite);
		validator.addControl(txtPath, "Project path cannot be null");

	}

	@Override
	public IWizardPage getNextPage() {
		if (!validator.isValidated()){
			return this;
		}
		FindHostProjectCommand find = new FindHostProjectCommand(txtServerURL.getText(), txtPath.getText());
		// fork, cancelable, process
		try {
			getContainer().run(false, true, find);
		} catch (InvocationTargetException e) {
			updateStatus(e.getMessage());
		} catch (InterruptedException e) {
			updateStatus(e.getMessage());
		}
		if (find.getResult().isSuccess())
			return ((CheckoutWizard) getWizard()).getProjectWizardPage();
		else {
			updateStatus(find.getResult().getMessage());
			return this;
		}
	}

	
	private void updateStatus(String message) {
		setErrorMessage(message);	
	}
}
