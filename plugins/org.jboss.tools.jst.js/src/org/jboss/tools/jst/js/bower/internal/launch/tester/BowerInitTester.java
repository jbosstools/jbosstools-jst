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
package org.jboss.tools.jst.js.bower.internal.launch.tester;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.jst.js.bower.internal.util.BowerUtil;
import org.jboss.tools.jst.js.internal.Activator;

/**
 * @author Ilya Buziuk (ibuziuk)
 */
public final class BowerInitTester extends PropertyTester {
	private static final String IS_BOWER_INIT = "isBowerInit"; //$NON-NLS-1$

	public BowerInitTester() {
	}

	@Override
	public boolean test(Object receiver, String property, Object[] objects, Object expectedValue) {
		try {
			if (IS_BOWER_INIT.equals(property) && receiver instanceof IResource) {
				IProject project = ((IResource) receiver).getProject();
				return BowerUtil.isBowerJsonExist(project);
			}
		} catch (CoreException e) {
			Activator.logError(e);
		}
		return false;
	}
}