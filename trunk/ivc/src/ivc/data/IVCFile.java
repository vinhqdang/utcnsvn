package ivc.data;

import java.util.Date;
import java.util.UUID;

public class IVCFile {
	
	private String path;
	private Long creatorID;
	private Date creationTime;
	private String projectName;
	
	private StringBuffer content;
	private TransformationHistory history; 
		
	public IVCFile() {
		super();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getCreatorID() {
		return creatorID;
	}

	public void setCreatorID(Long creatorID) {
		this.creatorID = creatorID;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return the content
	 */
	public StringBuffer getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(StringBuffer content) {
		this.content = content;
	}

	/**
	 * @return the history
	 */
	public TransformationHistory getHistory() {
		return history;
	}

	/**
	 * @param history the history to set
	 */
	public void setHistory(TransformationHistory history) {
		this.history = history;
	}
	
	
	
	

}
