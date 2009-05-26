package ivc.data;

import java.io.Serializable;
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
	private List<Transformation> transformations;
	
	

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
	public List<Transformation> getTransformations() {
		return transformations;
	}

	/**
	 * @param transformations the transformations to set
	 */
	public void setTransformations(List<Transformation> transformations) {
		this.transformations = transformations;
	}
	
	public void addTransformations(List<Transformation> trs){
		this.transformations.addAll(trs);
	}
	
	
	
	

}
