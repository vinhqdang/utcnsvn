package ivc.fireworks.markers;

import ivc.commands.CommandArgs;
import ivc.commands.GetUserCopyCommand;
import ivc.commands.Result;
import ivc.compare.DiffComparableFactory;
import ivc.compare.IVCCompareEditorInput;
import ivc.data.IVCProject;
import ivc.managers.ImageDescriptorManager;
import ivc.managers.ProjectsManager;
import ivc.util.Constants;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMarkerResolution2;

/**
 * 
 * @author alexm
 * 
 *         The class is used to display a quick fix for a marker
 */
public class QuickFix implements IMarkerResolution2 {
	private String label;
	private String description;
	private String user;

	/**
	 * Creates a new Quick fix object with the resources
	 * 
	 * @param label
	 *            the label of the quick fix
	 * @param description
	 *            the description of the quick fix
	 * @param user
	 *            the user with the quick fix
	 */
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
		try {
			CompareConfiguration config = new CompareConfiguration();
			config.setLeftEditable(true);
			config.setRightEditable(true);

			IVCCompareEditorInput input = new IVCCompareEditorInput(config);

			IFile left = (IFile) marker.getResource();

			GetUserCopyCommand command = new GetUserCopyCommand();
			CommandArgs args = new CommandArgs();
			args.putArgument(Constants.HOST_ADDRESS, user);
			IVCProject project = ProjectsManager.instance().getIVCProjectByName(
					left.getProject().getName());
			args.putArgument(Constants.IVCPROJECT, project);
			args.putArgument(Constants.FILE_PATH, left.getProjectRelativePath()
					.toOSString());
			Result result = command.execute(args);

			String right = result.getResultData().toString();

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
