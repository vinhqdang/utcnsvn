package ivc.fireworks.markers;

import ivc.plugin.IVCPlugin;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.texteditor.MarkerUtilities;

public class MarkersManager {

	public static boolean addMarker(IResource resource, Map attributes, String markerType) {
		try {
			resource.deleteMarkers(IMarker.PROBLEM, true, 1);
			IMarker marker = resource.createMarker("ivc.Marker");
			marker.setAttribute(IMarker.LINE_NUMBER, 12);
			marker.setAttribute(IMarker.MESSAGE, "Zi marker");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
