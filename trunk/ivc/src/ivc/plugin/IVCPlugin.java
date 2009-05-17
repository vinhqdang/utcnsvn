package ivc.plugin;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import ivc.fireworks.decorator.Decorator;
import ivc.listeners.ResourceChangedListener;
import ivc.manager.ProjectsManager;
import ivc.repository.SVNLocalCompareInput;

import org.eclipse.compare.CompareUI;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.UIPlugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class IVCPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "IVC";
	public URL baseURL;
	// The shared instance
	public static IVCPlugin plugin;

	/**
	 * The constructor
	 */
	public IVCPlugin() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		baseURL = context.getBundle().getEntry("/");
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		ResourceChangedListener changeListener = new ResourceChangedListener();
		workspace.addResourceChangeListener(changeListener);
		Decorator.enableDecoration = true;
		ProjectsManager.instance().findProjects();
		Decorator.enableDecoration=true;
		ProjectsManager.instance().findProjects();	
	
		
		ConnectionManager.getInstance().initiateConnections();
	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static IVCPlugin getDefault() {
		return plugin;
	}

	public String getAdminDirectoryName() {
		return ".ivc";
	}

	
	public void runWithProgress(Shell parent, boolean cancelable,
			final IRunnableWithProgress runnable) throws InvocationTargetException,
			InterruptedException {

		boolean createdShell = false;
		try {
			if (parent == null || parent.isDisposed()) {
				Display display = Display.getCurrent();
				if (display == null) {
					// cannot provide progress (not in UI thread)
					runnable.run(new NullProgressMonitor());
					return;
				}
				// get the active shell or a suitable top-level shell
				parent = display.getActiveShell();
				if (parent == null) {
					parent = new Shell(display);
					createdShell = true;
				}
			}
			// pop up progress dialog after a short delay
			final Exception[] holder = new Exception[1];
			BusyIndicator.showWhile(parent.getDisplay(), new Runnable() {
				public void run() {
					try {
						runnable.run(new NullProgressMonitor());
					} catch (InvocationTargetException e) {
						holder[0] = e;
					} catch (InterruptedException e) {
						holder[0] = e;
					}
				}
			});
			if (holder[0] != null) {
				if (holder[0] instanceof InvocationTargetException) {
					throw (InvocationTargetException) holder[0];
				} else {
					throw (InterruptedException) holder[0];
				}
			}
			// new TimeoutProgressMonitorDialog(parent, TIMEOUT).run(true
			// /*fork*/, cancelable, runnable);
		} finally {
			if (createdShell)
				parent.dispose();
		}
	}

}
