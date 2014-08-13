/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.jsdt.configuration;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.jst.jsdt.JstJsdtPlugin;
import org.jboss.tools.jst.web.kb.IFaceletPageContext;
import org.jboss.tools.jst.web.kb.IFacilityChecker;
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.jst.web.kb.internal.AngularJSRecognizer;
import org.osgi.service.prefs.BackingStoreException;

import tern.eclipse.ide.core.ITernNatureCapability;

public class AngularJSProjectCapability implements ITernNatureCapability, IFacilityChecker {
	private static final String USE_ANGULAR_MODULE = "UseAngularModule";
	private static final String SHOW_ANGULAR_MODULE_WARNING = "ShowAngularModuleWarning";

	public static boolean DEFULT_SHOW_ANGULAR_MODULE_WARNING = true;
	public static boolean DEFULT_USE_ANGULAR_MODULE = false;

	@Override
	public boolean hasTernNature(IProject project) throws CoreException {
		return getUseAngularModuleProjectPreference(project);
	}

	private static IEclipsePreferences getProjectPreferences(IProject project) {
		return new ProjectScope(project).getNode(JstJsdtPlugin.PLUGIN_ID);
	}

	public static boolean getShowAngularModuleWarningProjectPreference(IProject project) {
		return getProjectPreference(project, SHOW_ANGULAR_MODULE_WARNING, DEFULT_SHOW_ANGULAR_MODULE_WARNING);
	}

	public static void setShowAngularModuleWarningProjectPreference(IProject project, boolean value) {
		setProjectPreference(project, SHOW_ANGULAR_MODULE_WARNING, value);
	}

	public static boolean getUseAngularModuleProjectPreference(IProject project) {
		return getProjectPreference(project, USE_ANGULAR_MODULE, DEFULT_USE_ANGULAR_MODULE);
	}

	public static void setUseAngularModuleProjectPreference(IProject project, boolean value) {
		setProjectPreference(project, USE_ANGULAR_MODULE, value);
	}

	private static boolean getProjectPreference(IProject project, String key, boolean defaultValue) {
		IEclipsePreferences p = getProjectPreferences(project);
		if(p == null) {
			return defaultValue;
		}
		return p.getBoolean(key, defaultValue);
	}

	private static void setProjectPreference(IProject project, String key, boolean value) {
		IEclipsePreferences p = getProjectPreferences(project);
		p.putBoolean(key, value);
		try {
			p.flush();
		} catch (BackingStoreException e) {
			JstJsdtPlugin.getDefault().logError(e);
		}
	}

	private static final String ANGULAR_JS_IDE = "org.eclipse.angularjs.core";

	@Override
	public void checkFacilities(ELContext context, IFile file) {
		if(context==null) {
			context = PageContextFactory.createPageContext(file);
		}
		// Make sure the file is an HTML file and AngularJS IDE is installed
		if(context instanceof IFaceletPageContext && Platform.getBundle(ANGULAR_JS_IDE)!=null) {
			IProject project = file.getProject();

			boolean useAngularModule = getUseAngularModuleProjectPreference(project);
			if (useAngularModule) {
				return; // Flag is already set on the given project
			}

			if(!AngularJSRecognizer.detectAngularUsage(file)) {
				return;
			}

			boolean showAngularModuleWarning = getShowAngularModuleWarningProjectPreference(project);

			if (showAngularModuleWarning) {
				// Show Angular Module Warning
				AngularModuleInfoDialog dialog = new AngularModuleInfoDialog(project);
				dialog.open();
			}
		}
	}
}