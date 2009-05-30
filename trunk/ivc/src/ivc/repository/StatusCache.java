package ivc.repository;

import ivc.data.exception.IVCException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;


public class StatusCache implements IStatusCache {

	@Override
	public IResource addStatus(IResource resource, ResourceStatus status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceStatus getStatus(IResource resource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCachedStatus(IResource resource) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IResource removeStatus(IResource resource) {
		// TODO Auto-generated method stub
		return null;
	}

}

final class SyncInfoAccessor {
	protected byte[] internalGetCachedSyncBytes(IResource resource) throws IVCException {
		try {
			return ResourcesPlugin.getWorkspace().getSynchronizer().getSyncInfo(CacheManager.IVC_STATUS_KEY, resource);
		} catch (CoreException e) {
			throw new IVCException(e);
		}
	}

	/*
	 * Set the sync bytes to the synchronizer.
	 */
	protected void internalSetCachedSyncBytes(IResource resource, byte[] syncInfo) throws IVCException {
		try {
			ResourcesPlugin.getWorkspace().getSynchronizer().setSyncInfo(CacheManager.IVC_STATUS_KEY, resource, syncInfo);
		} catch (CoreException e) {
			throw new IVCException(e);
		}
	}
}