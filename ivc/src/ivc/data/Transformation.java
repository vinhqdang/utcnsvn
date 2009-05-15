package ivc.data;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Transformation implements Serializable {
	
	/**
	 * used for serialization
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int OPERATION_DELETE = -1;
	private static final int OPERATION_ADD = 1;
	
	private UUID transformationID;
	
	private String userID;
	
	private String filePath;
	
	private String fileVersion;
	
	private Date date;
	
	private int line;
	
	private int position;
	
	private String text;
	
	/**
	 *  DELETE / INSERT
	 */
	private int operationType;
	
	
	public Transformation()  {
		transformationID = UUID.randomUUID();
	}
	
	public Transformation(UUID transformationID){
		this.transformationID = transformationID;
	}

	public UUID getTransformationID() {
		return transformationID;
	}

	public void setTransformationID(UUID transformationID) {
		this.transformationID = transformationID;
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
	 * @return the fileVersion
	 */
	public String getFileVersion() {
		return fileVersion;
	}

	/**
	 * @param fileVersion the fileVersion to set
	 */
	public void setFileVersion(String fileVersion) {
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
	 * @return the line
	 */
	public int getLine() {
		return line;
	}

	/**
	 * @param line the line to set
	 */
	public void setLine(int line) {
		this.line = line;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
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
	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}

	
	/**
	 * @return the OPERATION_DELETE
	 */
	public static int getOPERATION_DELETE() {
		return OPERATION_DELETE;
	}

	/**
	 * @return the OPERATION_ADD
	 */
	public static int getOPERATION_ADD() {
		return OPERATION_ADD;
	}

		
	
	
	

}
