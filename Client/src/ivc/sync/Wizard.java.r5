package ivc.sync;

import org.eclipse.team.core.subscribers.Subscriber;
import org.eclipse.team.ui.TeamUI;
import org.eclipse.team.ui.synchronize.ISynchronizeManager;
import org.eclipse.team.ui.synchronize.ISynchronizeParticipant;
import org.eclipse.team.ui.synchronize.ISynchronizeView;

public class Wizard extends org.eclipse.jface.wizard.Wizard {


		@Override
	public boolean performFinish() {		
		LocalSubscriber participant = new LocalSubscriber();
		ISynchronizeManager manager = TeamUI.getSynchronizeManager();
		manager.addSynchronizeParticipants(new ISynchronizeParticipant[] {participant});
		ISynchronizeView view = manager.showSynchronizeViewInActivePage();
		Subscriber s=participant.getSubscriber();
		participant.getMatchingParticipant("1", participant.getResources());
		view.display(participant);
		
		return false;
	}

}
