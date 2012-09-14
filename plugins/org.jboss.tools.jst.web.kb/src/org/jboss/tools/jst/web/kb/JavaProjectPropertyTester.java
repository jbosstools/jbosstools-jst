/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.web.kb;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;

public class JavaProjectPropertyTester extends PropertyTester {

	public JavaProjectPropertyTester() {
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		IJavaProject prj = (IJavaProject)receiver;
		boolean result = false;
		IProject eclproj = prj.getProject();
		try {
			if(eclproj.isAccessible()) {
				result = eclproj.hasNature(expectedValue.toString());
			}
		} catch (CoreException e) {
			WebKbPlugin.getDefault().logError(e);
		}
		return result;
	}

}
