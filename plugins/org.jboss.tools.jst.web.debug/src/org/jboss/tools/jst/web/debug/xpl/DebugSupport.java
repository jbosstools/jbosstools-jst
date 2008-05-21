/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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
package org.jboss.tools.jst.web.debug.xpl;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.ISourceLocator;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.debug.core.model.JDIStackFrame;
import org.eclipse.jdt.internal.debug.core.model.JDIThread;
import org.jboss.tools.jst.web.launching.sourcelookup.xpl.WebSourceLocator;

/**
 * @author igels
 */
public class DebugSupport {

	private static final String HOST_PLUGIN_ID 
		= "org.jboss.tools.jst.web.debug";
	
	public static IJavaProject getJavaProject(JDIStackFrame stackFrame) {
		ILaunch launch = stackFrame.getLaunch();
		if (launch == null) {
			return null;
		}
		ISourceLocator locator = launch.getSourceLocator();
		if (locator == null) {
			return null;
		}

		Object sourceElement = locator.getSourceElement(stackFrame);
		if(sourceElement instanceof IFile) {
			IProject iproject = ((IFile)sourceElement).getProject();
			IJavaProject project = getJavaProject(iproject);
			return project;
		}
		if (sourceElement instanceof IJavaElement) {
			return ((IJavaElement) sourceElement).getJavaProject();
		}			
		return null;
	}

	public static void initWebSourceLocator(JDIThread thread) {
		ILaunch launch = thread.getLaunch();
		if (launch == null) {
			return;
		}
		ISourceLocator locator = launch.getSourceLocator();
		if (locator == null || locator instanceof WebSourceLocator) {
			return;
		}

		launch.setSourceLocator(new WebSourceLocator(locator));
	}
	
	public static IJavaProject getJavaProject(IProject project) {
		try {
			if(project == null || !project.isOpen()) return null;
			if(!project.hasNature(JavaCore.NATURE_ID)) return null;
			return JavaCore.create(project);
		} catch (Exception e) {
			ResourcesPlugin.getPlugin().getLog().log(
				new Status(IStatus.ERROR, HOST_PLUGIN_ID,0,e.getMessage()+"",e));
			return null;		
		}
	}
}