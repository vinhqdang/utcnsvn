package ivc.manager;

import ivc.data.IVCProject;
import ivc.fireworks.decorators.Decorator;
import ivc.util.Constants;
import ivc.util.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.tools.Tool;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

public class ProjectsManager {
	private static ProjectsManager instance;
	private Vector<IVCProject> projects;

	private ProjectsManager() {
		projects = new Vector<IVCProject>();
	}

	public boolean projectInRepository(IProject project) {
		return projects.contains(project);
	}
	
//	public boolean existsProjectWithName(String name){
//		for (IProj iterable_element : iterable) {
//			
//		}
//	}

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
			tryAddProject(project);
		}
	}

	public void tryAddProject(IProject project) {
		IFolder folder = project.getFolder(Constants.IvcFolder);
		if (folder.exists() && project.isOpen()) {
			// it is an active ivc project
			if (!projects.contains(project)) {
				// read server path
				String fullserverPath = (String) FileUtils.readObjectFromFile(project.getLocation().toOSString()+ Constants.IvcFolder+Constants.ServerFile);
				String serverAddress = fullserverPath.substring(0,fullserverPath.indexOf('\\'));
				String serverPath = fullserverPath.replace(serverAddress+'\\',"");
				IVCProject ivcProj =  new IVCProject();				
				ivcProj.setProject(project);
				ivcProj.setName(project.getName());
				ivcProj.setServerPath(serverPath);
				ivcProj.setServerAddress(serverAddress);
				projects.add(ivcProj);
				List<IResource> list = new ArrayList<IResource>();
				list.add(project);
				Decorator.getDecorator().refresh(list);
			}
		}
	}
}
