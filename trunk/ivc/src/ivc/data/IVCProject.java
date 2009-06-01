package ivc.data;

import ivc.repository.Status;
import ivc.util.Constants;
import ivc.util.FileUtils;

import java.io.Serializable;
import java.util.HashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

public class IVCProject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private IProject project;
	private String serverAddress;
	private String serverPath;

	public IVCProject() {
		super();
	}

	public IVCProject(String name) {
		this.name = name;
	}

	/**
	 * @return the project
	 */
	public IProject getProject() {
		return project;
	}

	/**
	 * @param project
	 *            the project to set
	 */
	public void setProject(IProject project) {
		this.project = project;
	}

	/**
	 * @return the serverPath
	 */
	public String getServerPath() {
		return serverPath;
	}

	/**
	 * @param serverPath
	 *            the serverPath to set
	 */
	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the serverAddress
	 */
	public String getServerAddress() {
		return serverAddress;
	}

	/**
	 * @param serverAddress
	 *            the serverAddress to set
	 */
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public Status getResourceStatus(IResource resource) {
		// TODO 2.Alex ceva cu statusul
		return null;
	}

	@SuppressWarnings("unchecked")
	public int getFileVersion(String path) {
		HashMap<String, Integer> localVersion = (HashMap<String, Integer>) FileUtils.readObjectFromFile(project.getLocation().toOSString()
				+ Constants.IvcFolder + Constants.CurrentVersionFile);
		if (localVersion.containsKey(path)) {
			return localVersion.get(path);
		}
		return 0;
	}

}
