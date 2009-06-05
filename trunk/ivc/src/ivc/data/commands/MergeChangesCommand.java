/**
 * 
 */
package ivc.data.commands;

import ivc.data.operation.Operation;
import ivc.data.operation.OperationHistory;
import ivc.data.operation.OperationHistoryList;
import ivc.util.Constants;
import ivc.util.FileUtils;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author danielan
 * 
 */
public class MergeChangesCommand implements CommandIntf {

	private OperationHistoryList thl1;
	private OperationHistoryList thl2;

	private OperationHistoryList newThl1;
	private OperationHistoryList newThl2;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.data.command.CommandIntf#execute(ivc.data.command.CommandArgs)
	 */
	@Override
	public Result execute(CommandArgs args) {
		// TODO 1.implement merge command
		String thl1Path = (String) args.getArgumentValue(Constants.OPERATION_HIST_LIST1);
		thl1 = (OperationHistoryList) FileUtils.readObjectFromFile(thl1Path);
		String thl2Path = (String) args.getArgumentValue(Constants.OPERATION_HIST_LIST2);
		thl2 = (OperationHistoryList) FileUtils.readObjectFromFile(thl2Path);

		newThl1 = new OperationHistoryList();
		newThl2 = new OperationHistoryList();
		if (thl1 == null || thl1.getTransformationHist().isEmpty()) {
			newThl2 = thl2;
			args.putArgument(Constants.OPERATION_HIST_LIST1, newThl1);
			args.putArgument(Constants.OPERATION_HIST_LIST2, newThl2);
			return new Result(true, "Success", null);
		}
		if (thl2 == null || thl2.getTransformationHist().isEmpty()) {
			newThl1 = thl1;
			args.putArgument(Constants.OPERATION_HIST_LIST1, newThl1);
			args.putArgument(Constants.OPERATION_HIST_LIST2, newThl2);
			return new Result(true, "Success", null);
		}

		// build newTh1
		Iterator<OperationHistory> it = thl1.iterator();
		while (it.hasNext()) {
			OperationHistory th1 = it.next();
			String filePath = th1.getFilePath();
			if (th1.getTransformations().getFirst().getOperationType() == Operation.REMOVE_FILE
					|| th1.getTransformations().getFirst().getOperationType() == Operation.REMOVE_FOLDER) {
				newThl1.appendOperation(th1.getTransformations().getFirst());
			} else
			// if both lists have transformations for the same file
			if (thl2.getTransformationsForFile(filePath) != null) {
				LinkedList<Operation> trs1 = thl1.getTransformationsForFile(filePath);
				LinkedList<Operation> trs2 = thl2.getTransformationsForFile(filePath);
				if (trs2.getFirst().getOperationType() == Operation.REMOVE_FILE || trs2.getFirst().getOperationType() == Operation.REMOVE_FOLDER) {
					newThl2.appendOperation(trs2.getFirst());
				} else {
					// merge lists of content transformations
					Iterator<Operation> itt1 = trs1.descendingIterator();
					while (itt1.hasNext()) {
						Operation tr1 = itt1.next();
						Operation newTr1 = tr1;
						Iterator<Operation> itt2 = trs2.descendingIterator();
						while (itt2.hasNext()) {
							Operation tr2 = itt2.next();
							newTr1 = mergeTransformations(newTr1, tr2);
						}
						newThl1.appendOperation(newTr1);
					}
				}
			} else {
				// only thl1 modified the file content
				newThl1.appendOperationHistory(th1);
			}
		}

		// build newTh2
		it = thl2.iterator();
		while (it.hasNext()) {
			OperationHistory th2 = it.next();
			String filePath = th2.getFilePath();
			if (th2.getTransformations().getFirst().getOperationType() == Operation.REMOVE_FILE
					|| th2.getTransformations().getFirst().getOperationType() == Operation.REMOVE_FOLDER) {
				newThl2.appendOperation(th2.getTransformations().getFirst());
			} else
			// if both lists have transformations for the same file
			if (thl1.getTransformationsForFile(filePath) != null) {
				LinkedList<Operation> trs1 = thl1.getTransformationsForFile(filePath);
				LinkedList<Operation> trs2 = thl2.getTransformationsForFile(filePath);
				if (trs1.getFirst().getOperationType() == Operation.REMOVE_FILE || trs1.getFirst().getOperationType() == Operation.REMOVE_FOLDER) {
					newThl1.appendOperation(trs2.getFirst());
				} else {
					// merge lists of content transformations
					Iterator<Operation> itt2 = trs2.descendingIterator();
					while (itt2.hasNext()) {
						Operation tr2 = itt2.next();
						Operation newTr2 = tr2;
						Iterator<Operation> itt1 = trs1.descendingIterator();
						while (itt1.hasNext()) {
							Operation tr1 = itt1.next();
							newTr2 = mergeTransformations(newTr2, tr1);
						}
						newThl2.appendOperation(newTr2);
					}
				}
			} else {
				// only thl1 modified the file content
				newThl2.appendOperationHistory(th2);
			}
		}
		args.putArgument(Constants.OPERATION_HIST_LIST1, newThl1);
		args.putArgument(Constants.OPERATION_HIST_LIST2, newThl2);
		return new Result(true, "Success", null);
	}

	/**
	 * merge:H H := []; append(RCL, H); UL := []; append(LL, UL); for (i:=0; i<|RUL|; i++) append(RUL[i], UL); for (i:=0; i<|UL|; i++) { O := UL[i];
	 * j:=0; while (j<|H| and H[j]!O) j++; O0 := transformSOCT2(O, sublist(H,j,|H|)); append(O0, H); } return H;
	 */

	/**
	 * T1 includes changes from T2
	 */
	private Operation mergeTransformations(Operation t1, Operation t2) {
		// get a clone of this operation
		Operation operation = new Operation();
		operation.setCommited(t1.getCommited());
		operation.setDate(t1.getDate());
		operation.setFilePath(t1.getFilePath());
		operation.setFileVersion(t1.getFileVersion());
		operation.setOperationType(t1.getOperationType());
		operation.setText(t1.getChr());
		operation.setUserID(t1.getUserID());

		// the positions of the operations
		int t1Position = t1.getPosition();
		int t2Position = t2.getPosition();

		// the effects of the operations
		int t1Type = t1.getOperationType();
		int t2Type = t2.getOperationType();

		// both local and remote operations are insertions
		if (t1Type == Operation.CHARACTER_ADD && t2Type == Operation.CHARACTER_ADD) {
			if (t1Position >= t2Position) {
				operation.setPosition(t1Position + 1);
			}
		}

		// operation1 is insertion and operation2 is deletion
		if (t1Type == Operation.CHARACTER_ADD && t2Type == Operation.CHARACTER_DELETE) {
			if (t1Position > t2Position) {
				operation.setPosition(t1Position - 1);
			}
		}

		// operation1 is deletion and operation2 is insertion
		if (t1Type == Operation.CHARACTER_DELETE && t2Type == Operation.CHARACTER_ADD) {
			if (t1Position >= t2Position) {
				operation.setPosition(t1Position + 1);
			}
		}

		// both operations are deletions
		if (t1Type == Operation.CHARACTER_DELETE && t2Type == Operation.CHARACTER_DELETE) {
			if (t1Position > t2Position) {
				operation.setPosition(t1Position - 1);
			}
		}
		return operation;
	}

}
