/**
 * 
 */
package ivc.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author danielan
 *
 */
public class NetworkUtils {
	
	/**
	 * Returns ip address of local host
	 * @return
	 */
	public static String getHostAddress() {
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			// HTMLLogger.warn(Exceptions.UNABLE_TO_READ_HOST);
		}
		if (addr != null) {
			return addr.getHostAddress();
		}
		return "localhost";
	}

}
