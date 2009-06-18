package ivc.data;

import ivc.data.annotation.ResourcesAnnotations;
import ivc.data.annotation.UsersAnnotations;
import ivc.data.operation.OperationHistoryList;
import ivc.repository.Status;
import ivc.util.Constants;
import ivc.util.FileUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.resources.IFile;
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
	private ResourcesAnnotations annotations;
	private ArrayList<IResource> deleted = new ArrayList<IResource>();

	public ArrayList<IResource> getDeleted() {
		return deleted;
	}

	public void addToDeleted(IResource resource) {
		deleted.add(resource);
	}

	public IVCProject() {
		super();
		annotations = new ResourcesAnnotations();
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
		return null;
	}

	public OperationHistoryList getLocalLog() {
		Object obj = FileUtils.readObjectFromFile(project.getLocation().toOSString()
				+ Constants.IvcFolder + Constants.LocalLog);
		if (obj != null && obj instanceof OperationHistoryList) {
			return (OperationHistoryList) obj;
		}
		return new OperationHistoryList();
	}

	public OperationHistoryList getRemoteCommitedLog() {
		Object obj = FileUtils.readObjectFromFile(project.getLocation().toOSString()
				+ Constants.IvcFolder + Constants.RemoteCommitedLog);
		if (obj != null && obj instanceof OperationHistoryList) {
			return (OperationHistoryList) obj;
		}
		return new OperationHistoryList();
	}

	public OperationHistoryList getRemoteUncommitedLog(String hostAddress) {
		try {
			File f = new File(project.getLocation().toOSString() + Constants.IvcFolder
					+ Constants.RemoteUnCommitedLog + "_"
					+ hostAddress.replaceAll("\\.", "_"));
			if (f.exists()) {
				Object obj = FileUtils.readObjectFromFile(project.getLocation()
						.toOSString()
						+ Constants.IvcFolder
						+ Constants.RemoteUnCommitedLog
						+ "_"
						+ hostAddress.replaceAll("\\.", "_"));
				if (obj != null && obj instanceof OperationHistoryList) {
					return (OperationHistoryList) obj;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new OperationHistoryList();
	}

	public void setLocalLog(OperationHistoryList ll) {
		FileUtils.writeObjectToFile(project.getLocation().toOSString()
				+ Constants.IvcFolder + Constants.LocalLog, ll);
	}

	public void setRemoteCommitedLog(OperationHistoryList rcl) {
		FileUtils.writeObjectToFile(project.getLocation().toOSString()
				+ Constants.IvcFolder + Constants.RemoteCommitedLog, rcl);
	}

	public void setRemoteUncommitedLog(OperationHistoryList rul, String hostAddress) {
		FileUtils.writeObjectToFile(project.getLocation().toOSString()
				+ Constants.IvcFolder + Constants.RemoteUnCommitedLog + "_"
				+ hostAddress.replaceAll("\\.", "_"), rul);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Integer> getCurrentVersion() {
		return (HashMap<String, Integer>) FileUtils.readObjectFromFile(project
				.getLocation().toOSString()
				+ Constants.IvcFolder + Constants.CurrentVersionFile);
	}

	public void setCurrentVersion(HashMap<String, Integer> cv) {
		FileUtils.writeObjectToFile(project.getLocation().toOSString()
				+ Constants.IvcFolder + Constants.CurrentVersionFile, cv);
	}

	public int getFileVersion(String path) {
		HashMap<String, Integer> localVersion = getCurrentVersion();
		if (localVersion == null)
			return 0;
		if (localVersion.containsKey(path)) {
			return localVersion.get(path);
		}
		return 0;
	}

	public String[] getConflictingUserList(IResource resource) {
		String[] a = new String[2];
		a[0] = "Johnny";
		a[1] = "Gimmy";
		return a;
	}

	public boolean hasRemoteUncomitedOperations(IFile resource) {
		return true;
	}

	public ResourcesAnnotations getResourcesAnnotations() {
		return annotations;
	}

	public UsersAnnotations getUsersAnnotations(IResource resource) {
		return annotations.getAnnotations(resource);
	}
}
