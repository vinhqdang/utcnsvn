package ivc.listeners;

import ivc.data.IVCProject;
import ivc.managers.ProjectsManager;
import ivc.repository.IVCRepositoryProvider;
import ivc.repository.Status;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

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
								if (projectsManager.getStatus(resource).equals(Status.Commited))
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
						if (IVCRepositoryProvider.isShared(resource.getProject())) {
							if (!(resource instanceof IProject)) {
								toBeRefreshed.add(resource.getParent());
							}
						}
						break;

					// an existing resource was changed; we don't handle this
					// case
					case IResourceDelta.OPEN: {

					}
						break;
					case IResourceDelta.CHANGED:
						// resource = delta.getResource();
						// if (resource instanceof IFile) {
						// IFile file = (IFile) resource;
						// toBeRefreshed.add(file);
						// }
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