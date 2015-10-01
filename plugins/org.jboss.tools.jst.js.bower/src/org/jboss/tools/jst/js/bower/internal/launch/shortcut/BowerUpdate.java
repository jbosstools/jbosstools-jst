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

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.jboss.tools.jst.js.bower.Activator;
import org.jboss.tools.jst.js.bower.BowerCommands;
import org.jboss.tools.jst.js.bower.internal.BowerConstants;
import org.jboss.tools.jst.js.bower.internal.ui.BowerExceptionNotifier;
import org.jboss.tools.jst.js.bower.util.BowerUtil;
import org.jboss.tools.jst.js.node.exception.NodeExceptionNotifier;
import org.jboss.tools.jst.js.node.launch.shortcut.GenericNativeNodeLaunch;
import org.jboss.tools.jst.js.node.util.NodeExternalUtil;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class BowerUpdate extends GenericNativeNodeLaunch {
	private static final String LAUNCH_NAME = "Bower Update"; //$NON-NLS-1$
	
	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			 Object element = ((IStructuredSelection)selection).getFirstElement();
			 if (element != null && element instanceof IResource) {
				try {
					IResource selectedResource = (IResource) element;
					String nodeLocation = NodeExternalUtil.getNodeExecutableLocation();
					String bowerLocation = BowerUtil.getBowerExecutableLocation();
					if (nodeLocation == null || nodeLocation.isEmpty()) {
						NodeExceptionNotifier.nodeLocationNotDefined();
					} else if (bowerLocation == null || bowerLocation.isEmpty()) {
						BowerExceptionNotifier.bowerLocationNotDefined();
					} else {
						this.setWorkingProject(selectedResource.getProject());
						execute(getWorkingDirectory(selectedResource), nodeLocation, bowerLocation);						
					}
				} catch (CoreException e) {
					Activator.logError(e);
				}
			 }
		}
	}

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
				if (file.exists()) {
					workingDir = resource.getFullPath().toOSString();
				} else {
					try {
						workingDir = getWorkingDirectory(project);
					} catch (IOException e) {
						Activator.logError(e);
					}
				}
			}
		}
		return workingDir;
	}

	/**
	 * Detects working directory for bower execution depending on .bowerrc file
	 * @throws CoreException 
	 * @throws UnsupportedEncodingException
	 * @see <a href="http://bower.io/docs/config/">Bower Configuration</a>
	 */
	private String getWorkingDirectory(IProject project) throws CoreException, UnsupportedEncodingException {
		String workingDir = null;
		IFile bowerrc = BowerUtil.getBowerrc(project);
		if (bowerrc != null) {
			IContainer parent = bowerrc.getParent();
			if (parent.exists() && parent.findMember(BowerConstants.BOWER_JSON) != null) {
				workingDir = parent.getFullPath().toOSString();
			} else {
				String directoryName = BowerUtil.getDirectoryName(bowerrc);
				directoryName = (directoryName != null) ? directoryName : BowerConstants.BOWER_COMPONENTS;
				workingDir = BowerUtil.getBowerWorkingDir(project, directoryName);
			}
		} else {
			// Trying to find bower.json file ignoring "bower_components"
			// (default components directory)
			workingDir = BowerUtil.getBowerWorkingDir(project, BowerConstants.BOWER_COMPONENTS);
		}
		return workingDir;
	}
	
}
