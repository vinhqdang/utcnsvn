/**
 * 
 */
package ivc.data;

import ivc.util.FileUtils;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

/**
 * @author danielan
 * 
 */
public class OperationHistoryList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LinkedList<OperationHistory> operations;

	/**
	 * 
	 */
	public OperationHistoryList() {
		operations = new LinkedList<OperationHistory>();
	}

	/**
	 * @return the operations
	 */
	public LinkedList<OperationHistory> getTransformationHist() {
		return operations;
	}

	/**
	 * @param operations
	 *            the operations to set
	 */
	public void setTransformationHist(LinkedList<OperationHistory> transformations) {
		this.operations = transformations;
	}

	public Iterator<OperationHistory> iterator() {
		return (Iterator<OperationHistory>) operations.iterator();
	}

	public LinkedList<Operation> getTransformationsForFile(String filePath) {
		Iterator<OperationHistory> it = this.operations.iterator();
		while (it.hasNext()) {
			OperationHistory th = it.next();
			if (th.getFilePath().equalsIgnoreCase(filePath)) {
				return th.getTransformations();
			}
		}
		return null;
	}
	
	public OperationHistory getOperationHistForFile(String filePath){
		Iterator<OperationHistory> it = this.operations.iterator();
		while (it.hasNext()) {
			OperationHistory th = it.next();
			if (th.getFilePath().equalsIgnoreCase(filePath)) {
				return th;
			}
		}
		return null;
	}

	public OperationHistoryList appendOperationHistory(OperationHistory oh) {
		boolean isnew = true;
		Iterator<OperationHistory> it = this.operations.iterator();
		while (it.hasNext()) {
			OperationHistory cth = it.next();
			if (oh.getFilePath().equalsIgnoreCase(oh.getFilePath())) {
				isnew = false;
				cth.addTransformations(oh.getTransformations());
				break;
			}
		}
		if (isnew) {
			operations.add(oh);
		}
		return this;
	}

	public OperationHistoryList appendOperationHistoryList(OperationHistoryList thl) {
		if (thl == null || thl.getTransformationHist() == null) {
			return this;
		}
		Iterator<OperationHistory> it = thl.iterator();
		while (it.hasNext()) {
			OperationHistory th = it.next();
			appendOperationHistory(th);
		}
		return this;
	}
	
	public OperationHistoryList appendOperation(Operation transf) {
		String filePath = transf.getFilePath();
		Iterator<OperationHistory> it = iterator();
		while(it.hasNext()){
			OperationHistory th = it.next();
			if (th.getFilePath().equalsIgnoreCase(filePath)){
				th.addOperation(transf);
			}
		}
	return this;
	}

	public OperationHistoryList removeTransformationHistory(OperationHistory th) {
		operations.remove(th);
		return this;
	}

	private OperationHistory getTransformationHistForFile(String filePath) {
		Iterator<OperationHistory> it = this.operations.iterator();
		while (it.hasNext()) {
			OperationHistory th = it.next();
			if (th.getFilePath().equalsIgnoreCase(th.getFilePath())) {
				return th;
			}
		}
		return null;
	}

	public OperationHistoryList removeTransformationHistForFile(String filePath) {
		OperationHistory th = getTransformationHistForFile(filePath);
		if (th != null) {
			operations.remove(th);
		}
		return this;
	}
	
	public OperationHistoryList removeTransformationHistList(OperationHistoryList thl) {
			operations.removeAll(thl.getTransformationHist());
		return this;
	}

	public void applyTransformationHistoryList(IProject project) {
		Iterator<OperationHistory> it = iterator();
		while (it.hasNext()) {
			OperationHistory th = it.next();
			// the most recent transformation
			Operation firstTransf = th.getTransformations().getFirst();
			if (firstTransf.getOperationType() == Operation.REMOVE_FILE || firstTransf.getOperationType() == Operation.REMOVE_FOLDER ){
				firstTransf.applyStructureTransformation();
				return;
			}
			String filePath = th.getFilePath();
			IFile file = (IFile) project.getFile(filePath);			
			InputStream is;
			try {
				StringBuffer baseContent =  new StringBuffer();
				if (file.exists()){
				is = file.getContents();
				 baseContent = FileUtils.InputStreamToStringBuffer(is);
				}
				for (Iterator<Operation> iterator = th.getTransformations().iterator(); iterator.hasNext();) {
					Operation operation = iterator.next();
					if (operation.getOperationType() == Operation.CHARACTER_ADD
							|| operation.getOperationType() == Operation.CHARACTER_DELETE) {
						baseContent = operation.applyContentTransformation(baseContent);
					} else {
						operation.applyStructureTransformation();
						break;
					}
				}
				FileUtils.writeStringBufferToFile(project.getLocation().toOSString() + "\\" + filePath, baseContent);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public OperationHistoryList includeOperationHistoryList(OperationHistoryList ohl){
		OperationHistoryList newOhl = new OperationHistoryList();
		Iterator<OperationHistory> it = iterator();
		while (it.hasNext()) {
			OperationHistory oh = it.next();
			String filePath = oh.getFilePath();
			// if both lists have operations for the same file
			if (ohl.getTransformationsForFile(filePath) != null) {
				LinkedList<Operation> ownOps = getTransformationsForFile(filePath);
				LinkedList<Operation> otherOps = ohl.getTransformationsForFile(filePath);
			// if the other list deleted the file\folder we have operations on abort operations 
			if (otherOps.getFirst().getOperationType() != Operation.REMOVE_FILE || otherOps.getFirst().getOperationType() != Operation.REMOVE_FOLDER) {
					// merge lists of content operations
					Iterator<Operation> itoOwn = ownOps.descendingIterator();
					while (itoOwn.hasNext()) {
						Operation ownOp = itoOwn.next();
						Operation newOp = ownOp;
						Iterator<Operation> itoOther = otherOps.descendingIterator();
						while (itoOther.hasNext()) {
							Operation op = itoOther.next();
							newOp = newOp.includeOperation(op);
						}
						newOhl.appendOperation(newOp);
					}
				}
			} else {
				// only we modified the file content
				newOhl.appendOperationHistory(oh);
			}
		}

		return newOhl;
	}
	
}
