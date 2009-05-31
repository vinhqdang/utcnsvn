package ivc.repository;

import org.eclipse.core.resources.IResource;

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
	IResource addStatus(IResource resource, ResourceStatus status);

	/**
	 * removes the status for a given resource
	 * 
	 * @param resource
	 * @return
	 */
	void removeStatus(IResource resource);
}
