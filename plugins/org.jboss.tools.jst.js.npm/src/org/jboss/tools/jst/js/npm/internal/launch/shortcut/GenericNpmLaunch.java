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
package org.jboss.tools.jst.js.npm.internal.launch.shortcut;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ide.ResourceUtil;
import org.jboss.tools.jst.js.node.exception.NodeExceptionNotifier;
import org.jboss.tools.jst.js.node.launch.shortcut.GenericNativeNodeLaunch;
import org.jboss.tools.jst.js.node.util.NodeExternalUtil;
import org.jboss.tools.jst.js.npm.NpmPlugin;
import org.jboss.tools.jst.js.npm.internal.NpmConstants;
import org.jboss.tools.jst.js.npm.internal.ui.NpmExceptionNotifier;
import org.jboss.tools.jst.js.npm.util.NpmUtil;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public abstract class GenericNpmLaunch extends GenericNativeNodeLaunch {
	
	@Override
	protected abstract String getCommandName();

	@Override
	protected abstract String getLaunchName();
	
	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			 Object element = ((IStructuredSelection)selection).getFirstElement();
			 if (element != null && element instanceof IResource) {
				try {
					IResource selectedResource = (IResource) element;
					launchNpm(selectedResource);
				} catch (CoreException e) {
					NpmPlugin.logError(e);
				}
			 }
		}
	}
	
	@Override
	public void launch(IEditorPart editor, String mode) {
		IEditorInput editorInput = editor.getEditorInput();
		IFile file = ResourceUtil.getFile(editorInput);
		if (file != null && file.exists() && NpmConstants.PACKAGE_JSON.equals(file.getName())) {
			try {
				launchNpm(file);
			} catch (CoreException e) {
				NpmPlugin.logError(e);
			}
		}
	}
	
	@Override
	protected String getWorkingDirectory(IResource resource) throws CoreException {
		String workingDir = null;
		if (resource != null && resource.exists()) {
			if (resource.getType() == IResource.FILE && NpmConstants.PACKAGE_JSON.equals(resource.getName())) {
				workingDir = resource.getParent().getFullPath().toOSString();
			} else if (resource.getType() == IResource.FOLDER) {
				workingDir = resource.getFullPath().toOSString();
			} else if (resource.getType() == IResource.PROJECT) {
				IProject project = (IProject) resource;
				IFile file = project.getFile(NpmConstants.PACKAGE_JSON);
				if (file.exists()) {
					workingDir = resource.getFullPath().toOSString();
				} else {
					// Trying to find package.json file ignoring "node_modules" (default modules dir that can not be changed)
					workingDir = NpmUtil.getNpmWorkingDir(project, NpmConstants.NODE_MODULES);
				}
			}
		}
		return workingDir;
	}
	
	private void launchNpm(IResource resource) throws CoreException {
		String nodeLocation = NodeExternalUtil.getNodeExecutableLocation();
		String npmLocation = NpmUtil.getNpmExecutableLocation();
		if (nodeLocation == null || nodeLocation.isEmpty()) {
			NodeExceptionNotifier.nodeLocationNotDefined();
		} else if (npmLocation == null || npmLocation.isEmpty()) {
			NpmExceptionNotifier.npmLocationNotDefined();
		} else {
			this.setWorkingProject(resource.getProject());
			launchNodeTool(getWorkingDirectory(resource), nodeLocation, npmLocation);
		}
	}

}
