package ivc.data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class TransformationHistory implements Serializable {

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
	private LinkedList<Transformation> transformations;
	
	

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
	 * @return the transformations
	 */
	public LinkedList<Transformation> getTransformations() {
		return transformations;
	}

	/**
	 * @param transformations the transformations to set
	 */
	public void setTransformations(LinkedList<Transformation> transformations) {
		this.transformations = transformations;
	}
	
	public void addTransformations(LinkedList<Transformation> trs){
		this.transformations.addAll(trs);
	}
	
	
	
	

}
