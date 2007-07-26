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

import java.io.*;
import java.util.*;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;

import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.project.WebProject;

public class WebUtils {
	public static final String ATTR_WEB_SERVER_NAME = "org.jboss.tools.jst.web.web_server_name";
	public static final String WEB_SERVER_TOMCAT = "Tomcat";
	public static final String APPLICATION_SERVER_J2EE = "J2EE";
	public static final String APPLICATION_SERVER_JBOSS = "JBoss";
	
	public static String[] getServletVersions(String templateBase) {
		String location = templateBase + "/../lib/servlet";
		File f = new File(location);
		if(!f.isDirectory()) return new String[0];
		File[] fs = f.listFiles();
		Set<String> set = new TreeSet<String>();
		if(fs != null) for (int i = 0; i < fs.length; i++) {
			if(fs[i].isDirectory()) set.add(fs[i].getName());
		}
		return set.toArray(new String[0]);
	}
	
	public static String[] getServletLibraries(String templateBase, String servletVersion) {
		String location = templateBase + "/../lib/servlet/" + servletVersion;
		File f = new File(location);
		if(!f.isDirectory()) return new String[0];

		List<String> jars = new ArrayList<String>();
		File[] fs = f.listFiles();
		if(fs != null) for (int i = 0; i < fs.length; i++) {
			if(!fs[i].isFile()) continue;
			String path = null;
			try {
				path = fs[i].getCanonicalPath();
			} catch (IOException e) {
				WebModelPlugin.getPluginLog().logError(e);
			}
			if(path != null) jars.add(path);
		}
		return jars.toArray(new String[jars.size()]);
	}

	public static String getWebRootPath(IProject project) {
		IModelNature modelNature = EclipseResourceUtil.getModelNature(project);
		return (modelNature != null) ? WebProject.getInstance(modelNature.getModel()).getWebRootLocation() : null;
	}

	
	public static String[] getServletLibraries(String natureId, String templateBase, String servletVersion) {
		String classPathVarName = findClassPathVarByNatureId(natureId);

		if (classPathVarName==null) return getServletLibraries(templateBase,servletVersion);

		String classPathVarValue = JavaCore.getClasspathVariable(classPathVarName).toOSString();
		
		String location = templateBase + "/../lib/servlet/" + servletVersion;
		File f = new File(location);
		if(!f.isDirectory()) return new String[0];

		List<String> jars = new ArrayList<String>();
		File[] fs = f.listFiles();
		if(fs != null) for (int i = 0; i < fs.length; i++) {
			if(!fs[i].isFile()) continue;
			String path = null;
			try {
				path = fs[i].getCanonicalPath();
				if(!path.startsWith("/")) 
					path="/"+path;
				if(path.startsWith(classPathVarValue)) {
					path = classPathVarName + path.substring(classPathVarValue.length());
				}
			} catch (IOException e) {
				WebModelPlugin.getPluginLog().logError(e);
			}
			if(path != null) jars.add(path);
		}
		return jars.toArray(new String[jars.size()]);
	}
	
	public static String CLASS_PATH_VAR_EXTENSION_POINT_ID = "org.jboss.tools.jst.web.classPathVar";
	public static String VAR_TAG_NAME = "variable";
	public static String VAR_NAME_ATTR = "name";
	public static String NATURE_ID_ATTR = "natureId";

	public static String findClassPathVarByNatureId(String nId) {
	    IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint(CLASS_PATH_VAR_EXTENSION_POINT_ID);
	    IExtension[] extensions = point.getExtensions();
	    for(int i=0; i<extensions.length; i++) {
	        IConfigurationElement[] configurationElements = extensions[i].getConfigurationElements();
	        for(int j=0; j<configurationElements.length; j++) {
	            if(VAR_TAG_NAME.equals(configurationElements[j].getName())) {
	                String name = configurationElements[j].getAttribute(VAR_NAME_ATTR);
	                String natureId = configurationElements[j].getAttribute(NATURE_ID_ATTR);
	                if(nId.equals(natureId)) {
	                    return name;
	                }
	            }
	        }
	    }
		return null;
	}

	public static IPath getRelativePath(IProject project, String path) {
		return EclipseResourceUtil.getRelativePath(project, path);
	}

	public static IClasspathEntry[] getDefaultJRELibrary() {
		return EclipseResourceUtil.getDefaultJRELibrary();
	}

}
