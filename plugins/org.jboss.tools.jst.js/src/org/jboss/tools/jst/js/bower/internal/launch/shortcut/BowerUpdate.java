/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *       Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.jst.js.bower.internal.launch.shortcut;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.jst.js.bower.BowerCommands;
import org.jboss.tools.jst.js.bower.internal.BowerConstants;
import org.jboss.tools.jst.js.bower.launch.GenericBowerLaunch;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class BowerUpdate extends GenericBowerLaunch {
	private static final String LAUNCH_NAME = "Bower Update"; //$NON-NLS-1$

	@Override
	protected String getCommandName() {
		return BowerCommands.UPDATE.getValue();
	}

	@Override
	protected String getLaunchName() {
		return LAUNCH_NAME;
	}

	@Override
	protected String getWorkingDirectory(IResource resource) throws CoreException {
		String workingDir = null;
		if (resource != null && resource.exists()) {
			if (resource.getType() == IResource.FILE && BowerConstants.BOWER_JSON.equals(resource.getName())) {
				workingDir = resource.getParent().getFullPath().toOSString();
			} else if (resource.getType() == IResource.FOLDER) {
				workingDir = resource.getFullPath().toOSString();
			} else if (resource.getType() == IResource.PROJECT) {
				IProject project = (IProject) resource;
				IFile file = project.getFile(BowerConstants.BOWER_JSON);
				if (file != null && file.exists()) {
					workingDir = resource.getFullPath().toOSString();
				} else {
					// TODO: Need more logic here - project root does not necessarily contain bower.json
				}
			}
		}
		return workingDir;
	}

}
