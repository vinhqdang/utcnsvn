package ivc.repository;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.team.core.RepositoryProvider;

public class IVCRepositoryProvider extends RepositoryProvider {

	@Override
	public void configureProject() throws CoreException {
		IProject proj=getProject();
		System.out.println("Starting project configuration");
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deconfigure() throws CoreException {
		// TODO Auto-generated method stub
		
	}

}
