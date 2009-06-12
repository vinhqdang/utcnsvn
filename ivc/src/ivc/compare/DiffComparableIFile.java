package ivc.compare;

import ivc.plugin.IVCPlugin;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.compare.CompareUI;
import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.graphics.Image;
import org.eclipse.team.core.TeamException;

/**
 * Class used to provide input for the IVCCompareEditorInput
 * 
 * @author alexm
 * 
 */
public class DiffComparableIFile implements IDiffComparable {
	public IFile resource;
	private String charset = null;

	public IResource getResource() {
		return resource;
	}

	protected DiffComparableIFile(IFile file) {
		resource = file;
	}

	public boolean equals(Object other) {
		if (other instanceof ITypedElement) {
			String otherName = ((ITypedElement) other).getName();
			return getName().equals(otherName);
		}
		return super.equals(other);
	}

	/*
	 * Returns the contents of the file
	 * 
	 * @see org.eclipse.compare.IStreamContentAccessor#getContents()
	 */
	public InputStream getContents() throws CoreException {
		try {
			final InputStream[] holder = new InputStream[1];
			IVCPlugin.plugin.runWithProgress(null, true, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
					try {
						holder[0] = resource.getContents();
					} catch (CoreException e1) {
						System.out.println(e1);
					}
				}
			});
			return holder[0];
		} catch (InterruptedException e) {
			// operation canceled
		} catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();
			if (t instanceof TeamException) {
				throw new CoreException(((TeamException) t).getStatus());
			}
			// should not get here
		}
		return new ByteArrayInputStream(new byte[0]);
	}

	/*
	 * @see org.eclipse.compare.ITypedElement#getImage()
	 */
	public Image getImage() {
		return CompareUI.getImage(resource);
	}

	/*
	 * Returns the name of this node.
	 * 
	 * @see org.eclipse.compare.ITypedElement#getName()
	 */
	public String getName() {
		return resource == null ? "" : resource.getName();
	}

	/**
	 * Returns the comparison type for this node.
	 * 
	 * @see org.eclipse.compare.ITypedElement#getType()
	 */
	public String getType() {
		return TEXT_TYPE;
	}

	public int hashCode() {
		return getName().hashCode();
	}

	public String getCharset() throws CoreException {
		return charset;
	}

	public void setCharset(String charset) throws CoreException {
		this.charset = charset;
	}

	/**
	 * the following methods are only implemented to support a simple compare between two IFiles
	 */
	


	@Override
	public boolean isEditable() {

		return true;
	}

	@Override
	public ITypedElement replace(ITypedElement dest, ITypedElement src) {
		return dest;
	}

	@Override
	public void setContent(byte[] arg0) {

	}
}