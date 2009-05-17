package ivc.fireworks.actions;

import java.io.File;

import ivc.compare.IVCCompareEditorInput;
import ivc.repository.SVNLocalCompareInput;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.contentmergeviewer.TextMergeViewer;
import org.eclipse.compare.internal.TextMergeViewerCreator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.team.ui.synchronize.SyncInfoCompareInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class CompareEditorAction implements IWorkbenchWindowActionDelegate {
	public void run(IAction action) {
		
		try {
			CompareConfiguration config =new CompareConfiguration();
			config.setLeftEditable(true);
			config.setRightEditable(true);
			IVCCompareEditorInput input=new IVCCompareEditorInput(config);
			CompareUI.openCompareDialog(input);
			//TextMergeViewerCreator cre=new TextMergeViewerCreator();
			//cre.createViewer(parent, mp)
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
