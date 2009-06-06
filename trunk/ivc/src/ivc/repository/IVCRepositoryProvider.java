package ivc.repository;

import ivc.manager.ProjectsManager;
import ivc.plugin.IVCPlugin;
import ivc.util.Constants;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.team.core.RepositoryProvider;

public class IVCRepositoryProvider extends RepositoryProvider {
	public static String ID = "ivc.reprovider";

	@Override
	public void configureProject() throws CoreException {
		IProject project = getProject();
		configureTeamPrivateResource(project);
		System.out.println("Starting project configuration");
	}

	private void configureTeamPrivateResource(IProject project) {
		try {
			project.accept(new IResourceVisitor() {
				public boolean visit(IResource resource) throws CoreException {
					if ((resource.getType() == IResource.FOLDER) && (resource.getName().equals(Constants.IvcFolder.replaceAll("\\", "")))
							&& (!resource.isTeamPrivateMember())) {
						resource.setTeamPrivateMember(true);
						return false;
						// TODO setdefault values
					} else {
						ProjectsManager.instance().setDefaultStatus(resource);
						return true;
					}
				}
			}, IResource.DEPTH_INFINITE, IContainer.INCLUDE_PHANTOMS | IContainer.INCLUDE_TEAM_PRIVATE_MEMBERS);
		} catch (CoreException e) {
			// SVNProviderPlugin.log(SVNException.wrapException(e));
			e.printStackTrace();
			System.out.println(e);
		}
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return ID;
	}

	@Override
	public void deconfigure() throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canHandleLinkedResources() {
		// TODO Auto-generated method stub
		return true;
	}

}
