package ivc.fireworks.markers;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

public class MarkerResolutionGenerator implements IMarkerResolutionGenerator {

	@Override
	public IMarkerResolution[] getResolutions(IMarker marker) {

		return new IMarkerResolution[] { new QuickFix("Fixme ", "try to fix this"), new QuickFix("Second possibility", "fix the problem"), };

	}

}
