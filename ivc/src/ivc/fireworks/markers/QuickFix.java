package ivc.fireworks.markers;

import ivc.compare.DiffComparableFactory;
import ivc.compare.IVCCompareEditorInput;
import ivc.managers.ImageDescriptorManager;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.core.resources.IMarker;
import org.eclipse.swt.graphics.Image;
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
			String left = "test";// ResourcesPlugin.getWorkspace().getRoot().getProject("Project").getFile(new Path("src\\com\\data\\Adapter.java"));
			String right = user;// ResourcesPlugin.getWorkspace().getRoot().getProject("Project").getFile(new Path("src\\com\\data\\Added.java"));
			config.setLeftEditable(true);
			config.setRightEditable(true);
			IVCCompareEditorInput input = new IVCCompareEditorInput(config);

			//CommandArgs args = new CommandArgs();
			// args.putArgument(Constants.IVCPROJECT, value)
			// GetUserCopyCommand command=new GetUserCopyCommand();

			input.setLeft(DiffComparableFactory.createComparable(left));
			input.setRight(DiffComparableFactory.createComparable(right));
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
