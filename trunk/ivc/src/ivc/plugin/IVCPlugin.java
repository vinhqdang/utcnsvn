package ivc.plugin;

import ivc.commands.CommandArgs;
import ivc.commands.StartCommand;
import ivc.commands.StopCommand;
import ivc.fireworks.decorators.Decorator;
import ivc.listeners.FileModificationListener;
import ivc.listeners.ResourceChangedListener;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
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
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		baseURL = context.getBundle().getEntry("/");
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		ResourceChangedListener changeListener = new ResourceChangedListener();
		FileModificationListener modifications = new FileModificationListener();
		workspace.addResourceChangeListener(changeListener, IResourceChangeEvent.PRE_BUILD);
		workspace.addResourceChangeListener(modifications, IResourceChangeEvent.PRE_BUILD);

		Decorator.enableDecoration = true;

		// call start command for establishing connections with the other peers
		StartCommand startCommand = new StartCommand();
		startCommand.execute(new CommandArgs());
	}

	/*
	 * 7 (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext )
	 */
	public void stop(BundleContext context) throws Exception {
		// disconnect from server and notify hosts
		System.out.println("Stooooooooooooooop");
		StopCommand stopCommand = new StopCommand();
		stopCommand.execute(new CommandArgs());
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

	public void runWithProgress(Shell parent, boolean cancelable, final IRunnableWithProgress runnable) throws InvocationTargetException,
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

	public static IStatus openError(Shell providedShell, String title, String message, Throwable exception, int flags) {
		// Unwrap InvocationTargetExceptions
		if (exception instanceof InvocationTargetException) {
			Throwable target = ((InvocationTargetException) exception).getTargetException();
			// re-throw any runtime exceptions or errors so they can be handled by the workbench
			if (target instanceof RuntimeException) {
				throw (RuntimeException) target;
			}
			if (target instanceof Error) {
				throw (Error) target;
			}
			return openError(providedShell, title, message, target, flags);
		}
		return null;
	}

}
