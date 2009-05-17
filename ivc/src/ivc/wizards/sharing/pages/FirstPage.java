package ivc.wizards.sharing.pages;

import ivc.plugin.ImageDescriptorManager;
import ivc.wizards.BaseWizardPage;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class FirstPage extends BaseWizardPage {

	public FirstPage() {
		super("Select repository");
		
		ImageDescriptor img=ImageDescriptorManager.getImageDescriptor(ImageDescriptorManager.SHARE_WIZARD);
		setImageDescriptor(img);
	}

	@Override
	public void createControl(Composite mainControl) {

		Composite composite = createComposite(mainControl, 2);
		setControl(composite);
		Text text = createTextField(composite);
		text.setText("the first Text");
	}
}
