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
package ivc.resource;


import ivc.compare.SummaryDifferencer;
import ivc.repository.SVNRevision;

import java.io.ByteArrayInputStream;
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
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
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
    private final SVNRevision remoteRevision;
	private ISVNLocalResource resource;
	private Shell shell;
	private final ISVNLocalResource remoteResource; // the remote resource to compare to or null if it does not exist
	private boolean readOnly;
	
    /**
     * Differencer that only uses teh status to determine if a file has changed
     */
    class StatusAwareDifferencer extends Differencer {
        
        /* (non-Javadoc)
         * @see org.eclipse.compare.structuremergeviewer.Differencer#contentsEqual(java.lang.Object, java.lang.Object)
         */
        protected boolean contentsEqual(Object left, Object right) {
            ISVNLocalResource local = null;
            
            if (left instanceof SVNLocalCompareInput.SVNLocalResourceNode) {
                local = ((SVNLocalCompareInput.SVNLocalResourceNode)left).getLocalResource();
            }
            
            if (local == null || right == null) {
                return false;
            }
            
            try {
                if (!local.isManaged()) {
                    return false;
                }
                return !(local.isDirty());
            } catch (IVCException e) {
            }
            
            return super.contentsEqual(left, right);
        }
    }
    
	/**
	 * Node representing a local SVN file.  We can query the status of the resource to determine if
     * it has changed.  It is also used to write the contents back to the file when setContent is called.
	 */
	class SVNLocalResourceNode extends ResourceNode {
		
		private final ISVNLocalResource svnResource;
		private ArrayList fChildren = null;
        
        public SVNLocalResourceNode(ISVNLocalResource svnResource) {
			super(svnResource.getIResource());
            this.svnResource = svnResource;
		}
		protected InputStream createStream() throws CoreException {
			return ((IFile)getResource()).getContents();
		}
		
        public ISVNLocalResource getLocalResource() {
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
				System.out.println("errortrsdf");
			} catch (InterruptedException e) {
				// Ignore
			}
			fireContentChanged();
		}
		
        public Object[] getChildren() {
            if (fChildren == null) {
                fChildren= new ArrayList();
                
//                if (svnResource instanceof ISVNLocalFolder) {
//                    try {
//                        ISVNLocalResource[] members = (ISVNLocalResource[])((ISVNLocalFolder)svnResource).members(null, ISVNFolder.ALL_EXISTING_UNIGNORED_MEMBERS);
//                        for (int i= 0; i < members.length; i++) {
//                            IStructureComparator child= createChild(members[i]);
//                            if (child != null)
//                                fChildren.add(child);
//                        }
//                    } catch (CoreException ex) {
//                        // NeedWork
//                    }
//                }
            }
            return fChildren.toArray();
        }
        
		/* (non-Javadoc)
		 * @see org.eclipse.compare.ResourceNode#createChild(org.eclipse.core.resources.IResource)
		 */
		protected IStructureComparator createChild(ISVNLocalResource child) {
			return new SVNLocalResourceNode(child);
		}
		
		public ITypedElement replace(ITypedElement child, ITypedElement other) {
			return null;
		}
	}
	
	/**
	 * @throws IVCException
	 * creates a SVNLocalCompareInput, allows setting whether the current local resource is read only or not.
	 */
	public SVNLocalCompareInput(ISVNLocalResource resource, SVNRevision revision, boolean readOnly) throws IVCException {
		super(new CompareConfiguration());
        this.remoteRevision = revision;
        this.readOnly = readOnly;
        this.resource = resource;
		this.remoteResource=null;//new SVNLocalResourceNode(ResourcesPlugin.getWorkspace().getRoot());
        // SVNRevision can be any valid revision : BASE, HEAD, number ...
		//TODO
        //this.remoteResource = resource.getRemoteResource(revision);
        
        // remoteResouce can be null if there is no corresponding remote resource
        // (for example no base because resource has just been added)
	}

	/**
	 * Constructor which allows 
	 * @throws IVCException
	 * creates a SVNLocalCompareInput, defaultin to read/write.  
	 */
	public SVNLocalCompareInput(ISVNLocalResource resource, SVNRevision revision) throws IVCException {
		this(resource, revision, false);
	}

	/**
	 * @throws IVCException
	 * creates a SVNCompareRevisionsInput  
	 */
	public SVNLocalCompareInput(ISVNLocalResource resource, ISVNLocalResource remoteResource) throws IVCException {
		super(new CompareConfiguration());
		this.resource = resource;
		this.remoteResource = remoteResource;
        this.remoteRevision = remoteResource.getRevision();
	}
	
	
	/**
	 * initialize the labels : the title, the lft label and the right one
	 */
	private void initLabels() {
		CompareConfiguration cc = getCompareConfiguration();
		String resourceName = resource.getName();	
		setTitle("SVNCompareRevisionsInput.compareResourceAndVersions"); //$NON-NLS-1$
		cc.setLeftEditable(! readOnly);
		cc.setRightEditable(false);
		
		String leftLabel = "SVNCompareRevisionsInput.workspace"; //$NON-NLS-1$
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
		SVNLocalResourceNode right = new SVNLocalResourceNode(remoteResource);
//		if(left.getType()==ITypedElement.FOLDER_TYPE){
//			right.setLocalResource((SVNLocalResourceNode) left);
//		}


//		String localCharset = Utilities.getCharset(resource.getIResource());
//		try {
//			right.setCharset(localCharset);
//		} catch (CoreException e) {
//			e.printStackTrace();
//		}

        if (SVNRevision.BASE.equals(remoteRevision)) {
            return new StatusAwareDifferencer().findDifferences(false, monitor,null,null,left,right);
        }
        return new SummaryDifferencer().findDifferences(false, monitor,null,null,left,right);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {
		try {
			saveChanges(monitor);
		} catch (CoreException e) {
			//Utils.handle(e);
			System.out.println("erorewoasdf");
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
