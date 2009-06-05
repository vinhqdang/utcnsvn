package ivc.wizards.commit;

import ivc.wizards.commit.pages.CommitWizardPage;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class CommitWizardDialog extends WizardDialog {

	public boolean yesNo;
	// private IDialogSettings settings;
	private CommitWizardPage wizardPage;

	public CommitWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
		// settings = SVNUIPlugin.getPlugin().getDialogSettings();
		CommitWizard wizard = (CommitWizard) getWizard();
		wizardPage = wizard.getSvnWizardDialogPage();
	}

	public CommitWizardDialog(Shell parentShell, IWizard newWizard, boolean yesNo) {
		this(parentShell, newWizard);
		this.yesNo = yesNo;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		wizardPage.createButtonsForButtonBar(parent, this);
		super.createButtonsForButtonBar(parent);
		if (yesNo) {
			Button cancelButton = getButton(IDialogConstants.CANCEL_ID);
			if (cancelButton != null)
				cancelButton.setText("No");
		}
	}

	public Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		String customLabel;
		if (id == IDialogConstants.FINISH_ID) {
			if (yesNo)
				customLabel = "Yes";
			else
				customLabel = "OK";
		} else
			customLabel = label;
		return super.createButton(parent, id, customLabel, defaultButton);
	}

	protected void cancelPressed() {
		// saveLocation();
		super.cancelPressed();
	}

	public void finishPressed() {
		// saveLocation();
		super.finishPressed();
	}

	protected void okPressed() {
		// saveLocation();
		super.okPressed();
	}

	protected Point getInitialSize() {
		// try {
		////	        int x = settings.getInt(wizardPage.getName() + ".size.x"); //$NON-NLS-1$
		////	        int y = settings.getInt(wizardPage.getName() + ".size.y"); //$NON-NLS-1$
		// // return new Point(x, y);
		// } catch (NumberFormatException e) {}
		return super.getInitialSize();
	}

	// protected void saveLocation() {
	// int x = getShell().getLocation().x;
	// int y = getShell().getLocation().y;
	////        settings.put(wizardPage.getName() + ".location.x", x); //$NON-NLS-1$
	////        settings.put(wizardPage.getName() + ".location.y", y); //$NON-NLS-1$  
	// x = getShell().getSize().x;
	// y = getShell().getSize().y;
	////        settings.put(wizardPage.getName() + ".size.x", x); //$NON-NLS-1$
	////        settings.put(wizardPage.getName() + ".size.y", y); //$NON-NLS-1$    
	// wizardPage.saveSettings();
	// }

}