package ivc.fireworks.decorators;

/**
 * 
 * @author alexm
 * 
 *         The class provides a mapping between an image path and the position where the
 *         image should be added
 */
public class ImageIcon {
	public Position position;
	public String path;

	/**
	 * Creates the image icon and sets the path and the position
	 * 
	 * @param path
	 *            the path of the image
	 * @param position
	 *            the position where the image should be drawn
	 */
	public ImageIcon(String path, Position position) {
		this.path = path;
		this.position = position;
	}
}
