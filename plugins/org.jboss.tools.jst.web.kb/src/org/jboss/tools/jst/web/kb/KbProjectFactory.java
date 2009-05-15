package org.jboss.tools.jst.web.kb;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.jst.web.WebModelPlugin;

public class KbProjectFactory {

	/**
	 * Factory method creating seam project instance by project resource.
	 * Returns null if 
	 * (1) project does not exist 
	 * (2) project is closed 
	 * (3) project has no seam nature
	 * (4) creating seam project failed.
	 * @param project
	 * @param resolve if true and results of last build have not been resolved they are loaded.
	 * @return
	 */
	public static IKbProject getSeamProject(IProject project, boolean resolve) {
		if(project == null || !project.exists() || !project.isOpen()) return null;
		try {
			if(!project.hasNature(IKbProject.NATURE_ID)) return null;
		} catch (CoreException e) {
			//ignore - all checks are done above
			return null;
		}

		IKbProject kbProject;
			try {
				kbProject = (IKbProject)project.getNature(IKbProject.NATURE_ID);
				if(resolve) kbProject.resolve();
				return kbProject;
			} catch (CoreException e) {
				WebModelPlugin.getPluginLog().logError(e);
			}
		return null;
	}

}
