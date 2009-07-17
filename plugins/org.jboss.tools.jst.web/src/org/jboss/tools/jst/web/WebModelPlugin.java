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

import java.util.Iterator;
import java.util.Properties;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;
import org.jboss.tools.common.log.BaseUIPlugin;
import org.jboss.tools.common.log.IPluginLog;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelConstants;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.projecttemplates.ProjectTemplatesPlugin;
import org.osgi.framework.BundleContext;

/**
 * 
 */
public class WebModelPlugin extends BaseUIPlugin {

	public static final String PLUGIN_ID = "org.jboss.tools.jst.web"; //$NON-NLS-1$

	static WebModelPlugin instance;

	public static WebModelPlugin getDefault() {
		if(instance == null) {
			Platform.getBundle(PLUGIN_ID);
		}
		return instance;
	}

	public static boolean isDebugEnabled() {
		return getDefault().isDebugging(); 
	}

	public WebModelPlugin() {
	    super();
	    instance = this;
	}

	protected void initializeDefaultPluginPreferences() {
		super.initializeDefaultPluginPreferences();
		Properties p = new Properties();
		p.setProperty(XModelConstants.WORKSPACE, EclipseResourceUtil.getInstallPath(this));
		p.setProperty("initialModel", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		XModel initialModel = PreferenceModelUtilities.createPreferenceModel(p);
		if (initialModel != null) {
			Iterator preferences = WebPreference.getPreferenceList().iterator();
			
			while(preferences.hasNext()) {
				Object preference = preferences.next();
				if(preference instanceof WebPreference) {
					try {
						PreferenceModelUtilities.initPreferenceValue(initialModel,(WebPreference)preference);
					} catch (XModelException e) {
						ModelPlugin.getPluginLog().logError(e);
					}
				}
			}
			PreferenceModelUtilities.getPreferenceModel().save();
		}
	}

	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		ProjectTemplatesPlugin.getDefault();
	}

	static public ILaunchConfiguration findLaunchConfig(String name) throws CoreException {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType launchConfigurationType = launchManager.getLaunchConfigurationType( "org.eclipse.ant.AntLaunchConfigurationType" ); //$NON-NLS-1$
		ILaunchConfiguration[] launchConfigurations = launchManager.getLaunchConfigurations( launchConfigurationType );

		for (int i = 0; i < launchConfigurations.length; i++) { // can't believe there is no look up by name API
			ILaunchConfiguration launchConfiguration = launchConfigurations[i];
			if(launchConfiguration.getName().equals(name)) {
				return launchConfiguration;
			}
		} 
		return null;
	}

	public static String getTemplateStateLocation() {
		return ProjectTemplatesPlugin.getTemplateStateLocation();
	}

	public static IPath getTemplateStatePath() {
		return ProjectTemplatesPlugin.getTemplateStatePath();
	}
	
	/**
	 * @return IPluginLog object
	 */
	public static IPluginLog getPluginLog() {
		return getDefault();
	}
}