package ivc.fireworks.markers;

import ivc.managers.ImageDescriptorManager;

import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolution2;

public class QuickFix implements IMarkerResolution2 {
	private String label;
	private String description;
	private String user;

	public QuickFix(String label, String description, String user) {
		this.label = label;
		this.description = description;
		this.user = user;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void run(IMarker marker) {
		System.out.println("asdasda");
		try {
			Map atr = marker.getAttributes();

		} catch (CoreException e) {

			e.printStackTrace();
		}

	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Image getImage() {
		return ImageDescriptorManager.getImage(ImageDescriptorManager.MARKER);
	}
}
