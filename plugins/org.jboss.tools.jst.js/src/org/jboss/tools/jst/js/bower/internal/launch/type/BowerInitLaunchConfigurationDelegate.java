/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.js.bower.internal.launch.type;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.internal.resources.Container;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.jboss.tools.jst.js.bower.BowerJson;
import org.jboss.tools.jst.js.bower.internal.BowerConstants;
import org.jboss.tools.jst.js.bower.internal.launch.BowerLaunchConstants;
import org.jboss.tools.jst.js.bower.internal.util.BowerUtil;
import org.jboss.tools.jst.js.util.WorkbenchResourceUtil;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
@SuppressWarnings("restriction")
public class BowerInitLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {
	public final static String ID = "org.jboss.tools.jst.js.bower.bowerLaunchConfigurationType"; //$NON-NLS-1$

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		String dir = configuration.getAttribute(BowerLaunchConstants.ATTR_BOWER_DIR, ""); //$NON-NLS-1$
		String name = configuration.getAttribute(BowerLaunchConstants.ATTR_BOWER_NAME, ""); //$NON-NLS-1$
		String version = configuration.getAttribute(BowerLaunchConstants.ATTR_BOWER_VERSION, ""); //$NON-NLS-1$
		String license = configuration.getAttribute(BowerLaunchConstants.ATTR_BOWER_LICENSE, ""); //$NON-NLS-1$
		List<String> authors = configuration.getAttribute(BowerLaunchConstants.ATTR_BOWER_AUTHORS, new ArrayList<String>());
		List<String> ignore = configuration.getAttribute(BowerLaunchConstants.ATTR_BOWER_IGNORE, new ArrayList<String>());
				
		IContainer root = ResourcesPlugin.getWorkspace().getRoot().getContainerForLocation(new Path(dir));
		if (root != null && root.exists()) {
			IFile file = ((Container) root).getFile(BowerConstants.BOWER_JSON);
			if (!file.exists()) {
				BowerJson bowerJson = new BowerJson.Builder().name(name).version(version)
						.authrors(authors).license(license)
						.ignore(ignore).build();
				String json = BowerUtil.generateJson(bowerJson);
				WorkbenchResourceUtil.createFile(file, json);
				WorkbenchResourceUtil.openInEditor(file);
			}
		} else {
			// TODO: need to handle that case - Error Dialog
		}
	}
}
