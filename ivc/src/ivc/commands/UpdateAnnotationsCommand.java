/**
 * 
 */
package ivc.commands;

import ivc.data.IVCProject;
import ivc.data.annotation.ResourcesAnnotations;
import ivc.data.operation.Operation;
import ivc.data.operation.OperationHistory;
import ivc.data.operation.OperationHistoryList;
import ivc.fireworks.markers.MarkersManager;
import ivc.util.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;

/**
 * @author danielan
 * 
 */
public class UpdateAnnotationsCommand implements CommandIntf {

	private IVCProject project;
	private OperationHistory rl;
	private String remoteAddress;

	private OperationHistoryList rcl;
	private OperationHistoryList rul;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.data.command.CommandIntf#execute(ivc.data.command.CommandArgs)
	 */
	@Override
	public Result execute(CommandArgs args) {
		// TODO 1. implement update annotations command
		project = (IVCProject) args.getArgumentValue(Constants.IVCPROJECT);
		rl = (OperationHistory) args.getArgumentValue(Constants.OPERATION_HIST);
		remoteAddress = (String) args.getArgumentValue(Constants.HOST_ADDRESS);
		Boolean isCommit = (Boolean) args.getArgumentValue(Constants.ISCOMMIT);
		if (isCommit) {
			rcl = project.getRemoteCommitedLog();
			computeCommitedAnnotations(rl);
			project.setRemoteCommitedLog(rcl);
		} else {
			rul = project.getRemoteUncommitedLog(remoteAddress);
			computeUncommitedAnnotations(rl, remoteAddress);
			project.setRemoteUncommitedLog(rul, remoteAddress);
		}
		return new Result(true, "Success", null);
	}

	/**
	 * Procedure computeCommittedAnnotations generates annotations from the list RL of committed operations. All operations in list RL are
	 * contextually preceding each other and they have all the same base version. As RL represents a list of committed operations not integrated on
	 * the local document version, the base version of this list is higher than the base version of the local document. If RL is causally ready for
	 * execution, it has to exclude the list RCL in order to be defined on the same context as LL[0]. Operations in the result list are then
	 * transformed to be each defined on the context of LL[0] and then applied to annotate the document with the committed changes. The original list
	 * RL is then appended to RCL.
	 * 
	 * if (causallyReady(RL)) { ARL := ET(RL, RCL); ARL := transformIntoConc(ARL); applyAnnotations(ARL, true); append(RL, RCL); } else append(RL,
	 * TempRCL)
	 * 
	 * @param rl
	 */
	private void computeCommitedAnnotations(OperationHistory rl) {
		OperationHistory arl = new OperationHistory();
		if (causallyReady(rl)) {
			arl = rl.excludeOperations(rcl.getOperationHistForFile(rl.getFilePath()));
			arl = transformIntoConc(arl);
			applyAnnotations(arl, true, null);
		}
		rcl.appendOperationHistory(rl);
	}

	/**
	 * Procedure transformIntoConc transforms operations of list L to be defined on the context of definition of the operation L[0]. Each operation in
	 * L excludes the effects of the operations in L that precede it.
	 * 
	 * transformIntoConc(L):L’ L0 := L; for (i:=|L0|-1; i>1; i--) for (j:=i-1; j0; j--) L0[i] := ET(L0[i], L0[j]); return L0;
	 * 
	 * @param l
	 */
	private OperationHistory transformIntoConc(OperationHistory L) {
		OperationHistory L0 = new OperationHistory();
		if (L == null) {
			return L0;
		}
		L0 = L;
		try {
			int size = L0.getOperations().size();
			for (int i = 0; i < size; i++) {
				for (int j = i + 1; j < size; j++) {
					Operation opi = L0.getOperations().get(i);
					Operation opj = L0.getOperations().get(j);
					Operation et = opi.excludeOperation(opj);
					L0.setOperation(i, et);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return L0;
	}

	/**
	 * Procedure applyAnnotations annotates the positions of the document defined by the list of operations ARL. flag defines if operations are
	 * committed or uncommitted. Operations contained in ARL are transformed against the local list of operations LL.
	 * 
	 * applyAnnotations(ARL, flag) for (i:=0; i<|ARL|; i++) { IT(ARL[i], LL); annotate(ARL[i], flag); }
	 * 
	 * @param arl
	 * @param flag
	 */
	private void applyAnnotations(OperationHistory arl, boolean flag, String host) {
		OperationHistoryList ll = project.getLocalLog();
		OperationHistory llOh = ll.getOperationHistForFile(arl.getFilePath());
		if (llOh != null && !llOh.getOperations().isEmpty()) {
			arl = arl.includeOperations(llOh);
		}
		if (flag) {
			operationsToAnnotation(arl, Constants.COMMITED);
		} else {
			operationsToAnnotation(arl, host);
		}

	}

	private boolean causallyReady(OperationHistory rl) {
		String filePath = rl.getFilePath();
		Integer localVersion = project.getCurrentVersion().get(filePath);
		Integer remoteVersion = rl.getOperations().getLast().getFileVersion();
		return (localVersion.equals(remoteVersion));
	}

	/**
	 * Procedure computeUncommittedAnnotations generates annotations from the list of uncommitted operations RL received directly from Useri. List RL
	 * contains contextually preceding remote operations having the same base version. If list RL is causally ready, it has to exclude all operations
	 * stored in RUL previously sent by Useri. ARL denotes the result of the transformation of RL. If Useri worked on an older version of the document
	 * than the local base version, ARL has to be transformed to include ms the list of operations representing their difference. If Useri worked on a
	 * more recent version of the document than the local base version, ARL has to be transformed to exclude their difference. In this way ARL and LL
	 * are defined on the same document state. Procedure transformIntoConc is then called to transform operations in ARL to be all defined on the
	 * generation context of the local log LL. Operations obtained as result of transformation are applied then to annotate positions of the local
	 * document where uncommitted changes occurred. List RL is then appended to the list of uncommitted operations of Useri. Due to space limitations,
	 * we do not include the algorithm that implements the procedure described above.
	 * 
	 * @param rl
	 * @param hostAddress
	 */
	private void computeUncommitedAnnotations(OperationHistory rl, String hostAddress) {
		OperationHistory arl = new OperationHistory();
		String filePath = rl.getFilePath();
		OperationHistory rulOh = rul.getOperationHistForFile(filePath);
		if (causallyReady(rl)) {
			arl = rl.excludeOperations(rulOh);
			if (rulOh != null && !rulOh.getOperations().isEmpty()) {
				if (rl.getOperations().getLast().getFileVersion().intValue() != rulOh.getOperations().getLast().getFileVersion().intValue()) {
					OperationHistory diff = getVersionDiffs(rl, rulOh);
					if (rl.getOperations().getLast().getFileVersion().intValue() < rulOh.getOperations().getLast().getFileVersion().intValue()) {
						arl = arl.includeOperations(diff);
					} else {
						arl = arl.excludeOperations(diff);
					}
				}
			}
			arl = transformIntoConc(arl);
			applyAnnotations(arl, false, hostAddress);
		}
		rul.appendOperationHistory(rl);
	}

	private void operationsToAnnotation(OperationHistory oh, String user) {
		ResourcesAnnotations ra = project.getResourcesAnnotations();
		String filePath = oh.getFilePath();
		List<Integer> positions = new ArrayList<Integer>();
		Iterator<Operation> it = oh.getOperations().descendingIterator();
		while (it.hasNext()) {
			Operation op = it.next();
			Integer position = op.getPosition();
			positions.add(position);
		}
		ra.setAnnotations(filePath, user, positions);

		IFile file = project.getProject().getFile(filePath);
		if (file.exists()) {
			try {
				MarkersManager.updateMarkers(file);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private OperationHistory getVersionDiffs(OperationHistory oh1, OperationHistory oh2) {
		OperationHistory newOh = new OperationHistory();
		newOh.setFilePath(oh1.getFilePath());
		int min = oh1.getOperations().getLast().getFileVersion().intValue();
		int max = oh2.getOperations().getLast().getFileVersion().intValue();
		Iterator<Operation> it = oh1.getOperations().descendingIterator();
		if (min > max) {
			int aux = min;
			min = max;
			max = aux;
			it = oh2.getOperations().descendingIterator();
		}
		while (it.hasNext()) {
			Operation op = it.next();
			if (op.getFileVersion() < max) {
				newOh.addOperation(op);
			}
		}
		return newOh;
	}
}
