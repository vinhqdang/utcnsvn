package ivc.actions;

import ivc.compare.IVCCompareEditorInput;
import ivc.compare.ResourceEditionNode;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class CompareEditorAction extends BaseActionDelegate {
	public void run(IAction action) {

		try {
			CompareConfiguration config = new CompareConfiguration();
			ISelection as = getSelection();
			IResource[] resources = getSelectedResources();
			config.setLeftEditable(true);
			config.setRightEditable(true);
			IVCCompareEditorInput input = new IVCCompareEditorInput(config);
			input.setLeft(new ResourceEditionNode((IFile) resources[0]));
			input.setRight(new ResourceEditionNode((IFile) resources[1]));
			CompareUI.openCompareDialog(input);

		} catch (Exception e) {

			System.out.println("Exception comparing");
		}
	}

	@Override
	public boolean menuItemEnabled() {
		return true;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IWorkbenchWindow arg0) {
		// TODO Auto-generated method stub

	}
}
