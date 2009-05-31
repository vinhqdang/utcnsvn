package ivc.data;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.UUID;

public class Transformation implements Serializable {
	
	/**
	 * used for serialization
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int CHARACTER_DELETE = -1;
	public static final int CHARACTER_ADD = 1;
	
	public static final int ADD_FILE = 2;
	public static final int ADD_FOLDER = 3;
	
	public static final int REMOVE_FILE = -2;
	public static final int REMOVE_FOLDER = -3;
	
	
	private String userID;
	
	private String filePath;
	
	/*
	 * Version of file on which this transformation took place 
	 * Mandatory to be set for each transformation
	 */
	private Integer fileVersion;
	
	private Date date;
	
	private Integer position;
	
	private String text;
	
	private Boolean commited;
	
	/**
	 *  DELETE / INSERT
	 */
	private Integer operationType;
	
	
	public Transformation()  {
		date = new Date();
	}
	

	/**
	 * @return the userID
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * @param userID the userID to set
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

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
	 * @return the commited
	 */
	public Boolean getCommited() {
		return commited;
	}

	/**
	 * @param commited the commited to set
	 */
	public void setCommited(Boolean commited) {
		this.commited = commited;
	}

	/**
	 * @return the fileVersion
	 */
	public Integer getFileVersion() {
		return fileVersion;
	}

	/**
	 * @param fileVersion the fileVersion to set
	 */
	public void setFileVersion(Integer fileVersion) {
		this.fileVersion = fileVersion;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the position
	 */
	public Integer getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Integer position) {
		this.position = position;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the operationType
	 */
	public int getOperationType() {
		return operationType;
	}

	/**
	 * @param operationType the operationType to set
	 */
	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}
	
	
	public void applyStructureTransformation(){
		
	}
	
	public StringBuffer applyContentTransformation(StringBuffer content){
		//TODO 1. apply transformation
//		String[] lines = content.toString().split("\n");		
//		if (lines.length > 0 && lines.length > line){
//			StringBuffer lineStr = new StringBuffer(lines[line]);
//			switch (operationType) {
//			case CHARACTER_ADD:
//				lineStr.insert(position.intValue(), text);
//				break;
//			case CHARACTER_DELETE:
//				lineStr.deleteCharAt(position);
//				break;
//			default:
//				break;
//			}
//			lines[line] = lineStr.toString();
//		}
//		return new StringBuffer(lines.toString());
		return content;
	}



		
	
	
	

}
