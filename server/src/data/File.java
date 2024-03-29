package data;

import java.util.Date;
import java.util.UUID;

public class File {
	
	private UUID fileID;
	private String path;
	private Long creatorID;
	private Date creationTime;
	private Long projectID;
		
	public File() {
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

	public Long getProjectID() {
		return projectID;
	}

	public void setProjectID(Long projectID) {
		this.projectID = projectID;
	}

	/**
	 * @return the fileID
	 */
	public UUID getFileID() {
		return fileID;
	}

	/**
	 * @param fileID the fileID to set
	 */
	public void setFileID(UUID fileID) {
		this.fileID = fileID;
	}
	
	
	
	

}
