package ivc.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import ivc.fireworks.decorator.Decorator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

public class ProjectsManager {
	private static ProjectsManager instance;
	private Vector<IProject> projects;

	private ProjectsManager() {
		projects = new Vector<IProject>();
	}

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

		IFolder folder = project.getFolder(".ivc");

		if (folder.exists()) {
			
			if (!projects.contains(project)) {
				System.out.println("found " + project.getName());
				projects.add(project);
				List<IResource> list=new ArrayList<IResource>();
				list.add(project);
				Decorator.getDecorator().refresh(list);
			}
		}
	}
}
