package ivc.compare;

import org.eclipse.compare.structuremergeviewer.Differencer;

public class SummaryDifferencer extends Differencer {
	// private SVNDiffSummary[] diffSummary;

	// public SummaryDifferencer(SVNDiffSummary[] diffSummary) {
	public SummaryDifferencer() {
		super();
		// this.diffSummary = diffSummary;
	}

	protected boolean contentsEqual(Object input1, Object input2) {
		return false;
		// SummaryEditionNode node = (SummaryEditionNode)input1;
		// for (int i = 0; i < diffSummary.length; i++) {
		//			if (node.getRemoteResource().getRepositoryRelativePath().endsWith("/" + diffSummary[i].getPath())) //$NON-NLS-1$
		// return false;
		// }
		// return true;
	}
}