package ivc.fireworks.markers;

import ivc.data.IVCProject;
import ivc.data.annotation.UsersAnnotations;
import ivc.managers.ProjectsManager;
import ivc.repository.Status;
import ivc.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * 
 * @author alexm The class is used to create markers for the speciffied resources
 */

public class MarkersManager {
	public static String IVC_MARKER = "ivc.Marker";

	/**
	 * Adds a marker at the specified line number
	 * 
	 * @param resource
	 *            the resource for which the marker should be added
	 * @param line
	 *            the line where the marker should be added
	 * @return returns if the set succeded or not
	 */
	private static boolean addMarker(IResource resource, int line) {
		try {

			IMarker marker = resource.createMarker(IVC_MARKER);
			marker.setAttribute(IMarker.MESSAGE,
					"Other users are concurently modifying this resource");
			marker.setAttribute(IMarker.LINE_NUMBER, line);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Updates all markers from the specified file
	 * 
	 * @param file
	 *            the file for which the update should be executed
	 * @throws CoreException
	 */
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

	/**
	 * Returns the line number if the position in the file
	 * 
	 * @param buff
	 *            the substring where to search
	 * @return the line number if the position in the file
	 */
	private static int getCount(String buff) {
		int count = 1;
		for (Character c : buff.toCharArray()) {
			if (c.equals('\n'))
				count++;
		}
		return count;
	}

	/**
	 * Returns all lines for the specified resource
	 * 
	 * @param file
	 *            the file for which to calculate the lines
	 * @return a list of lines for the specified
	 * @throws CoreException
	 */
	private static List<Integer> getLines(IFile file) throws CoreException {
		StringBuffer sBuffer = FileUtils.InputStreamToStringBuffer(file.getContents());
		String values = sBuffer.toString();
		List<Integer> lines = new ArrayList<Integer>();
		List<Integer> positions = getPositions(file);
		for (int i : positions) {
			int line = 0;
			if (i >= values.length() || (i < 0))
				line = getCount(values);
			else
				line = getCount(values.substring(0, i));
			if (!lines.contains(line)) {
				lines.add(line);
			}
		}
		return lines;
	}

	/**
	 * Returns the list of the modified positions for the IFile
	 * 
	 * @param file
	 *            the file for which to get the positions
	 * @return the list of the modified positions for the IFile
	 */
	private static List<Integer> getPositions(IFile file) {
		IVCProject proj = ProjectsManager.instance().getIVCProjectByName(
				file.getProject().getName());
		if (proj != null) {
			UsersAnnotations annotations = proj.getUsersAnnotations(file);
			return annotations.getPositions();
		} else {
			return new ArrayList<Integer>();
		}
	}
}
