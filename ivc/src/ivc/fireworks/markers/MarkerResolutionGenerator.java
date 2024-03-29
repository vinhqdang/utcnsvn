package ivc.fireworks.markers;

import java.util.ArrayList;
import java.util.Set;

import ivc.data.IVCProject;
import ivc.managers.ProjectsManager;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

/**
 * 
 * @author alexm The class is used to create marker resolutions when the marker is clicked
 */
public class MarkerResolutionGenerator implements IMarkerResolutionGenerator {

	@Override
	public IMarkerResolution[] getResolutions(IMarker marker) {
		try {
			IFile file = (IFile) marker.getResource();
			IVCProject proj = ProjectsManager.instance().getIVCProjectByName(
					file.getProject().getName());
			Set<String> users = proj.getUsersAnnotations(file).getUsers();

			// creates a list of fixes depending on the uer annotations

			ArrayList<QuickFix> fixes = new ArrayList<QuickFix>();

			for (String user : users) {
				fixes.add(new QuickFix("Merge with " + user,
						"Open a difference window to show the differences in the file.",
						user));
			}
			IMarkerResolution[] result = new QuickFix[fixes.size()];
			fixes.toArray(result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new QuickFix[0];
	}
}
