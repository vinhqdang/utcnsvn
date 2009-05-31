package ivc.compare;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class IVCCompareEditorInput extends CompareEditorInput {
	ResourceEditionNode left;
	ResourceEditionNode right;

	public IVCCompareEditorInput(CompareConfiguration config) {
		super(config);

	}

	@Override
	protected Object prepareInput(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		Differencer diferencer = new Differencer();
		DiffNode result;

		result = (DiffNode) diferencer.findDifferences(false, monitor, null, null, left, right);
		return result;

		// try {
		// String s1 = StringUtils.readFromInputStream(left.getContents());
		// String s2 = StringUtils.readFromInputStream(right.getContents());
		//
		// TokenComparator comp1 = new TokenComparator(s1);
		// TokenComparator comp2 = new TokenComparator(s2);
		//
		// RangeDifference[] diff = RangeDifferencer.findDifferences(comp1, comp2);
		// System.out.println(diff.length + RangeDifference.CHANGE + " ____________________________");
		// for (int i = 0; i < diff.length; i++) {
		// System.out.println("left: "
		// + s1.substring(comp1.getTokenStart(diff[i].leftStart()), comp1.getTokenStart(diff[i].leftStart() + diff[i].leftLength())));
		// System.out.println("right: "
		// + s2.substring(comp2.getTokenStart(diff[i].rightStart()), comp2.getTokenStart(diff[i].rightStart() + diff[i].rightLength())));
		// }
		// } catch (Exception e) {
		// System.out.println("coreException wtf???");
		// e.printStackTrace();
		// }

	}

	public void setLeft(ResourceEditionNode resource) {
		left = resource;
	}

	public void setRight(ResourceEditionNode resource) {
		right = resource;
	}
}
