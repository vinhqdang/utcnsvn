package ivc.repository;

import ivc.plugin.IVCPlugin;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.QualifiedName;

public class CacheManager {
	private IStatusCache statusCache;
	public static final QualifiedName IVC_STATUS_KEY = new QualifiedName(IVCPlugin.PLUGIN_ID, "IVC_STATUS_KEY");

	public CacheManager() {
		ResourcesPlugin.getWorkspace().getSynchronizer().add(IVC_STATUS_KEY);
		statusCache = new StatusCache();
	}

	public IStatusCache getStatusCache() {
		return statusCache;
	}

	public boolean hasCachedStatus(IResource resource) {
		return statusCache.hasCachedStatus(resource);
	}
}
