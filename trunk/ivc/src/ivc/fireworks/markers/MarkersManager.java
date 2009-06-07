package ivc.fireworks.markers;

import ivc.data.IVCProject;
import ivc.data.annotation.UsersAnnotations;
import ivc.manager.ProjectsManager;
import ivc.plugin.IVCPlugin;
import ivc.repository.Status;
import ivc.util.FileUtils;
import ivc.util.WorkspaceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

public class MarkersManager {
	public static String IVC_MARKER = "ivc.Marker";

	private static boolean addMarker(IResource resource, int line) {
		try {

			IMarker marker = resource.createMarker(IVC_MARKER);
			marker.setAttribute(IMarker.MESSAGE, "Other users are concurently modifying this resource");
			marker.setAttribute(IMarker.LINE_NUMBER, line);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void updateMarkers(IFile file) throws CoreException {
		file.deleteMarkers(IVC_MARKER, true, 1);

		if (ProjectsManager.instance().getStatus(file).compareTo(Status.Added) > 0) {
			try {
				for (int line : getLines(file)) {
					addMarker(file, line);
				}
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	}

	private static int getCount(String buff) {
		int count = 1;
		for (Character c : buff.toCharArray()) {
			if (c.equals('\n'))
				count++;
		}
		return count;
	}

	private static List<Integer> getLines(IFile file) throws CoreException {
		StringBuffer sBuffer = FileUtils.InputStreamToStringBuffer(file.getContents());
		String values = sBuffer.toString();
		List<Integer> lines = new ArrayList<Integer>();
		List<Integer> positions = getPositions(file);
		for (int i : positions) {
			int line = 0;
			if (i >= values.length())
				line = getCount(values);
			else
				line = getCount(values.substring(0, i));
			if (!lines.contains(line)) {
				lines.add(line);
			}
		}
		return lines;
	}

	private static List<Integer> getPositions(IFile file) {
		IVCProject proj = ProjectsManager.instance().getIVCProjectByName(file.getProject().getName());
		if (proj != null) {
			UsersAnnotations annotations = proj.getUsersAnnotations(file);
			return annotations.getPositions();
		} else {
			return new ArrayList<Integer>();
		}
	}
}
