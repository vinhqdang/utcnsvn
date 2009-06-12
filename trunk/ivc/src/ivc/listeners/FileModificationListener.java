package ivc.listeners;

import ivc.commands.CommandArgs;
import ivc.commands.HandleOperationCommand;
import ivc.compare.StringComparer;
import ivc.data.operation.OperationHistory;
import ivc.managers.ProjectsManager;
import ivc.repository.Status;
import ivc.util.Constants;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

public class FileModificationListener implements IResourceChangeListener {

	private ProjectsManager projectsManager = ProjectsManager.instance();

	/**
	 * This field is used to ignore all modifications if an update operation is performed. It stops the program from evaluating an update like a
	 * modification on an existing file
	 */
	public static boolean ignoreModifications = false;

	/**
	 * The list of resources which contain transformations of the kind speciffied in the WATCHED_CHANGES field
	 */
	private List<IResource> modifiedResources = new ArrayList<IResource>();

	/**
	 * The changes that are watched by this listener
	 */
	private int WATCHED_CHANGES = IResourceDelta.CONTENT;

	public void resourceChanged(IResourceChangeEvent event) {

		try {
			event.getDelta().accept(new IResourceDeltaVisitor() {

				public boolean visit(IResourceDelta delta) throws CoreException {
					IResource resource = delta.getResource();

					/**
					 * We test if the resource is managed, if so, we add the resource to the list of modified resources
					 */
					if (ProjectsManager.instance().isManaged(resource)) {
						if (resource.getType() == IResource.FILE) {
							if (delta.getKind() == IResourceDelta.CHANGED && resource.exists()) {
								if ((delta.getFlags() & WATCHED_CHANGES) != 0) {
									if (!ignoreModifications) {
										modifiedResources.add(resource);
									}
									return true;
								}
							}
						}
					}
					return true;
				}
			});

			/**
			 * We go trough the list of modified resources, retrieve the changes and update the status of the resource to modiffied
			 */
			for (IResource resource : modifiedResources) {
				try {
					if (resource instanceof IFile) {
						IFile file = (IFile) resource;
						getChanges(file);
						projectsManager.updateStatus(resource, Status.Modified, true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			modifiedResources.clear();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * This method uses a StringComparer instance and generates the differences between two files. The actual input to the comparer are the file and
	 * the latest version of the file in history. After we get the differences a operation command is created to update the annotations of this file
	 * to the other users.
	 * 
	 * @param file
	 *            the file for which we need the differences
	 * @throws CoreException
	 */
	private void getChanges(IFile file) throws CoreException {
		IFileState[] states = file.getHistory(null);
		if (states.length > 0) {
			// We create a new comparer for the file and it's history
			StringComparer comparer = new StringComparer(file, states[0].getContents());
			comparer.compare();
			// We create a operation history and execute the command to update the annotations
			OperationHistory oh = comparer.getOperationHistory();
			HandleOperationCommand hoc = new HandleOperationCommand();
			CommandArgs args = new CommandArgs();
			args.putArgument(Constants.IPROJECT, file.getProject());
			args.putArgument(Constants.OPERATION_HIST, oh);
			hoc.execute(args);
		}
	}

}
