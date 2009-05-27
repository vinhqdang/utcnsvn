package ivc.compare;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import ivc.util.StringUtils;

import org.eclipse.compare.contentmergeviewer.TokenComparator;
import org.eclipse.compare.rangedifferencer.RangeDifference;
import org.eclipse.compare.rangedifferencer.RangeDifferencer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

public class StringComparer {
	static ResourceEditionNode left;
	static ResourceEditionNode right;

	public static void main(String[] args) throws FileNotFoundException {
//		left = new ResourceEditionNode((IFile) (ResourcesPlugin.getWorkspace().getRoot()
//				.getFile(new Path("asd\\a.txt"))));

	//	right = new ResourceEditionNode((IFile) (ResourcesPlugin.getWorkspace().getRoot()
//				.getFile(new Path("asd\\b.txt"))));

		try {
			File file1=new File("d:\\temp\\a.txt");
			File file2=new File("d:\\temp\\aa.txt");
			FileInputStream f1 = new FileInputStream(file1);
			FileInputStream f2 = new FileInputStream(file2);
			String s1 = StringUtils.readFromInputStream(f1);
			String s2 = StringUtils.readFromInputStream(f2);

			TokenComparator comp1 = new TokenComparator(s1);
			TokenComparator comp2 = new TokenComparator(s2);

			RangeDifference[] diff = RangeDifferencer.findDifferences(comp1, comp2);
			System.out.println(diff.length + RangeDifference.CHANGE + " ");
			for (int i = 0; i < diff.length; i++) {
				System.out.println(s1.substring(comp1.getTokenStart(diff[i].leftStart()), comp1
						.getTokenStart(diff[i].leftStart())
						+ comp1.getTokenLength(diff[i].leftLength())));
			}
		} catch (Exception e) {
			System.out.println("coreException wtf???");
			e.printStackTrace();
		}
	}
}
