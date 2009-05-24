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
	
	private static final int OPERATION_DELETE = -1;
	private static final int OPERATION_ADD = 1;
	
	private UUID transformationID;
	
	private String userID;
	
	private String filePath;
	
	private Integer fileVersion;
	
	private Date date;
	
	private Integer line;
	
	private Integer position;
	
	private char text;
	
	/**
	 *  DELETE / INSERT
	 */
	private Integer operationType;
	
	
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
	 * @return the line
	 */
	public Integer getLine() {
		return line;
	}

	/**
	 * @param line the line to set
	 */
	public void setLine(Integer line) {
		this.line = line;
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
	public char getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(char text) {
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
	
	public StringBuffer applyTransformation(StringBuffer content){
		String[] lines = content.toString().split("\n");		
		if (lines.length > 0 && lines.length > line){
			StringBuffer lineStr = new StringBuffer(lines[line]);
			switch (operationType) {
			case OPERATION_ADD:
				lineStr.insert(position.intValue(), text);
				break;
			case OPERATION_DELETE:
				lineStr.deleteCharAt(position);
				break;
			default:
				break;
			}
			lines[line] = lineStr.toString();
		}
		return new StringBuffer(lines.toString());
	}



		
	
	
	

}
