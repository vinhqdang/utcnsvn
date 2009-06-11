package ivc.fireworks.markers;

import ivc.compare.IVCCompareEditorInput;
import ivc.compare.ResourceEditionNode;
import ivc.data.IVCProject;
import ivc.managers.ImageDescriptorManager;
import ivc.plugin.IVCPlugin;

import java.util.Map;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
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
			CompareConfiguration config = new CompareConfiguration();
			IResource left = ResourcesPlugin.getWorkspace().getRoot().getProject("Project").getFile(new Path("src\\com\\data\\Adapter.java"));
			IResource right = ResourcesPlugin.getWorkspace().getRoot().getProject("Project").getFile(new Path("src\\com\\data\\Added.java"));
			config.setLeftEditable(true);
			config.setRightEditable(true);
			IVCCompareEditorInput input = new IVCCompareEditorInput(config);
			input.setLeft(new ResourceEditionNode((IFile) left));
			input.setRight(new ResourceEditionNode((IFile) right));
			CompareUI.openCompareDialog(input);

		} catch (Exception e) {

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
