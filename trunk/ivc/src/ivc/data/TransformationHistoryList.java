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
	public LinkedList<TransformationHistory> getTransformationHist() {
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

	public LinkedList<Transformation> getTransformationsForFile(String filePath) {
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
	
	public TransformationHistoryList appendTransformation(Transformation transf) {
		String filePath = transf.getFilePath();
		Iterator<TransformationHistory> it = iterator();
		while(it.hasNext()){
			TransformationHistory th = it.next();
			if (th.getFilePath().equalsIgnoreCase(filePath)){
				th.addTransformation(transf);
			}
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

	public TransformationHistoryList removeTransformationHistForFile(String filePath) {
		TransformationHistory th = getTransformationHistForFile(filePath);
		if (th != null) {
			transformations.remove(th);
		}
		return this;
	}
	
	public TransformationHistoryList removeTransformationHistList(TransformationHistoryList thl) {
			transformations.removeAll(thl.getTransformationHist());
		return this;
	}

	public void applyTransformationHistoryList(IProject project) {
		Iterator<TransformationHistory> it = iterator();
		while (it.hasNext()) {
			TransformationHistory th = it.next();
			// the most recent transformation
			Transformation firstTransf = th.getTransformations().getFirst();
			if (firstTransf.getOperationType() == Transformation.REMOVE_FILE || firstTransf.getOperationType() == Transformation.REMOVE_FOLDER ){
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
				for (Iterator<Transformation> iterator = th.getTransformations().iterator(); iterator.hasNext();) {
					Transformation transformation = iterator.next();
					if (transformation.getOperationType() == Transformation.CHARACTER_ADD
							|| transformation.getOperationType() == Transformation.CHARACTER_DELETE) {
						baseContent = transformation.applyContentTransformation(baseContent);
					} else {
						transformation.applyStructureTransformation();
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
}
