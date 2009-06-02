package ivc.data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class OperationHistory implements Serializable {

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
	
	

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return the operations
	 */
	public LinkedList<Operation> getTransformations() {
		return operations;
	}

	/**
	 * @param operations the operations to set
	 */
	public void setTransformations(LinkedList<Operation> operations) {
		this.operations = operations;
	}
	
	public void addTransformations(LinkedList<Operation> trs){
		this.operations.addAll(trs);
	}
	
	public void addTransformation(Operation tr){
		operations.addFirst(tr);
	}
	
	
	
	

}
