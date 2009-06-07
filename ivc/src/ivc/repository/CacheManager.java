package ivc.repository;

import ivc.fireworks.decorators.Decorator;
import ivc.plugin.IVCPlugin;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.QualifiedName;

public class CacheManager implements IResourceChangeListener {
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

	public ResourceStatus getResourceStatus(IResource resource) {
		return statusCache.getStatus(resource);
	}

	public boolean isManaged(IResource resource) {
		return statusCache.getStatus(resource) != null;
	}

	public void setStatus(IResource resource, ResourceStatus status) {
		statusCache.setStatus(resource, status);
		Decorator.getDecorator().refresh(resource);
	}
	
	public void removeStatus(IResource resource){		
		statusCache.removeStatus(resource);
		Decorator.getDecorator().refresh(resource);
	}

	@Override
	public void resourceChanged(IResourceChangeEvent arg0) {
		// TODO Auto-generated method stub

	}
}