package ivc.compare;

import ivc.data.exception.IVCException;

import org.eclipse.core.resources.IFile;

public class DiffComparableFactory {
	public static IDiffComparable createComparable(Object source) throws IVCException {
		if (source instanceof IFile) {
			return new DiffComparableIFile((IFile) source);
		} else {
			if (source instanceof String) {
				return new DiffComparableString((String) source);
			} else
				throw new IVCException("Could not create resources to compare");
		}
	}
}
