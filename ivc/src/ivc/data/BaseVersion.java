/**
 * 
 */
package ivc.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author danielan
 *
 */
public class BaseVersion implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String projectName;
	private String projectPath;
	private Map<String,StringBuffer> files;
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
	 * @return the projectPath
	 */
	public String getProjectPath() {
		return projectPath;
	}
	/**
	 * @param projectPath the projectPath to set
	 */
	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}
	/**
	 * @return the files
	 */
	public Map<String, StringBuffer> getFiles() {
		return files;
	}
	/**
	 * @param files the files to set
	 */
	public void setFiles(Map<String, StringBuffer> files) {
		this.files = files;
	}
	
	public void addFile(String filePath, StringBuffer strB){
		if (files == null){
			files = new HashMap<String, StringBuffer>();			
		}
		files.put(filePath,strB);
	}
	

}
