package ivc.managers;

import ivc.plugin.IVCPlugin;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;

public class ImageDescriptorManager {
	public static final String SHARE_WIZARD = "img\\share_icon.png";
	public static final String DECORATOR_SHARED = "img\\shared.png";
	public static final String DECORATOR_CHANGED = "img\\modified.gif";
	public static final String DECORATOR_ADDED = "img\\added.gif";
	public static final String DECORATOR_DELETED = "img\\deleted.png";
	public static final String DECORATOR_UNVERSIONED = "img\\unversioned.gif";
	public static final String DECORATOR_ = "";
	public static final String MARKER = "img\\shared.png";
	public static final String TREE_FLAT = "img\\tree_flat.png";
	public static final String TREE_AFFECTED_PATHS = "img\\tree.png";
	public static final String TREE_AFFECTED_PATHS_COMPRESSED = "img\\tree_compressed.gif";

	/**
	 * Returns an image descriptor
	 * 
	 * @param imageUrl
	 *            the url to the image
	 * @return
	 */
	public static ImageDescriptor getImageDescriptor(String imageUrl) {
		URL url;
		try {
			url = new URL(IVCPlugin.plugin.baseURL, imageUrl);

			return ImageDescriptor.createFromURL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns an eclipse Image object from a specified URL
	 * 
	 * @param imageUrl
	 *            url of the image
	 * @return
	 */
	public static org.eclipse.swt.graphics.Image getImage(String imageUrl) {
		URL url;
		try {
			url = new URL(IVCPlugin.plugin.baseURL, imageUrl);

			return ImageDescriptor.createFromURL(url).createImage();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ImageData getImageData(String imageUrl) {
		URL url;
		try {
			url = new URL(IVCPlugin.plugin.baseURL, imageUrl);

			return ImageDescriptor.createFromURL(url).createImage().getImageData();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
