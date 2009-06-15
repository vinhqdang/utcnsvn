package ivc.managers;

import ivc.data.IVCProject;
import ivc.repository.CacheManager;
import ivc.repository.IVCRepositoryProvider;
import ivc.repository.ResourceStatus;
import ivc.repository.Status;
import ivc.rmi.server.ServerIntf;
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

	private static ProjectsManager instance;
	private HashMap<String, IVCProject> projects;
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

	public void findProjects() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject[] wsProjects = workspace.getRoot().getProjects();
		for (IProject project : wsProjects) {
			// try {
			// for (IResource resource : project.members()) {
			// remove(resource);
			// }
			//
			// } catch (Exception e) {
			// e.printStackTrace();
			// }

			tryAddProject(project);
		}
	}

	public void removeProject(IVCProject project) throws RemoteException {
		projects.remove(project.getName());
		ServerIntf server = ConnectionManager.getInstance(project.getName()).getServer();
		server.removePeerProject(NetworkUtils.getHostAddress(), project.getServerPath());

	}

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
		// IFolder folder = project.getFolder(Constants.IvcFolder);
		// if (folder.exists() && project.isOpen()) {
		// // it is an active ivc project
		// if (!projects.containsKey(project)) {
		// // read server path
		// String fullserverPath = (String) FileUtils.readObjectFromFile(project.getLocation().toOSString() + Constants.IvcFolder
		// + Constants.ServerFile);
		// String serverAddress = fullserverPath.substring(0, fullserverPath.indexOf('\\'));
		// String serverPath = fullserverPath.replace(serverAddress + '\\', "");
		// IVCProject ivcProj = new IVCProject();
		// ivcProj.setProject(project);
		// ivcProj.setName(project.getName());
		// ivcProj.setServerPath(serverPath);
		// ivcProj.setServerAddress(serverAddress);
		// projects.put(project.getName(), ivcProj);
		// List<IResource> list = new ArrayList<IResource>();
		// list.add(project);
		// Decorator.getDecorator().refresh(list);
		// }
		// }
	}

	public IProject getProjectByName(String projectName) {
		if (projects.get(projectName) != null) {
			return ((IVCProject) projects.get(projectName)).getProject();
		}
		return null;
	}

	public IVCProject getIVCProjectByResource(IResource resource) {
		return getIVCProjectByName(resource.getProject().getName());
	}

	public IVCProject getIVCProjectByName(String projectName) {
		if (projects.get(projectName) != null) {
			return ((IVCProject) projects.get(projectName));
		}
		return null;
	}

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

	public void updateStatus(IResource resource, Status status, boolean recursive) {
		ResourceStatus resStatus = cacheManager.getResourceStatus(resource);
		resStatus.setStatus(status);
		cacheManager.setStatus(resource, resStatus);
		if (recursive) {
			updateTree(resource.getParent());
		}
	}

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

	// TODO 2 use method
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
