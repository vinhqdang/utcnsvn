package ivc.manager;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;

public class ProjectInfo {

	public IProject project;
	public String directory;

	void createExistingProject(IProgressMonitor monitor) throws CoreException {
		String projectName = project.getName();
		IProjectDescription description;

		try {
			monitor.beginTask("Creating " + projectName, 2 * 1000);
			description = ResourcesPlugin.getWorkspace().loadProjectDescription(new Path(directory + File.separatorChar + ".project")); //$NON-NLS-1$
			description.setName(projectName);
			project.create(description, new SubProgressMonitor(monitor, 1000));
			project.open(new SubProgressMonitor(monitor, 1000));
		} finally {
			monitor.done();
		}
	}
}
