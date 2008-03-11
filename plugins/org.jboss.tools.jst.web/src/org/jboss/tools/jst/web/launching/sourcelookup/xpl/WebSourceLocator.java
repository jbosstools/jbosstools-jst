/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Exadel, Inc.
 *     Red Hat, Inc. 
 *******************************************************************************/
package org.jboss.tools.jst.web.launching.sourcelookup.xpl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.ISourceLocator;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.jdt.internal.debug.core.breakpoints.JavaStratumLineBreakpoint;
import org.jboss.tools.jst.web.launching.sourcelookup.IBreakpointSourceFinder;

/**
 * @author Igels
 */
public class WebSourceLocator implements ISourceLocator {

	private ISourceLocator locator;

	public WebSourceLocator(ISourceLocator locator) {
		this.locator = locator;
	}

	/**
	 * Returns resource of breakpoint Marker if breakpoint is instance of IBreakpointSourceFinder
	 * else forward to original locator.
	 */
	public Object getSourceElement(IStackFrame stackFrame) {
		Object result = null;

		IBreakpoint breakpoints[] = stackFrame.getThread().getBreakpoints();
		if (breakpoints != null) {
			List<IResource> resources = new ArrayList<IResource>();
			for (int i = 0; i < breakpoints.length; i++) {
				if((breakpoints[i] instanceof IBreakpointSourceFinder && ((IBreakpointSourceFinder)breakpoints[i]).isSource(stackFrame)) || (breakpoints[i] instanceof JavaStratumLineBreakpoint)) {
					resources.add(breakpoints[i].getMarker().getResource()); 
				}
			}
			if (resources.size() > 0) {
			    result = resources.get(0);
			}
		}

		return (result != null) ? result : locator.getSourceElement(stackFrame);	
	}
}