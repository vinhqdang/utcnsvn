package ivc.repository;

import ivc.fireworks.decorators.Decorator;
import ivc.plugin.IVCPlugin;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.QualifiedName;

/**
 * 
 * @author alexm
 * 
 *         The class used to store and retrieve the status from the Eclipse
 *         Synchronization Information
 */
public class CacheManager {
	private IStatusCache statusCache;
	public static final QualifiedName IVC_STATUS_KEY = new QualifiedName(
			IVCPlugin.PLUGIN_ID, "IVC_STATUS_KEY");

	public CacheManager() {
		ResourcesPlugin.getWorkspace().getSynchronizer().add(IVC_STATUS_KEY);
		statusCache = new StatusCache();
	}

	public IStatusCache getStatusCache() {
		return statusCache;
	}

	/**
	 * returns if the resource has a cached status
	 * 
	 * @param resource
	 *            the resurce for which to return if the resource has a cached status
	 * @return if the resource has a cached status
	 */
	public boolean hasCachedStatus(IResource resource) {
		return statusCache.hasCachedStatus(resource);
	}

	/**
	 * Returns the resource's ResourceStatus
	 * 
	 * @param resource
	 *            the resource
	 * @return the resource's ResourceStatus
	 */
	public ResourceStatus getResourceStatus(IResource resource) {
		return statusCache.getStatus(resource);
	}

	/**
	 * Returns if a resource is managed with the IVCRepositoryProvider or not
	 * 
	 * @param resource
	 *            the resource
	 * @return if a resource has a status
	 */
	public boolean isManaged(IResource resource) {
		return statusCache.getStatus(resource) != null;
	}

	/**
	 * sets the status for a resource
	 * 
	 * @param resource
	 *            the resource
	 * @param status
	 */
	public void setStatus(IResource resource, ResourceStatus status) {
		statusCache.setStatus(resource, status);
		Decorator.getDecorator().refresh(resource);
	}

	/**
	 * removes the status for a resource
	 * 
	 * @param resource
	 *            the resource
	 */
	public void removeStatus(IResource resource) {
		statusCache.removeStatus(resource);
		Decorator.getDecorator().refresh(resource);
	}
}