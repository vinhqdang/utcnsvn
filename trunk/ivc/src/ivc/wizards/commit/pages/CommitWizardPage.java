package ivc.wizards.commit.pages;

import ivc.managers.ImageDescriptorManager;
import ivc.managers.ProjectsManager;
import ivc.repository.Status;
import ivc.util.ResourceSelectionTree;
import ivc.wizards.commit.CommitWizardDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

public class CommitWizardPage extends WizardPage {
	private SashForm sashForm;
	private IResource[] resourcesToCommit;

	private IResource[] selectedResources;

	private Button includeUnversionedButton;
	private boolean keepLocks;
	private boolean includeUnversioned;
	private String removalError;

	private Map<IResource, Status> statusMap;
	private ResourceSelectionTree resourceSelectionTree;

	public CommitWizardPage(IResource[] resourcesToCommit, Map<IResource, Status> statusMap, boolean fromSyncView) {
		super("Commit Wizard", "Commit", ImageDescriptorManager.getImageDescriptor(ImageDescriptorManager.SHARE_WIZARD));
		setTitle("Commit");
		includeUnversioned = true;

		this.resourcesToCommit = resourcesToCommit;

		this.statusMap = statusMap;

	}

	public void createControl(Composite parent) {
		Composite outerContainer = new Composite(parent, SWT.NONE);
		GridLayout outerLayout = new GridLayout();
		outerLayout.numColumns = 1;
		outerLayout.marginHeight = 0;
		outerLayout.marginWidth = 0;
		outerContainer.setLayout(outerLayout);
		outerContainer.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));

		createControls(outerContainer);

		setMessage();

		setControl(outerContainer);
	}

	public void createControls(Composite composite) {
		sashForm = new SashForm(composite, SWT.VERTICAL);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		sashForm.setLayout(gridLayout);
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite cBottom1 = new Composite(sashForm, SWT.NULL);
		GridLayout bottom1Layout = new GridLayout();
		bottom1Layout.marginHeight = 0;
		bottom1Layout.marginWidth = 0;
		cBottom1.setLayout(bottom1Layout);
		cBottom1.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite cBottom2 = new Composite(cBottom1, SWT.NULL);
		GridLayout bottom2Layout = new GridLayout();
		bottom2Layout.marginHeight = 0;
		bottom2Layout.marginWidth = 0;
		cBottom2.setLayout(bottom2Layout);
		cBottom2.setLayoutData(new GridData(GridData.FILL_BOTH));

		addResourcesArea(cBottom2);

		// set F1 help
		PlatformUI.getWorkbench().getHelpSystem().setHelp(composite, "Commit");
		setPageComplete(canFinish());
	}

	private void addResourcesArea(Composite composite) {
		ResourceSelectionTree.IToolbarControlCreator toolbarControlCreator = new ResourceSelectionTree.IToolbarControlCreator() {
			public void createToolbarControls(ToolBarManager toolbarManager) {

				toolbarManager.add(new ControlContribution("ignoreUnversioned") {
					protected Control createControl(Composite parent) {
						includeUnversionedButton = new Button(parent, SWT.CHECK);
						includeUnversionedButton.setText("Include unversioned");
						includeUnversionedButton.setSelection(includeUnversioned);
						includeUnversionedButton.addSelectionListener(new SelectionListener() {
							public void widgetSelected(SelectionEvent e) {
								includeUnversioned = includeUnversionedButton.getSelection();
								if (!includeUnversioned) {
									resourceSelectionTree.removeUnversioned();
								} else {
									resourceSelectionTree.addUnversioned();
								}
								selectedResources = resourceSelectionTree.getSelectedResources();
								setPageComplete(canFinish());

							}

							public void widgetDefaultSelected(SelectionEvent e) {
							}
						});
						return includeUnversionedButton;
					}
				});

			}

			public int getControlCount() {
				return 1;
			}
		};
		resourceSelectionTree = new ResourceSelectionTree(composite, SWT.NONE, "Resources to commit", resourcesToCommit, statusMap, null, true,
				toolbarControlCreator);
		if (!resourceSelectionTree.showIncludeUnversionedButton())
			includeUnversionedButton.setVisible(false);

		resourceSelectionTree.setRemoveFromViewValidator(new ResourceSelectionTree.IRemoveFromViewValidator() {
			public boolean canRemove(ArrayList<IResource> resourceList, IStructuredSelection selection) {
				return removalOk(resourceList, selection);
			}

			public String getErrorMessage() {
				return removalError;
			}
		});
		resourceSelectionTree.getTreeViewer().addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				selectedResources = resourceSelectionTree.getSelectedResources();
			}
		});
		((CheckboxTreeViewer) resourceSelectionTree.getTreeViewer()).addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				selectedResources = resourceSelectionTree.getSelectedResources();
			}
		});
		resourceSelectionTree.getTreeViewer().addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				// IStructuredSelection sel = (IStructuredSelection) event.getSelection();
				// Object sel0 = sel.getFirstElement();
				// if (sel0 instanceof IFile) {
				// final ISVNLocalResource localResource = SVNWorkspaceRoot.getSVNResourceFor((IFile) sel0);
				// try {
				// new CompareDialog(getShell(), new IVCCompareEditorInput(localResource)).open();
				// } catch (SVNException e1) {
				// }
				// }
			}
		});
		if (!includeUnversioned) {
			resourceSelectionTree.removeUnversioned();
		}
		selectedResources = resourceSelectionTree.getSelectedResources();
		setPageComplete(canFinish());
	}

	public boolean performCancel() {
		return true;
	}

	public boolean performFinish() {
		return true;
	}

	@SuppressWarnings("unchecked")
	private boolean removalOk(ArrayList resourceList, IStructuredSelection selection) {
		ArrayList clonedList = (ArrayList) resourceList.clone();
		List deletedFolders = new ArrayList();
		Iterator iter = selection.iterator();
		while (iter.hasNext())
			clonedList.remove(iter.next());
		ArrayList folderPropertyChanges = new ArrayList();
		boolean folderDeletionSelected = false;
		iter = clonedList.iterator();
		while (iter.hasNext()) {
			IResource resource = (IResource) iter.next();
			if (resource instanceof IContainer) {
				if (ProjectsManager.instance().getStatus(resource).equals(Status.Deleted)) {
					folderDeletionSelected = true;
					deletedFolders.add(resource);
				}
				// String propertyStatus = ResourceWithStatusUtil.getPropertyStatus(resource);
				// if (propertyStatus != null && propertyStatus.length() > 0)
				// folderPropertyChanges.add(resource);
			}
		}
		if (folderDeletionSelected) {
			iter = selection.iterator();
			while (iter.hasNext()) {
				IResource resource = (IResource) iter.next();
				Iterator iter2 = deletedFolders.iterator();
				while (iter2.hasNext()) {
					IContainer deletedFolder = (IContainer) iter2.next();
					if (isChild(resource, deletedFolder)) {
						removalError = "CommitDialog.parentDeleted";
						return false;
					}
				}
			}
		}
		if (!folderDeletionSelected || folderPropertyChanges.size() == 0)
			return true;
		boolean unselectedPropChangeChildren = false;
		iter = folderPropertyChanges.iterator();
		outer: while (iter.hasNext()) {
			IContainer container = (IContainer) iter.next();
			for (int i = 0; i < resourcesToCommit.length; i++) {
				if (!clonedList.contains(resourcesToCommit[i])) {
					if (isChild(resourcesToCommit[i], container)) {
						unselectedPropChangeChildren = true;
						removalError = "CommitDialog.unselectedPropChangeChildren";
						break outer;
					}
				}
			}
		}
		return !unselectedPropChangeChildren;
	}

	private boolean isChild(IResource resource, IContainer folder) {
		IContainer container = resource.getParent();
		while (container != null) {
			if (container.getFullPath().toString().equals(folder.getFullPath().toString()))
				return true;
			container = container.getParent();
		}
		return false;
	}

	public void setMessage() {
		setMessage("Commit your changes");
	}

	private boolean canFinish() {
		if (selectedResources.length == 0) {
			return false;
		}
		return true;
	}

	public IResource[] getSelectedResources() {
		if (selectedResources == null) {
			return resourcesToCommit;
		} else {
			List<IResource> result = Arrays.asList(selectedResources);
			return (IResource[]) result.toArray(new IResource[result.size()]);
		}
	}

	public boolean isKeepLocks() {
		return keepLocks;
	}

	public String getWindowTitle() {
		return "Commit";
	}

	public void createButtonsForButtonBar(Composite parent, CommitWizardDialog wizardDialog) {
	}

}
