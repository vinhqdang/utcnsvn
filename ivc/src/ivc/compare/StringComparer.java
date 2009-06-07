package ivc.compare;

import ivc.data.operation.Operation;
import ivc.data.operation.OperationHistory;
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
		history.setFilePath(file.getProjectRelativePath().toOSString());
	}

	public void compare() {

		try {
			String SL = StringUtils.readFromInputStream(file.getContents());
			String SR = StringUtils.readFromInputStream(right);

			TokenComparator CL = new TokenComparator(SL);
			TokenComparator CR = new TokenComparator(SR);

			RangeDifference[] diff = RangeDifferencer.findDifferences(CL, CR);
			System.out.println("Changes to file: " + diff.length);
			for (int i = 0; i < diff.length; i++) {
				String addedText = SL.substring(CL.getTokenStart(diff[i].leftStart()), CL.getTokenStart(diff[i].leftEnd()));
				System.out.println("+|" + addedText + "|");
				addOperations(addedText, Operation.CHARACTER_ADD, CL.getTokenStart(diff[i].leftStart()));

				String removedText = SR.substring(CR.getTokenStart(diff[i].rightStart()), CR.getTokenStart(diff[i].rightEnd()));
				System.out.println("-|" + removedText + "|");
				addOperations(removedText, Operation.CHARACTER_DELETE, CR.getTokenStart(diff[i].rightStart()));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addOperations(String value, int operationType, int basePosition) {
		if (value == null)
			return;
		char[] chars = value.toCharArray();
		if (operationType == Operation.CHARACTER_ADD) {
			for (int i = 0; i < chars.length; i++) {
				Operation operation = new Operation(chars[i], operationType);
				operation.setFilePath(file.getProjectRelativePath().toOSString());				
				operation.setPosition(basePosition + i);
				operation.setFileVersion(fileVersion);
				history.addOperation(operation);
			}
		} else {
			for (int i = chars.length - 1; i > -1; i--) {
				Operation operation = new Operation(chars[i], operationType);
				operation.setFilePath(file.getProjectRelativePath().toOSString());	
				operation.setFileVersion(fileVersion);
				operation.setPosition(basePosition + i);
				history.addOperation(operation);
			}
		}
	}

	public OperationHistory getOperationHistory() {
		return history;
	}
}
