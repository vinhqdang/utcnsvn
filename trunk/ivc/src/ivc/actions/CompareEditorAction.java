package ivc.actions;

import ivc.compare.DiffComparableFactory;
import ivc.compare.IVCCompareEditorInput;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchWindow;

public class CompareEditorAction extends BaseActionDelegate {
	public void run(IAction action) {

		try {
			CompareConfiguration config = new CompareConfiguration();
			IResource[] resources = getSelectedResources();
			config.setLeftEditable(true);
			config.setRightEditable(true);
			IVCCompareEditorInput input = new IVCCompareEditorInput(config);
			input.setLeft(DiffComparableFactory.createComparable(resources[0]));
			input.setRight(DiffComparableFactory.createComparable(resources[1]));
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
