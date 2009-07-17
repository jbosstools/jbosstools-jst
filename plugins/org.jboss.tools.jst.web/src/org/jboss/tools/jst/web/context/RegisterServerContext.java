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
package org.jboss.tools.jst.web.context;

import java.text.MessageFormat;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IProject;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.internal.Module;
import org.eclipse.wst.server.core.internal.ModuleFactory;
import org.eclipse.wst.server.core.internal.ServerPlugin;
import org.jboss.tools.common.model.options.Preference;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.server.RegistrationHelper;
import org.jboss.tools.jst.web.server.ServerManager;

public class RegisterServerContext {
	public static int PROJECT_MODE_NEW = 0;
	public static int PROJECT_MODE_IMPORT = 1;
	public static int PROJECT_MODE_EXISTING = 2;
	
	static String HELP_KEY_BASE = "FileSystems_RegisterInServerXML"; //$NON-NLS-1$
	
	IProject project = null;
	String servletVersion = "";	 //$NON-NLS-1$
	
		int mode; 
	boolean isEnabled = false;
	boolean isInitiallyEnabled = false;
	String location = null;
	String applicationName = null;
	String natureIndex = null;
	Preference registerProjectPreference;
	
	IRuntime runtime = null;
	String runtimeName = ""; //$NON-NLS-1$
	IServer[] targetServers = new IServer[0];
	
	long xmlTimeStamp = -1;
	String xmlError = null;
    
	private static final String YES_STRING = "yes"; //$NON-NLS-1$
	private static final String EMPTY_LOCATION = ""; //$NON-NLS-1$
	
	
	public RegisterServerContext(int mode) {
		this.mode = mode;
		init();
	}
	
	public void setServletVersion(String v) {
		servletVersion = v;
	}
	
	public String getServletVersion() {
		return servletVersion;
	}

	public void setProjectHandle(IProject p) {
		project = p;
	}
	
	public void setPreferences(Preference registerProjectPreference) {
		this.registerProjectPreference = registerProjectPreference;
	}
	
	public void setNatureIndex(String n) {
		this.natureIndex = n;
	}
	
	public void setRuntimeName(String runtimeName) {
		if(this.runtimeName.equals(runtime)) return;
		this.runtimeName = runtimeName;
		IRuntime[] rs = ServerCore.getRuntimes();
		for (int i = 0; i < rs.length; i++) {
			if(rs[i].getName().equals(runtimeName)) {
				runtime = rs[i];
				return;
			}
		}
		runtime = null;
	}
	
	public String getRuntimeName() {
		return runtimeName;
	}
	
	public void setTargetServers(IServer[] info) {
		targetServers = info;
	}
	
	public IServer[] getTargetServers() {
		return targetServers;
	}
	
	public void setEnabled(boolean b) {
		isEnabled = (mode == PROJECT_MODE_EXISTING) || b;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public void setApplicationName(String name) {
		applicationName = name;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public boolean isInitiallyEnabled() {
		return isInitiallyEnabled;
	}
	
	public String getLocation() {
		return location;
	}
	
	public String getApplicationName() {
		return applicationName;
	}

	@SuppressWarnings("restriction")
	public String getErrorMessage() {
		if(runtimeName == null || runtimeName.length() == 0) return WebUIMessages.RUNTIME_IS_REQUIRED;
		if(runtime == null) return WebUIMessages.SPECIFIED_RUNTIME_DOESNOT_EXIST;

		if(!isEnabled()) {
			if(servletVersion != null && servletVersion.length() == 0) {
				return Messages.ERR_SERVLET_VERSION_IS_NOT_SET; 
			}
		    return null;
		}

		if(applicationName == null || applicationName.length() == 0) {
		    return Messages.ERR_APP_NAME_IS_NOT_SPECIFIED; 
		}
		
		if(true) {
			String contextRootError = RegistrationHelper.checkContextRoot(applicationName);
			if(contextRootError != null) return contextRootError;
			for (int i = 0; i < targetServers.length; i++) {
				if(RegistrationHelper.isRegistered(applicationName, targetServers[i])) {
					return NLS.bind(WebUIMessages.APPLICATION_IS_ALREADY_REGISTERED, applicationName, targetServers[i].getName());
				}
				if(this.project != null /*&& !this.project.exists()*/ && servletVersion != null && servletVersion.length() > 0) {
					if(project.exists() && RegistrationHelper.isRegistered(project)) {
						return MessageFormat
								.format(WebUIMessages.PROJECT_IS_ALREADY_REGISTERED,
										project.getName());
					}
					ModuleFactory f = ServerPlugin.findModuleFactory("org.eclipse.jst.j2ee.server"); //$NON-NLS-1$
					IModule module = RegistrationHelper.findModule(project);
					if(module == null) {
						module = (f == null) ? null : new Module(f, project.getName(), project.getName(), "jst.web", servletVersion, null); //$NON-NLS-1$
					}
					String m = (module == null) ? null : RegistrationHelper.getRegistrationError(module, applicationName, targetServers[i]);
					if(m != null) return m;
				}
			}
			if(servletVersion != null && servletVersion.length() == 0) {
				return Messages.ERR_SERVLET_VERSION_IS_NOT_SET; 
			} else if(!checkServletVersionFormat()) {
				return Messages.ERR_SERVLET_VERSION_IS_NOT_VALID;				 
			}
			return null;
		}
		
		return xmlError;
	}
	
	boolean checkServletVersionFormat() {
		if(servletVersion == null || servletVersion.length() == 0) return true;
		StringTokenizer st = new StringTokenizer(servletVersion, "."); //$NON-NLS-1$
		if(st.countTokens() < 2 || st.countTokens() > 3) return false;
		while(st.hasMoreTokens()) {
			String t = st.nextToken().trim();
			try {
				Integer.parseInt(t);
			} catch (NumberFormatException e) {
				WebModelPlugin.getPluginLog().logError(e);
				return false;
			}
		}			
		return true;
		
	}
	
	public void init() {
	   	isEnabled = (registerProjectPreference == null) 
	   	  || YES_STRING.equals(registerProjectPreference.getValue());
	   	isInitiallyEnabled = isEnabled;
		applicationName = null;
		
		IServer server = ServerManager.getInstance().getSelectedServer();
		if(server == null || server.getRuntime() == null) {
			location = EMPTY_LOCATION;
		} else {
			location = server.getRuntime().getLocation().toString();
		}
	}
	
	public int getMode() {
		return mode;
	}
	
	public IRuntime getRuntime() {
		return runtime;
	}
	
	public String getHelpBase() {
		return (natureIndex == null) ? HELP_KEY_BASE : HELP_KEY_BASE + "_" + natureIndex;  //$NON-NLS-1$
	}

}
