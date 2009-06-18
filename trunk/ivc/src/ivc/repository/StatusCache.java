package ivc.repository;

import ivc.data.exception.IVCException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

/**
 * 
 * @author alexm
 * 
 *         The class is used to store to manage the status cache
 */
public class StatusCache implements IStatusCache {
	private StatusSyncAccessor accessor;

	public StatusCache() {
		accessor = new StatusSyncAccessor();

	}

	@Override
	public IResource setStatus(IResource resource, ResourceStatus status) {
		byte[] statusBytes = status.getBytes();

		try {
			accessor.setCachedStatusBytes(resource, statusBytes);
		} catch (IVCException e) {
			e.printStackTrace();
		}

		return resource;
	}

	@Override
	public ResourceStatus getStatus(IResource resource) {
		try {
			byte[] res = accessor.internalGetCachedStatusBytes(resource);
			if (res == null)
				return null;
			ResourceStatus resourceStatus = new ResourceStatus(res);

			return resourceStatus;
		} catch (IVCException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean hasCachedStatus(IResource resource) {
		try {
			return !(accessor.internalGetCachedStatusBytes(resource) == null);
		} catch (IVCException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void removeStatus(IResource resource) {
		try {
			accessor.setCachedStatusBytes(resource, null);
		} catch (IVCException e) {
			e.printStackTrace();
		}
	}

}

/**
 * 
 * @author alexm
 * 
 *         Class used to get and store the status bytes
 */
final class StatusSyncAccessor {

	/**
	 * Gets the status bytes from the cache
	 * 
	 * @param resource
	 * @return
	 * @throws IVCException
	 */
	protected byte[] internalGetCachedStatusBytes(IResource resource) throws IVCException {
		try {
			byte[] info = ResourcesPlugin.getWorkspace().getSynchronizer().getSyncInfo(
					CacheManager.IVC_STATUS_KEY, resource);
			return info;
		} catch (CoreException e) {
			throw new IVCException(e);
		}
	}

	/**
	 * Sets the status bytes in the cache
	 * 
	 * @param resource
	 * @param syncInfo
	 * @throws IVCException
	 */
	protected void setCachedStatusBytes(IResource resource, byte[] syncInfo)
			throws IVCException {
		try {
			ResourcesPlugin.getWorkspace().getSynchronizer().setSyncInfo(
					CacheManager.IVC_STATUS_KEY, resource, syncInfo);
		} catch (CoreException e) {
			throw new IVCException(e);
		}
	}
}