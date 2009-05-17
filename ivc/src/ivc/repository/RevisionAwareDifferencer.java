/*******************************************************************************
 * Copyright (c) 2005, 2006 Subclipse project and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Subclipse project committers - initial API and implementation
 ******************************************************************************/
package ivc.repository;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;

public final class RevisionAwareDifferencer extends Differencer {
	// comparison constants
	private static final int NODE_EQUAL = 0;
	private static final int NODE_NOT_EQUAL = 1;
	private static final int NODE_UNKNOWN = 2;

	/**
	 * compare two ResourceEditionNode
	 */
//	protected boolean contentsEqual(Object input1, Object input2) {
//		int compare;
//
//		if (input1 instanceof SVNLocalCompareInput.SVNLocalResourceNode) {
//			compare = compareStatusAndRevisions(input1, input2);
//		} else {
//			compare = compareEditions(input1, input2);
//		}
//		if (compare == NODE_EQUAL) {
//			return true;
//		}
//		if (compare == NODE_NOT_EQUAL) {
//			return false;
//		}
//		// revert to slow content comparison
//		return super.contentsEqual(input1, input2);
//	}

	/**
	 * Called for every leaf or node compare to update progress information.
	 */
	protected void updateProgress(IProgressMonitor progressMonitor, Object node) {
		if (node instanceof ITypedElement) {
			ITypedElement element = (ITypedElement) node;
			progressMonitor.subTask("CompareEditorInput.fileProgress"); //$NON-NLS-1$
			progressMonitor.worked(1);
		}
	}

	/**
	 * Compares two nodes to determine if they are equal. Returns NODE_EQUAL of
	 * they are the same, NODE_NOT_EQUAL if they are different, and NODE_UNKNOWN
	 * if comparison was not possible.
	 */
	protected int compareStatusAndRevisions(Object left, Object right) {
		IResource localResource = null;
		if (left instanceof SVNLocalCompareInput.SVNLocalResourceNode) {
			localResource = ((SVNLocalCompareInput.SVNLocalResourceNode) left)
					.getLocalResource();
		}

		IResource edition = null;
		// if (right instanceof ResourceEditionNode)
		// edition = ((ResourceEditionNode)right).getRemoteResource();
		//        
		if (localResource == null || edition == null) {
			return NODE_UNKNOWN;
		}

		// if they're both non-files, they're the same
		// if (localResource.isFolder() && edition.isContainer()) {
		// return NODE_EQUAL;
		// }
		// if they have different types, they're different
		// if (localResource.isFolder() != edition.isContainer()) {
		// return NODE_NOT_EQUAL;
		// }

		String leftLocation = localResource.getFullPath().toOSString();
		String rightLocation = edition.getProjectRelativePath().toOSString();
		if (!leftLocation.equals(rightLocation)) {
			return NODE_UNKNOWN;
		}

		// LocalResourceStatus localStatus = null;
		// try {
		// localStatus = localResource.getStatus();
		//        
		// if (localStatus == null) {
		// return NODE_UNKNOWN;
		// }
		//            
		// if (!localResource.isDirty() &&
		// localResource.getUrl().equals(edition.getUrl()) &&
		// localStatus.getLastChangedRevision().equals(edition.getLastChangedRevision()))
		// {
		// return NODE_EQUAL;
		// }
		// } catch (SVNException e) {
		// return NODE_UNKNOWN;
		// }

		return NODE_UNKNOWN;
	}

	/**
	 * Compares two nodes to determine if they are equal. Returns NODE_EQUAL of
	 * they are the same, NODE_NOT_EQUAL if they are different, and NODE_UNKNOWN
	 * if comparison was not possible.
	 */
	protected int compareEditions(Object left, Object right) {
		// calculate the type for the left contribution
		IResource leftEdition = null;
		// if (left instanceof ResourceEditionNode) {
		// leftEdition = ((ResourceEditionNode)left).getRemoteResource();
		// }
		//        
		// // calculate the type for the right contribution
		// ISVNRemoteResource rightEdition = null;
		// if (right instanceof ResourceEditionNode)
		// rightEdition = ((ResourceEditionNode)right).getRemoteResource();
		//        

		// compare them

		// if (leftEdition == null || rightEdition == null) {
		// return NODE_UNKNOWN;
		// }
		// // if they're both non-files, they're the same
		// if (leftEdition.isContainer() && rightEdition.isContainer()) {
		// return NODE_EQUAL;
		// }
		// // if they have different types, they're different
		// if (leftEdition.isContainer() != rightEdition.isContainer()) {
		// return NODE_NOT_EQUAL;
		// }

		// // String leftLocation = leftEdition.getRepository().getLocation();
		// // String rightLocation = rightEdition.getRepository().getLocation();
		// // if (!leftLocation.equals(rightLocation)) {
		// // return NODE_UNKNOWN;
		// // }
		// //
		// // if (leftEdition.getUrl().equals(rightEdition.getUrl()) &&
		// //
		// leftEdition.getLastChangedRevision().equals(rightEdition.getLastChangedRevision()))
		// {
		// // return NODE_EQUAL;
		// } else {
		// // if(considerContentIfRevisionOrPathDiffers()) {
		// return NODE_UNKNOWN;
		// } else {
		// return NODE_NOT_EQUAL;
		// }
		return 1;
	}

}

// private boolean considerContentIfRevisionOrPathDiffers() {
// return
// SVNUIPlugin.getPlugin().getPreferenceStore().getBoolean(ISVNUIConstants.PREF_CONSIDER_CONTENTS);
// }
//
// public Viewer createDiffViewer(Composite parent) {
// Viewer viewer = super.createDiffViewer(parent);
// viewer.addSelectionChangedListener(new ISelectionChangedListener() {
// public void selectionChanged(SelectionChangedEvent event) {
// CompareConfiguration cc = getCompareConfiguration();
// setLabels(cc, (IStructuredSelection)event.getSelection());
// }
// });
// return viewer;
// }