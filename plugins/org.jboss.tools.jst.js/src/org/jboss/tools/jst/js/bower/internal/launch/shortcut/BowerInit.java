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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.internal.resources.Container;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.jst.js.bower.BowerCommands;
import org.jboss.tools.jst.js.bower.internal.BowerConstants;
import org.jboss.tools.jst.js.bower.internal.BowerJsonGenerator;
import org.jboss.tools.jst.js.bower.launch.GenericBowerLaunch;
import org.jboss.tools.jst.js.internal.Activator;
import org.jboss.tools.jst.js.internal.util.EditorUtil;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
@SuppressWarnings("restriction")
public class BowerInit extends GenericBowerLaunch {
	private static final String LAUNCH_NAME = "Bower Init"; //$NON-NLS-1$
	private Container root;

	@Override
	protected String getCommandName() {
		return BowerCommands.INIT.getValue() + " --config.interactive"; //$NON-NLS-1$
	}

	@Override
	protected String getLaunchName() {
		return LAUNCH_NAME;
	}

	@Override
	protected String getWorkingDirectory(IResource resource) throws CoreException {
		if (resource != null && resource.exists() && resource instanceof Container) {
			this.root = (Container) resource;
			return resource.getFullPath().toOSString();
		}
		return null;
	}

	@Override
	protected void execute(String workingDirectory, String nodeExecutableLocation, String bowerExecutableLocation) {
		try {
			String name = root.getProject().getName();
			IFile file = this.root.getFile(BowerConstants.BOWER_JSON);
			if (!file.exists()) {
				String bowerJson = BowerJsonGenerator.generateDefault(name);
				InputStream source = new ByteArrayInputStream(bowerJson.getBytes());
				file.create(source, IResource.NONE, null);
				EditorUtil.openInEditor(file);
			}
		} catch (CoreException e) {
			Activator.logError(e);
		}
	}
	
}