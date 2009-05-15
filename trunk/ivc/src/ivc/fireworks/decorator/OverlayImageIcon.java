package ivc.fireworks.decorator;

import java.util.Vector;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

/**
 * @author iilea
 * 
 * Class is used for overlaying image icons
 */
@SuppressWarnings("unchecked")
public class OverlayImageIcon extends CompositeImageDescriptor
{
  /**
   * Base image of the object
   */ 
  private Image baseImage;
  
  /**
   * Size of the base image 
   */ 
  private Point sizeOfImage;
  
  /**
   * Vector of image keys
   */
  private Vector imageKey; 
  
  /**
   * Demo Image instance 
   */
  private DemoImages demoImage;
  
  private static final int TOP_LEFT = 0;
  private static final int TOP_RIGHT = 1;
  private static final int BOTTOM_LEFT = 2;
  private static final int BOTTOM_RIGHT = 3;
  
  /**
   * Constructor for overlayImageIcon.
   */
  public OverlayImageIcon(Image baseImage, 
                          DemoImages demoImage,
                          Vector imageKey)
  {
    // Base image of the object
    this.baseImage = baseImage;
    // Demo Image Object 
    this.demoImage = demoImage;
    this.imageKey = imageKey;
    sizeOfImage = new Point(baseImage.getBounds().width, 
                             baseImage.getBounds().height);
  }

  /**
   * @see org.eclipse.jface.resource.CompositeImageDescriptor#drawCompositeImage(int, int)
   * DrawCompositeImage is called to draw the composite image.
   * 
   */
  protected void drawCompositeImage(int arg0, int arg1)
  {
    // Draw the base image
     drawImage(baseImage.getImageData(), 0, 0); 
     int[] locations = organizeImages();
     for(int i=0; i < imageKey.size(); i++)
     {
        ImageData imageData = demoImage.getImageData((String)imageKey.get(i));
        switch(locations[i])
        {
          // Draw on the top left corner
          case TOP_LEFT:
            drawImage(imageData, 0, 0);
            break;
            
          // Draw on top right corner  
          case TOP_RIGHT:
            drawImage(imageData, sizeOfImage.x - imageData.width, 0);
            break;
            
          // Draw on bottom left  
          case BOTTOM_LEFT:
            drawImage(imageData, 0, sizeOfImage.y - imageData.height);
            break;
            
          // Draw on bottom right corner  
          case BOTTOM_RIGHT:
            drawImage(imageData, sizeOfImage.x - imageData.width,
                      sizeOfImage.y - imageData.height);
            break;
            
        }
     }
   
  }
  
  /**
   * Organize the images. This function scans through the image key and 
   * finds out the location of the images. Here we use only one image(with key "Lock") 
   */ 
  private int [] organizeImages()
  {
    int[] locations = new int[imageKey.size()];
    String imageKeyValue;
    for (int i = 0; i < imageKey.size(); i++)
    {
      imageKeyValue = (String)imageKey.get(i);
      if (imageKeyValue.equals("Lock"))
      {
        // Draw he lock icon in top left corner. 
        locations[i] = TOP_LEFT;
      }
    }
    return locations;
  }
      

  /**
   * @see org.eclipse.jface.resource.CompositeImageDescriptor#getSize()
   * Get the size of the object
   */
  protected Point getSize()
  {
    return sizeOfImage;
  }
  
  /**
   * Get the image formed by overlaying different images on the base image
   * 
   * @return composite image
   */ 
  public Image getImage()
  {
    return createImage();
  }


}






