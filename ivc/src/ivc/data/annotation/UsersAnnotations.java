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

	public void setAnnotations(String user, List<Integer> lineNumbers) {
		if (annotations.containsKey(user)) {
			annotations.remove(user);
		}
		annotations.put(user, lineNumbers);
	}

	public void addAnnotation(String user, Integer lineNumber) {
		if (!annotations.containsKey(user)) {
			ArrayList<Integer> lines = new ArrayList<Integer>();
			lines.add(lineNumber);
			annotations.put(user, lines);
		} else {
			if (!annotations.get(user).contains(lineNumber)) {
				annotations.get(user).add(lineNumber);
			}
		}
	}

	public void clearAnnotations(String user) {
		if (annotations.containsKey(user)) {
			annotations.remove(user);
		}
	}

	public List<Integer> getPositions() {
		ArrayList<Integer> lines = new ArrayList<Integer>();
		for (String user : annotations.keySet()) {
			for (int line : annotations.get(user)) {
				if (!lines.contains(line)) {
					lines.add(line);
				}
			}
		}
		return lines;
	}

	public HashMap<Integer, ArrayList<String>> getAnnotationsUsersForAllLines() {
		HashMap<Integer, ArrayList<String>> result = new HashMap<Integer, ArrayList<String>>();
		for (String user : annotations.keySet()) {
			for (int line : annotations.get(user)) {
				if (result.containsKey(line)) {
					result.get(line).add(user);
				} else {
					ArrayList<String> users = new ArrayList<String>();
					users.add(user);
					result.put(line, users);
				}
			}
		}
		return result;
	}

	public Set<String> getUsers() {
		return annotations.keySet();
	}
}