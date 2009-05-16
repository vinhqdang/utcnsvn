/*******************************************************************************
 * Copyright (c) 2004, 2006 Subclipse project and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Subclipse project committers - initial API and implementation
 ******************************************************************************/
package ivc.repository;


import ivc.plugin.Activator;
import ivc.util.Utilities;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.ResourceNode;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.compare.structuremergeviewer.IStructureComparator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.team.ui.ISaveableWorkbenchPart;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * A compare input for comparing local resource with its remote revision
 * Perform textual check on:
 * - any local modification
 * - revision numbers don't match
 */
public class SVNLocalCompareInput extends CompareEditorInput implements ISaveableWorkbenchPart {
    //private final SVNRevision remoteRevision;
	private IResource resource;
	private Shell shell;
	private final IResource remoteResource; // the remote resource to compare to or null if it does not exist
	private boolean readOnly;
	
    /**
     * Differencer that only uses teh status to determine if a file has changed
     */
    class StatusAwareDifferencer extends Differencer {
        
        /* (non-Javadoc)
         * @see org.eclipse.compare.structuremergeviewer.Differencer#contentsEqual(java.lang.Object, java.lang.Object)
         */
        protected boolean contentsEqual(Object left, Object right) {
            IResource local = null;
            
            if (left instanceof SVNLocalCompareInput.SVNLocalResourceNode) {
                local = ((SVNLocalCompareInput.SVNLocalResourceNode)left).getResource();
            }
            
            if (local == null || right == null) {
                return false;
            }
            
//            try {
//                //if (!local.isManaged()) {
//                    return false;
//                }
//                //return !(local.isDirty());
//            } catch (SVNException e) {
//            }
            
            return super.contentsEqual(left, right);
        }
    }
    
	/**
	 * Node representing a local SVN file.  We can query the status of the resource to determine if
     * it has changed.  It is also used to write the contents back to the file when setContent is called.
	 */
	class SVNLocalResourceNode extends ResourceNode {
		
		private final IResource svnResource;
		private ArrayList fChildren = null;
        
        public SVNLocalResourceNode(IResource svnResource) {
			super(svnResource);
            this.svnResource = svnResource;
		}
		protected InputStream createStream() throws CoreException {
			return ((IFile)getResource()).getContents();
		}
		
        public IResource getLocalResource() {
            return svnResource;
        }
        
		// used by getContentsAction
		public void setContent(byte[] contents) {
			if (contents == null) contents = new byte[0];
			final InputStream is = new ByteArrayInputStream(contents);
			IRunnableWithProgress runnable = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException  {
					try {
						IFile file = (IFile)getResource();
						if (is != null) {
							if (!file.exists()) {
								file.create(is, false, monitor);
							} else {
								file.setContents(is, false, true, monitor);
							}
						} else {
							file.delete(false, true, monitor);
						}
					} catch (CoreException e) {
						throw new InvocationTargetException(e);
					}
				}
			};
			try {
				new ProgressMonitorDialog(shell).run(false, false, runnable);
			} catch (InvocationTargetException e) {
				//SVNUIPlugin.openError(SVNUIPlugin.getPlugin().getWorkbench().getActiveWorkbenchWindow().getShell(), Policy.bind("TeamFile.saveChanges", resource.getName()), null, e); //$NON-NLS-1$
				System.out.println("error");
			} catch (InterruptedException e) {
				// Ignore
			}
			fireContentChanged();
		}
		
//        public Object[] getChildren() {
//            if (fChildren == null) {
//                fChildren= new ArrayList();
//                
//                if (svnResource instanceof ISVNLocalFolder) {
//                    try {
//                        IResource[] members = (ISVNLocalResource[])((ISVNLocalFolder)svnResource).members(null, ISVNFolder.ALL_EXISTING_UNIGNORED_MEMBERS);
//                        for (int i= 0; i < members.length; i++) {
//                            IStructureComparator child= createChild(members[i]);
//                            if (child != null)
//                                fChildren.add(child);
//                        }
//                    } catch (CoreException ex) {
//                        // NeedWork
//                    }
//                }
//            }
//            return fChildren.toArray();
//        }
        
		/* (non-Javadoc)
		 * @see org.eclipse.compare.ResourceNode#createChild(org.eclipse.core.resources.IResource)
		 */
		protected IStructureComparator createChild(IResource child) {
			return new SVNLocalResourceNode(child);
		}
		
		public ITypedElement replace(ITypedElement child, ITypedElement other) {
			return null;
		}
	}
	
	/**
	 * @throws SVNException
	 * creates a SVNLocalCompareInput, allows setting whether the current local resource is read only or not.
	 */
	public SVNLocalCompareInput(IResource resource,  boolean readOnly) throws Exception {
		super(new CompareConfiguration());
        //this.remoteRevision = revision;
        this.readOnly = readOnly;
        this.resource = resource;
		// SVNRevision can be any valid revision : BASE, HEAD, number ...
		//todo check
        this.remoteResource = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path("d:\\Temp\\test.txt"));
        
        // remoteResouce can be null if there is no corresponding remote resource
        // (for example no base because resource has just been added)
	}

	/**
	 * Constructor which allows 
	 * @throws SVNException
	 * creates a SVNLocalCompareInput, defaultin to read/write.  
	 */
	public SVNLocalCompareInput(IResource resource) throws Exception {
		this(resource, false);
	}

	/**
	 * @throws SVNException
	 * creates a SVNCompareRevisionsInput  
	 */
	public SVNLocalCompareInput(IResource resource, IResource remoteResource) throws Exception {
		super(new CompareConfiguration());
		this.resource = resource;
		this.remoteResource = remoteResource;
        //this.remoteRevision = remoteResource.getRevision();
	}
	
	
	/**
	 * initialize the labels : the title, the lft label and the right one
	 */
	private void initLabels() {
		CompareConfiguration cc = getCompareConfiguration();
		String resourceName = resource.getName();	
		setTitle("Bossy"); //$NON-NLS-1$
		cc.setLeftEditable(! readOnly);
		cc.setRightEditable(false);
		
		String leftLabel = "hole"; //$NON-NLS-1$
		cc.setLeftLabel(leftLabel);
		String rightLabel = "SVNCompareRevisionsInput.repository"; //$NON-NLS-1$
		cc.setRightLabel(rightLabel);
	}
	
	/**
	 * Runs the compare operation and returns the compare result.
	 */
	protected Object prepareInput(IProgressMonitor monitor){
		initLabels();
		ITypedElement left = new SVNLocalResourceNode(resource);
		//ResourceEditionNode right = new ResourceEditionNode(remoteResource);
		//if(left.getType()==ITypedElement.FOLDER_TYPE){
		//	right.setLocalResource((SVNLocalResourceNode) left);
		//}

		IResource right=ResourcesPlugin.getWorkspace().getRoot().getFile(new Path("d:\\Temp\\test.txt"));
		String localCharset = Utilities.getCharset(resource);
		
        
        return new RevisionAwareDifferencer ().findDifferences(false, monitor,null,null,left,right);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {
		try {
			saveChanges(monitor);
		} catch (CoreException e) {
			//Utils.handle(e);
			System.out.println("DoSave Exception");
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
	 */
	public void doSaveAs() {
		// noop
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#isDirty()
	 */
	public boolean isDirty() {
		return isSaveNeeded();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#isSaveOnCloseNeeded()
	 */
	public boolean isSaveOnCloseNeeded() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#addPropertyListener(org.eclipse.ui.IPropertyListener)
	 */
	public void addPropertyListener(IPropertyListener listener) {
		
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		createContents(parent);
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose() {
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#getSite()
	 */
	public IWorkbenchPartSite getSite() {
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#getTitleToolTip()
	 */
	public String getTitleToolTip() {
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#removePropertyListener(org.eclipse.ui.IPropertyListener)
	 */
	public void removePropertyListener(IPropertyListener listener) {
	}
    
}
