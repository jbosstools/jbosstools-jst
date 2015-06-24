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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchGroup;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.jst.js.bower.internal.launch.BowerLaunchConstants;
import org.jboss.tools.jst.js.internal.Activator;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class BowerInit implements ILaunchShortcut {

	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object object = structuredSelection.getFirstElement();

			IContainer basedir = null;
			if (object instanceof IProject || object instanceof IFolder) {
				basedir = (IContainer) object;
			}
			try {
				launch(basedir, mode);
			} catch (CoreException e) {
				Activator.logError(e);
			}
		}
	}

	private void launch(IContainer basedir, String mode) throws CoreException {
		ILaunchConfiguration launchConfiguration = getLaunchConfiguration(basedir, mode);
		ILaunchGroup group = DebugUITools.getLaunchGroup(launchConfiguration, mode);
		String groupId = group != null ? group.getIdentifier() : BowerLaunchConstants.ID_EXTERNAL_TOOLS_LAUNCH_GROUP;
		DebugUITools.openLaunchConfigurationDialog(getShell(), launchConfiguration, groupId, null);
	}

	private ILaunchConfiguration getLaunchConfiguration(IContainer basedir, String mode) throws CoreException {
		IPath basedirLocation = basedir.getLocation();
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType launchConfigurationType = launchManager
				.getLaunchConfigurationType(BowerLaunchConstants.LAUNCH_CONFIGURATION_TYPE_ID);
		String name = launchManager.generateLaunchConfigurationName(basedirLocation.lastSegment());
		ILaunchConfigurationWorkingCopy workingCopy = launchConfigurationType.newInstance(null, name);
		workingCopy.setAttribute(BowerLaunchConstants.ATTR_BOWER_DIR, basedirLocation.toString());
		workingCopy.setAttribute(BowerLaunchConstants.ATTR_BOWER_NAME, basedir.getProject().getName());
		return workingCopy;
	}

	@Override
	public void launch(IEditorPart part, String mode) {
	}

	private Shell getShell() {
		return Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
	}
}