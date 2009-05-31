/**
 * 
 */
package ivc.data.commands;

import ivc.data.Transformation;
import ivc.data.TransformationHistory;
import ivc.data.TransformationHistoryList;
import ivc.util.Constants;
import ivc.util.FileUtils;

import java.util.Iterator;

/**
 * @author danielan
 * 
 */
public class MergeChangesCommand implements CommandIntf {

	private TransformationHistoryList thl1;
	private TransformationHistoryList thl2;

	private TransformationHistoryList newThl1;
	private TransformationHistoryList newThl2;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.data.command.CommandIntf#execute(ivc.data.command.CommandArgs)
	 */
	@Override
	public Result execute(CommandArgs args) {
		// TODO 1.implement merge command
		String thl1Path = (String) args.getArgumentValue(Constants.TRANSFORMATION_HIST_LIST1);
		thl1 = (TransformationHistoryList) FileUtils.readObjectFromFile(thl1Path);
		String thl2Path = (String) args.getArgumentValue(Constants.TRANSFORMATION_HIST_LIST2);
		thl2 = (TransformationHistoryList) FileUtils.readObjectFromFile(thl2Path);

		newThl1 = new TransformationHistoryList();
		newThl2 = new TransformationHistoryList();
		if (thl1 == null || thl1.getTransformationHist().isEmpty()) {
			newThl2 = thl2;
			args.putArgument(Constants.TRANSFORMATION_HIST_LIST1, newThl1);
			args.putArgument(Constants.TRANSFORMATION_HIST_LIST2, newThl2);
			return new Result(true, "Success", null);
		}
		if (thl2 == null || thl2.getTransformationHist().isEmpty()) {
			newThl1 = thl1;
			args.putArgument(Constants.TRANSFORMATION_HIST_LIST1, newThl1);
			args.putArgument(Constants.TRANSFORMATION_HIST_LIST2, newThl2);
			return new Result(true, "Success", null);
		}
		
		Iterator<TransformationHistory> it = thl1.iterator();
		while(it.hasNext()){
			TransformationHistory th1 = it.next();
		}
			

		return  new Result(true, "Success", null);
	}

	/**
	 * merge:H H := []; append(RCL, H); UL := []; append(LL, UL); for (i:=0; i<|RUL|; i++) append(RUL[i], UL); for (i:=0; i<|UL|; i++) { O := UL[i];
	 * j:=0; while (j<|H| and H[j]!O) j++; O0 := transformSOCT2(O, sublist(H,j,|H|)); append(O0, H); } return H;
	 */

	/**
	 * T1 includes changes from T2
	 */
	private Transformation mergeTransformations(Transformation t1, Transformation t2) {
		// get a clone of this operation
		Transformation transformation = new Transformation();
		transformation.setCommited(t1.getCommited());
		transformation.setDate(t1.getDate());
		transformation.setFilePath(t1.getFilePath());
		transformation.setFileVersion(t1.getFileVersion());
		transformation.setOperationType(t1.getOperationType());
		transformation.setText(t1.getText());
		transformation.setUserID(t1.getUserID());

		// the positions of the operations
		int t1Position = t1.getPosition();
		int t2Position = t2.getPosition();

		// the effects of the operations
		int t1Type = t1.getOperationType();
		int t2Type = t2.getOperationType();

		// both local and remote operations are insertions
		if (t1Type == Transformation.CHARACTER_ADD && t2Type == Transformation.CHARACTER_ADD) {
			if (t1Position >= t2Position) {
				transformation.setPosition(t1Position + t2.getText().length());
			}
		}

		// operation1 is insertion and operation2 is deletion
		if (t1Type == Transformation.CHARACTER_ADD && t2Type == Transformation.CHARACTER_DELETE) {
			if (t1Position > t2Position) {
				transformation.setPosition(t1Position - t2.getText().length());
			}
		}

		// operation1 is deletion and operation2 is insertion
		if (t1Type == Transformation.CHARACTER_DELETE && t2Type == Transformation.CHARACTER_ADD) {
			if (t1Position >= t2Position) {
				transformation.setPosition(t1Position + t2.getText().length());
			}
		}

		// both operations are deletions
		if (t1Type == Transformation.CHARACTER_DELETE && t2Type == Transformation.CHARACTER_DELETE) {
			if (t1Position > t2Position) {
				transformation.setPosition(t1Position - t2.getText().length());
			}
		}
		return transformation;
	}

}
