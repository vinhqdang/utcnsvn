package ivc.fireworks.decorators;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;

/**
 * @author 
 * 
 * Provides methods for handling the set of images that are used for decorating resources 
 */
public class DemoImages
{
  /**
   * Image descriptor for lock.gif image
   */ 
  private static final ImageDescriptor lockDescriptor = ImageDescriptor.
    createFromFile(DemoImages.class, "lock.gif");
  
  /**
   * Constructor 
   */
  public DemoImages() {
    super();
  }

  /**
   * Get the image data of the image with key "Lock" 
   * 
   * @return image data for the lock flag
   */   
  public ImageData getLockImageData() {
    return lockDescriptor.getImageData();
  }
  
  /**
   * Get the image data depending on the key
   * 
   * @param imageKey Key of the Image to be returned
   * @return image data 
   * 
   */ 
  public ImageData getImageData(String imageKey) {
	  
	// if the key of the image is "Lock"  
    if (imageKey.equals("Lock")) {
      // return the Image whose key is Lock	
      return getLockImageData();
    }
    return null;
  }
}

