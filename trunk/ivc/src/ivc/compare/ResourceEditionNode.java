/*******************************************************************************
 * Copyright (c) 2003, 2006 Subclipse project and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Subclipse project committers - initial API and implementation
 ******************************************************************************/
package ivc.compare;

import ivc.plugin.IVCPlugin;
import ivc.resource.ISVNResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import org.eclipse.compare.CompareUI;
import org.eclipse.compare.IContentChangeListener;
import org.eclipse.compare.IContentChangeNotifier;
import org.eclipse.compare.IEditableContent;
import org.eclipse.compare.IEncodedStreamContentAccessor;
import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.IDiffContainer;
import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.compare.structuremergeviewer.IStructureComparator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.graphics.Image;
import org.eclipse.team.core.TeamException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * A class for comparing ISVNRemoteResource objects
 * 
 * <p>
 * 
 * <pre>
 * ResourceEditionNode left = new ResourceEditionNode(editions[0]);
 * ResourceEditionNode right = new ResourceEditionNode(editions[1]);
 * CompareUI.openCompareEditorOnPage(new SVNCompareEditorInput(left, right), getTargetPage());
 * </pre>
 * 
 * </p>
 * 
 */
public class ResourceEditionNode implements ITypedElement, IStreamContentAccessor,
		IEncodedStreamContentAccessor, IDiffContainer, IContentChangeNotifier, IEditableContent {
	public IFile resource;
	Vector<IContentChangeListener> listenerList = new Vector<IContentChangeListener>();
	private String charset = null;

	/**
	 * Creates a new ResourceEditionNode on the given resource edition.
	 */

	/*
	 * get the remote resource for this node
	 */

	/**
	 * Returns true if both resources names are identical. The content is not
	 * considered.
	 * 
	 * @see IComparator#equals
	 */

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

	/**
	 * Enumerate children of this node (if any).
	 * 
	 * @see IStructureComparator#getChildren
	 */

	/**
	 * @see IStreamContentAccessor#getContents()
	 */
	public InputStream getContents() throws CoreException {
		// if (resource == null || resource.isContainer()) {
		// return null;
		// }
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
		return resource == null ? "" : resource.getName(); //$NON-NLS-1$
	}

	/**
	 * Returns the comparison type for this node.
	 * 
	 * @see org.eclipse.compare.ITypedElement#getType()
	 */
	public String getType() {

		String name = resource.getName();
		name = name.substring(name.lastIndexOf('.') + 1);
		return TEXT_TYPE;
	}

	/**
	 * @see IComparator#equals
	 */
	public int hashCode() {
		return getName().hashCode();
	}

	public String getCharset() throws CoreException {
		return charset;
	}

	public void setCharset(String charset) throws CoreException {
		this.charset = charset;
	}

	@Override
	public void add(IDiffElement arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public IDiffElement[] getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeToRoot(IDiffElement arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getKind() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IDiffContainer getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(IDiffContainer arg0) {
		// TODO Auto-generated method stub

	}

	// TODO DE AICI
	@Override
	public void addContentChangeListener(IContentChangeListener contentChangeListener) {
		listenerList.add(contentChangeListener);

	}

	@Override
	public void removeContentChangeListener(IContentChangeListener contentChangeListener) {
		listenerList.remove(contentChangeListener);

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
		throw new NotImplementedException();

	}
}