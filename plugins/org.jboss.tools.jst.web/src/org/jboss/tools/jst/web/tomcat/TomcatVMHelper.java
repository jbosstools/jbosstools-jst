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
package org.jboss.tools.jst.web.tomcat;

import java.io.File;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.launching.*;
import org.jboss.tools.common.model.options.Preference;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.jst.web.*;

public class TomcatVMHelper {
	
	static String TOOLS_JAR_SUFFIX = File.separator + "lib" + File.separator + "tools.jar";

	public static String findToolsJarInVM(String jvmPath) {
		if(jvmPath == null) return null;
		File jvmFile = new File(jvmPath);
		if(!jvmFile.exists()) return null;
		String path = jvmPath + TOOLS_JAR_SUFFIX;
		if(new File(path).exists()) return path;
		path = jvmFile.getParent() + TOOLS_JAR_SUFFIX;
		if(new File(path).exists()) return path;
		return null;
	}
	
	public static String createVM(String path) {		
		String jvm = findVM(path);
		if(jvm == null) {
			jvm = new File(path).getName();
			if("jre".equals(jvm)) jvm = new File(path).getParentFile().getName();
			if(JavaRuntime.getDefaultVMInstall().getVMInstallType().findVMInstall(jvm) != null) {
				int i = 0;
				while(JavaRuntime.getDefaultVMInstall().getVMInstallType().findVMInstall(jvm + (++i)) != null);
				jvm = jvm + i;
			}

			IVMInstallType type = JavaRuntime.getVMInstallType("org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType");
			IVMInstall vm = type.createVMInstall(jvm);
			vm.setInstallLocation(new File(path));
			vm.setLibraryLocations(type.getDefaultLibraryLocations(new File(path)));
			vm.setName(jvm);
			try {
                JavaRuntime.saveVMConfiguration();
            } catch (CoreException e) {
            	WebModelPlugin.getPluginLog().logError(e);
            }

		}
		if(!jvm.equals(JavaRuntime.getDefaultVMInstall().getName())) {
			getUseDefaultJVMPreference().setValue("no");
		}
		getJVMNamePreference().setValue(jvm);
		return jvm; 
	}
	
	static String findVM(String path) {
		IVMInstallType[] ts = JavaRuntime.getVMInstallTypes();
		for (int i = 0; i < ts.length; i++) {
			IVMInstall[] js = ts[i].getVMInstalls();
			for (int j = 0; j < js.length; j++) {
			    File installPath = js[j].getInstallLocation();
				if(installPath!=null && installPath.getAbsolutePath().equalsIgnoreCase(path)) {
				    	return js[j].getName();
				}
			}
		}
		return null;
	}
	
	public static IVMInstall getJVMInstall(String name) {
		IVMInstallType[] ts = JavaRuntime.getVMInstallTypes();
		for (int i = 0; i < ts.length; i++) {
			IVMInstall[] js = ts[i].getVMInstalls();
			for (int j = 0; j < js.length; j++) {
				if(js[j].getName().equals(name)) {
				    	return js[j];
				}
			}
		}
		return null;
	}

	public static IVMInstall getJVMInstallById(String id) {
		IVMInstallType[] ts = JavaRuntime.getVMInstallTypes();
		for (int i = 0; i < ts.length; i++) {
			IVMInstall[] js = ts[i].getVMInstalls();
			for (int j = 0; j < js.length; j++) {
				if(js[j].getId().equals(id)) {
				    	return js[j];
				}
			}
		}
		return null;
	}

	public static IVMInstall getJVMInstall() {
		if("no".equals(getUseDefaultJVMPreference().getValue())) {
			IVMInstallType[] jvmType = JavaRuntime.getVMInstallTypes();
			String selectedJVMName = getJVMNamePreference().getValue();
			if(ModelPlugin.isDebugEnabled()) {			
				WebModelPlugin.getPluginLog().logInfo("Finding selected JVM is " + selectedJVMName);
			}		
			for (int i = 0; i < jvmType.length; i++) {
				IVMInstall[] jvmInstall = jvmType[i].getVMInstalls();
				for (int j = 0; j < jvmInstall.length; j++) {
					
					if(jvmInstall[j].getName().equals(selectedJVMName)) {
						return jvmInstall[j];	
					}
				}
			}
		}
		return JavaRuntime.getDefaultVMInstall();
	}	 

	public static Preference getUseDefaultJVMPreference() {
		if(ModelPlugin.isDebugEnabled()) {			
			WebModelPlugin.getPluginLog().logInfo("SELECTED_SERVER_USE_DEFAULT_JVM = " + WebPreference.USE_DEFAULT_JVM.getValue());
		}
		return WebPreference.USE_DEFAULT_JVM; 
	}

	public static Preference getJVMNamePreference() {
		if(ModelPlugin.isDebugEnabled()) {			
			WebModelPlugin.getPluginLog().logInfo("SELECTED_SERVER_JVM = " + WebPreference.SERVER_JVM.getValue());
		}
		return WebPreference.SERVER_JVM; 
	}

	public static Preference getWarningPreference() {
		if(ModelPlugin.isDebugEnabled()) {			
			WebModelPlugin.getPluginLog().logInfo("SELECTED_SERVER_WARNING = " + WebPreference.SERVER_WARNING.getValue());
		}
		return WebPreference.SERVER_WARNING; 
	}

}
