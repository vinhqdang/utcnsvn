package ivc.data;

import ivc.repository.Status;
import ivc.util.Constants;
import ivc.util.FileUtils;

import java.io.Serializable;
import java.lang.reflect.Array;
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
	private HashMap<String, Integer> localVersion;

	public HashMap<String, Integer> getLocalVersion() {
		if (localVersion == null) {
			localVersion = (HashMap<String, Integer>) FileUtils.readObjectFromFile(project.getLocation().toOSString() + Constants.IvcFolder
					+ Constants.CurrentVersionFile);
		}
		return localVersion;
	}

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

	public int getFileVersion(String path) {
		if (localVersion == null)
			return 0;
		if (localVersion.containsKey(path)) {
			return localVersion.get(path);
		}
		return 0;
	}

	public OperationHistoryList getLocalLog() {
		return (OperationHistoryList) FileUtils.readObjectFromFile(name + Constants.IvcFolder + Constants.LocalLog);
	}

	public OperationHistoryList getRemoteCommitedLog() {
		return (OperationHistoryList) FileUtils.readObjectFromFile(name + Constants.IvcFolder + Constants.RemoteCommitedLog);
	}

	public OperationHistoryList getRemoteUncommitedLog(String hostAddress) {
		return (OperationHistoryList) FileUtils.readObjectFromFile(name + Constants.IvcFolder + Constants.RemoteUnCommitedLog + "_"
				+ hostAddress.replaceAll(".", "_"));
	}
	
	public void setLocalLog(OperationHistoryList ll){
		FileUtils.writeObjectToFile(name+Constants.IvcFolder+Constants.LocalLog, ll);
	}
	
	public void setRemoteCommitedLog(OperationHistoryList rcl){
		FileUtils.writeObjectToFile(name+Constants.IvcFolder+Constants.LocalLog, rcl);
	}
	
	public void setRemoteUncommitedLog(OperationHistoryList rul, String hostAddress){
		FileUtils.writeObjectToFile(name+Constants.IvcFolder+Constants.RemoteUnCommitedLog+ "_"
				+ hostAddress.replaceAll(".", "_"), rul);
	}
	
	public HashMap<String, Integer> getCurrentVersion(){
		return(HashMap<String,Integer>) FileUtils.readObjectFromFile(name +Constants.IvcFolder+Constants.CurrentVersionFile);
	}
	
	public void setCurrentVersion(HashMap<String,Integer> cv){
		FileUtils.writeObjectToFile(name+Constants.IvcFolder +Constants.CurrentVersionFile, cv);
	}
	

	public String[] getConflictingUserList(IResource resource) {
		String[] a = new String[2];
		a[0] = "Johnny";
		a[1] = "Gimmy";
		return a;
	}

	public boolean hasRemoteUncomitedOperations(IFile resource){
		return true;
	}
}
