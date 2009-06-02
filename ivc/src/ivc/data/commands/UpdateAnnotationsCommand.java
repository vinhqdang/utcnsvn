/**
 * 
 */
package ivc.data.commands;

import java.util.List;

import ivc.data.Operation;

/**
 * @author danielan
 * 
 */
public class UpdateAnnotationsCommand implements CommandIntf {

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.data.command.CommandIntf#execute(ivc.data.command.CommandArgs)
	 */
	@Override
	public Result execute(CommandArgs args) {
		// TODO 1. implement update annotations command
		return null;
	}

	/**
	 * Procedure computeCommittedAnnotations generates annotations from the list
	 * RL of committed operations. All operations in list RL are contextually
	 * preceding each other and they have all the same base version. As RL
	 * represents a list of committed operations not integrated on the local
	 * document version, the base version of this list is higher than the base
	 * version of the local document. If RL is causally ready for execution, it
	 * has to exclude the list RCL in order to be defined on the same context as
	 * LL[0]. Operations in the result list are then transformed to be each
	 * defined on the context of LL[0] and then applied to annotate the document
	 * with the committed changes. The original list RL is then appended to RCL.
	 * 
	 * if (causallyReady(RL)) { ARL := ET(RL, RCL); ARL :=
	 * transformIntoConc(ARL); applyAnnotations(ARL, true); append(RL, RCL); }
	 * else append(RL, TempRCL)
	 * 
	 * @param rl
	 */
	private void computeCommitedAnnotations(List<Operation> rl) {

	}

	/**
	 * Procedure transformIntoConc transforms operations of list L to be defined
	 * on the context of definition of the operation L[0]. Each operation in L
	 * excludes the effects of the operations in L that precede it.
	 * 
	 * transformIntoConc(L):L’ L0 := L; for (i:=|L0|-1; i>1; i--) for (j:=i-1;
	 * j0; j--) L0[i] := ET(L0[i], L0[j]); return L0;
	 * 
	 * @param l
	 */
	private void transformIntoConc(List<Operation> l) {

	}

	/**
	 * Procedure applyAnnotations annotates the positions of the document
	 * defined by the list of operations ARL. flag defines if operations are
	 * committed or uncommitted. Operations contained in ARL are transformed
	 * against the local list of operations LL. 
	 * 
	 * applyAnnotations(ARL, flag) for
	 * (i:=0; i<|ARL|; i++) { IT(ARL[i], LL); annotate(ARL[i], flag); }
	 * 
	 * @param arl
	 * @param flag
	 */
	private void applyAnnotations(List<Operation> arl, boolean flag) {

	}

}
