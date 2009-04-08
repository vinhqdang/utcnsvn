package ivc.sync.org;

import org.eclipse.core.resources.IResource;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.core.synchronize.SyncInfo;
import org.eclipse.team.core.variants.IResourceVariant;
import org.eclipse.team.core.variants.IResourceVariantComparator;

public class LocalHistorySyncInfo extends SyncInfo {
	
	public LocalHistorySyncInfo(IResource local, IResourceVariant remote, IResourceVariantComparator comparator) {
		super(local, null, remote, comparator);
	}

	protected int calculateKind() throws TeamException {
		if (getRemote() == null)
			return IN_SYNC;
		return super.calculateKind();
	}
}
