package ivc.fireworks.decorators;

import ivc.managers.ImageDescriptorManager;
import ivc.managers.ProjectsManager;
import ivc.plugin.IVCPlugin;
import ivc.repository.IVCRepositoryProvider;
import ivc.repository.Status;

import java.util.ArrayList;
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
 * @author alexm
 * 
 *         The class is used to decorate the resources
 * 
 */
@SuppressWarnings("unchecked")
public class Decorator extends LabelProvider implements ILabelDecorator {
	public static final String ID = "ivc.fireworks.decorators.Decorator";
	/**
	 * Flag indicating whether decorations are enabled or not true - decorations are
	 * enabled(resources can be decorated) false - decorations are not enabled(resources
	 * cannot be decorated)
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
	 * @return Returns a reference to Decorator, if it is enabled
	 */
	public static Decorator getDecorator() {

		// get the decorator manager(which manages the decorators contributed
		// via the decorators extension point)
		IDecoratorManager decoratorManager = IVCPlugin.getDefault().getWorkbench()
				.getDecoratorManager();

		// if the decorator manager is enabled
		if (decoratorManager.getEnabled(Decorator.ID)) {

			// return the label decorator which applies the decorations from all
			// enabled decorators
			return (Decorator) decoratorManager.getLabelDecorator(Decorator.ID);
		}

		return null;
	}

	/**
	 * Used to provide image decorations for resources
	 * 
	 * @param baseImage
	 *            Base image with which the object to be decorated is already decorated
	 *            with Eclipse. Our decoration will be added to this decoration.
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
			if (object instanceof IResource) {

				IResource resource = (IResource) object;
				if (IVCRepositoryProvider.isShared(resource.getProject())) {

					try {
						Status status = ProjectsManager.instance().getStatus(resource);
						decoratorImageKeys = findDecorationImage(status);

						if (decoratorImageKeys.size() != 0) {
							image = drawIconImage(baseImage, decoratorImageKeys);
							return image;

						} else
							return null;
					} catch (Exception e) {
						System.out.println("Error decorating image");
					}
				}

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
	 * @return label with which the object should be decorated. If null is returned, then
	 *         the object is decorated with default label
	 * 
	 */
	public String decorateText(String label, Object object) {
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
	 * Refresh the resources of the project that are contained in resourcesToBeUpdaterd
	 * List
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
			fireLabelEvent(new LabelProviderChangedEvent(decorator));
		}
	}

	public void refresh(IResource resource) {

		Decorator decorator = getDecorator();
		if (decorator == null) {
			return;
		} else {
			// fire a label provider changed event to decorate the
			// resource whose image needs to be updated
			ArrayList<IResource> resources = new ArrayList();
			resources.add(resource);
			fireLabelEvent(new LabelProviderChangedEvent(decorator, resource));
		}
	}

	/**
	 * Fire a Label Change event so that the label decorators are automatically refreshed.
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
	private static Image drawIconImage(Image baseImage, Vector images) {

		OverlayImageIcon overlayIcon = new OverlayImageIcon(baseImage, images);
		return overlayIcon.getImage();

	}

	/**
	 * Create a vector with the keys of the images that will be used for resource
	 * decoration; here only one image key is used. Image keys consist of the names of the
	 * images.
	 * 
	 * @return A Vector with the keys of the images that will be used for decorating the
	 *         resources of the project.
	 */
	public static Vector findDecorationImage(Status status) {
		// create a new Vector
		Vector images = new Vector();
		// add an image key to the vector
		ImageIcon versioned = new ImageIcon(ImageDescriptorManager.DECORATOR_SHARED,
				Position.BOTTOM_RIGHT);
		images.add(versioned);
		switch (status) {
		case Added:
			images.add(new ImageIcon(ImageDescriptorManager.DECORATOR_ADDED,
					Position.TOP_LEFT));
			break;
		case Modified:
			images.add(new ImageIcon(ImageDescriptorManager.DECORATOR_CHANGED,
					Position.TOP_LEFT));
			break;
		case Deleted:
			images.add(new ImageIcon(ImageDescriptorManager.DECORATOR_DELETED,
					Position.TOP_LEFT));
			break;
		case Unversioned:
			images.add(new ImageIcon(ImageDescriptorManager.DECORATOR_UNVERSIONED,
					Position.TOP_LEFT));
			images.remove(versioned);
			break;
		default:
			break;
		}
		return images;
	}

	/**
	 * returns an image based on the status of the resource
	 * 
	 * @param base
	 *            the base image of the resource
	 * @param status
	 *            the status of the resource
	 * @return the new image of the resource
	 */
	public static Image getImage(Image base, Status status) {
		return drawIconImage(base, findDecorationImage(status));
	}
}
