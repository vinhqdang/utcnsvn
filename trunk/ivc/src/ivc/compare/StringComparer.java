package ivc.compare;

import ivc.util.StringUtils;

import org.eclipse.compare.contentmergeviewer.TokenComparator;
import org.eclipse.compare.rangedifferencer.RangeDifference;
import org.eclipse.compare.rangedifferencer.RangeDifferencer;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

public class StringComparer {
	ResourceEditionNode left;
	ResourceEditionNode right;

	public void main() {
		left = new ResourceEditionNode((IFile) (ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new Path("asd\\a.txt"))));

		right = new ResourceEditionNode((IFile) (ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new Path("asd\\b.txt"))));

		try {
			String s1 = StringUtils.readFromInputStream(left.getContents());
			String s2 = StringUtils.readFromInputStream(right.getContents());

			TokenComparator comp1 = new TokenComparator(s1);
			TokenComparator comp2 = new TokenComparator(s2);

			RangeDifference[] diff = RangeDifferencer.findDifferences(comp1, comp2);
			System.out.println(diff.length + RangeDifference.CHANGE + " ");
			for (int i = 0; i < diff.length; i++) {
				System.out.println(s1.substring(comp1.getTokenStart(diff[i].leftStart()), comp1
						.getTokenStart(diff[i].leftStart())
						+ comp1.getTokenLength(diff[i].leftLength())));
			}
		} catch (CoreException e) {
			System.out.println("coreException wtf???");
		}
	}
}
