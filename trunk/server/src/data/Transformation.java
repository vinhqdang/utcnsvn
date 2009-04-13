package data;

import java.util.Date;
import java.util.UUID;

import action.SimpleAction;

public class Transformation {
	
	private UUID transformationID;
	private UUID userID;
	private SimpleAction action;
	private UUID fileID;
	private String filePath;
	private Date date;
	
	
	public Transformation() {
		super();
	}

	public UUID getTransformationID() {
		return transformationID;
	}

	public void setTransformationID(UUID transformationID) {
		this.transformationID = transformationID;
	}

	public UUID getUserID() {		
		return userID;
	}

	public void setUserID(UUID userID) {
		this.userID = userID;
	}

	public SimpleAction getAction() {
		return action;
	}

	public void setAction(SimpleAction action) {
		this.action = action;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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
