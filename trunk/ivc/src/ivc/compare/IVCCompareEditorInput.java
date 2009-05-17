package ivc.compare;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.ISaveablesSource;
import org.eclipse.ui.Saveable;

public class IVCCompareEditorInput extends CompareEditorInput implements ISaveablesSource{
	ResourceEditionNode left;
	ResourceEditionNode right;
	public IVCCompareEditorInput(CompareConfiguration config) {
		super(config);
	}

	@Override
	protected Object prepareInput(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		left = new ResourceEditionNode((IFile) (ResourcesPlugin.getWorkspace()
				.getRoot().getFile(new Path("asd\\a.txt"))));
		
		 right= new ResourceEditionNode((IFile) (ResourcesPlugin.getWorkspace()
				.getRoot().getFile(new Path("asd\\b.txt"))));
		
		SummaryDifferencer diferencer = new SummaryDifferencer();

		return diferencer.findDifferences(false, monitor, left, right, left, right);
	}

	@Override
	public Saveable[] getActiveSaveables() {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public Saveable[] getSaveables() {
		// TODO Auto-generated method stub
		return null;
	}

}
