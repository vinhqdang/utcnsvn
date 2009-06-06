package ivc.fireworks.actions;

import javax.jws.Oneway;

import ivc.data.commands.CommandArgs;
import ivc.data.commands.UpdateCommand;
import ivc.util.Constants;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class UpdateAction implements IWorkbenchWindowActionDelegate {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IWorkbenchWindow arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(IAction arg0) {
		UpdateCommand uc = new UpdateCommand();
		CommandArgs args =  new CommandArgs();
		args.putArgument(Constants.IVCPROJECT, null);
		args.putArgument(Constants.FILE_PATHS,null);
		uc.execute(args);
		System.out.println("trestdse");
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub

	}

}
