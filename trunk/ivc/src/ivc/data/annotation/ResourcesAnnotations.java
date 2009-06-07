package ivc.data.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IResource;

/**
 * This class contains all annotations by users for all shared resources in a project
 * 
 * @author alexm
 * 
 */
public class ResourcesAnnotations {
	public HashMap<String, UsersAnnotations> annotations;

	public ResourcesAnnotations() {
		annotations = new HashMap<String, UsersAnnotations>();
	}

	/**
	 * Creates a new UsersAnnotations object and adds the supplied characters to the annotations
	 * 
	 * @param filePath
	 *            the path of the file to which to add the annotations
	 * @param userName
	 *            the name of the user who commited the annotations
	 * @param charPositions
	 *            the positions of the characters
	 */
	public void setAnnotations(String filePath, String userName, List<Integer> charPositions) {
		UsersAnnotations userAnnot = new UsersAnnotations();
		if (annotations.containsKey(filePath)) {
			userAnnot = annotations.get(filePath);
		} else {
			annotations.put(filePath, userAnnot);
		}
		userAnnot.setAnnotations(userName, charPositions);
	}

	/**
	 * Gets the key used for a resource
	 * 
	 * @param resource
	 *            the resource for which to get the key
	 * @return the string key of the resource
	 */
	public static String key(IResource resource) {
		return resource.getProjectRelativePath().toOSString();
	}

	/**
	 * Returne the users annotations for a given resource
	 * 
	 * @param resource
	 *            the given resource
	 * @return a UsersAnnotations object
	 */
	public UsersAnnotations getAnnotations(IResource resource) {
		String key = key(resource);
		if (!annotations.containsKey(key)) {
			return null;
		}
		return annotations.get(key);
	}

	/**
	 * Returns a map of characters as keys and a list of usernames as values for a given resource.
	 * 
	 * @param resource
	 *            the resource
	 * @return the users who modiffied the characters
	 */
	public HashMap<Integer, ArrayList<String>> getAnnotationsUsersForAllLines(IResource resource) {
		if (!annotations.containsKey(key(resource))) {
			return null;
		}
		return annotations.get(key(resource)).getAnnotationsUsersForAllLines();
	}

	/**
	 * Removes all annotations for the given file path
	 * 
	 * @param filePath
	 *            the path of the file to remove the annotations
	 */
	public void removeAnnotations(String filePath) {
		if (annotations.containsKey(filePath)) {
			annotations.remove(filePath);
		}
	}

	/**
	 * returns the Annotations map
	 * 
	 * @return The annotations map
	 */
	public HashMap<String, UsersAnnotations> getAnnotationsMap() {
		return annotations;
	}
}