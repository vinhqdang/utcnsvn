package ivc.compare;

import org.eclipse.compare.IEditableContent;
import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;

/**
 * Interface used for unifying all interfaces needed for the objects to be valid input for
 * comparers
 * 
 * @author alexm
 * 
 */
public interface IDiffComparable extends ITypedElement, IStreamContentAccessor,
		IEditableContent {

}
