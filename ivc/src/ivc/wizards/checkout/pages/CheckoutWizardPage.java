package ivc.wizards.checkout.pages;

import ivc.plugin.ImageDescriptorManager;
import ivc.wizards.BaseWizardPage;
import ivc.wizards.checkout.CheckoutWizard;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class CheckoutWizardPage extends BaseWizardPage {

	Text txtServerURL;
	Text txtPath;

	public CheckoutWizardPage(String pageName) {
		super(pageName);
		setTitle(pageName);
		ImageDescriptor img = ImageDescriptorManager
				.getImageDescriptor(ImageDescriptorManager.SHARE_WIZARD);
		setImageDescriptor(img);
	}

	@Override
	public void createControl(Composite mainControl) {
		Composite composite = createComposite(mainControl, 2);
		setControl(composite);

		createLabel(composite, "Server URL:");
		txtServerURL = createTextField(composite);

		createLabel(composite, "Project Path:");
		txtPath = createTextField(composite);

	}

	@Override
	public IWizardPage getNextPage() {
		return ((CheckoutWizard) getWizard()).projectPage;
	}

	@Override
	public boolean canFlipToNextPage() {
		return true;
	}
}
