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

import java.io.File;
import java.util.Iterator;
import java.util.Properties;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IRuntimeType;
import org.eclipse.wst.server.core.IRuntimeWorkingCopy;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.server.core.internal.RuntimeWorkingCopy;
import org.jboss.ide.seam.gen.actions.SeamGenAction;
import org.jboss.tools.common.log.BaseUIPlugin;
import org.jboss.tools.common.log.IPluginLog;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelConstants;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.projecttemplates.ProjectTemplatesPlugin;
import org.osgi.framework.BundleContext;

public class WebModelPlugin extends BaseUIPlugin {

	public static final String JBOSS_AS_HOME = "../../../../jbossas"; 	// JBoss AS home directory (relative to plugin)- <RHDS_HOME>/jbossas.
	// TODO agreement about actual seam-gen location is needed
	public static final String SEAM_GEN_HOME = "../../../../jbosseam/seam-gen"; 
	
	public static final String JBOSS_AS_RUNTIME_TYPE_ID = "org.jboss.ide.eclipse.as.runtime.42";
	public static final String JBOSS_AS_TYPE_ID = "org.jboss.ide.eclipse.as.42";
	public static final String JBOSS_AS_NAME = "JBoss Application Server 4.2";
	public static final String JBOSS_AS_HOST = "localhost";
	public static final String JBOSS_AS_DEFAULT_CONFIGURATION_NAME = "default";

	public static final String FIRST_START_PREFERENCE_NAME = "FIRST_START";
	public static final String PLUGIN_ID = "org.jboss.tools.jst.web";

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
		p.setProperty("initialModel", "true");
		XModel initialModel = PreferenceModelUtilities.createPreferenceModel(p);
		if (initialModel != null) {
			Iterator preferences = WebPreference.getPreferenceList().iterator();
			
			while(preferences.hasNext()) {
				Object preference = preferences.next();
				if(preference instanceof WebPreference) {
					PreferenceModelUtilities.initPreferenceValue(initialModel,(WebPreference)preference);
				}
			}
			PreferenceModelUtilities.getPreferenceModel().save();
		}
	}

	public void stop(BundleContext context) throws Exception {
//		PreferenceModelUtilities.getPreferenceModel().save();
		super.stop(context);
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		// Eskimo
		// asinc exec removed because registered server dosen't appears in New Project dialog
		// when calling of these dialogs is cause of loadind the plugin hierarchy
		ProjectTemplatesPlugin.getDefault();
		initJbossAS();
		initSeamGen();
	}

	private void initSeamGen() {
		ILaunchConfiguration config=null;
		try {
			config = findLaunchConfig("seamgen");
		} catch (CoreException e1) {
			getPluginLog().logError("Exception occured during search in Launch Configuration list.", e1);
		}
		String buildXmlPath = null;
		if(config==null) {
			try {
				buildXmlPath = getSeamGenBuildPath();
				SeamGenAction.createSeamgenLaunchConfig(buildXmlPath);
			} catch (CoreException e) {
				getPluginLog().logError("Cannot create configuration for Seam-Gen tool. Seamgen build.xml file: " + buildXmlPath, e);
				return;
			}
		}
	}

	public String getSeamGenBuildPath() {
		String pluginLocation = EclipseResourceUtil.getInstallPath(this.getBundle());
		File seamGenDir = new File(pluginLocation, SEAM_GEN_HOME);
		File seamGenBuildXml = new File(seamGenDir,"build.xml");
		if(seamGenBuildXml.isFile()) {
			return seamGenBuildXml.getAbsolutePath();
		} else {
			return "";
		}
	}

	static public ILaunchConfiguration findLaunchConfig(String name) throws CoreException {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType launchConfigurationType = launchManager.getLaunchConfigurationType( "org.eclipse.ant.AntLaunchConfigurationType" );
		ILaunchConfiguration[] launchConfigurations = launchManager.getLaunchConfigurations( launchConfigurationType );

		for (int i = 0; i < launchConfigurations.length; i++) { // can't believe there is no look up by name API
			ILaunchConfiguration launchConfiguration = launchConfigurations[i];
			if(launchConfiguration.getName().equals(name)) {
				return launchConfiguration;
			}
		} 
		return null;
	}

	/*
	 * Init bundled JBoss AS Runtime and Server.
	 */
	private void initJbossAS() {
		try {
			this.getPreferenceStore().setDefault(FIRST_START_PREFERENCE_NAME, true);
			boolean firstStart = this.getPreferenceStore().getBoolean(FIRST_START_PREFERENCE_NAME);
			if(!firstStart) {
				return;
			}
			this.getPreferenceStore().setValue(FIRST_START_PREFERENCE_NAME, false);

			String jbossASLocation = null;
			String pluginLocation = EclipseResourceUtil.getInstallPath(this.getBundle());
			File jbossASDir = new File(pluginLocation, JBOSS_AS_HOME);
			if(jbossASDir.isDirectory()) {
				jbossASLocation = jbossASDir.getAbsolutePath();
			} else {
				return;
			}

			IPath jbossAsLocationPath = new Path(jbossASLocation);
			String type = null;
			String version = null;

			IServer[] servers = ServerCore.getServers();
			for(int i=0; i<servers.length; i++) {
				IRuntime runtime = servers[i].getRuntime();
				if(runtime!=null && runtime.getLocation().equals(jbossAsLocationPath)) {
					return;
				}
			}

			IRuntimeWorkingCopy runtime = null;
			IRuntime[] runtimes = ServerCore.getRuntimes();
			String runtimeId = null;
			for(int i=0; i<runtimes.length; i++) {
				if(runtimes[0].getLocation().equals(jbossASLocation)) {
					runtime = runtimes[0].createWorkingCopy();
					runtimeId = null;
					break;
				}
			}

			IProgressMonitor progressMonitor = new NullProgressMonitor();
			if(runtime==null) {
				IRuntimeType[] runtimeTypes = ServerUtil.getRuntimeTypes(type, version, JBOSS_AS_RUNTIME_TYPE_ID);
				if(runtimeTypes.length>0) {
					runtime = runtimeTypes[0].createRuntime(runtimeId, progressMonitor);
					runtime.setLocation(jbossAsLocationPath);
					IVMInstall defaultVM = JavaRuntime.getDefaultVMInstall();
					// IJBossServerRuntime.PROPERTY_VM_ID
					((RuntimeWorkingCopy)runtime).setAttribute("PROPERTY_VM_ID", defaultVM.getId());
					// IJBossServerRuntime.PROPERTY_VM_TYPE_ID
					((RuntimeWorkingCopy)runtime).setAttribute("PROPERTY_VM_TYPE_ID", defaultVM.getVMInstallType().getId());
					// IJBossServerRuntime.PROPERTY_CONFIGURATION_NAME
					((RuntimeWorkingCopy)runtime).setAttribute("org.jboss.ide.eclipse.as.core.runtime.configurationName", JBOSS_AS_DEFAULT_CONFIGURATION_NAME);

					runtime.save(false, progressMonitor);
				}
			}

			if(runtime!=null) {
				IServerType serverType = ServerCore.findServerType(JBOSS_AS_TYPE_ID);
				IServerWorkingCopy server = serverType.createServer(null, null, runtime, progressMonitor);

				server.setHost(JBOSS_AS_HOST);
				server.setName(JBOSS_AS_NAME);
				server.save(false, progressMonitor);
			}
		} catch (Exception e) {
			getPluginLog().logError("Can't create new JBoss Server.", e);
		}
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