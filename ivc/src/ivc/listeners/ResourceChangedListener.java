package ivc.listeners;

import ivc.manager.ProjectsManager;

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
	public void resourceChanged(IResourceChangeEvent event) {

		try {
			event.getDelta().accept(new IResourceDeltaVisitor() {

				public boolean visit(IResourceDelta delta) throws CoreException {
					IResource resource;
					switch (delta.getKind()) {
					// a new resource was created
					case IResourceDelta.ADDED:

						// get a reference to the newly created resource
						resource = delta.getResource();

						// newly created resource is a java file
						if ((resource instanceof IFile)
								&& (resource.getFileExtension().compareTo("java") == 0)) {
							// attach document listener to the newly created
							// java file
							// AttachListeners.attachFileListener((IFile)resource);
							// get the parent resources of the created resource
							// getFileParents(resource);
						} else if (resource instanceof IProject) {
							
							ProjectsManager.instance().tryAddProject(
									(IProject) resource);
						}
						break;

					// an existing resource was removed
					case IResourceDelta.REMOVED:

						// get a reference to the newly created resource
						resource = delta.getResource();

						// the removed resource is a java file
						if (resource instanceof IFile
								&& (resource.getFileExtension().compareTo(
										"java") == 0)) {
							// perform updates of the local data structures in
							// order to
							// reflect the deletion of the file
							// AttachListeners.detachFileListener((IFile)resource);
							// get the parent resources of the created resource
							// getFileParents(resource);
						}
						break;

					// an existing resource was changed; we don't handle this
					// case
					case IResourceDelta.CHANGED:
						resource=delta.getResource();						
						if (resource instanceof IFile){
							IFile file=(IFile)resource;
							
						}
						break;
					default:
						break;
					}
					return true;
				}
			});
		} catch (CoreException ex) {
		}
	}
}