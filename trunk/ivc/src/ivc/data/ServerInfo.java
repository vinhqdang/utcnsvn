/**
 * 
 */
package ivc.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author danielan
 *
 */
public class ServerInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String serverAddress;
	
	private List<IVCProject> projects;

	/**
	 * @return the serverAddress
	 */
	public String getServerAddress() {
		return serverAddress;
	}

	/**
	 * @param serverAddress the serverAddress to set
	 */
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	/**
	 * @return the projects
	 */
	public List<IVCProject> getProjects() {
		return projects;
	}

	/**
	 * @param projects the projects to set
	 */
	public void setProjects(List<IVCProject>  projects) {
		this.projects = projects;
	}
	
	

}
