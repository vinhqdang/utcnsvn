package ivc.plugin;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;

public class ImageDescriptorManager {
	public static final String SHARE_WIZARD = "share_icon.png";
	public static final String DCORATOR_SHARED = "shared.png";
	public static final String DCORATOR_CHANGED = "modified.png";
	public static final String DCORATOR_ADDED = "added.gif";
	public static final String DCORATOR_ = "";
	public static final String MARKER = "shared.png";
	public static final String TREE_FLAT = "tree_flat.png";
	public static final String TREE_AFFECTED_PATHS = "tree.png";
	public static final String TREE_AFFECTED_PATHS_COMPRESSED = "tree_compressed.gif";

	public static ImageDescriptor getImageDescriptor(String imageUrl) {
		URL url;
		try {
			url = new URL(IVCPlugin.plugin.baseURL, imageUrl);

			return ImageDescriptor.createFromURL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static org.eclipse.swt.graphics.Image getImage(String imageUrl) {
		URL url;
		try {
			url = new URL(IVCPlugin.plugin.baseURL, imageUrl);

			return ImageDescriptor.createFromURL(url).createImage();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
