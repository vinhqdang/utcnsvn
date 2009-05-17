package ivc.plugin;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;

public class ImageDescriptorManager {
	public static final String SHARE_WIZARD = "share_icon.png";

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
}
