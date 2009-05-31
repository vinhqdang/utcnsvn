package ivc.listeners;

import ivc.compare.IVCCompareEditorInput;
import ivc.compare.StringComparer;
import ivc.manager.ProjectsManager;
import ivc.repository.ResourceStatus;
import ivc.repository.Status;
import ivc.util.FileUtils;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

public class FileModificationManager implements IResourceChangeListener {
	private ProjectsManager projectsManager = ProjectsManager.instance();
	private List<IResource> modifiedResources = new ArrayList<IResource>();
	private int WATCHED_CHANGES = IResourceDelta.CONTENT;

	public void resourceChanged(IResourceChangeEvent event) {

		try {
			event.getDelta().accept(new IResourceDeltaVisitor() {

				public boolean visit(IResourceDelta delta) throws CoreException {
					IResource resource = delta.getResource();

					if (resource.getType() == IResource.FILE) {
						if (delta.getKind() == IResourceDelta.CHANGED && resource.exists()) {
							if ((delta.getFlags() & WATCHED_CHANGES) != 0) {
								modifiedResources.add(resource);
								return true;
							}
						}
					}
					return true;
				}
			});

			for (IResource resource : modifiedResources) {
				if (projectsManager.isManaged(resource)) {
					if (resource instanceof IFile) {
						IFile file = (IFile) resource;
						getChanges(file);
					}
					projectsManager.updateStatus(resource, Status.Modified);
				}
			}
			modifiedResources.clear();
		} catch (CoreException ex) {
			ex.printStackTrace();
		}
	}

	private void getChanges(IFile file) throws CoreException {
		IFileState[] states = file.getHistory(null);
		System.out.println(file);
		if (states.length > 1) {
			StringComparer comparer = new StringComparer(states[0].getContents(), file.getContents());
			comparer.compare();
		}
	}
}
