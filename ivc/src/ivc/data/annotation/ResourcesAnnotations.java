package ivc.data.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IResource;

public class ResourcesAnnotations {
	public HashMap<String, UsersAnnotations> annotations;

	public ResourcesAnnotations() {
		annotations = new HashMap<String, UsersAnnotations>();
	}

	public void setAnnotations(String filePath, String userName, List<Integer> lineNumbers) {
		UsersAnnotations userAnnot = new UsersAnnotations();
		if (annotations.containsKey(filePath)) {
			userAnnot = annotations.get(filePath);
		} else {
			annotations.put(filePath, userAnnot);
		}
		userAnnot.setAnnotations(userName, lineNumbers);
	}

	public static String key(IResource resource) {
		return resource.getLocation().toOSString();
	}

	public UsersAnnotations getAnnotations(IResource resource) {
		String key = resource.getLocation().toOSString();
		if (!annotations.containsKey(key)) {
			return null;
		}
		return annotations.get(key);
	}

	public HashMap<Integer, ArrayList<String>> getAnnotationsUsersForAllLines(IResource resource) {
		if (!annotations.containsKey(key(resource))) {
			return null;
		}
		return annotations.get(key(resource)).getAnnotationsUsersForAllLines();
	}

	public void removeAnnotations(String filePath) {
		if (annotations.containsKey(filePath)) {
			annotations.remove(filePath);
		}
	}

	public HashMap<String, UsersAnnotations> getAnnotationsMap() {
		return annotations;
	}
}