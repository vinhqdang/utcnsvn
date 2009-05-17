package ivc.wizards.trash;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.team.internal.ui.TeamUIMessages;
import org.eclipse.team.ui.IConfigurationWizard;
import org.eclipse.ui.IWorkbench;


public class NewMergeFileAssociationWizard extends Wizard implements IConfigurationWizard{
	private MergeFileAssociation[] mergeFileAssociations;
	private NewMergeFileAssociationWizardPage mainPage;
	private MergeFileAssociation mergeFileAssociation;

	public NewMergeFileAssociationWizard(MergeFileAssociation[] mergeFileAssociations) {
		this.mergeFileAssociations = mergeFileAssociations;
		setWindowTitle(TeamUIMessages.TextPreferencePage_6);
	}

	public void addPages() {
		mainPage = new NewMergeFileAssociationWizardPage("mainPage", "ass", null, mergeFileAssociations); //$NON-NLS-1$
		mainPage.setDescription("assda"); //$NON-NLS-1$		
		addPage(mainPage);		
	}

	public boolean performFinish() {
		mergeFileAssociation = new MergeFileAssociation();
		mergeFileAssociation.setFileType(mainPage.fileTypeText.getText().trim());
		if (mainPage.builtInMergeRadioButton.getSelection()) mergeFileAssociation.setType(MergeFileAssociation.BUILT_IN);
		else if (mainPage.externalMergeRadioButton.getSelection()) mergeFileAssociation.setType(MergeFileAssociation.DEFAULT_EXTERNAL);
		else if (mainPage.customMergeRadioButton.getSelection()) {
			mergeFileAssociation.setType(MergeFileAssociation.CUSTOM_EXTERNAL);
			mergeFileAssociation.setMergeProgram(mainPage.customProgramLocationCombo.getText().trim());
			mergeFileAssociation.setParameters(mainPage.customProgramParametersText.getText().trim());
		}
		return true;
	}

	public MergeFileAssociation getMergeFileAssociation() {
		return mergeFileAssociation;
	}

	@Override
	public void init(IWorkbench arg0, IProject arg1) {
		// TODO Auto-generated method stub
		
	}

}
