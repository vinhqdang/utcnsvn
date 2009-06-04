package ivc.manager;

import ivc.data.IVCProject;
import ivc.repository.CacheManager;
import ivc.repository.IVCRepositoryProvider;
import ivc.repository.ResourceStatus;
import ivc.repository.Status;
import ivc.util.Constants;
import ivc.util.FileUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.team.core.RepositoryProvider;

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

	private void remove(IResource resource) throws CoreException {
		removeStatus(resource);
		if (resource instanceof IFolder)
			for (IResource res : ((IFolder) resource).members()) {
				remove(res);
			}
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

	public void updateStatus(IResource resource, Status status) {
		ResourceStatus resStatus = cacheManager.getResourceStatus(resource);
		resStatus.setStatus(status);
		cacheManager.setStatus(resource, resStatus);
	}

	public void setDefaultStatus(IResource resource) {
		cacheManager.setStatus(resource, getDefaultStatus());
	}

	public void setAddedStatus(IResource resource) {
		cacheManager.setStatus(resource, getAddedStatus());
	}

	public HashMap<String, IVCProject> getProjects() {
		return projects;
	}

	public Status getStatus(IResource resource) {
		return cacheManager.getResourceStatus(resource).getStatus();
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
		if (!(RepositoryProvider.getProvider(resource.getProject()) instanceof IVCRepositoryProvider))
			return 0;
		IVCProject proj = getIVCProjectByName(resource.getProject().getName());
		if (proj != null)
			proj.getFileVersion(resource.getLocation().toOSString());
		return 0;
	}

}