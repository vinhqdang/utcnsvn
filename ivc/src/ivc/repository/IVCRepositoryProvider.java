package ivc.repository;

import ivc.managers.ProjectsManager;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.team.core.RepositoryProvider;

/**
 * 
 * @author alexm
 * 
 *         The class is used to implement a RepositoryProvider
 */
public class IVCRepositoryProvider extends RepositoryProvider {
	public static String ID = "ivc.reprovider";

	@Override
	public void configureProject() throws CoreException {
		System.out.println("Starting project configuration");
		IProject project = getProject();
		configureTeamPrivateResource(project);
		ProjectsManager.instance().tryAddProject(project);
	}

	/**
	 * this class configures the project as a IVCProject and updates the decorators
	 * 
	 * @param project
	 *            the project which is added
	 */
	private void configureTeamPrivateResource(IProject project) {
		try {
			project.accept(new IResourceVisitor() {
				public boolean visit(IResource resource) throws CoreException {
					if ((resource.getType() == IResource.FOLDER)
							&& ((resource.getName().equals("bin")) || (resource.getName()
									.equals(".ivc")))) {
						resource.setTeamPrivateMember(true);
						return true;
					} else {
						ProjectsManager.instance().setDefaultStatus(resource);
						return true;
					}
				}
			}, IResource.DEPTH_INFINITE, IContainer.INCLUDE_PHANTOMS
					| IContainer.INCLUDE_TEAM_PRIVATE_MEMBERS);
		} catch (CoreException e) {
			// SVNProviderPlugin.log(SVNException.wrapException(e));
			e.printStackTrace();
			System.out.println(e);
		}
	}

	@Override
	public String getID() {
		return ID;
	}

	@Override
	public void deconfigure() throws CoreException {
		System.out.println("testdeconfigure");
	}

	@Override
	public boolean canHandleLinkedResources() {
		return true;
	}

}
