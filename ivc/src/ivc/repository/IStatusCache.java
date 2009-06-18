package ivc.repository;

import org.eclipse.core.resources.IResource;

/**
 * 
 * @author alexm
 * 
 *         the interface is used to manage the status cache
 */
public interface IStatusCache {
	/**
	 * Returns if a resource has a cached status
	 * 
	 * @param resource
	 * @return Returns if a resource has a cached status
	 */
	boolean hasCachedStatus(IResource resource);

	/**
	 * Returns the resource status for a given resource
	 * 
	 * @param resource
	 * @return
	 */
	ResourceStatus getStatus(IResource resource);

	/**
	 * Adds status to the given resource
	 * 
	 * @param resource
	 * @param status
	 * @return
	 */
	IResource setStatus(IResource resource, ResourceStatus status);

	/**
	 * removes the status for a given resource
	 * 
	 * @param resource
	 * @return
	 */
	void removeStatus(IResource resource);
}
