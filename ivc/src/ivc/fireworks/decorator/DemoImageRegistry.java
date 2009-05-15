package ivc.fireworks.decorator;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * @author iilea
 *
 * Image registry for Resources. This class is a utility class to get
 * the image given the image key. 
 * 
 */ 
public class DemoImageRegistry
{
  /**
   * An image registry that maintains a mapping between symbolic image names
   * and SWT image objects. The images maintained here will be used for resource decoration. 	
   */
  private ImageRegistry imageRegistry;

	/**
	 * Constructor for DemoImageRegistry.
	 */
	public DemoImageRegistry()
	{
		if (imageRegistry == null)
    {
      imageRegistry = new ImageRegistry();
    }
	}
  
  /**
   * Get the image from image registry given the key
   *
   * @param key Image key
   * @return Image
   */  
  public Image getImage(String key)
  {
    return imageRegistry.get(key);
  }
  
  /**
   * Associate the image with image key
   * 
   * @param key Image key
   * @param image Image to be associated with image key
   * 
   */ 
  public void setImage(String key, Image image)
  {
	// store into the ImageRegistry a key and its corresponding image  
    imageRegistry.put(key, image);
  }
}

