package ivc.wizards.commit;

import ivc.wizards.commit.pages.CommitWizardPage;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author alexm
 * 
 *         Dialog used to set the resources to commit
 */
public class CommitWizardDialog extends WizardDialog {

	public boolean yesNo;
	private CommitWizardPage wizardPage;

	public CommitWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
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

	public Button createButton(Composite parent, int id, String label,
			boolean defaultButton) {
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

	public void finishPressed() {
		super.finishPressed();
	}
}