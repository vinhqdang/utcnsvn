package ivc.compare;

import ivc.plugin.ImageDescriptorManager;
import ivc.util.StringUtils;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.IContentChangeListener;
import org.eclipse.compare.IContentChangeNotifier;
import org.eclipse.compare.IEditableContent;
import org.eclipse.compare.IEncodedStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.contentmergeviewer.TokenComparator;
import org.eclipse.compare.rangedifferencer.RangeDifference;
import org.eclipse.compare.rangedifferencer.RangeDifferencer;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Image;

public class IVCCompareEditorInput extends CompareEditorInput {
	ResourceEditionNode left;
	ResourceEditionNode right;
	Vector<IContentChangeListener> listenerList = new Vector<IContentChangeListener>();

	public IVCCompareEditorInput(CompareConfiguration config) {
		super(config);

	}

	// @Override
	// protected void fireContentChanged() {
	// this.setDirty(true);
	// IContentChangeListener[] listeners = this.listenerList
	// .toArray(new IContentChangeListener[0]);
	// for (int i = 0; i < listeners.length; i++) {
	// listeners[i].contentChanged(this);
	// }
	// }

	@Override
	protected Object prepareInput(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		left = new ResourceEditionNode((IFile) (ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new Path("asd\\a.txt"))));

		right = new ResourceEditionNode((IFile) (ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new Path("asd\\b.txt"))));

		Differencer diferencer = new Differencer();
		DiffNode result = (DiffNode) diferencer.findDifferences(false, monitor, null, null, left,
				right);
		try{
			String s1 = StringUtils.readFromInputStream(left.getContents());
		String s2 = StringUtils.readFromInputStream(right.getContents());

		TokenComparator comp1 = new TokenComparator(s1);
		TokenComparator comp2 = new TokenComparator(s2);

		RangeDifference[] diff = RangeDifferencer.findDifferences(comp1, comp2);
		System.out.println(diff.length + RangeDifference.CHANGE + " ____________________________");
		for (int i = 0; i < diff.length; i++) {
			System.out.println("left: "+s1.substring(comp1.getTokenStart(diff[i].leftStart()), comp1.getTokenStart(diff[i].leftStart()+diff[i].leftLength())));
			System.out.println("right: "+s2.substring(comp2.getTokenStart(diff[i].rightStart()), comp2.getTokenStart(diff[i].rightStart()+diff[i].rightLength())));
		}
	} catch (Exception e) {
		System.out.println("coreException wtf???");
		e.printStackTrace();
	}
		return result;
	}
}

class ResourceElement implements ITypedElement, IEncodedStreamContentAccessor,
		IContentChangeNotifier, IEditableContent {
	protected Vector<IContentChangeListener> listenerList;
	protected boolean dirty;
	protected String charset;

	protected IFile resource;
	protected IFile localAlias;
	protected boolean editable;

	public ResourceElement(IFile resource, IFile alias, boolean showContent) {
		this.resource = resource;
		this.localAlias = alias;
		this.editable = false;
		this.listenerList = new Vector<IContentChangeListener>();

	}

	public String getCharset() {
		return this.charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void addContentChangeListener(IContentChangeListener listener) {
		this.listenerList.add(listener);
	}

	public void removeContentChangeListener(IContentChangeListener listener) {
		this.listenerList.remove(listener);
	}

	public boolean isDirty() {
		return this.dirty;
	}

	public boolean isEditable() {
		return true;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public ITypedElement replace(ITypedElement dest, ITypedElement src) {
		return dest;
	}

	public void commit(IProgressMonitor pm) throws CoreException {
		if (this.isDirty()) {
			IFile file = (IFile) localAlias;

			this.dirty = false;
		}
	}

	public void setContent(byte[] newContent) {

	}

	@Override
	public Image getImage() {

		return ImageDescriptorManager.getImageDescriptor(ImageDescriptorManager.SHARE_WIZARD)
				.createImage();
	}

	@Override
	public String getName() {

		return "resource";
	}

	@Override
	public String getType() {

		return IFile.class.toString();
	}

	@Override
	public InputStream getContents() throws CoreException {

		return resource.getContents();
	}

}
