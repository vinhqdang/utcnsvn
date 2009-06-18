/**
 * 
 */
package ivc.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author danielan
 * 
 */
public class Peer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String hostAddress;
	private List<String> projectPaths;
	private String connectionStatus;

	/**
	 * @param hostAddress
	 * @param projectPaths
	 * @param connectionStatus
	 */
	public Peer(String hostAddress, List<String> projectPaths, String connectionStatus) {
		super();
		this.hostAddress = hostAddress;
		this.projectPaths = projectPaths;
		this.connectionStatus = connectionStatus;
	}

	/**
	 * @return the hostAddress
	 */
	public String getHostAddress() {
		return hostAddress;
	}

	/**
	 * @param hostAddress
	 *            the hostAddress to set
	 */
	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}

	/**
	 * @return the projectPaths
	 */
	public List<String> getProjectPaths() {
		return projectPaths;
	}

	/**
	 * @param projectPaths
	 *            the projectPaths to set
	 */
	public void setProjectPaths(List<String> projectPaths) {
		this.projectPaths = projectPaths;
	}

	/**
	 * @return the connectionStatus
	 */
	public String getConnectionStatus() {
		return connectionStatus;
	}

	/**
	 * @param connectionStatus
	 *            the connectionStatus to set
	 */
	public void setConnectionStatus(String connectionStatus) {
		this.connectionStatus = connectionStatus;
	}

}
