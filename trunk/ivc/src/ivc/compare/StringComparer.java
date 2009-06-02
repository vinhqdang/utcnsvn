package ivc.compare;

import ivc.data.Operation;
import ivc.data.OperationHistory;
import ivc.manager.ProjectsManager;
import ivc.util.StringUtils;

import java.io.InputStream;

import javax.print.attribute.standard.MediaSize.Other;

import org.eclipse.compare.contentmergeviewer.TokenComparator;
import org.eclipse.compare.rangedifferencer.RangeDifference;
import org.eclipse.compare.rangedifferencer.RangeDifferencer;
import org.eclipse.core.resources.IFile;

public class StringComparer {
	private IFile file;
	private InputStream right;
	private OperationHistory history;
	private int fileVersion;

	public StringComparer(IFile file, InputStream right) {
		this.file = file;
		this.right = right;
		this.fileVersion = ProjectsManager.instance().getFileVersion(file);
		history = new OperationHistory();
		history.setFilePath(file.getLocation().toOSString());
	}

	public void compare() {

		try {
			String SL = StringUtils.readFromInputStream(file.getContents());
			String SR = StringUtils.readFromInputStream(right);
			// System.out.println(SL);
			TokenComparator CL = new TokenComparator(SL);
			TokenComparator CR = new TokenComparator(SR);

			RangeDifference[] diff = RangeDifferencer.findDifferences(CL, CR);
			System.out.println("Changes to file: " + diff.length);
			for (int i = 0; i < diff.length; i++) {
				String addedText = SL.substring(CL.getTokenStart(diff[i].leftStart()), CL.getTokenStart(diff[i].leftEnd()));
				System.out.println("+|" + addedText + "|");
				addOperations(addedText, Operation.CHARACTER_ADD);

				String removedText = SR.substring(CR.getTokenStart(diff[i].rightStart()), CR.getTokenStart(diff[i].rightEnd()));
				System.out.println("-|" + removedText + "|");
				addOperations(removedText, Operation.CHARACTER_DELETE);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addOperations(String value, int operationType) {
		if (value == null)
			return;
		char[] chars = value.toCharArray();
		if (operationType == Operation.CHARACTER_ADD) {
			for (Character c : chars) {
				Operation operation = new Operation(c, operationType);
				operation.setFileVersion(fileVersion);
				history.addOperation(operation);
			}
		} else {
			for (int i = value.length() - 1; i > -1; i--) {
				Operation operation = new Operation(chars[i], operationType);
				operation.setFileVersion(fileVersion);
				history.addOperation(operation);
			}
		}
	}
}
