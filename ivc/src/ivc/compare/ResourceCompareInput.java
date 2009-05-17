package ivc.compare;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.IContentChangeListener;
import org.eclipse.compare.IContentChangeNotifier;
import org.eclipse.compare.IEditableContent;
import org.eclipse.compare.IEncodedStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.compare.structuremergeviewer.DiffTreeViewer;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.compare.structuremergeviewer.IDiffContainer;
import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.IWorkbenchPage;

/**
 * Compare editor input for the versioned trees
 * 
 * @author Alexander Gurov
 */
public abstract class ResourceCompareInput extends CompareEditorInput {

	public ResourceCompareInput(CompareConfiguration configuration) {
		super(configuration);
		// TODO Auto-generated constructor stub
	}
//	protected ResourceCompareViewer viewer;
//	protected BaseCompareNode root;
	
//	protected IRepositoryResource rootLeft;
//	protected IRepositoryResource rootAncestor;
//	protected IRepositoryResource rootRight;
//	
//	protected String forceId;
//
//	public static void openCompareEditor(ResourceCompareInput compare, boolean forceReuse) {
//		IWorkbenchPage page = UIMonitorUtility.getActivePage();
//		IEditorReference []editorRefs = page.getEditorReferences();	
//		for (int i = 0; i < editorRefs.length; i++) {
//			IEditorPart part = editorRefs[i].getEditor(false);
//			if (part instanceof IReusableEditor && !part.isDirty() && compare.getClass().equals(part.getEditorInput().getClass())) {
//				ResourceCompareInput existing = (ResourceCompareInput)part.getEditorInput();
//				if (compare.equals(existing)) {
//					page.activate(part);
//					return;
//				}
//				else if (forceReuse || existing.getForceId() != null && existing.getForceId().equals(compare.getForceId())) {
//					CompareUI.reuseCompareEditor(compare, (IReusableEditor)part);
//					page.activate(part);
//					return;
//				}
//			}
//		}
//		CompareUI.openCompareEditor(compare);
//	}
//	
//	public ResourceCompareInput(CompareConfiguration configuration) {
//		super(configuration);
//	}
//	
//	public void setForceId(String forceId) {
//		this.forceId = forceId;
//	}
//
//	public String getForceId() {
//		return this.forceId;
//	}
//
//	public void initialize(IProgressMonitor monitor) throws Exception {
//		this.refreshTitles();
//	}
//	
//	public final Viewer createDiffViewer(Composite parent) {
//		this.viewer = this.createDiffViewerImpl(parent, this.getCompareConfiguration());
//		
//		MenuManager menuMgr = new MenuManager();
//		Menu menu = menuMgr.createContextMenu(this.viewer.getControl());	
//		menuMgr.addMenuListener(new IMenuListener() {
//			public void menuAboutToShow(IMenuManager manager) {
//				manager.removeAll();
//				TreeSelection selection = (TreeSelection)ResourceCompareInput.this.viewer.getSelection();
//				if (selection.size() != 0) {
//					ResourceCompareInput.this.fillMenu(manager, selection);
//					manager.add(new Separator());
//				}
//				manager.add(new Action(SVNUIMessages.SynchronizeActionGroup_ExpandAll) {
//					public void run() {
//						ResourceCompareInput.this.viewer.expandAll();
//					}
//				});
//			}
//		});
//		this.viewer.getControl().setMenu(menu);
//		
//		return this.viewer;
//	}
//	
//	protected abstract void fillMenu(IMenuManager manager, TreeSelection selection);
//	
//	public boolean equals(Object obj) {
//		if (obj != null && obj.getClass().equals(this.getClass())) {
//			ResourceCompareInput other = (ResourceCompareInput)obj;
//			return this.rootLeft.equals(other.rootLeft) && this.rootRight.equals(other.rootRight) && (this.rootAncestor == other.rootAncestor || this.rootAncestor != null && this.rootAncestor.equals(other.rootAncestor));
//		}
//		return false;
//	}
//	
//	protected ResourceCompareViewer createDiffViewerImpl(Composite parent, CompareConfiguration config) {
//		return new ResourceCompareViewer(parent, config);
//	}
//	
//	protected Object prepareInput(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
//		if (this.root != null) {
//			ResourceElement left = (ResourceElement)this.root.getLeft();
//			ResourceElement ancestor = (ResourceElement)this.root.getAncestor();
//			ResourceElement right = (ResourceElement)this.root.getRight();
//			
//			//TODO additionally decorate resources in order to show property changes
//			if ((left.getType() == ITypedElement.FOLDER_TYPE || ancestor != null && ancestor.getType() == ITypedElement.FOLDER_TYPE || right.getType() == ITypedElement.FOLDER_TYPE) && 
//				(this.root.getKind() & Differencer.CHANGE_TYPE_MASK) != 0) {
//				this.root = (BaseCompareNode)this.root.getParent();
//			}
//			ProgressMonitorUtility.doTaskExternal(this.root.getFetcher(), monitor);
//		}
//		monitor.done();
//		return this.root;
//	}
//
//	protected void findRootNode(Map<IPath, IDiffElement> path2node, IRepositoryResource resource, IProgressMonitor monitor) {
//		this.root = (BaseCompareNode)path2node.get(new Path(resource.getUrl()));
//	}
//	
//	protected void refreshTitles() throws Exception {
//		if (this.root == null) {
//			return;
//		}
//		CompareConfiguration cc = this.getCompareConfiguration();
//		
//		cc.setLeftLabel(this.getLeftLabel());
//		cc.setLeftImage(this.getLeftImage());
//		
//		cc.setRightLabel(this.getRightLabel());
//		cc.setRightImage(this.getRightImage());
//		
//		ResourceElement left = this.getLeftResourceElement();
//		String leftRevisionPart = this.getRevisionPart(left);
//		String leftResourceName = left.getName();
//		ResourceElement right = this.getRightResourceElement();
//		String rightRevisionPart = this.getRevisionPart(right);
//		String rightResourceName = right.getName();
//		
//		if (this.isThreeWay()) {
//			cc.setAncestorLabel(this.getAncestorLabel());
//			cc.setAncestorImage(this.getAncestorImage());
//			
//			ResourceElement ancestor = this.getAncestorResourceElement();
//			String ancestorRevisionPart = this.getRevisionPart(ancestor);
//			String ancestorResourceName = ancestor.getName();
//			
//			String leftPart = leftResourceName + " [" + leftRevisionPart; //$NON-NLS-1$
//			String ancestorPart = " "; //$NON-NLS-1$
//			String rightPart = " "; //$NON-NLS-1$
//			boolean leftEquals = leftResourceName.equals(ancestorResourceName);
//			boolean rightEquals = rightResourceName.equals(ancestorResourceName);
//			if (leftEquals) {
//				leftPart += " "; //$NON-NLS-1$
//				if (rightEquals) {
//					ancestorPart += ancestorRevisionPart + " "; //$NON-NLS-1$
//					rightPart += rightRevisionPart + "]"; //$NON-NLS-1$
//				}
//				else {
//					ancestorPart += ancestorRevisionPart + "] "; //$NON-NLS-1$
//					rightPart += rightResourceName + " [" + rightRevisionPart + "]"; //$NON-NLS-1$ //$NON-NLS-2$
//				}
//			}
//			else if (rightEquals) {
//				leftPart += "] "; //$NON-NLS-1$
//				ancestorPart += ancestorResourceName + " [" + ancestorRevisionPart + " "; //$NON-NLS-1$ //$NON-NLS-2$
//				rightPart += rightRevisionPart + "]"; //$NON-NLS-1$
//			}
//			else {
//				leftPart += "] "; //$NON-NLS-1$
//				ancestorPart += ancestorResourceName + " [" + ancestorRevisionPart + "] "; //$NON-NLS-1$ //$NON-NLS-2$
//				rightPart += rightResourceName + " [" + rightRevisionPart + "]"; //$NON-NLS-1$ //$NON-NLS-2$
//			}
//
//			this.setTitle(SVNUIMessages.format(SVNUIMessages.ResourceCompareInput_Title3, new Object[] {leftPart, ancestorPart, rightPart}));
//		} 
//		else {
//			String leftPart = leftResourceName + " [" + leftRevisionPart; //$NON-NLS-1$
//			String rightPart = " "; //$NON-NLS-1$
//			if (leftResourceName.equals(rightResourceName)){
//				leftPart += " "; //$NON-NLS-1$
//				rightPart += rightRevisionPart + "]"; //$NON-NLS-1$
//			}
//			else {
//				leftPart += "] "; //$NON-NLS-1$
//				rightPart += rightResourceName + " [" + rightRevisionPart + "]"; //$NON-NLS-1$ //$NON-NLS-2$
//			}
//			
//			this.setTitle(SVNUIMessages.format(SVNUIMessages.ResourceCompareInput_Title2, new Object[] {leftPart, rightPart}));
//		}
//	}
//	
//	protected String getAncestorLabel() throws Exception {
//		return this.getLabel(this.getAncestorResourceElement());
//	}
//
//	protected Image getAncestorImage() throws Exception {
//		return CompareUI.getImage(RepositoryFolder.wrapChild(null, this.getAncestorResourceElement().getRepositoryResource()));
//	}
//
//	protected String getLeftLabel() throws Exception {
//		return this.getLabel(this.getLeftResourceElement());
//	}
//	
//	protected Image getLeftImage() throws Exception {
//		return CompareUI.getImage(RepositoryFolder.wrapChild(null, this.getLeftResourceElement().getRepositoryResource()));
//	}
//	
//	protected String getRightLabel() throws Exception {
//		return this.getLabel(this.getRightResourceElement());
//	}
//	
//	protected Image getRightImage() throws Exception {
//		return CompareUI.getImage(RepositoryFolder.wrapChild(null, this.getRightResourceElement().getRepositoryResource()));
//	}
//	
//	protected String getLabel(ResourceElement element) throws Exception {
//		return element.getRepositoryResource().getUrl() + " [" + this.getRevisionPart(element) + "]"; //$NON-NLS-1$ //$NON-NLS-2$
//	}
//	
//	protected String getRevisionPart(ResourceElement element) throws Exception {
//		IRepositoryResource resource = element.getRepositoryResource();
//		SVNRevision selected = resource.getSelectedRevision();
//		if (selected == SVNRevision.INVALID_REVISION) {
//			return SVNUIMessages.ResourceCompareInput_ResourceIsNotAvailable;
//		}
//		return SVNUIMessages.format(SVNUIMessages.ResourceCompareInput_RevisionSign, new String[] {String.valueOf(resource.getRevision())});
//	}
//	
//	protected ResourceElement getLeftResourceElement() {
//		DiffNode node = this.getSelectedNode();
//		if (node != null) {
//			return (ResourceElement)node.getLeft();
//		}
//		return (ResourceElement)this.root.getLeft();
//	}
//	
//	protected ResourceElement getRightResourceElement() {
//		DiffNode node = this.getSelectedNode();
//		if (node != null) {
//			return (ResourceElement)node.getRight();
//		}
//		return (ResourceElement)this.root.getRight();
//	}
//	
//	protected ResourceElement getAncestorResourceElement() {
//		DiffNode node = this.getSelectedNode();
//		if (node != null) {
//			return (ResourceElement)node.getAncestor();
//		}
//		return (ResourceElement)this.root.getAncestor();
//	}
//	
//	protected DiffNode getSelectedNode() {
//		if (this.viewer != null) {
//			IStructuredSelection selection = (IStructuredSelection)this.viewer.getSelection();
//			if (selection != null && !selection.isEmpty() && selection.getFirstElement() instanceof DiffNode) {
//				return (DiffNode)selection.getFirstElement();
//			}
//		}
//		return null;
//	}
//	
//	protected IDiffContainer getParentCompareNode(IRepositoryResource current, Map<IPath, IDiffContainer> path2node) throws Exception {
//		IRepositoryResource parent = current.getParent();
//		if (parent == null) {
//			return null;
//		}
//		
//		Path parentUrl = new Path(parent.getUrl());
//		IDiffContainer node = path2node.get(parentUrl);
//		if (node == null) {
//			path2node.put(parentUrl, node = this.makeStubNode(this.getParentCompareNode(parent, path2node), parent));
//		}
//		return node;
//	}
//	
//	protected static int getDiffKind(int textStatus, int propStatus) {
//		if (textStatus == org.eclipse.team.svn.core.connector.SVNEntryStatus.Kind.ADDED ||
//			textStatus == org.eclipse.team.svn.core.connector.SVNEntryStatus.Kind.UNVERSIONED) {
//			return Differencer.ADDITION;
//		}
//		if (textStatus == org.eclipse.team.svn.core.connector.SVNEntryStatus.Kind.DELETED) {
//			return Differencer.DELETION;
//		}
//		if (textStatus == org.eclipse.team.svn.core.connector.SVNEntryStatus.Kind.REPLACED) {
//			return Differencer.CHANGE;
//		}
//		if (textStatus == org.eclipse.team.svn.core.connector.SVNEntryStatus.Kind.MODIFIED ||
//			propStatus == org.eclipse.team.svn.core.connector.SVNEntryStatus.Kind.MODIFIED) {
//			return Differencer.CHANGE;
//		}
//		return Differencer.NO_CHANGE;
//	}
//	
//	protected int getNodeKind(SVNDiffStatus st) {
//		return SVNUtility.getNodeKind(st.pathPrev, st.nodeKind, false);
//	}
//	
//	protected IRepositoryResource createResourceFor(IRepositoryLocation location, int kind, String url) {
//		IRepositoryResource retVal = null;
//		if (kind == SVNEntry.Kind.FILE) {
//			retVal = location.asRepositoryFile(url, false);
//		}
//		else if (kind == SVNEntry.Kind.DIR) {
//			retVal = location.asRepositoryContainer(url, false);
//		}
//		if (retVal == null) {
//			throw new RuntimeException(SVNUIMessages.getErrorString("Error_CompareUnknownNodeKind")); //$NON-NLS-1$
//		}
//		return retVal;
//	}
//	
//	protected abstract boolean isThreeWay();
//	protected abstract IDiffContainer makeStubNode(IDiffContainer parent, IRepositoryResource node);
//	
//	public class ResourceElement implements ITypedElement, IEncodedStreamContentAccessor, IContentChangeNotifier, IEditableContent {
//		protected Vector<IContentChangeListener> listenerList;
//		protected boolean dirty;
//		protected String charset;
//		
//		protected IRepositoryResource resource;
//		protected AbstractGetFileContentOperation op;
//		protected ILocalResource localAlias;
//		protected boolean editable;
//		
//		public ResourceElement(IRepositoryResource resource, ILocalResource alias, boolean showContent) {
//			this.resource = resource;
//			this.localAlias = alias;
//			this.editable = false;
//			this.listenerList = new Vector<IContentChangeListener>();
//			if (!showContent) {
//				this.resource.setSelectedRevision(SVNRevision.INVALID_REVISION);
//			}
//		}
//		
//		public String getCharset() {
//			return this.charset;
//		}
//		
//		public void setCharset(String charset){
//			this.charset = charset;
//		}
//		
//		public void addContentChangeListener(IContentChangeListener listener) {
//			this.listenerList.add(listener);
//		}
//		
//		public void removeContentChangeListener(IContentChangeListener listener) {
//			this.listenerList.remove(listener);
//		}
//		
//		public boolean isDirty() {
//			return this.dirty;
//		}
//		
//		public boolean isEditable() {
//			return this.editable && this.localAlias instanceof ILocalFile;
//		}
//
//		public void setEditable(boolean editable) {
//			this.editable = editable;
//		}
//
//		public ITypedElement replace(ITypedElement dest, ITypedElement src) {
//			return dest;
//		}
//		
//		public void commit(IProgressMonitor pm) throws CoreException {
//			if (this.isDirty()) {
//				IFile file = (IFile)this.localAlias.getResource();
//				file.refreshLocal(IResource.DEPTH_ZERO, pm);
//				this.dirty = false;
//			}
//		}
//
//		public void setContent(byte[] newContent) {
//			if (this.isEditable()) {
//				if (this.op != null) {
//					this.op.setContent(newContent);
//					this.fireContentChanged();
//				}
//			}
//		}
//		
//		public IRepositoryResource getRepositoryResource() {
//			return this.resource;
//		}
//		
//		public ILocalResource getLocalResource() {
//			return this.localAlias;
//		}
//		
//		public String getName() {
//			return this.resource.getName();
//		}
//	
//		public Image getImage() {
//			return CompareUI.getImage(RepositoryFolder.wrapChild(null, this.resource));
//		}
//	
//		public String getType() {
//			if (this.resource instanceof IRepositoryContainer) {
//				return ITypedElement.FOLDER_TYPE;
//			}
//			String fileName = this.resource.getName();
//			int dotIdx = fileName.lastIndexOf('.');
//			return dotIdx == -1 ? ITypedElement.UNKNOWN_TYPE : fileName.substring(dotIdx + 1);
//		}
//
//		public AbstractGetFileContentOperation getFetcher() {
//			if (this.op != null && this.op.getExecutionState() == IActionOperation.OK) {
//				return null;
//			}
//			if (this.resource.getSelectedRevision() != SVNRevision.INVALID_REVISION && this.resource instanceof IRepositoryFile) {
//				int revisionKind = this.resource.getSelectedRevision().getKind();
//				return this.op = revisionKind == SVNRevision.Kind.WORKING || revisionKind == SVNRevision.Kind.BASE ? 
//					(AbstractGetFileContentOperation)new GetLocalFileContentOperation(this.localAlias.getResource(), revisionKind) : 
//					new GetFileContentOperation(this.resource);
//			}
//			return this.op = null;
//		}
//	
//		public InputStream getContents() throws CoreException {
//			return this.op == null || this.op.getExecutionState() != IActionOperation.OK ? null : this.op.getContent();
//		}
//	
//		protected void fireContentChanged() {
//			this.dirty = true;
//			IContentChangeListener []listeners = this.listenerList.toArray(new IContentChangeListener[0]);
//			for (int i= 0; i < listeners.length; i++) {
//				listeners[i].contentChanged(this);
//			}
//		}
//
//	}
//
//	protected class ResourceCompareViewer extends DiffTreeViewer {
//		public ResourceCompareViewer(Composite parent, CompareConfiguration configuration) {
//			super(parent, configuration);
//		}
//		
//		public void setComparator(ViewerComparator comparator) {
//			super.setComparator(new ViewerComparator() {
//				public int category(Object element) {
//					return ((IDiffElement)element).getType() == ITypedElement.FOLDER_TYPE ? 0 : 1;
//				}
//			});
//		}
//		
//		protected void handleOpen(final SelectionEvent event) {
//			final BaseCompareNode node = (BaseCompareNode)((TreeItem)event.item).getData();
//			CompositeOperation fetchContent = node.getFetcher();
//			fetchContent.add(new AbstractActionOperation("Operation_FetchContent") { //$NON-NLS-1$
//				protected void runImpl(IProgressMonitor monitor) throws Exception {
//					final Throwable []t = new Throwable[1];
//					UIMonitorUtility.getDisplay().syncExec(new Runnable() {
//						public void run() {
//							try {
//								ResourceCompareInput.this.refreshTitles();
//								ResourceCompareViewer.super.handleOpen(event);
//							} 
//							catch (Exception e) {
//								t[0] = e;
//							}
//						}
//					});
//					if (t[0] != null ){
//						this.reportError(t[0]);
//					}
//				}
//			});
//			UIMonitorUtility.doTaskNowDefault(fetchContent, true);
//		}
//		
//		protected class LabelProviderWrapper implements ILabelProvider {
//			protected Map<Object, Image> images;
//			protected ILabelProvider baseProvider;
//			
//			public LabelProviderWrapper(ILabelProvider baseProvider) {
//				this.images = new HashMap<Object, Image>();
//				this.baseProvider = baseProvider;
//			}
//			
//			public void addListener(ILabelProviderListener listener) {
//				this.baseProvider.addListener(listener);
//			}
//			
//			public void removeListener(ILabelProviderListener listener) {
//				this.baseProvider.removeListener(listener);
//			}
//			
//			public boolean isLabelProperty(Object element, String property) {
//				return this.baseProvider.isLabelProperty(element, property);
//			}
//			
//			public String getText(Object element) {
//				return this.baseProvider.getText(element);
//			}
//			
//			public Image getImage(Object element) {
//				return this.baseProvider.getImage(element);
//			}
//			
//			public void dispose() {
//				for (Iterator<Image> it = this.images.values().iterator(); it.hasNext(); ) {
//					it.next().dispose();
//				}
//				this.baseProvider.dispose();
//			}
//			
//		}
//	}
//	
//	protected class BaseCompareNode extends DiffNode {
//		public BaseCompareNode(IDiffContainer parent, int kind) {
//			super(parent, kind);
//		}
//		
//		protected String detectCharset(InputStream stream) throws Exception {
//			try {
//				IContentDescription description = Platform.getContentTypeManager().getDescriptionFor(stream, this.getName(), IContentDescription.ALL);
//				return description == null ? null : description.getCharset();
//			} 
//			finally {
//				try {stream.close();} catch (Exception ex) {}
//			}
//		}
//		
//		public CompositeOperation getFetcher() {
//			final ResourceElement left = (ResourceElement)this.getLeft();
//			final ResourceElement ancestor = (ResourceElement)this.getAncestor();
//			final ResourceElement right = (ResourceElement)this.getRight();
//			CompositeOperation op = new CompositeOperation(SVNUIMessages.ResourceCompareInput_Fetch);
//			
//			if (left != null && left.getType() != ITypedElement.FOLDER_TYPE) {
//				final AbstractGetFileContentOperation fetchOp = left.getFetcher();
//				if (fetchOp != null) {
//					op.add(fetchOp);
//					op.add(new AbstractActionOperation("Operation_DetectCharset") { //$NON-NLS-1$
//		                protected void runImpl(IProgressMonitor monitor) throws Exception {
//		                	left.setCharset(BaseCompareNode.this.detectCharset(fetchOp.getContent()));
//		                }
//			        }, new IActionOperation[] {fetchOp});
//				}
//			}
//			if (ancestor != null && ancestor.getType() != ITypedElement.FOLDER_TYPE) {
//				final AbstractGetFileContentOperation fetchOp = ancestor.getFetcher();
//				if (fetchOp != null) {
//					op.add(fetchOp);
//					op.add(new AbstractActionOperation("Operation_DetectCharset") { //$NON-NLS-1$
//		                protected void runImpl(IProgressMonitor monitor) throws Exception {
//		                	ancestor.setCharset(BaseCompareNode.this.detectCharset(fetchOp.getContent()));
//		                }
//			        }, new IActionOperation[] {fetchOp});
//				}
//			}
//			if (right != null && right.getType() != ITypedElement.FOLDER_TYPE) {
//				final AbstractGetFileContentOperation fetchOp = right.getFetcher();
//				if (fetchOp != null) {
//					op.add(fetchOp);
//					op.add(new AbstractActionOperation("Operation_DetectCharset") { //$NON-NLS-1$
//		                protected void runImpl(IProgressMonitor monitor) throws Exception {
//		                	right.setCharset(BaseCompareNode.this.detectCharset(fetchOp.getContent()));
//		                }
//			        }, new IActionOperation[] {fetchOp});
//				}
//			}
//			return op;
//		}
//	}
	
}
