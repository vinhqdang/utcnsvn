package ivc.manager;

import ivc.data.IVCProject;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

public class ProjectsManager {
	private static ProjectsManager instance;
	private HashMap<String, IVCProject> projects;

	private ProjectsManager() {
		projects = new HashMap<String, IVCProject>();
	}

	public boolean projectInRepository(IProject project) {
		return projects.containsKey(project.getName());
	}

	public void findProjects() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject[] wsProjects = workspace.getRoot().getProjects();
		for (IProject project : wsProjects) {
			tryAddProject(project);
		}
	}

	public void tryAddProject(IProject project) {
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

	public IProject getProjectByName(String projectName){
		if (projects.get(projectName) != null){
			return ((IVCProject)projects.get(projectName)).getProject();
		}
		return null;		
	}
	
	public IVCProject getIVCProjectByName(String projectName){
		if (projects.get(projectName) != null){
			return ((IVCProject)projects.get(projectName));
		}
		return null;		
	}
	
	public String getProjectPath(String projectServerPath){
		Iterator<String> it = projects.keySet().iterator();
		while (it.hasNext()){
			String pName =  it.next();
			IVCProject proj = projects.get(pName);
			if(proj.getServerPath().equalsIgnoreCase(projectServerPath)){
				return proj.getProject().getLocation().toOSString();
			}
		}
		
		return null;		
	}
	
	public static ResourceStatus getResourceStatus(IResource resource) {

		return null;
	}

	public static ProjectsManager instance() {
		if (instance == null) {
			instance = new ProjectsManager();
		}
		return instance;
	}
}
