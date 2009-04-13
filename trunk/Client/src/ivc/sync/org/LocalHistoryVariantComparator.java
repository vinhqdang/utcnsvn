package ivc.sync.org;

import org.eclipse.core.resources.IResource;
import org.eclipse.team.core.variants.IResourceVariant;
import org.eclipse.team.core.variants.IResourceVariantComparator;

public class LocalHistoryVariantComparator implements IResourceVariantComparator {
	public boolean compare(IResource local, IResourceVariant remote) {
		return false;
	}

	public boolean compare(IResourceVariant base, IResourceVariant remote) {
		return false;
	}

	public boolean isThreeWay() {
		return false;
	}
}
