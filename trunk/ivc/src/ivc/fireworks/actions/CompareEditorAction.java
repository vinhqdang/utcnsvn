package ivc.fireworks.actions;

import ivc.compare.IVCCompareEditorInput;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class CompareEditorAction implements IWorkbenchWindowActionDelegate {
	public void run(IAction action) {

		try {
			CompareConfiguration config = new CompareConfiguration();
			config.setLeftEditable(true);
			config.setRightEditable(true);
			IVCCompareEditorInput input = new IVCCompareEditorInput(config);
			CompareUI.openCompareDialog(input);
			
		} catch (Exception e) {

			System.out.println("Exception comparing");
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IWorkbenchWindow arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub

	}
}
