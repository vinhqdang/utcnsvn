/**
 * 
 */
package ivc.data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author danielan
 * 
 */
public class TransformationHistoryList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LinkedList<TransformationHistory> transformations;

	/**
	 * 
	 */
	public TransformationHistoryList() {
		transformations = new LinkedList<TransformationHistory>();
	}

	/**
	 * @return the transformations
	 */
	public List<TransformationHistory> getTransformationHist() {
		return transformations;
	}

	/**
	 * @param transformations
	 *            the transformations to set
	 */
	public void setTransformationHist(LinkedList<TransformationHistory> transformations) {
		this.transformations = transformations;
	}

	public Iterator<TransformationHistory> iterator() {
		return (Iterator<TransformationHistory>) transformations.iterator();
	}

	public List<Transformation> getTransformationsForFile(String filePath) {
		Iterator<TransformationHistory> it = this.transformations.iterator();
		while (it.hasNext()) {
			TransformationHistory th = it.next();
			if (th.getFilePath().equalsIgnoreCase(filePath)) {
				return th.getTransformations();
			}
		}
		return null;
	}

	public TransformationHistoryList appendTransformationHistory(TransformationHistory th) {
		boolean isnew = true;
		Iterator<TransformationHistory> it = this.transformations.iterator();
		while (it.hasNext()) {
			TransformationHistory cth = it.next();
			if (th.getFilePath().equalsIgnoreCase(th.getFilePath())) {
				isnew = false;
				cth.addTransformations(th.getTransformations());
				break;
			}
		}
		if (isnew) {
			transformations.add(th);
		}
		return this;
	}

	public TransformationHistoryList appendTransformationHistoryList(TransformationHistoryList thl) {
		if (thl == null || thl.getTransformationHist() == null) {
			return this;
		}
		Iterator<TransformationHistory> it = thl.iterator();
		while (it.hasNext()) {
			TransformationHistory th = it.next();
			appendTransformationHistory(th);
		}
		return this;
	}

	public TransformationHistoryList removeTransformationHistory(TransformationHistory th) {
		transformations.remove(th);
		return this;
	}

	private TransformationHistory getTransformationHistForFile(String filePath) {
		Iterator<TransformationHistory> it = this.transformations.iterator();
		while (it.hasNext()) {
			TransformationHistory th = it.next();
			if (th.getFilePath().equalsIgnoreCase(th.getFilePath())) {
				return th;
			}
		}
		return null;
	}

	public TransformationHistoryList removeTransformationHistForFile(String filePath){
		TransformationHistory th = getTransformationHistForFile(filePath);
		if (th != null){
			transformations.remove(th);
		}
		return this;
	}
}
