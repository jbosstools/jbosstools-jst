/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.launching.sourcelookup;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.debug.core.breakpoints.JavaStratumLineBreakpoint;
import org.eclipse.jdt.launching.sourcelookup.JavaSourceLocator;

import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.jst.web.WebModelPlugin;

/**
 * @deprecated This class was used for first tomcat luncher. Before Studio 3.0.0
 * see WebSourceLocator 
 */
public class WebServerSourceLocator extends JavaSourceLocator {

    public WebServerSourceLocator(IJavaProject[] projects, boolean includeRequired)	throws CoreException {
		super(projects, includeRequired);
	}

	public Object getSourceElement(IStackFrame stackFrame) {
		if(ModelPlugin.isDebugEnabled()) {			
			WebModelPlugin.getPluginLog().logInfo("WebServerSourceLocator.getSourceElement()"); //$NON-NLS-1$
		}

		Object result = null;

		IBreakpoint breakpoints[] = stackFrame.getThread().getBreakpoints();
		if (breakpoints != null) {
			List<Object> resources = new ArrayList<Object>();
			for (int i = 0; i < breakpoints.length; i++) {
				if((breakpoints[i] instanceof IBreakpointSourceFinder && ((IBreakpointSourceFinder)breakpoints[i]).isSource(stackFrame)) || (breakpoints[i] instanceof JavaStratumLineBreakpoint)) {
					resources.add(breakpoints[i].getMarker().getResource()); 
				}
			}
			if (resources.size() > 0) {
			    result = resources.get(0);
			}
		}

		return (result != null) ? result : super.getSourceElement(stackFrame);	
	}
}