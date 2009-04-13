package ivc.sync.org;


import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.team.internal.ui.ITeamUIImages;
import org.eclipse.team.ui.TeamImages;
import org.eclipse.team.ui.TeamUI;
import org.eclipse.team.ui.synchronize.*;
import org.eclipse.team.ui.synchronize.ISynchronizeManager;
import org.eclipse.team.ui.synchronize.ISynchronizeParticipant;


public class LocalHistorySynchronizeWizard extends Wizard {

	private class MessagePage extends WizardPage {		
		protected MessagePage(String pageName, String title, ImageDescriptor titleImage) {
			super(pageName, title, titleImage);
		}

		public void createControl(Composite parent) {
			Composite top = new Composite(parent, SWT.NONE);
			top.setLayout(new GridLayout());
			top.setLayoutData(new GridData(GridData.FILL_BOTH));
			Label label = new Label(top, SWT.WRAP);
			label.setText("This will create a synchronization against the latest file state in local history."); //$NON-NLS-1$
			label.setLayoutData(new GridData(GridData.FILL_BOTH));
			setControl(top);
		}
	}
	
	public LocalHistorySynchronizeWizard() {
		super();
	}
	
	public void addPages() {
		addPage(new MessagePage("Local History", "Create a local history synchronization", TeamImages.getImageDescriptor(ITeamUIImages.IMG_WIZBAN_SHARE)));  //$NON-NLS-1$//$NON-NLS-2$
	}

	public boolean performFinish() {
		LocalHistoryParticipant participant = new LocalHistoryParticipant();
		ISynchronizeManager manager = TeamUI.getSynchronizeManager();
		manager.addSynchronizeParticipants(new ISynchronizeParticipant[] {participant});
		ISynchronizeView view = manager.showSynchronizeViewInActivePage();
try{
		view.display(participant);
}catch(Exception e){
	e.printStackTrace();
}
		return true;
	}
}
