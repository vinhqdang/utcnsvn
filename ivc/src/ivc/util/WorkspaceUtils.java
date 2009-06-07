package ivc.util;

import ivc.plugin.IVCPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IWorkbenchPage;

/**
 * Workspace utilities.
 */
public class WorkspaceUtils {

	/**
	 * Gets the current file from the workspace
	 * 
	 * @return the IFile from the active workbench window
	 */
	public static IFile getCurrentFile() {
		IEditorPart editor = IVCPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		IEditorInput input = editor.getEditorInput();
		IFile file = null;
		if (input instanceof IFileEditorInput) {
			file = ((IFileEditorInput) input).getFile();
		}
		return file;
	}
}
