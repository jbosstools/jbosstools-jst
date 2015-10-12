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
package org.jboss.tools.jst.js.npm.internal.launch.tester;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.jst.js.npm.NpmPlugin;
import org.jboss.tools.jst.js.npm.util.NpmUtil;
import org.eclipse.core.expressions.PropertyTester;

/**
 * @author Ilya Buziuk (ibuziuk)
 */
public final class NpmLaunchTester extends PropertyTester {
	private static final String IS_NPM_INIT = "isNpmInit"; //$NON-NLS-1$

	public NpmLaunchTester() {
	}

	@Override
	public boolean test(Object receiver, String property, Object[] arg2, Object expectedValue) {
		try {
			if (IS_NPM_INIT.equals(property) && receiver instanceof IResource) {
				IResource resource = (IResource) receiver;
				if (resource instanceof IProject) {
					return NpmUtil.isPackageJsonExist((IProject) resource);
				} else if (resource instanceof IFolder) {
					return NpmUtil.hasPackageJson((IFolder) resource);
				} else if (resource instanceof IFile) {
					return NpmUtil.isPackageJson((IFile) resource);
				}
			}
		} catch (CoreException e) {
			NpmPlugin.logError(e);
		}
		return false;
	}
	
}