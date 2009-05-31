package ivc.compare;

import ivc.util.StringUtils;

import java.io.InputStream;

import org.eclipse.compare.contentmergeviewer.TokenComparator;
import org.eclipse.compare.rangedifferencer.RangeDifference;
import org.eclipse.compare.rangedifferencer.RangeDifferencer;

public class StringComparer {
	private InputStream left;
	private InputStream right;

	public StringComparer(InputStream left, InputStream right) {
		this.left = left;
		this.right = right;
	}

	public void compare() {

		try {
			String SL = StringUtils.readFromInputStream(left);
			String SR = StringUtils.readFromInputStream(right);
			// System.out.println(SL);
			TokenComparator CL = new TokenComparator(SL);
			TokenComparator CR = new TokenComparator(SR);

			RangeDifference[] diff = RangeDifferencer.findDifferences(CL, CR);
			System.out.println("Changes to file: " + diff.length);
			for (int i = 0; i < diff.length; i++) {
				System.out.println("-|" + SL.substring(CL.getTokenStart(diff[i].leftStart()), CL.getTokenStart(diff[i].leftEnd())) + "|");
				System.out.println("+|" + SR.substring(CR.getTokenStart(diff[i].rightStart()), CR.getTokenStart(diff[i].rightEnd())) + "|");
			}
		} catch (Exception e) {
			System.out.println("coreException wtf???");
			e.printStackTrace();
		}
	}
}
