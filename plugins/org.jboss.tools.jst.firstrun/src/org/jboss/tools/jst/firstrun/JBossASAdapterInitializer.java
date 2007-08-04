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

package org.jboss.tools.jst.firstrun;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.ui.IStartup;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IRuntimeType;
import org.eclipse.wst.server.core.IRuntimeWorkingCopy;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.server.core.internal.RuntimeWorkingCopy;

/**
 * @author eskimo
 *
 */
public class JBossASAdapterInitializer implements IStartup {

	public static final String JBOSS_AS_HOME = "../../../../jboss-eap/jboss-as"; 	// JBoss AS home directory (relative to plugin)- <RHDS_HOME>/jbossas.
	
	public static final String JBOSS_AS_RUNTIME_TYPE_ID = "org.jboss.ide.eclipse.as.runtime.42";
	public static final String JBOSS_AS_TYPE_ID = "org.jboss.ide.eclipse.as.42";
	public static final String JBOSS_AS_NAME = "JBoss Application Server 4.2";
	public static final String JBOSS_AS_HOST = "localhost";
	public static final String JBOSS_AS_DEFAULT_CONFIGURATION_NAME = "default";

	public static final String FIRST_START_PREFERENCE_NAME = "FIRST_START";
	
	/**
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 */
	public void earlyStartup() {

		try {
			
			JstFirstRunPlugin.getDefault().getPreferenceStore().setDefault(FIRST_START_PREFERENCE_NAME, true);
			boolean firstStart = JstFirstRunPlugin.getDefault().getPreferenceStore().getBoolean(FIRST_START_PREFERENCE_NAME);
			if(!firstStart) {
				return;
			}
			JstFirstRunPlugin.getDefault().getPreferenceStore().setValue(FIRST_START_PREFERENCE_NAME, false);

			String jbossASLocation = null;
			String pluginLocation = FileLocator.resolve(JstFirstRunPlugin.getDefault().getBundle().getEntry("/")).getPath();
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
		} catch (CoreException e) {
			JstFirstRunPlugin.getPluginLog().log(new Status(IStatus.ERROR,"org.jboss.tools.jst.first.run","Can't create new JBoss Server.", e));
		} catch (IOException e) {
			JstFirstRunPlugin.getPluginLog().log(new Status(IStatus.ERROR,"org.jboss.tools.jst.first.run","Can't create new JBoss Server.", e));
		}
	}
}
