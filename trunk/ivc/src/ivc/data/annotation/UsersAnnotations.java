package ivc.data.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * The class contains a hashMap of users and positions where the users modified resources
 * 
 * @author alexm
 * 
 */
public class UsersAnnotations {
	private HashMap<String, List<Integer>> annotations;

	public UsersAnnotations() {
		annotations = new HashMap<String, List<Integer>>();
	}

	/**
	 * sets the annotations
	 * 
	 * @param user
	 *            the user
	 * @param postitions
	 *            a list of positions where the user modified the resource
	 */
	public void setAnnotations(String user, List<Integer> postitions) {
		if (annotations.containsKey(user)) {
			annotations.remove(user);
		}
		annotations.put(user, postitions);
	}

	/**
	 * adds an annotation for a user at the specified position
	 * 
	 * @param user
	 *            the user
	 * @param position
	 *            the position where the modification was made
	 */
	public void addAnnotation(String user, Integer position) {
		if (!annotations.containsKey(user)) {
			ArrayList<Integer> positions = new ArrayList<Integer>();
			positions.add(position);
			annotations.put(user, positions);
		} else {
			if (!annotations.get(user).contains(position)) {
				annotations.get(user).add(position);
			}
		}
	}

	/**
	 * Clears all annotations for the user
	 * 
	 * @param user
	 *            the user
	 */
	public void clearAnnotations(String user) {
		if (annotations.containsKey(user)) {
			annotations.remove(user);
		}
	}

	/**
	 * Returns a list of all positions modified on the resource
	 * 
	 * @return a list of all positions modified on the resource
	 */
	public List<Integer> getPositions() {
		ArrayList<Integer> positions = new ArrayList<Integer>();
		for (String user : annotations.keySet()) {
			for (int position : annotations.get(user)) {
				if (!positions.contains(position)) {
					positions.add(position);
				}
			}
		}
		return positions;
	}

	/**
	 * Returns a map in which we have the position of a modification and a list of all
	 * users which modified the resource at the speciffied position
	 * 
	 * @return a map in which we have the position of a modification and a list of all
	 *         users which modified the resource at the speciffied position
	 */
	public HashMap<Integer, ArrayList<String>> getAnnotationsUsersForAllPositions() {
		HashMap<Integer, ArrayList<String>> result = new HashMap<Integer, ArrayList<String>>();
		for (String user : annotations.keySet()) {
			for (int position : annotations.get(user)) {
				if (result.containsKey(position)) {
					result.get(position).add(user);
				} else {
					ArrayList<String> users = new ArrayList<String>();
					users.add(user);
					result.put(position, users);
				}
			}
		}
		return result;
	}

	/**
	 * Returns the list of users which modified the resource
	 * 
	 * @return
	 */
	public Set<String> getUsers() {
		return annotations.keySet();
	}
}