package ivc.data.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class UsersAnnotations {
	private HashMap<String, List<Integer>> annotations;

	public UsersAnnotations() {
		annotations = new HashMap<String, List<Integer>>();
	}

	public void setAnnotations(String user, List<Integer> postitions) {
		if (annotations.containsKey(user)) {
			annotations.remove(user);
		}
		annotations.put(user, postitions);
	}

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

	public void clearAnnotations(String user) {
		if (annotations.containsKey(user)) {
			annotations.remove(user);
		}
	}

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

	public Set<String> getUsers() {
		return annotations.keySet();
	}
}