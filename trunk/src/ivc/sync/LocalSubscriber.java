package ivc.sync;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.core.synchronize.SyncInfo;
import org.eclipse.team.core.variants.IResourceVariant;
import org.eclipse.team.ui.synchronize.AbstractSynchronizeParticipant;
import org.eclipse.team.ui.synchronize.ISynchronizeModelElement;
import org.eclipse.team.ui.synchronize.ISynchronizePageConfiguration;
import org.eclipse.team.ui.synchronize.SynchronizeModelAction;
import org.eclipse.team.ui.synchronize.SynchronizeModelOperation;
import org.eclipse.team.ui.synchronize.SynchronizePageActionGroup;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPageBookViewPage;

public class LocalSubscriber extends AbstractSynchronizeParticipant {
	
	public IResource[] members(IResource resource) throws TeamException {
		try {
			if (resource.getType() == IResource.FILE)
				return new IResource[0];
			IContainer container = (IContainer) resource;
			List existingChildren = new ArrayList(Arrays.asList(container
					.members()));
			existingChildren.addAll(Arrays.asList(container
					.findDeletedMembersWithHistory(IResource.DEPTH_INFINITE,
							null)));
			return (IResource[]) existingChildren
					.toArray(new IResource[existingChildren.size()]);
		} catch (CoreException e) {
			throw TeamException.asTeamException(e);
		}
	}

	public SyncInfo getSyncInfo(IResource resource) throws TeamException {
		try {
			IResourceVariant variant = null;
			if (resource.getType() == IResource.FILE) {
				IFile file = (IFile) resource;
				IFileState[] states = file.getHistory(null);
				if (states.length > 0) {
					// last state only
					variant = new LocalHistoryVariant(states[0]);
				}
			}
			SyncInfo info = new LocalHistorySyncInfo(resource, variant,
					new LocalHistoryVariantComparator());
			info.init();
			return info;
		} catch (CoreException e) {
			throw TeamException.asTeamException(e);
		}
	}

	protected void initializeConfiguration(
			ISynchronizePageConfiguration configuration) {
		//super.initializeConfiguration(configuration);
		configuration.addMenuGroup(
				ISynchronizePageConfiguration.P_CONTEXT_MENU,
				CONTEXT_MENU_CONTRIBUTION_GROUP);
		configuration
				.addActionContribution(new LocalHistoryActionContribution());
		configuration.addLabelDecorator(new LocalHistoryDecorator());
	}

	public static final String CONTEXT_MENU_CONTRIBUTION_GROUP = "context_group_1"; //$NON-NLS-1$

	private class LocalHistoryActionContribution extends
			SynchronizePageActionGroup {
		public void initialize(ISynchronizePageConfiguration configuration) {
			super.initialize(configuration);
			appendToGroup(
					ISynchronizePageConfiguration.P_CONTEXT_MENU,
					CONTEXT_MENU_CONTRIBUTION_GROUP,
					new SynchronizeModelAction(
							"Revert to latest in local history", configuration) { //$NON-NLS-1$

						@Override
						protected SynchronizeModelOperation getSubscriberOperation(
								ISynchronizePageConfiguration configuration,
								IDiffElement[] elements) {
							return new RevertAllOperation(configuration,
									elements);

						}
					});
		}
	}

	private class LocalHistoryDecorator extends LabelProvider implements
			ILabelDecorator {
		public String decorateText(String text, Object element) {
			if (element instanceof ISynchronizeModelElement) {
				ISynchronizeModelElement node = (ISynchronizeModelElement) element;
				if (node instanceof IAdaptable) {
					SyncInfo info = (SyncInfo) ((IAdaptable) node)
							.getAdapter(SyncInfo.class);
					if (info != null) {
						LocalHistoryVariant state = (LocalHistoryVariant) info
								.getRemote();
						return text + " (" + state.getContentIdentifier() + ")";
					}
				}
			}
			return text;
		}

		public Image decorateImage(Image image, Object element) {
			return null;
		}
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public IPageBookViewPage createPage(ISynchronizePageConfiguration arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void run(IWorkbenchPart arg0) {
		// TODO Auto-generated method stub

	}

}
