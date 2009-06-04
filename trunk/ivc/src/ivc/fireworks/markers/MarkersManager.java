package ivc.fireworks.markers;

import ivc.data.IVCProject;
import ivc.manager.ProjectsManager;
import ivc.plugin.IVCPlugin;
import ivc.repository.Status;
import ivc.util.WorkspaceUtils;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

public class MarkersManager {
	public static String IVC_MARKER = "ivc.Marker";

	private static boolean addMarker(IResource resource) {
		try {
			resource.deleteMarkers(IVC_MARKER, true, 1);
			IMarker marker = resource.createMarker(IVC_MARKER);
			// marker.setAttribute(IMarker.LINE_NUMBER, 12);
			// marker.setAttribute(IMarker.CHAR_START, 12);
			// marker.setAttribute(IMarker.CHAR_END, 19);
			marker.setAttribute(IMarker.MESSAGE, "Other users are concurently modifying this resource");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void updateMarkers(IFile file) {

		if (file == WorkspaceUtils.getCurrentFile()) {
			if (ProjectsManager.instance().getStatus(file).compareTo(Status.Added) > 0) {
				IVCProject proj = ProjectsManager.instance().getIVCProjectByName(file.getProject().getName());
				if (proj.hasRemoteUncomitedOperations(file))
					addMarker(file);
			}
		}
	}
}
