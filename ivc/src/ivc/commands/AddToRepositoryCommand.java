package ivc.commands;

import ivc.data.operation.Operation;
import ivc.data.operation.OperationHistory;
import ivc.managers.ProjectsManager;
import ivc.util.Constants;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.team.ui.TeamOperation;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author danielan
 * 
 */
public class AddToRepositoryCommand extends TeamOperation {
	private IResource resource;

	public AddToRepositoryCommand(IWorkbenchPart part, IResource resource) {
		super(part);
		this.resource = resource;
	}

	@Override
	/*
	 * * Creates new structural operation of type ADD_FILE or ADD_FOLDER whenever a new
	 * resource is created. After the creation of the new operation it calls the
	 * HandleOperationCommand to store this operation in the corresponding log files
	 */
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		if (!ProjectsManager.instance().isManaged(resource)) {
			// 1. add file to repository
			monitor.beginTask("Adding resource to workspace", 1);
			Operation operation = new Operation();
			if (resource instanceof IFile) {
				operation.setOperationType(Operation.ADD_FILE);
			} else {
				operation.setOperationType(Operation.ADD_FOLDER);
			}
			operation.setFilePath(resource.getProjectRelativePath().toOSString());
			operation.setFileVersion(1);
			operation.setDate(new Date());
			OperationHistory oh = new OperationHistory();
			oh.setFilePath(operation.getFilePath());
			oh.addOperation(operation);
			HandleOperationCommand command = new HandleOperationCommand();
			CommandArgs args = new CommandArgs();
			args.putArgument(Constants.OPERATION_HIST, oh);
			args.putArgument(Constants.IPROJECT, resource.getProject());
			command.execute(args);

			ProjectsManager.instance().setAddedStatus(resource);
			monitor.done();
		}
	}

}
