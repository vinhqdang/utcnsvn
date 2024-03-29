package ivc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A helper class for various string operations
 * 
 */
public class StringUtils {

	public static synchronized String readFromInputStream(InputStream stream) {
		StringBuilder build = new StringBuilder();
		try {
			BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
			String line;
			while ((line = buffer.readLine()) != null) {
				build.append(line);
				build.append("\n");
			}
		} catch (IOException e) {
			System.out.println("IOEXception trying reading stream");
		}
		return build.toString();

	}

	/**
	 * 
	 * 
	 * @param str
	 * @param separator
	 * @return an array of string segments
	 */
	static public String[] split(String str, char separator) {
		int pos = 0;
		List<String> list = new ArrayList<String>();
		int length = str.length();
		for (int i = 0; i < length; i++) {
			char ch = str.charAt(i);
			if (ch == separator) {
				list.add(str.substring(pos, i));
				pos = i + 1;
			}
		}
		if (pos != length) {
			list.add(str.substring(pos, length));
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * split using a string separator
	 * 
	 * @param str
	 * @param separator
	 * @return an array of string segments
	 */
	static public String[] split(String str, String separator) {
		List<String> list = new ArrayList<String>();
		StringBuffer sb = new StringBuffer(str);
		int pos;

		while ((pos = sb.indexOf(separator)) != -1) {
			list.add(sb.substring(0, pos));
			sb.delete(0, pos + separator.length());
		}
		if (sb.length() > 0) {
			list.add(sb.toString());
		}
		return list.toArray(new String[list.size()]);
	}

	
	public static String stripStart(String str, String stripChars) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		int start = 0;
		if (stripChars == null) {
			while ((start != strLen) && Character.isWhitespace(str.charAt(start))) {
				start++;
			}
		} else if (stripChars.length() == 0) {
			return str;
		} else {
			while ((start != strLen) && (stripChars.indexOf(str.charAt(start)) != -1)) {
				start++;
			}
		}
		return str.substring(start);
	}

}
