
package ivc.util;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;

public class IVCSecurityManager extends SecurityManager  {
	public IVCSecurityManager() {
	}
	public void checkPermission() {
	}
	public void checkPermission(Permission perm) {
	}
	public void checkPermission(Permission perm, Object context) {
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkAccept(java.lang.String, int)
	 */
	@Override
	public void checkAccept(String arg0, int arg1) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkAccess(java.lang.Thread)
	 */
	@Override
	public void checkAccess(Thread arg0) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkAccess(java.lang.ThreadGroup)
	 */
	@Override
	public void checkAccess(ThreadGroup arg0) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkAwtEventQueueAccess()
	 */
	@Override
	public void checkAwtEventQueueAccess() {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkConnect(java.lang.String, int, java.lang.Object)
	 */
	@Override
	public void checkConnect(String arg0, int arg1, Object arg2) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkConnect(java.lang.String, int)
	 */
	@Override
	public void checkConnect(String arg0, int arg1) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkCreateClassLoader()
	 */
	@Override
	public void checkCreateClassLoader() {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkDelete(java.lang.String)
	 */
	@Override
	public void checkDelete(String arg0) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkExec(java.lang.String)
	 */
	@Override
	public void checkExec(String arg0) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkExit(int)
	 */
	@Override
	public void checkExit(int arg0) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkLink(java.lang.String)
	 */
	@Override
	public void checkLink(String arg0) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkListen(int)
	 */
	@Override
	public void checkListen(int arg0) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkMemberAccess(java.lang.Class, int)
	 */
	@Override
	public void checkMemberAccess(Class<?> arg0, int arg1) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkMulticast(java.net.InetAddress, byte)
	 */
	@Override
	public void checkMulticast(InetAddress arg0, byte arg1) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkMulticast(java.net.InetAddress)
	 */
	@Override
	public void checkMulticast(InetAddress arg0) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkPackageAccess(java.lang.String)
	 */
	@Override
	public void checkPackageAccess(String arg0) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkPackageDefinition(java.lang.String)
	 */
	@Override
	public void checkPackageDefinition(String arg0) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkPrintJobAccess()
	 */
	@Override
	public void checkPrintJobAccess() {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkPropertiesAccess()
	 */
	@Override
	public void checkPropertiesAccess() {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkPropertyAccess(java.lang.String)
	 */
	@Override
	public void checkPropertyAccess(String arg0) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkRead(java.io.FileDescriptor)
	 */
	@Override
	public void checkRead(FileDescriptor arg0) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkRead(java.lang.String, java.lang.Object)
	 */
	@Override
	public void checkRead(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkRead(java.lang.String)
	 */
	@Override
	public void checkRead(String arg0) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkSecurityAccess(java.lang.String)
	 */
	@Override
	public void checkSecurityAccess(String arg0) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkSetFactory()
	 */
	@Override
	public void checkSetFactory() {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkSystemClipboardAccess()
	 */
	@Override
	public void checkSystemClipboardAccess() {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkTopLevelWindow(java.lang.Object)
	 */
	@Override
	public boolean checkTopLevelWindow(Object arg0) {
		// TODO Auto-generated method stub
		return super.checkTopLevelWindow(arg0);
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkWrite(java.io.FileDescriptor)
	 */
	@Override
	public void checkWrite(FileDescriptor arg0) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkWrite(java.lang.String)
	 */
	@Override
	public void checkWrite(String arg0) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#classDepth(java.lang.String)
	 */
	@Override
	protected int classDepth(String arg0) {
		// TODO Auto-generated method stub
		return super.classDepth(arg0);
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#classLoaderDepth()
	 */
	@Override
	protected int classLoaderDepth() {
		// TODO Auto-generated method stub
		return super.classLoaderDepth();
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#currentClassLoader()
	 */
	@Override
	protected ClassLoader currentClassLoader() {
		// TODO Auto-generated method stub
		return super.currentClassLoader();
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#currentLoadedClass()
	 */
	@Override
	protected Class<?> currentLoadedClass() {
		// TODO Auto-generated method stub
		return super.currentLoadedClass();
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#getClassContext()
	 */
	@Override
	protected Class[] getClassContext() {
		// TODO Auto-generated method stub
		return super.getClassContext();
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#getInCheck()
	 */
	@Override
	public boolean getInCheck() {
		// TODO Auto-generated method stub
		return super.getInCheck();
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#getSecurityContext()
	 */
	@Override
	public Object getSecurityContext() {
		// TODO Auto-generated method stub
		return super.getSecurityContext();
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#getThreadGroup()
	 */
	@Override
	public ThreadGroup getThreadGroup() {
		// TODO Auto-generated method stub
		return super.getThreadGroup();
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#inClass(java.lang.String)
	 */
	@Override
	protected boolean inClass(String arg0) {
		// TODO Auto-generated method stub
		return super.inClass(arg0);
	}
	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#inClassLoader()
	 */
	@Override
	protected boolean inClassLoader() {
		// TODO Auto-generated method stub
		return super.inClassLoader();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		return super.equals(arg0);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
}



