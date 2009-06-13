package ivc.compare;

import ivc.managers.ImageDescriptorManager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.compare.ITypedElement;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.graphics.Image;

public class DiffComparableString implements IDiffComparable {
	private String content;

	protected DiffComparableString(String content) {
		this.content = content;
	}

	@Override
	public Image getImage() {
		return ImageDescriptorManager.getImage(ImageDescriptorManager.DCORATOR_SHARED);
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public String getType() {
		return ITypedElement.TEXT_TYPE;
	}

	@Override
	public InputStream getContents() throws CoreException {
		ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes());
		return bais;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public ITypedElement replace(ITypedElement dest, ITypedElement src) {
		return dest;
	}

	@Override
	public void setContent(byte[] arg0) {

	}

}
