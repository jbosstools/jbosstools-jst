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
package org.jboss.tools.jst.web.debug.internal;

import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jdt.internal.debug.core.breakpoints.JavaPatternBreakpoint;
import org.eclipse.jdt.internal.debug.core.model.JDIDebugTarget;
import org.eclipse.jdt.internal.debug.core.model.JDIThread;

import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.jst.web.WebUtils;
import org.jboss.tools.jst.web.debug.DebugMessages;
import org.jboss.tools.jst.web.debug.IBreakpointPresentation;
import org.jboss.tools.jst.web.debug.WebDebugPlugin;
import org.jboss.tools.jst.web.debug.xpl.DebugSupport;
import org.jboss.tools.jst.web.debug.xpl.JspDebugUtils;
import org.jboss.tools.jst.web.launching.sourcelookup.IBreakpointSourceFinder;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.event.Event;

public class JspLineBreakpoint extends JavaPatternBreakpoint implements IBreakpointSourceFinder, IBreakpointPresentation {

	private static final String JSP_BREAKPOINT = "org.jboss.tools.jst.web.debug.jspLineBreakpointMarker";
	private static final String BREAKPOINT_LABEL_TEXT_KEY = "JspLineBreakpoint.name";

	private JDIDebugTarget debugTarget = null;
	private boolean startServer = false;

	public JspLineBreakpoint() {
	}

	public JspLineBreakpoint(IResource resource, String pattern, int lineNumber, int charStart, int charEnd, boolean add, Map attributes, String markerType) throws DebugException {
		super(resource, null, pattern, lineNumber, charStart, charEnd, 0, add, attributes, markerType);
	}

	public JspLineBreakpoint(IResource resource, String pattern, int lineNumber, int charStart, int charEnd, boolean add, Map attributes) throws DebugException {
		this(resource, pattern, lineNumber, charStart, charEnd, add, attributes, JSP_BREAKPOINT);
	}

	public void addToTarget(JDIDebugTarget target) throws CoreException	{	
		debugTarget = target;
		try	{		
			super.addToTarget(target);
		} finally {
			debugTarget = null;
		}
	}

	protected boolean installableReferenceType(ReferenceType type, JDIDebugTarget target) throws CoreException {
		debugTarget = target;
		boolean result = false;

		try {		
			result = super.installableReferenceType(type, target);
		} finally {
			debugTarget = null;
		}

		return result;		
	}

	public String getPattern() throws CoreException {
		String result = null;

		if (debugTarget != null) {
			result = JspDebugUtils.getGeneratedJavaClassName(
						super.getPattern(),
						debugTarget.getLaunch().getLaunchConfiguration().getAttribute(WebUtils.ATTR_WEB_SERVER_NAME, "") 
					);
		}
		if(result==null) {
			return super.getPattern();
		}

		return result;		
	}

	protected String getEnclosingReferenceTypeName() throws CoreException {
		String name= getTypeName();
		int index = (name == null) ? -1 : name.indexOf('$');
		if (index == -1) {
			return name;
		} else {
			return name.substring(0, index);
		}
	}

	public String getTypeName() throws CoreException {
		if (fInstalledTypeName == null) {
			return getReferenceTypeName();
		} else {
			return fInstalledTypeName;
		}
	}

	protected void createRequests(JDIDebugTarget target) throws CoreException {
		debugTarget = target;
		try	{
			super.createRequests(target);
		} finally {
			debugTarget = null;
		}
	}

	public boolean isSource(IStackFrame stackFrame) {
		boolean result = false;

		if (stackFrame instanceof IJavaStackFrame) {
			IJavaStackFrame javaStackFrame = (IJavaStackFrame)stackFrame;
			try {
				String receivingTypeName = javaStackFrame.getReceivingTypeName();
				String configName = javaStackFrame.getLaunch().getLaunchConfiguration().getAttribute(WebUtils.ATTR_WEB_SERVER_NAME, "");
				String generatedClassName = JspDebugUtils.getGeneratedJavaClassName(super.getPattern(), configName);

				result = receivingTypeName.equals(generatedClassName);
			} catch (CoreException ex) {
				ModelPlugin.log(ex);
			}
		}

		return result;
	}

	public String getModelIdentifier() {
		if(startServer) {
			setStartServerStatus(false);
			return super.getModelIdentifier();
		}
		return WebDebugPlugin.PLUGIN_ID;
	}

	public void setStartServerStatus(boolean status) {
		startServer = status;
	}

	public String getLabelText() {
		try {
			return DebugMessages.getString(BREAKPOINT_LABEL_TEXT_KEY, new String[]{getPattern(), "" + getLineNumber()});
		} catch (CoreException e) {
			ModelPlugin.log(e);
			return "error";
		}
	}

	protected boolean suspendForCondition(Event event, JDIThread thread) {
		DebugSupport.initWebSourceLocator(thread);
		return super.suspendForCondition(event, thread);
	}

	protected boolean suspendForEvent(Event event, JDIThread thread) {
		DebugSupport.initWebSourceLocator(thread);
		return super.suspendForEvent(event, thread);
	}

}
