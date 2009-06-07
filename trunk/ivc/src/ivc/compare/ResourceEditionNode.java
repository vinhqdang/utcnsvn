package ivc.compare;

import ivc.plugin.IVCPlugin;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.compare.CompareUI;
import org.eclipse.compare.IContentChangeListener;
import org.eclipse.compare.IContentChangeNotifier;
import org.eclipse.compare.IEditableContent;
import org.eclipse.compare.IEncodedStreamContentAccessor;
import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.IDiffContainer;
import org.eclipse.compare.structuremergeviewer.IDiffElement;
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
public class ResourceEditionNode implements ITypedElement, IStreamContentAccessor, IEncodedStreamContentAccessor, IDiffContainer,
		IContentChangeNotifier, IEditableContent {
	public IFile resource;
	private String charset = null;

	public IResource getResource() {
		return resource;
	}

	public ResourceEditionNode(IFile file) {
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
	public void add(IDiffElement arg0) {

	}

	@Override
	public IDiffElement[] getChildren() {
		return null;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public void removeToRoot(IDiffElement arg0) {
	}

	@Override
	public int getKind() {
		return 0;
	}

	@Override
	public IDiffContainer getParent() {
		return null;
	}

	@Override
	public void setParent(IDiffContainer arg0) {

	}

	@Override
	public void addContentChangeListener(IContentChangeListener contentChangeListener) {

	}

	@Override
	public void removeContentChangeListener(IContentChangeListener contentChangeListener) {

	}

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