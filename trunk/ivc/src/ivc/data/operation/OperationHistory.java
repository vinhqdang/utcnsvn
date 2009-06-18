package ivc.data.operation;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

public class OperationHistory implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String filePath;

	/**
	 * 
	 */
	private LinkedList<Operation> operations;

	public OperationHistory() {
		operations = new LinkedList<Operation>();
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath
	 *            the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return the operations
	 */
	public LinkedList<Operation> getOperations() {
		return operations;
	}

	/**
	 * @param operations
	 *            the operations to set
	 */
	public void setOperations(LinkedList<Operation> operations) {
		this.operations = operations;
	}

	public void addOperations(LinkedList<Operation> trs) {
		this.operations.addAll(0, trs);
	}

	public void addOperation(Operation tr) {
		operations.addFirst(tr);
	}

	public void setOperation(int i, Operation op) {
		operations.set(i, op);
	}

	public OperationHistory excludeOperations(OperationHistory oh) {
		if (oh == null || oh.getOperations().isEmpty()) {
			try {
				return (OperationHistory) clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				return new OperationHistory();
			}
		}
		OperationHistory newOh = new OperationHistory();
		newOh.setFilePath(oh.getFilePath());
		Iterator<Operation> itoOwn = operations.descendingIterator();
		while (itoOwn.hasNext()) {
			Operation ownOp = itoOwn.next();
			Operation newOp = new Operation();
			try {
				newOp = (Operation) ownOp.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			Iterator<Operation> itoOther = oh.getOperations().descendingIterator();
			while (itoOther.hasNext()) {
				Operation op = itoOther.next();
				newOp = newOp.excludeOperation(op);
			}
			newOh.addOperation(newOp);
		}
		return newOh;
	}

	public OperationHistory includeOperations(OperationHistory oh) {
		OperationHistory newOh = new OperationHistory();
		newOh.setFilePath(oh.getFilePath());
		Iterator<Operation> itoOwn = operations.descendingIterator();
		while (itoOwn.hasNext()) {
			Operation ownOp = itoOwn.next();
			Operation newOp = ownOp;
			Iterator<Operation> itoOther = oh.getOperations().descendingIterator();
			while (itoOther.hasNext()) {
				Operation op = itoOther.next();
				newOp = newOp.includeOperation(op);
			}
			newOh.addOperation(newOp);
		}
		return newOh;
	}

	public void removeOperations(LinkedList<Operation> ops) {
		 operations.removeAll(ops);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		OperationHistory oh = (OperationHistory) super.clone();
		oh.setOperations(new LinkedList<Operation>());
		for (Operation op : getOperations()) {
			oh.getOperations().add((Operation) op.clone());
		}
		return oh;
	}

}
