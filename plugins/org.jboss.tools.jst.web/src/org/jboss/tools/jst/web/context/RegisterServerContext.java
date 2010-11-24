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
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IProject;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.runtime.RuntimeManager;
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

	String serverErrorCache = null;
    
	private static final String YES_STRING = "yes"; //$NON-NLS-1$
	private static final String EMPTY_LOCATION = ""; //$NON-NLS-1$
	
	
	public RegisterServerContext(int mode) {
		this.mode = mode;
		init();
	}
	
	public void setServletVersion(String v) {
		if(v != null && !v.equals(servletVersion)) {
			serverErrorCache = null;
		}
		servletVersion = v;
	}
	
	public String getServletVersion() {
		return servletVersion;
	}

	public void setProjectHandle(IProject p) {
		project = p;
	}

	public IProject getProjectHandle() {
		return project;
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
		serverErrorCache = null;
	}
	
	public String getRuntimeName() {
		return runtimeName;
	}
	
	public void setTargetServers(IServer[] info) {
		if(!targetServersEqual(targetServers, info)) {
			serverErrorCache = null;
		}
		targetServers = info;
	}

	boolean targetServersEqual(IServer[] s1, IServer[] s2) {
		if(s1.length != s2.length) return false;
		for (int i = 0; i < s1.length; i++) if(s1[i] != s2[i]) return false;
		return true;
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
			if(targetServers.length > 0) {
				IModule module = RegistrationHelper.findModule(project);
				boolean isFakeModule = false;
				if(module == null) {
					if(serverErrorCache != null && serverErrorCache.length() > 0) {
						return serverErrorCache;
					}
					ModuleFactory f = ServerPlugin.findModuleFactory("org.eclipse.jst.j2ee.server"); //$NON-NLS-1$
					module = (f == null) ? null : new Module(f, project.getName(), project.getName(), "jst.web", servletVersion, null); //$NON-NLS-1$
					isFakeModule = true;
				} else {
					serverErrorCache = null;
				}
				if(serverErrorCache == null) for (int i = 0; i < targetServers.length; i++) {
					if(RegistrationHelper.isRegistered(applicationName, targetServers[i])) {
						return NLS.bind(WebUIMessages.APPLICATION_IS_ALREADY_REGISTERED, applicationName, targetServers[i].getName());
					}
					if(this.project != null /*&& !this.project.exists()*/ && servletVersion != null && servletVersion.length() > 0) {
						if(project.exists() && RegistrationHelper.isRegistered(project)) {
							return MessageFormat
									.format(WebUIMessages.PROJECT_IS_ALREADY_REGISTERED,
											project.getName());
						}
						String m = (module == null) ? null : RegistrationHelper.getRegistrationError(module, applicationName, targetServers[i]);
						if(m != null) {
							if(isFakeModule) {
								serverErrorCache = m;
							}
							return m;
						}
					}
				}
				serverErrorCache = ""; //$NON-NLS-1$
			}
			if(servletVersion != null && servletVersion.length() == 0) {
				return Messages.ERR_SERVLET_VERSION_IS_NOT_SET; 
			} else if(!checkServletVersionFormat()) {
				return Messages.ERR_SERVLET_VERSION_IS_NOT_VALID;				 
			}
			if(runtime != null) {
				IProjectFacet f = ProjectFacetsManager.getProjectFacet("jst.web"); //$NON-NLS-1$
				if(f != null) {
					IProjectFacetVersion v = f.getVersion(servletVersion);
					if (v == null) return NLS.bind(Messages.ERR_SERVLET_VERSION_IS_NOT_SUPPORTED, servletVersion);
					org.eclipse.wst.common.project.facet.core.runtime.IRuntime facetRuntime = null;
					Set<?> set = RuntimeManager.getRuntimes();
					Iterator<?> it = set.iterator();
					while(it.hasNext()) {
						org.eclipse.wst.common.project.facet.core.runtime.IRuntime r = (org.eclipse.wst.common.project.facet.core.runtime.IRuntime)it.next();
						if(runtimeName.equals(r.getName())) facetRuntime = r;
					}
					if(facetRuntime != null && !facetRuntime.supports(v)) {
						return NLS.bind(Messages.ERR_SERVLET_VERSION_IS_NOT_SUPPORTED_BY_RUNTIME, runtimeName, servletVersion);
					}
				}
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
				//Do not log! This is the validation of user input!
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
