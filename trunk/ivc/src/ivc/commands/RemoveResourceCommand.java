package ivc.commands;

import ivc.data.operation.Operation;
import ivc.data.operation.OperationHistoryList;
import ivc.managers.ProjectsManager;
import ivc.repository.Status;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.team.ui.TeamOperation;
import org.eclipse.ui.IWorkbenchPart;

public class RemoveResourceCommand extends TeamOperation {
	private IResource resource;
	private ProjectsManager projectsManager = ProjectsManager.instance();

	public RemoveResourceCommand(IWorkbenchPart part, IResource resource) {
		super(part);
		this.resource = resource;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		monitor.beginTask("Deleting resource", 1);
		Operation operation = new Operation();
		operation.setFileVersion(projectsManager.getFileVersion(resource));
		operation.setFilePath(resource.getProjectRelativePath().toOSString());

		if (resource instanceof IFile) {
			operation.setOperationType(Operation.REMOVE_FILE);
		}
		if (resource instanceof IFolder) {
			operation.setOperationType(Operation.REMOVE_FOLDER);
		}
		OperationHistoryList localLog = projectsManager.getIVCProjectByResource(resource).getLocalLog();
		localLog.appendOperation(operation);
		projectsManager.getIVCProjectByResource(resource).setLocalLog(localLog);

		projectsManager.updateStatus(resource, Status.Deleted, false);
		monitor.done();
	}
}
