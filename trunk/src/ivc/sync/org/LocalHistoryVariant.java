package ivc.sync.org;
import java.text.DateFormat;
import java.util.Date;

import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.team.core.variants.IResourceVariant;

public class LocalHistoryVariant implements IResourceVariant {

	private final IFileState state;

	public LocalHistoryVariant(IFileState state) {
		this.state = state;
	}
	
	public String getName() {
		return state.getName();
	}

	public boolean isContainer() {
		return false;
	}

	public IStorage getStorage(IProgressMonitor monitor) {
		return state;
	}

	public String getContentIdentifier() {
		return DateFormat.getDateTimeInstance().format(new Date(state.getModificationTime()));
	}

	public byte[] asBytes() {
		return null;
	}
	
	public IFileState getFileState() {
		return state;
	}
}
