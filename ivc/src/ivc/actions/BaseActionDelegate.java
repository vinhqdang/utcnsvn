package ivc.actions;

import ivc.managers.ProjectsManager;
import ivc.plugin.IVCPlugin;
import ivc.repository.Status;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceTraversal;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * 
 * @author alexm This
 * 
 *         This class provides the main methods used to execute actions
 */
public abstract class BaseActionDelegate implements IWorkbenchWindowActionDelegate {
	private ISelection selection;
	private Shell shell;

	/**
	 * This method is used by the subclasses to return the list of selected resources and
	 * all the children
	 * 
	 * @param forCommit
	 *            speciffies if the resources are returned for a commit operation
	 * @return the list of selected resources and all the children
	 * @throws CoreException
	 */
	public IResource[] findAllResources(boolean forCommit) throws CoreException {
		ArrayList<IResource> resourcesTo = new ArrayList<IResource>();
		IResource[] resources = getSelectedResources();
		for (IResource resource : resources) {
			if (!resource.isTeamPrivateMember()) {
				if (forCommit) {
					if ((ProjectsManager.instance().isManaged(resource))) {
						if (ProjectsManager.instance().getStatus(resource).compareTo(
								Status.Modified) > 0) {
							continue;
						}
					}
				}
				// adds the resurce to the final resources list
				addResource(resourcesTo, resource, forCommit);
			}
		}
		// resourcesTo.addAll(ProjectsManager.instance().getIVCProjectByName(resources[0].
		// getProject().getName()).getDeleted());
		IResource[] result = new IResource[resourcesTo.size()];
		resourcesTo.toArray(result);

		return result;
	}

	/**
	 * Adds a resource to a existing list and recursively calls itself for the children
	 * 
	 * @param resources
	 *            the list of resources to retrieve
	 * @param resource
	 *            the current resource
	 * @param forCommit
	 *            speciffies if the resources are returned for a commit operation
	 * @throws CoreException
	 */
	private void addResource(List<IResource> resources, IResource resource,
			boolean forCommit) throws CoreException {
		if (!resources.contains(resource)) {
			if (forCommit) {
				if ((ProjectsManager.instance().isManaged(resource))) {
					if (ProjectsManager.instance().getStatus(resource).compareTo(
							Status.Modified) > 0) {
						return;
					}
				}
			}
			resources.add(resource);
			if (resource instanceof IProject) {
				IProject proj = (IProject) resource;
				for (IResource res : proj.members(true)) {
					if (!res.getName().equals("bin") && !res.getName().equals(".ivc")) {
						addResource(resources, res, forCommit);
					}
				}
			} else {
				if (resource instanceof IFolder) {
					IFolder fold = (IFolder) resource;
					for (IResource res : fold.members(true)) {
						addResource(resources, res, forCommit);
					}
				}
			}
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
		action.setEnabled(menuItemEnabled());
	}

	public abstract boolean menuItemEnabled();

	protected boolean resourceInRepository(IResource resource) {
		return ProjectsManager.instance().isManaged(resource);
	}

	/**
	 * Is used to return the current shell or a new shell
	 * 
	 * @return the current shell
	 */
	public Shell getShell() {
		if (shell != null) {
			return shell;
		} else {
			IWorkbench workbench = IVCPlugin.plugin.getWorkbench();
			if (workbench == null)
				return null;
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			if (window == null)
				return null;
			return window.getShell();
		}
	}

	/**
	 * Returns a list of selected adaptables
	 * 
	 * @param selection
	 *            the current selection
	 * @param c
	 *            the class of the adaptables
	 * @returna list of selected adaptables
	 */
	protected Object[] getSelectedAdaptables(ISelection selection, Class<?> c) {
		ArrayList<Object> result = null;
		if (selection != null && !selection.isEmpty()) {
			result = new ArrayList<Object>();
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Iterator<?> elements = structuredSelection.iterator();
			while (elements.hasNext()) {
				Object adapter = getAdapter(elements.next(), c);
				if (c.isInstance(adapter)) {
					result.add(adapter);
				}
			}
		}
		if (result != null && !result.isEmpty()) {
			return result.toArray((Object[]) Array.newInstance(c, result.size()));
		}
		return (Object[]) Array.newInstance(c, 0);
	}

	protected Object getAdapter(Object adaptable, Class<?> c) {
		if (c.isInstance(adaptable)) {
			return adaptable;
		}
		if (adaptable instanceof IAdaptable) {
			IAdaptable a = (IAdaptable) adaptable;
			Object adapter = a.getAdapter(c);
			if (c.isInstance(adapter)) {
				return adapter;
			}
		}
		return null;
	}

	/**
	 * Returns an object array of adaptables from getSelectedAdaptables function
	 * 
	 * @param c
	 *            the class of the adaptables
	 * @return an object array of adaptables from getSelectedAdaptables function
	 */
	protected Object[] getSelectedResources(Class<?> c) {
		return getSelectedAdaptables(selection, c);
	}

	/**
	 * Returns the selected resources
	 * 
	 * @return
	 */
	protected IResource[] getSelectedResources() {
		ArrayList<IResource> resourceArray = new ArrayList<IResource>();
		IResource[] resources = (IResource[]) getSelectedResources(IResource.class);
		for (int i = 0; i < resources.length; i++)
			resourceArray.add(resources[i]);
		ResourceMapping[] resourceMappings = (ResourceMapping[]) getSelectedAdaptables(
				selection, ResourceMapping.class);
		for (int i = 0; i < resourceMappings.length; i++) {
			ResourceMapping resourceMapping = (ResourceMapping) resourceMappings[i];
			try {
				ResourceTraversal[] traversals = resourceMapping
						.getTraversals(null, null);
				for (int j = 0; j < traversals.length; j++) {
					IResource[] traversalResources = traversals[j].getResources();
					for (int k = 0; k < traversalResources.length; k++) {
						if (!resourceArray.contains(traversalResources[k]))
							resourceArray.add(traversalResources[k]);
					}
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		IResource[] selectedResources = new IResource[resourceArray.size()];
		resourceArray.toArray(selectedResources);
		return selectedResources;
	}

	/**
	 * 
	 * @return the current selection
	 */
	protected ISelection getSelection() {
		return selection;
	}

}
