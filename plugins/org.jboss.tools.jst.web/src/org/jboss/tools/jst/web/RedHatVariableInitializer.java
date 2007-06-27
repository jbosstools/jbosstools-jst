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
package org.jboss.tools.jst.web;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.ClasspathVariableInitializer;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.preference.IPreferenceStore;

import org.jboss.tools.common.model.util.EclipseResourceUtil;

public class RedHatVariableInitializer extends ClasspathVariableInitializer {
	public static final String REDHAT_LIB_HOME = "REDHAT_LIB_HOME";

	public void initialize(String variable)	{
		if (REDHAT_LIB_HOME.equals(variable)) {
			IPath ssLibPath = null;
			IPreferenceStore store = WebModelPlugin.getDefault().getPreferenceStore();
			if (store.contains(REDHAT_LIB_HOME) && !store.isDefault(REDHAT_LIB_HOME)) {
				String value = store.getString(REDHAT_LIB_HOME);
				ssLibPath = new Path(value);
				if (!ssLibPath.toFile().isDirectory()) ssLibPath = null;  
			} 
			
			if (ssLibPath == null)
			{
				IPath pluginPath = new Path(EclipseResourceUtil.getInstallPath(WebModelPlugin.getDefault()));
				ssLibPath = pluginPath.removeLastSegments(3).append("lib");
			}			
			try {
				JavaCore.setClasspathVariable(variable, ssLibPath, new NullProgressMonitor());
			} catch (JavaModelException ex) {
				WebModelPlugin.getPluginLog().logError(ex);
			}
		}
	}

	public static void save(IPreferenceStore store)	{
		IPath value = JavaCore.getClasspathVariable(REDHAT_LIB_HOME);
		if (value != null) store.setValue(REDHAT_LIB_HOME, value.toString());
	}

}
