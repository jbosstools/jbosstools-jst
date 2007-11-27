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
package org.jboss.tools.jst.web.ui.operation;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

public class RemoveJavaSource {
	public void execute(IProject project) {
		IJavaProject javaProject = JavaCore.create(project);
		try {
			IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
			ArrayList list = new ArrayList();
			ArrayList src = new ArrayList();
			for (int i = 0; i < oldEntries.length; i++) {
				if(oldEntries[i].getEntryKind() != IClasspathEntry.CPE_SOURCE) {
					list.add(oldEntries[i]);
				} else {
					IResource r = project.findMember(oldEntries[i].getPath().removeFirstSegments(1));
					src.add(r);					
				}
			}
			IClasspathEntry[] classpathEntries = (IClasspathEntry[])list.toArray(new IClasspathEntry[0]);
			javaProject.setRawClasspath(classpathEntries, null);
			IResource[] rs = (IResource[])src.toArray(new IResource[0]);
			for (int i = 0; i < rs.length; i++) {
				try {
					if(rs[i] != null) rs[i].delete(true, null);
				} catch (Exception ce) {
					WebUiPlugin.getPluginLog().logError(ce);
				}
			}
		} catch (JavaModelException e) {
			WebUiPlugin.getPluginLog().logError(e);
		}
	}	
}
