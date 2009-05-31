package ivc.fireworks.decorators;

import ivc.plugin.ImageDescriptorManager;

import java.util.Vector;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

/**
 * @author iilea
 * 
 *         Class is used for overlaying image icons
 */
@SuppressWarnings("unchecked")
public class OverlayImageIcon extends CompositeImageDescriptor {
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
	private Vector<ImageIcon> images;

	/**
	 * Constructor for overlayImageIcon.
	 */
	public OverlayImageIcon(Image baseImage, Vector images) {
		// Base image of the object
		this.baseImage = baseImage;
		// Demo Image Object
		this.images = images;
		sizeOfImage = new Point(baseImage.getBounds().width, baseImage.getBounds().height);
	}

	/**
	 * @see org.eclipse.jface.resource.CompositeImageDescriptor#drawCompositeImage(int,
	 *      int) DrawCompositeImage is called to draw the composite image.
	 * 
	 */
	protected void drawCompositeImage(int width, int height) {
		// Draw the base image
		drawImage(baseImage.getImageData(), 0, 0);

		for (int i = 0; i < images.size(); i++) {
			ImageIcon icon=images.get(i);
			ImageData imageData = ImageDescriptorManager.getImageData(icon.path);
			Position position=icon.position;
			switch (position) {
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
				drawImage(imageData, sizeOfImage.x - imageData.width, sizeOfImage.y - imageData.height);
				break;

			}
		}

	}


	/**
	 * @see org.eclipse.jface.resource.CompositeImageDescriptor#getSize() Get
	 *      the size of the object
	 */
	protected Point getSize() {
		return sizeOfImage;
	}

	/**
	 * Get the image formed by overlaying different images on the base image
	 * 
	 * @return composite image
	 */
	public Image getImage() {
		return createImage();
	}

}
