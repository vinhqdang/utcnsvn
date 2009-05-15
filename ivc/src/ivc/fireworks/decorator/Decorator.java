package ivc.fireworks.decorator;

import ivc.plugin.Activator;

import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IResource;

import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IDecoratorManager;

/**
 * @author Class used to decorate the folders, files and classes.
 * 
 */
@SuppressWarnings("unchecked")
public class Decorator extends LabelProvider implements ILabelDecorator {

	private static DemoImages demoImage = new DemoImages();

	/**
	 * Flag indicating whether decorations are enabled or not true - decorations
	 * are enabled(resources can be decorated) false - decorations are not
	 * enabled(resources cannot be decorated)
	 */
	public static boolean enableDecoration = false;

	/**
	 * Public constructor
	 */
	public Decorator() {
		super();
	}

	/**
	 * 
	 * @return Returns a reference to DemoDecorator, if it is enabled
	 */
	public static Decorator getDecorator() {

		// get the decorator manager(which manages the decorators contributed
		// via the decorators extension point)
		IDecoratorManager decoratorManager = Activator.getDefault()
				.getWorkbench().getDecoratorManager();

		// if the decorator manager is enabled
		if (decoratorManager.getEnabled("ivc.fireworks.decorator.Decorator")) {

			// return the label decorator which applies the decorations from all
			// enabled decorators
			return (Decorator) decoratorManager
					.getLabelDecorator("ivc.fireworks.decorator.Decorator");
		}

		return null;
	}

	/**
	 * Used to provide image decorations for resources
	 * 
	 * @param baseImage
	 *            Base image with which the object to be decorated is already
	 *            decorated with Eclipse. Our decoration will be added to this
	 *            decoration.
	 * @param object
	 *            Object representing the resource to be decorated
	 * 
	 */
	public Image decorateImage(Image baseImage, Object object) {

		Vector decoratorImageKeys = new Vector();
		Image image;

		// decorations are enabled
		if (enableDecoration) {

			// resource to be decorated is a method
			// if (object instanceof IMethod) {
			//
			// IMethod objectMethod = (IMethod) object;
			//
			// if (objectMethod == null) {
			// return null;
			// }
			//
			// if (ResourceManager.getResource(object.toString())) {
			//
			 try {
			 decoratorImageKeys = findDecorationImage();
			
			 if (decoratorImageKeys.size() != 0) {
			 image = drawIconImage(baseImage,decoratorImageKeys);
			 return image;
			 } else return null;
			 } catch (Exception e) {
			 System.out.println("Error decorating image"); }
			 
			// }

			// the object to be decorated is a resource( an instance of
			// IResource)
			if (object instanceof IResource) {
				IResource objectResource = (IResource) object;
				if (objectResource == null) {
					return null;
				}

				// we check if this object needs to be decorated;
				// ResourceManager class maintains a list of object that need to
				// be decorated
				// if (ResourceManager.getResource(objectResource.getFullPath()
				// .toString())) {
				//				
				// // we get the type of this resource
				// int objectType = objectResource.getType();
				//
				// // resource to be decorated is either a project or a folder
				// or a file
				// if (objectType == IResource.PROJECT
				// || objectType == IResource.FOLDER
				// || objectType == IResource.FILE) {
				//
				// try {
				// // we get a vector that contains the images
				// // that will be used in decorating the object passed as
				// parameter
				// decoratorImageKeys = findDecorationImage();
				//
				// if (decoratorImageKeys.size() != 0) {
				// // we get the image that will be used for the decoration of
				// the parameter object
				// image = drawIconImage(baseImage,
				// decoratorImageKeys);
				// return image;
				// } else {
				// return null;
				// }
				// } catch (Exception e) {
				// System.out.println("Error decorating image");
				// }
				//
				// }
				//
				//
				// // classes are not decorated
				// if (objectResource.getClass() == IResource.class) {
				// return null;
				// }
				//
				// }

			}
		}

		return null;
	}

	/**
	 * Used to provide text decorations for resources
	 * 
	 * @param label
	 *            Default label of the object
	 * @param object
	 *            Object representing the resource to be decorated
	 * 
	 * @return label with which the object should be decorated. If null is
	 *         returned, then the object is decorated with default label
	 * 
	 */
	public String decorateText(String label, Object object) {

		// decorations are enabled
		if (enableDecoration) {

			// the object passed as parameter is an IResource
			if (object instanceof IResource) {
				IResource objectResource = (IResource) object;
				if (objectResource == null)
					return null;

				// if the resource to be decorated has been remotely deleted
				// if
				// (ResourceManager.hasDeletedResource(objectResource.getFullPath()
				// .toString())) {
				// // get the type of the resource
				// int objectType = objectResource.getType();
				// // decorate only files
				// if (objectType == IResource.FILE) {
				// try {
				// return label+" [deleted]";
				//
				// } catch (Exception e) {
				// System.out.println("Error decorating image"); }
				// }
				// }
			}
		}

		return null;
	}

	/**
	 * Refresh all the resources in the project
	 */
	public void refreshAll(boolean displayDecoration, boolean displayProject) {
		enableDecoration = displayDecoration;

		Decorator decorator = getDecorator();
		if (decorator == null) {
			return;
		} else {
			decorator.fireLabelEvent(new LabelProviderChangedEvent(decorator));

		}
	}

	/**
	 * Refresh the resources only those of the project that are contained in
	 * resourcesToBeUpdaterd List
	 * 
	 * @param resourcesToBeUpdated
	 *            List of resources whose decorations need to be refreshed
	 */
	public void refresh(List resourcesToBeUpdated) {

		Decorator decorator = getDecorator();
		if (decorator == null) {
			return;
		} else {
			// fire a label provider changed event to decorate the
			// resources whose image needs to be updated
			fireLabelEvent(new LabelProviderChangedEvent(decorator,
					resourcesToBeUpdated.toArray()));
		}
	}

	/**
	 * Fire a Label Change event so that the label decorators are automatically
	 * refreshed.
	 * 
	 * @param event
	 *            LabelProviderChangedEvent
	 */
	private void fireLabelEvent(final LabelProviderChangedEvent event) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				fireLabelProviderChanged(event);
			}
		});

	}

	/**
	 * Function to draw icon image
	 * 
	 * @param baseImage
	 *            base image of the object resource
	 * @param decoratorImageKeys
	 *            vector of image keys
	 * 
	 * @return icon image with which the resource is to be decorated
	 */
	 private Image drawIconImage(Image baseImage, Vector decoratorImageKeys) {
	 Image image;
	 OverlayImageIcon overlayIcon = new OverlayImageIcon(baseImage,
	 demoImage, decoratorImageKeys);
	 image = overlayIcon.getImage();
	 return image;
	 }
	/**
	 * Create a vector with the keys of the images that will be used for
	 * resource decoration; here only one image key is used. Image keys consist
	 * of the names of the images.
	 * 
	 * @return A Vector with the keys of the images that will be used for
	 *         decorating the resources of the project.
	 */
	public static Vector findDecorationImage() {
		// create a new Vector
		Vector qualifiedValue = new Vector();
		String value;
		value = "Lock";
		// add an image key to the vector
		qualifiedValue.add(value);

		return qualifiedValue;
	}
}
