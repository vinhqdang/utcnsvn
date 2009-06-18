package ivc.compare;

import ivc.data.exception.IVCException;

import org.eclipse.core.resources.IFile;

/**
 * The class is used to create IDiffComparable objects based on the type of the input
 * object
 * 
 * @author alexm
 * 
 */
public class DiffComparableFactory {
	/**
	 * Creates a DiffComparableObject based on the input source
	 * 
	 * @param source
	 *            the source from which to create the object
	 * @return if the source is a string then the result is a DiffComparable string else
	 *         if the source is a IFile then the result is a DiffComparableFile
	 * @throws IVCException
	 */
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
