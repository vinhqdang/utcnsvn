package ivc.compare;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Class used to compare two files
 * 
 * @author alexm
 * 
 */
public class IVCCompareEditorInput extends CompareEditorInput {
	/**
	 * the left resource to compare
	 */
	IDiffComparable left;
	/**
	 * the right resource to compare
	 */
	IDiffComparable right;

	public IVCCompareEditorInput(CompareConfiguration config) {
		super(config);

	}

	@Override
	protected Object prepareInput(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		Differencer diferencer = new Differencer();
		DiffNode result;

		result = (DiffNode) diferencer.findDifferences(false, monitor, null, null, left, right);
		return result;

	}

	public void setLeft(IDiffComparable resource) {
		left = resource;
	}

	public void setRight(IDiffComparable resource) {
		right = resource;
	}
}
