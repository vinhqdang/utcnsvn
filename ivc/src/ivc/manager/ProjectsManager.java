package ivc.manager;

import ivc.data.IVCProject;
import ivc.repository.CacheManager;
import ivc.repository.ResourceStatus;
import ivc.repository.Status;

import java.awt.color.CMMException;
import java.util.Date;
import java.util.HashMap;

import java.util.Iterator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

public class ProjectsManager {
	private static ProjectsManager instance;
	private HashMap<String, IVCProject> projects;
	private CacheManager cacheManager;

	private ProjectsManager() {
		projects = new HashMap<String, IVCProject>();
		cacheManager = new CacheManager();
	}

	public boolean projectInRepository(IProject project) {
		return projects.containsKey(project.getName());
	}

	// public boolean existsProjectWithName(String name){
	// for (IProj iterable_element : iterable) {
	//			
	// }
	// }

	public static ProjectsManager instance() {
		if (instance == null) {
			instance = new ProjectsManager();
		}
		return instance;
	}

	public void findProjects() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject[] wsProjects = workspace.getRoot().getProjects();
		for (IProject project : wsProjects) {
			// tryAddProject(project);
		}
	}

	ResourceStatus rs = new ResourceStatus(1L, new Date(), "John", Status.Added);

	public void tryAddProject(IProject project) {
		if (project.getName().equals("a")) {
			try {
				for (IResource resource : project.members()) {
					cacheManager.setStatus(resource, rs);
					if (resource instanceof IFolder) {
						// setStatus((IFolder) resource);
					}
				}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

	public Status getResourceStatus(IResource resource) {
		return cacheManager.getResourceStatus(resource).getStatus();
	}

	public boolean isManaged(IResource resource) {
		return cacheManager.isManaged(resource);
	}

	public void updateStatus(IResource resource, Status status) {
		ResourceStatus resStatus = cacheManager.getResourceStatus(resource);
		resStatus.setStatus(status);
		cacheManager.setStatus(resource, resStatus);
	}

	public void addDefaultStatus(IResource resource) {
		cacheManager.setStatus(resource, rs);
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

	public String getProjectPath(String projectServerPath) {
		Iterator<String> it = projects.keySet().iterator();
		while (it.hasNext()) {
			String pName = it.next();
			IVCProject proj = projects.get(pName);
			if (proj.getServerPath().equalsIgnoreCase(projectServerPath)) {
				return proj.getProject().getLocation().toOSString();
			}
		}

		return null;
	}

}
