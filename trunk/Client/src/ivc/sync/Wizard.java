package ivc.sync;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.team.ui.TeamUI;
import org.eclipse.team.ui.synchronize.ISynchronizeManager;
import org.eclipse.team.ui.synchronize.ISynchronizeParticipant;
import org.eclipse.team.ui.synchronize.ISynchronizeScope;
import org.eclipse.team.ui.synchronize.ISynchronizeView;
import org.eclipse.team.ui.synchronize.SubscriberParticipant;
import org.eclipse.team.ui.synchronize.SubscriberParticipantWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class Wizard extends org.eclipse.jface.wizard.Wizard {
	private SubscriberParticipant participant;

//	@Override
//	protected SubscriberParticipant createParticipant(ISynchronizeScope arg0) {
//	}
//
//	private SubscriberParticipant GetParticipant() {
//		if (participant == null) {
//			createParticipant(null);
//		}
//		return participant;
//	}
//
//	@Override
//	protected IWizard getImportWizard() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	protected String getPageTitle() {
//		return "My title";
//	}
//
//	@Override
//	protected IResource[] getRootResources() {
//		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
//		IResource resource = root.findMember(new Path(containerName));
//		if (!resource.exists() || !(resource instanceof IContainer)) {
//			throwCoreException("Container \"" + containerName
//					+ "\" does not exist.");
//		}
//		IContainer container = (IContainer) resource;
//		final IFile iFile = container.getFile(new Path(fileName));
//		return resources;
//	}
	public Wizard(){
		super();
        setNeedsProgressMonitor(true);
        ImageDescriptor image =
            AbstractUIPlugin.
                imageDescriptorFromPlugin("test",
                   "k.gif");
        setDefaultPageImageDescriptor(image);

	}
	
//	public void init(IWorkbench workbench,
//            IStructuredSelection selection) {
//        this.selection = selection;
//    }

	@Override
	public boolean performFinish() {
		final String containerName = this.getPages()[0].getName();
		final String fileName = "d:\temp\test.txt";		
		

		// Now invoke the finish method.
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					doFinish(containerName, fileName,  monitor);
				} catch (Exception e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException
					.getMessage());
			return false;
		}
		return true;

	}

	public boolean doFinish(String containerName,String fileName,
	        IProgressMonitor monitor) {
		LocalSubscriber participant = new LocalSubscriber();
		ISynchronizeManager manager = TeamUI.getSynchronizeManager();
		ISynchronizeParticipant[] part = new ISynchronizeParticipant[] { participant };
		try {
			manager.addSynchronizeParticipants(part);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ISynchronizeView view = manager.showSynchronizeViewInActivePage();
		return true;
	}

}
