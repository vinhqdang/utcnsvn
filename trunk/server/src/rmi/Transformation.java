package rmi;

import java.util.Date;

import action.SimpleAction;

public class Transformation {
	
	private Long transformationID;
	private Long userID;
	private SimpleAction action;
	private String filePath;
	private Date date;
	
	public Transformation() {
		super();
	}

	public Long getTransformationID() {
		return transformationID;
	}

	public void setTransformationID(Long transformationID) {
		this.transformationID = transformationID;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
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
	
	
	
	
	

}
