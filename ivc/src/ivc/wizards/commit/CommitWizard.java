package ivc.wizards.commit;

import ivc.wizards.commit.pages.CommitWizardPage;

import org.eclipse.jface.wizard.Wizard;

public class CommitWizard extends Wizard {
	private CommitWizardPage svnWizardDialogPage;
	private CommitWizardDialog parentDialog;

	public CommitWizard(CommitWizardPage svnWizardDialogPage) {
		super();
		this.svnWizardDialogPage = svnWizardDialogPage;
	}

	public void addPages() {
		super.addPages();
		setWindowTitle(svnWizardDialogPage.getWindowTitle());
		addPage(svnWizardDialogPage);
	}

	public boolean performFinish() {
		return svnWizardDialogPage.performFinish();
	}

	public boolean performCancel() {
		return svnWizardDialogPage.performCancel();
	}

	public CommitWizardPage getSvnWizardDialogPage() {
		return svnWizardDialogPage;
	}

	public void setParentDialog(CommitWizardDialog dialog) {
		this.parentDialog = dialog;
	}

	public void finishAndClose() {
		if (parentDialog != null && canFinish()) {
			parentDialog.finishPressed();
		}
	}

}
