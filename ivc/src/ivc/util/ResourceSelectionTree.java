package ivc.util;

import ivc.fireworks.decorators.Decorator;

import ivc.managers.ImageDescriptorManager;
import ivc.managers.ProjectsManager;
import ivc.plugin.IVCPlugin;
import ivc.repository.Status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * 
 * @author alexm
 * 
 *         This class is used to provide the interface to check / uncheck resources before
 *         committing them
 */
@SuppressWarnings("unchecked")
public class ResourceSelectionTree extends Composite {
	private Tree tree;
	private int mode;
	private IResource[] resources;

	private ArrayList resourceList;
	private Set unversionedResourceList;
	private IContainer[] compressedFolders;
	private IContainer[] folders;
	private ArrayList folderList;
	private IContainer[] rootFolders;
	private ArrayList compressedFolderList;
	private TreeViewer treeViewer;
	private LabelProvider labelProvider;
	private String label;
	private Action treeAction;
	private Action flatAction;
	private Action compressedAction;
	private IDialogSettings settings;
	private Map<IResource, Status> statusMap;
	private ResourceComparator comparator = new ResourceComparator();
	private boolean checkbox;
	private IToolbarControlCreator toolbarControlCreator;
	private IRemoveFromViewValidator removeFromViewValidator;

	private boolean showRemoveFromViewAction = true;

	private boolean resourceRemoved = false;
	private boolean includeUnversioned = true;
	private ResourceSelectionContentProvider resourceSelectionContentProvider = new ResourceSelectionContentProvider();

	public final static String MODE_SETTING = "ResourceSelectionTree.mode";
	public final static int MODE_COMPRESSED_FOLDERS = 0;
	public final static int MODE_FLAT = 1;
	public final static int MODE_TREE = 2;

	/**
	 * Creates a new object with the given parameters
	 * 
	 * @param parent
	 *            the parent of the component
	 * @param style
	 *            the style
	 * @param label
	 *            the label
	 * @param resources
	 *            the list of resources
	 * @param statusMap
	 *            a map with the statuses of the resources
	 * @param labelProvider
	 *            the label provider
	 * @param checkbox
	 *            display checkboxes
	 * @param toolbarControlCreator
	 *            the toolbar control creator
	 */
	public ResourceSelectionTree(Composite parent, int style, String label,
			IResource[] resources, Map<IResource, Status> statusMap,
			LabelProvider labelProvider, boolean checkbox,
			IToolbarControlCreator toolbarControlCreator) {
		super(parent, style);
		this.label = label;
		this.resources = resources;
		this.statusMap = statusMap;
		this.labelProvider = labelProvider;
		this.checkbox = checkbox;
		this.toolbarControlCreator = toolbarControlCreator;
		this.settings = IVCPlugin.getDefault().getDialogSettings();
		if (resources != null) {
			Arrays.sort(resources, comparator);
			resourceList = new ArrayList();
			for (int i = 0; i < resources.length; i++) {
				IResource resource = resources[i];
				resourceList.add(resource);
			}
			unversionedResourceList = new HashSet();
			try {
				for (int i = 0; i < resources.length; i++) {
					IResource resource = resources[i];
					if (!ProjectsManager.instance().isManaged(resource)) {
						unversionedResourceList.add(resource);
					}
				}
			} catch (Exception e) {
				IVCPlugin.openError(getShell(), "Commit dialog error", e.getMessage(), e,
						0);
			}
		}
		createControls();
	}

	/**
	 * returns the TreeViewer
	 * 
	 * @return the treeViewer
	 */
	public TreeViewer getTreeViewer() {
		return treeViewer;
	}

	/**
	 * Returns the array of the selected resources
	 * 
	 * @return the array with the selected resources
	 */
	public IResource[] getSelectedResources() {
		if (!checkbox)
			return resources;
		ArrayList selected = new ArrayList();
		Object[] checkedResources = ((CheckboxTreeViewer) treeViewer)
				.getCheckedElements();
		for (int i = 0; i < checkedResources.length; i++) {
			if (resourceList.contains(checkedResources[i]))
				selected.add(checkedResources[i]);
		}
		IResource[] selectedResources = new IResource[selected.size()];
		selected.toArray(selectedResources);
		return selectedResources;
	}

	private void createControls() {
		setLayout(new GridLayout(2, false));
		setLayoutData(new GridData(GridData.FILL_BOTH));

		ViewForm viewerPane = new ViewForm(this, SWT.BORDER | SWT.FLAT);
		viewerPane.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		CLabel toolbarLabel = new CLabel(viewerPane, SWT.NONE) {
			public Point computeSize(int wHint, int hHint, boolean changed) {
				return super.computeSize(wHint, Math.max(24, hHint), changed);
			}
		};

		if (label != null) {
			toolbarLabel.setText(label);
		}
		viewerPane.setTopLeft(toolbarLabel);

		int buttonGroupColumns = 1;
		if (toolbarControlCreator != null) {
			buttonGroupColumns = buttonGroupColumns
					+ toolbarControlCreator.getControlCount();
		}

		ToolBar toolbar = new ToolBar(viewerPane, SWT.FLAT);

		viewerPane.setTopCenter(toolbar);

		ToolBarManager toolbarManager = new ToolBarManager(toolbar);

		if (toolbarControlCreator != null) {
			toolbarControlCreator.createToolbarControls(toolbarManager);
			toolbarManager.add(new Separator());
		}

		flatAction = new Action("Flat list", Action.AS_CHECK_BOX) {
			public void run() {
				mode = MODE_FLAT;
				settings.put(MODE_SETTING, MODE_FLAT);
				treeAction.setChecked(false);
				compressedAction.setChecked(false);
				refresh();
			}
		};
		flatAction.setImageDescriptor(ImageDescriptorManager
				.getImageDescriptor(ImageDescriptorManager.TREE_FLAT));
		toolbarManager.add(flatAction);

		treeAction = new Action("Tree", Action.AS_CHECK_BOX) {
			public void run() {
				mode = MODE_TREE;
				settings.put(MODE_SETTING, MODE_TREE);
				flatAction.setChecked(false);
				compressedAction.setChecked(false);
				refresh();
			}
		};
		treeAction.setImageDescriptor(ImageDescriptorManager
				.getImageDescriptor(ImageDescriptorManager.TREE_AFFECTED_PATHS));
		toolbarManager.add(treeAction);

		compressedAction = new Action("Compressed", Action.AS_CHECK_BOX) {
			public void run() {
				mode = MODE_COMPRESSED_FOLDERS;
				settings.put(MODE_SETTING, MODE_COMPRESSED_FOLDERS);
				treeAction.setChecked(false);
				flatAction.setChecked(false);
				refresh();
			}
		};
		compressedAction
				.setImageDescriptor(ImageDescriptorManager
						.getImageDescriptor(ImageDescriptorManager.TREE_AFFECTED_PATHS_COMPRESSED));
		toolbarManager.add(compressedAction);

		toolbarManager.update(true);

		mode = MODE_COMPRESSED_FOLDERS;
		try {
			mode = settings.getInt(MODE_SETTING);
		} catch (Exception e) {
		}
		switch (mode) {
		case MODE_COMPRESSED_FOLDERS:
			compressedAction.setChecked(true);
			break;
		case MODE_FLAT:
			flatAction.setChecked(true);
			break;
		case MODE_TREE:
			treeAction.setChecked(true);
			break;
		default:
			break;
		}

		if (checkbox) {
			treeViewer = new CheckboxTreeViewer(viewerPane, SWT.MULTI);
		} else {
			treeViewer = new TreeViewer(viewerPane, SWT.MULTI);
		}
		tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		viewerPane.setContent(tree);

		if (labelProvider == null) {
			labelProvider = new ResourceSelectionLabelProvider();
		}

		treeViewer.setLabelProvider(labelProvider);
		treeViewer.setContentProvider(resourceSelectionContentProvider);
		treeViewer.setUseHashlookup(true);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.heightHint = 125;
		treeViewer.getControl().setLayoutData(gd);
		treeViewer.setInput(this);
		treeViewer.expandAll();

		if (checkbox) {
			setAllChecked(true);
			if (mode == MODE_TREE) {
				treeViewer.collapseAll();
			}
			((CheckboxTreeViewer) treeViewer)
					.addCheckStateListener(new ICheckStateListener() {
						public void checkStateChanged(CheckStateChangedEvent event) {
							handleCheckStateChange(event);
						}
					});
		}

		MenuManager menuMgr = new MenuManager();
		Menu menu = menuMgr.createContextMenu(treeViewer.getTree());
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager menuMgr) {
				fillTreeMenu(menuMgr);
			}
		});
		menuMgr.setRemoveAllWhenShown(true);
		treeViewer.getTree().setMenu(menu);
	}

	@SuppressWarnings("deprecation")
	void setAllChecked(boolean state) {
		((CheckboxTreeViewer) treeViewer).setAllChecked(state);
	}

	/**
	 * Fills the tree menu
	 * 
	 * @param menuMgr
	 *            the menu manager
	 */
	protected void fillTreeMenu(IMenuManager menuMgr) {
		if (checkbox) {
			Action selectAllAction = new Action("Select All") {
				public void run() {
					setAllChecked(true);
				}
			};
			menuMgr.add(selectAllAction);
			Action deselectAllAction = new Action("Deselect All") {
				public void run() {
					setAllChecked(false);
				}
			};
			menuMgr.add(deselectAllAction);
			if (showIncludeUnversionedButton() && includeUnversioned) {
				menuMgr.add(new Separator());
				Action selectUnversionedAction = new Action("Select Unversioned") {
					public void run() {
						checkUnversioned(tree.getItems(), true);
					}
				};
				menuMgr.add(selectUnversionedAction);
				Action deselectUnversionedAction = new Action("Deselect Unversioned") {
					public void run() {
						checkUnversioned(tree.getItems(), false);
					}
				};
				menuMgr.add(deselectUnversionedAction);
			}
		}
		menuMgr.add(new Separator());
		if (mode != MODE_FLAT) {
			Action expandAllAction = new Action("Expand All") {
				public void run() {
					treeViewer.expandAll();
				}
			};
			menuMgr.add(expandAllAction);
		}
		if (showRemoveFromViewAction && !checkbox && !treeViewer.getSelection().isEmpty()) {
			Action removeAction = new Action("Remove") {
				public void run() {
					removeFromView();
				}
			};
			menuMgr.add(removeAction);
		}
	}

	private void removeFromView() {
		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
		if (removeFromViewValidator != null) {
			if (!removeFromViewValidator.canRemove(resourceList, selection)) {
				if (removeFromViewValidator.getErrorMessage() != null) {
					MessageDialog.openError(getShell(), "ResourceSelectionTree.remove",
							removeFromViewValidator.getErrorMessage());
				}
				return;
			}
		}
		Iterator iter = selection.iterator();
		while (iter.hasNext()) {
			IResource resource = (IResource) iter.next();
			remove(resource);
			resourceRemoved = true;
		}
		resources = new IResource[resourceList.size()];
		resourceList.toArray(resources);
		compressedFolders = null;
		rootFolders = null;
		folders = null;
		refresh();
	}

	private void remove(IResource resource) {
		ArrayList removedResources = new ArrayList();
		Iterator iter = resourceList.iterator();
		while (iter.hasNext()) {
			IResource checkResource = (IResource) iter.next();
			if (checkResource.getFullPath().toString().equals(
					resource.getFullPath().toString())
					|| (mode != MODE_FLAT && isChild(checkResource, resource)))
				removedResources.add(checkResource);
		}
		iter = removedResources.iterator();
		while (iter.hasNext())
			resourceList.remove(iter.next());
	}

	public boolean showIncludeUnversionedButton() {
		return unversionedResourceList != null && unversionedResourceList.size() > 0;
	}

	public void removeUnversioned() {
		try {
			Iterator iter = unversionedResourceList.iterator();
			while (iter.hasNext())
				resourceList.remove(iter.next());

			resources = new IResource[resourceList.size()];
			resourceList.toArray(resources);
			compressedFolders = null;
			rootFolders = null;
			folders = null;
			refresh();
			includeUnversioned = false;
		} catch (Exception e) {
			IVCPlugin.openError(getShell(), "Commit error", e.getMessage(), e, 0);
		}
	}

	public void addUnversioned() {
		try {
			Iterator iter = unversionedResourceList.iterator();
			while (iter.hasNext())
				resourceList.add(iter.next());

			resources = new IResource[resourceList.size()];
			resourceList.toArray(resources);
			Arrays.sort(resources, comparator);
			compressedFolders = null;
			rootFolders = null;
			folders = null;
			refresh();
			checkUnversioned(tree.getItems(), true);
			includeUnversioned = true;
		} catch (Exception e) {
			IVCPlugin.openError(getShell(), "Commit error", e.getMessage(), e, 0);
		}
	}

	private void checkUnversioned(TreeItem[] items, boolean state) {
		for (int i = 0; i < items.length; i++) {
			if (unversionedResourceList.contains(items[i].getData())) {
				items[i].setChecked(state);
			}
			checkUnversioned(items[i].getItems(), state);
		}
	}

	private boolean isChild(IResource resource, IResource parent) {
		IContainer container = resource.getParent();
		while (container != null) {
			if (container.getFullPath().toString()
					.equals(parent.getFullPath().toString()))
				return true;
			container = container.getParent();
		}
		return false;
	}

	private void handleCheckStateChange(CheckStateChangedEvent event) {
		((CheckboxTreeViewer) treeViewer).setGrayed(event.getElement(), false);
		((CheckboxTreeViewer) treeViewer).setSubtreeChecked(event.getElement(), event
				.getChecked());
		IResource resource = (IResource) event.getElement();
		updateParentState(resource, event.getChecked());
	}

	private void updateParentState(IResource child, boolean baseChildState) {
		if (mode == MODE_FLAT || child == null || child.getParent() == null
				|| resourceList.contains(child.getParent())) {
			return;
		}
		CheckboxTreeViewer checkboxTreeViewer = (CheckboxTreeViewer) treeViewer;
		if (child == null)
			return;
		Object parent = resourceSelectionContentProvider.getParent(child);
		if (parent == null)
			return;
		boolean allSameState = true;
		Object[] children = null;
		children = resourceSelectionContentProvider.getChildren(parent);
		for (int i = children.length - 1; i >= 0; i--) {
			if (checkboxTreeViewer.getChecked(children[i]) != baseChildState
					|| checkboxTreeViewer.getGrayed(children[i])) {
				allSameState = false;
				break;
			}
		}
		checkboxTreeViewer.setGrayed(parent, !allSameState);
		checkboxTreeViewer.setChecked(parent, !allSameState || baseChildState);
		updateParentState((IResource) parent, baseChildState);
	}

	private void refresh() {
		Object[] checkedElements = null;
		if (checkbox)
			checkedElements = ((CheckboxTreeViewer) treeViewer).getCheckedElements();
		treeViewer.refresh();
		treeViewer.expandAll();
		if (checkbox)
			((CheckboxTreeViewer) treeViewer).setCheckedElements(checkedElements);
		if (checkbox && mode == MODE_TREE) {
			treeViewer.collapseAll();
		}
	}

	private IContainer[] getRootFolders() {
		if (rootFolders == null)
			getFolders();
		return rootFolders;
	}

	private IContainer[] getCompressedFolders() {
		if (compressedFolders == null) {
			compressedFolderList = new ArrayList();
			for (int i = 0; i < resources.length; i++) {
				if (resources[i] instanceof IContainer
						&& !compressedFolderList.contains(resources[i]))
					compressedFolderList.add(resources[i]);
				if (!(resources[i] instanceof IContainer)) {
					IContainer parent = resources[i].getParent();
					if (parent != null && !(parent instanceof IWorkspaceRoot)
							&& !compressedFolderList.contains(parent)) {
						compressedFolderList.add(parent);
					}
				}
			}
			compressedFolders = new IContainer[compressedFolderList.size()];
			compressedFolderList.toArray(compressedFolders);
			Arrays.sort(compressedFolders, comparator);
		}
		return compressedFolders;
	}

	private IResource[] getChildResources(IContainer parent) {
		ArrayList children = new ArrayList();
		for (int i = 0; i < resources.length; i++) {
			if (!(resources[i] instanceof IContainer)) {
				IContainer parentFolder = resources[i].getParent();
				if (parentFolder != null && parentFolder.equals(parent)
						&& !children.contains(parentFolder))
					children.add(resources[i]);
			}
		}
		IResource[] childArray = new IResource[children.size()];
		children.toArray(childArray);
		return childArray;
	}

	private IResource[] getFolderChildren(IContainer parent) {
		ArrayList children = new ArrayList();
		folders = getFolders();
		for (int i = 0; i < folders.length; i++) {
			if (folders[i].getParent() != null && folders[i].getParent().equals(parent))
				children.add(folders[i]);
		}
		for (int i = 0; i < resources.length; i++) {
			if (!(resources[i] instanceof IContainer) && resources[i].getParent() != null
					&& resources[i].getParent().equals(parent))
				children.add(resources[i]);
		}
		IResource[] childArray = new IResource[children.size()];
		children.toArray(childArray);
		return childArray;
	}

	private IContainer[] getFolders() {
		List rootList = new ArrayList();
		if (folders == null) {
			folderList = new ArrayList();
			for (int i = 0; i < resources.length; i++) {
				if (resources[i] instanceof IContainer)
					folderList.add(resources[i]);
				IResource parent = resources[i];
				while (parent != null && !(parent instanceof IWorkspaceRoot)) {
					if (!(parent.getParent() instanceof IWorkspaceRoot)
							&& folderList.contains(parent.getParent()))
						break;
					if (parent.getParent() == null
							|| parent.getParent() instanceof IWorkspaceRoot) {
						rootList.add(parent);
					}
					parent = parent.getParent();
					folderList.add(parent);
				}
			}
			folders = new IContainer[folderList.size()];
			folderList.toArray(folders);
			Arrays.sort(folders, comparator);
			rootFolders = new IContainer[rootList.size()];
			rootList.toArray(rootFolders);
			Arrays.sort(rootFolders, comparator);
		}
		return folders;
	}

	private class ResourceSelectionContentProvider extends WorkbenchContentProvider {
		public Object getParent(Object element) {
			return ((IResource) element).getParent();
		}

		public boolean hasChildren(Object element) {
			if (mode != MODE_FLAT && element instanceof IContainer)
				return true;
			else
				return false;
		}

		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof ResourceSelectionTree) {
				if (mode == MODE_FLAT)
					return resources;
				else if (mode == MODE_COMPRESSED_FOLDERS)
					return getCompressedFolders();
				else
					return getRootFolders();
			}
			if (parentElement instanceof IContainer) {
				if (mode == MODE_COMPRESSED_FOLDERS) {
					return getChildResources((IContainer) parentElement);
				}
				if (mode == MODE_TREE) {
					return getFolderChildren((IContainer) parentElement);
				}
			}
			return new Object[0];
		}
	}

	private class ResourceSelectionLabelProvider extends LabelProvider {
		private LabelProvider workbenchLabelProvider = new WorkbenchLabelProvider();

		public Image getImage(Object element) {
			Image image = null;
			if (resourceList.contains(element)) {
				if (element instanceof IResource) {
					image = workbenchLabelProvider.getImage(element);
					image = Decorator.getImage(image, statusMap.get(element));
				}
				return image;

			} else {
				image = workbenchLabelProvider.getImage(element);
			}

			return image;
		}
	}

	private class ResourceComparator implements Comparator<IResource> {
		public int compare(IResource resource0, IResource resource1) {

			return resource0.getFullPath().toOSString().compareTo(
					resource1.getFullPath().toOSString());
		}
	}

	public static interface IToolbarControlCreator {
		public void createToolbarControls(ToolBarManager toolbarManager);

		public int getControlCount();
	}

	public static interface IRemoveFromViewValidator {
		public boolean canRemove(ArrayList<IResource> resourceList,
				IStructuredSelection selection);

		public String getErrorMessage();
	}

	public void setRemoveFromViewValidator(
			IRemoveFromViewValidator removeFromViewValidator) {
		this.removeFromViewValidator = removeFromViewValidator;
	}

	public void setShowRemoveFromViewAction(boolean showRemoveFromViewAction) {
		this.showRemoveFromViewAction = showRemoveFromViewAction;
	}

	public boolean isResourceRemoved() {
		if (checkbox) {
			resourceRemoved = resources.length > getSelectedResources().length;
		}
		return resourceRemoved;
	}
}
