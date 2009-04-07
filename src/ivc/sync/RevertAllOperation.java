package ivc.sync;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.*;
import org.eclipse.team.core.synchronize.SyncInfo;
import org.eclipse.team.ui.synchronize.ISynchronizePageConfiguration;
import org.eclipse.team.ui.synchronize.SynchronizeModelOperation;
import org.eclipse.ui.actions.*;

public class RevertAllOperation extends SynchronizeModelOperation {

	protected RevertAllOperation(ISynchronizePageConfiguration configuration, IDiffElement[] elements) {
		super(configuration, elements);
	}
	
	protected boolean canRunAsJob() {
		return true;
	}

	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		final SyncInfo infos[] = getSyncInfoSet().getSyncInfos();
		if(infos.length == 0) return;
			
//		 operation= new WorkspaceModifyOperation() {
//			public void execute(IProgressMonitor pm) throws InvocationTargetException {
//				try {
//					pm.beginTask("Reverting from local history", 100 * infos.length);	 //$NON-NLS-1$
//					for (int i = 0; i < infos.length; i++) {
//						SyncInfo info = infos[i];
//						LocalHistoryVariant state = (LocalHistoryVariant)info.getRemote();
//						IFile file = (IFile)info.getLocal();
//						if(file.exists()) {
//							file.setContents(state.getFileState(), false, true, new SubProgressMonitor(pm, 100));
//						} else {
//							// TODO: have to pre-create parents if they dont exist
//							file.create(state.getFileState().getContents(), false, new SubProgressMonitor(pm, 100));
//						}
//					}
//				} catch (CoreException e) {
//					throw new InvocationTargetException(e);
//				} finally {
//					pm.done();
//				}
//			}			
//			public void cacat(){
//			
//			}
//		};
//		operation
		
	}
}
