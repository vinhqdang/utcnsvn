/**
 * 
 */
package ivc.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
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
	LinkedList<String> folders;
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
	
	public LinkedList<String> getFolders(){
		return folders;
	}
	
	public void setFolders(LinkedList<String> folders) {
		this.folders = folders;
	}
	
	public void addFolder(String folderPath){
		if (folders == null){
			folders = new LinkedList<String>();			
		}
		folders.add(folderPath);
	}
	
	public StringBuffer getFileContent(String filePath){
		if (files.containsKey(filePath)){
			StringBuffer sb = files.get(filePath);
			return sb;
		}
		return null;
	}
	
	

}
