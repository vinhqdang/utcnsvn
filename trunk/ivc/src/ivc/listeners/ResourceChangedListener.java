package ivc.listeners;

import ivc.commands.RemoveResourceCommand;
import ivc.data.IVCProject;
import ivc.managers.ProjectsManager;
import ivc.repository.IVCRepositoryProvider;
import ivc.repository.Status;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

/**
 * 
 * @author alexm
 * 
 *         The class is used to retrieve the resources modifications which can be add or
 *         remove and modify the resource status
 */
public class ResourceChangedListener implements IResourceChangeListener {
	/**
	 * Method called each time a resource is changed
	 * 
	 * @param event
	 *            Describes the resource change that occurred
	 */
	private ProjectsManager projectsManager = ProjectsManager.instance();
	ArrayList<IResource> toBeRefreshed = new ArrayList<IResource>();
	ArrayList<IResource> opened = new ArrayList<IResource>();

	public void resourceChanged(IResourceChangeEvent event) {

		try {
			event.getDelta().accept(new IResourceDeltaVisitor() {

				public boolean visit(IResourceDelta delta) throws CoreException {
					IResource resource;
					switch (delta.getKind()) {
					// a new resource was created
					case IResourceDelta.ADDED: {
						resource = delta.getResource();
						if (IVCRepositoryProvider.isShared(resource.getProject())) {
							if (projectsManager.isManaged(resource)) {
								if (projectsManager.getStatus(resource).equals(
										Status.Commited))
									return true;
							}
							toBeRefreshed.add(resource.getParent());
							if (projectsManager.getFileVersion(resource) != 0) {
								projectsManager.setDefaultStatus(resource);
							}
						}
					}
						break;

					case IResourceDelta.REMOVED:
						resource = delta.getResource();
						if (resource instanceof IProject) {
							IVCProject project = projectsManager
									.getIVCProjectByResource(resource);
							if (project != null) {
								try {
									projectsManager.removeProject(project);
								} catch (RemoteException e) {
									e.printStackTrace();
								}
							}
						}
						if (IVCRepositoryProvider.isShared(resource.getProject())) {
							if (!(resource instanceof IProject)) {
								toBeRefreshed.add(resource.getParent());
							}
							if (projectsManager.isManaged(resource)) {
								RemoveResourceCommand command = new RemoveResourceCommand(
										null, resource);
								try {
									command.run();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
						break;
					default:
						break;
					}
					return true;
				}
			});
			refreshResources(toBeRefreshed);
			toBeRefreshed.clear();

		} catch (CoreException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Updates the status and the decorators for all resources in the resources list
	 * 
	 * @param resources
	 */
	private void refreshResources(ArrayList<IResource> resources) {
		for (IResource resource : resources) {
			try {
				projectsManager.updateStatus(resource, Status.Modified, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}