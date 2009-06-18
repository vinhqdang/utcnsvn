package ivc.managers;

import ivc.data.IVCProject;
import ivc.repository.CacheManager;
import ivc.repository.IVCRepositoryProvider;
import ivc.repository.ResourceStatus;
import ivc.repository.Status;
import ivc.server.rmi.ServerIntf;
import ivc.util.Constants;
import ivc.util.FileUtils;
import ivc.util.NetworkUtils;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

public class ProjectsManager {

	/**
	 * static instance of the single ProjectsManager object in the system; returned by getInstance
	 */
	private static ProjectsManager instance;

	/**
	 * lis of loaded projects
	 */
	private HashMap<String, IVCProject> projects;

	/**
	 * reference to a cache manager
	 */
	private CacheManager cacheManager;

	private ResourceStatus getAddedStatus() {
		return new ResourceStatus(1, new Date(), Status.Added);
	}

	private ResourceStatus getDefaultStatus() {
		return new ResourceStatus(1, new Date(), Status.Commited);
	}

	private ProjectsManager() {
		projects = new HashMap<String, IVCProject>();
		cacheManager = new CacheManager();
	}

	public boolean projectInRepository(IProject project) {
		return projects.containsKey(project.getName());
	}
	/**
	 * Finds all ivc type projects
	 */
	public void findProjects() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject[] wsProjects = workspace.getRoot().getProjects();
		for (IProject project : wsProjects) {
			tryAddProject(project);
		}
	}

	/**
	 * Removes a project from the set of kept projects
	 * @param project
	 * @throws RemoteException
	 */
	public void removeProject(IVCProject project) throws RemoteException {
		projects.remove(project.getName());
		ServerIntf server = ConnectionManager.getInstance(project.getName()).getServer();
		server.removePeerProject(NetworkUtils.getHostAddress(), project.getServerPath());

	}

	/**
	 * Tries to add a project that has the .ivc folder in kept set of loaded projects
	 * @param project
	 */
	public void tryAddProject(IProject project) {
		IFolder folder = project.getFolder(Constants.IvcFolder);
		if (folder.exists() && project.isOpen()) {
			// it is an active ivc project
			if (!projects.containsKey(project)) {
				// read server path
				String fullserverPath = (String) FileUtils.readObjectFromFile(project.getLocation().toOSString() + Constants.IvcFolder
						+ Constants.ServerFile);
				String serverAddress = fullserverPath.substring(0, fullserverPath.indexOf('\\'));
				String serverPath = fullserverPath.replace(serverAddress + '\\', "");
				serverPath = serverPath.toLowerCase();
				IVCProject ivcProj = new IVCProject();
				ivcProj.setProject(project);
				ivcProj.setName(project.getName());
				ivcProj.setServerPath(serverPath);
				ivcProj.setServerAddress(serverAddress);
				projects.put(project.getName(), ivcProj);

				// Decorator.getDecorator().refresh(list);
			}
		}
	}

	/**
	 * Searches project by name
	 * @param projectName
	 * @return
	 */
	public IProject getProjectByName(String projectName) {
		if (projects.get(projectName) != null) {
			return ((IVCProject) projects.get(projectName)).getProject();
		}
		return null;
	}
	/**
	 * Searches project by IResource object
	 * @param resource
	 * @return
	 */
	public IVCProject getIVCProjectByResource(IResource resource) {
		return getIVCProjectByName(resource.getProject().getName());
	}

	/**
	 * Returns the IVCProject with the name projectName. Project name is considered the name
	 * the project has in the local workspace
	 * @param projectName
	 * @return
	 */
	public IVCProject getIVCProjectByName(String projectName) {
		if (projects.get(projectName) != null) {
			return ((IVCProject) projects.get(projectName));
		}
		return null;
	}

	/**
	 * Returns an IVCProject based on a project server path
	 * @param projectServerPath
	 * @return
	 */
	public IVCProject getIVCProjectByServerPath(String projectServerPath) {
		Iterator<String> it = projects.keySet().iterator();
		while (it.hasNext()) {
			String pName = it.next();
			IVCProject proj = projects.get(pName);
			if (proj.getServerPath().equalsIgnoreCase(projectServerPath)) {
				return proj;
			}
		}

		return null;
	}

	public void removeStatus(IResource resource) {
		cacheManager.removeStatus(resource);
	}

	public boolean isManaged(IResource resource) {
		return cacheManager.isManaged(resource);
	}
	/**
	 *  Updates the status of a resource
	 * @param resource
	 * @param status
	 * @param recursive
	 */

	public void updateStatus(IResource resource, Status status, boolean recursive) {
		ResourceStatus resStatus = cacheManager.getResourceStatus(resource);
		resStatus.setStatus(status);
		cacheManager.setStatus(resource, resStatus);
		if (recursive) {
			updateTree(resource.getParent());
		}
	}
 /**
  * Updates tree of resources
  * @param resource
  */
	private void updateTree(IResource resource) {
		if (resource != null) {
			ResourceStatus resStatus = cacheManager.getResourceStatus(resource);
			if (resStatus != null) {
				if (!resStatus.getStatus().equals(Status.Modified)) {
					resStatus.setStatus(Status.Modified);
					cacheManager.setStatus(resource, resStatus);
					updateTree(resource.getParent());
				}
			}
		}

	}

	public void setDefaultStatus(IResource resource) {
		cacheManager.setStatus(resource, getDefaultStatus());
	}

	public void setCommitedStatus(IResource resource) {
		if (cacheManager.getResourceStatus(resource) == null) {
			cacheManager.setStatus(resource, getDefaultStatus());
		} else {
			ResourceStatus status = cacheManager.getResourceStatus(resource);
			status.setStatus(Status.Commited);
			cacheManager.setStatus(resource, status);
		}

	}

	private void setAddedStatusToParents(IResource resource) {
		if (resource != null) {
			if (cacheManager.getResourceStatus(resource) == null) {
				cacheManager.setStatus(resource, getAddedStatus());
				setAddedStatusToParents(resource.getParent());
			}
		}
	}

	public void setAddedStatus(IResource resource) {
		cacheManager.setStatus(resource, getAddedStatus());
		setAddedStatusToParents(resource.getParent());
	}

	public HashMap<String, IVCProject> getProjects() {
		return projects;
	}

	public Status getStatus(IResource resource) {

		ResourceStatus rStatus = cacheManager.getResourceStatus(resource);
		if (rStatus == null) {
			return Status.Unversioned;
		}
		return rStatus.getStatus();
	}

	public ResourceStatus getResourceStatus(IResource resource) {
		return cacheManager.getResourceStatus(resource);
	}

	public static ProjectsManager instance() {
		if (instance == null) {
			instance = new ProjectsManager();
		}
		return instance;
	}
	
	/**
	 * Returns the file version of an IResource object based on .cv file 
	 * @param resource
	 * @return
	 */
	public int getFileVersion(IResource resource) {
		if (!IVCRepositoryProvider.isShared(resource.getProject()))
			return 0;
		IVCProject proj = getIVCProjectByName(resource.getProject().getName());
		if (proj != null) {
			return proj.getFileVersion(resource.getProjectRelativePath().toOSString());
		}
		return 0;
	}

}
