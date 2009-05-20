/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.jst.web.WebModelPlugin;

public class KbProjectFactory {

	/**
	 * Factory method creating kb project instance by project resource.
	 * Returns null if 
	 * (1) project does not exist 
	 * (2) project is closed 
	 * (3) project has no kb nature
	 * (4) creating kb project failed.
	 * @param project
	 * @param resolve if true and results of last build have not been resolved they are loaded.
	 * @return
	 */
	public static IKbProject getKbProject(IProject project, boolean resolve) {
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
