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

import java.io.File;
import java.util.*;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.osgi.util.NLS;

import org.jboss.tools.common.meta.XAttribute;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.engines.impl.EnginesLoader;
import org.jboss.tools.common.model.filesystems.impl.AbstractXMLFileImpl;
import org.jboss.tools.common.model.filesystems.impl.FileAnyImpl;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;

public abstract class ImportWebProjectContext implements IImportWebProjectContext {
	protected XModelObject target;
	
	protected long webXMLTimeStamp = -1;
	protected String webXmlLocation = null;
	protected String projectName;
	protected String classesLocation = ""; //$NON-NLS-1$
	protected String libLocation = ""; //$NON-NLS-1$
	protected String buildXmlLocation = ""; //$NON-NLS-1$
	protected String webInfLocation;
	protected XModelObject webxml = null;
	protected XModelObject[] modules = new XModelObject[0];
	protected XModelObject[] allmodules = new XModelObject[0];
	protected String[] existingSources = new String[0];
	RegisterServerContext registry = new RegisterServerContext(RegisterServerContext.PROJECT_MODE_IMPORT);
	protected boolean addLibraries = false;
	protected String servletVersion = null;
	protected String templateVersion = null;
	protected boolean isLinkingToProjectOutsideWorkspace = true;
	
	public ImportWebProjectContext() {}
	
	public void setTarget(XModelObject target) {
		this.target = target;
	}
	
	public XModelObject getTarget() {
		return target;
	}

	public RegisterServerContext getRegisterServerContext() {
		return registry;
	}
	
	public String getWebXmlLocation() {
		return webXmlLocation;
	}
	
	public XModelObject[] getModules() {
		return modules;
	}
	
	public XModelObject[] getAllModules() {
		return allmodules;
	}

	protected void createAllModules() {
		List<XModelObject> list = new ArrayList<XModelObject>();
		XModelObject[] os = getModules();
		for (int i = 0; i < os.length; i++) {
			list.add(os[i]); 
			XModelObject[] cs = os[i].getChildren();
			for (int k = 0; k < cs.length; k++) {
				list.add(cs[k]);
			}
		}
		allmodules = list.toArray(new XModelObject[0]);
	}
	
	public String getProjectName() {
		return projectName;
	}
	
	public void setProjectName(String value) {
		projectName = value;
		registry.setProjectHandle(getProjectHandle());
	}
	
	public IProject getProjectHandle() {
		String n = getProjectName();
		if(n == null || n.length() == 0) return null;
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IStatus nameStatus = workspace.validateName(projectName, IResource.PROJECT);
		if (!nameStatus.isOK()) return null;
		return ResourcesPlugin.getWorkspace().getRoot().getProject(n);
	}

	public String getApplicationName() {
		return registry.getApplicationName();
	}

	public void setApplicationName(String value) {
		registry.setApplicationName(value);
	}

	public String getClassesLocation() {
		return classesLocation;
	}

	public void setClassesLocation(String value) {
		classesLocation = value;
	}

	public String getLibLocation() {
		if((libLocation == null || libLocation.trim().length() == 0) && addLibraries) {
			return getDefaultLibLocation();
		}
		return libLocation;
	}

	public void setLibLocation(String value) {
		libLocation = value;
	}

	/**
	 * Called if libLocation is not set, but addLibraries flag is true.
	 * @return
	 */
	public String getDefaultLibLocation() {
		return webInfLocation == null ? "" : webInfLocation + "/lib"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public String getBuildXmlLocation() {
		return buildXmlLocation;
	}

	public void setBuildXmlLocation(String value) {
		buildXmlLocation = value;
	}

	public String getWebInfLocation() {
		return webInfLocation;	
	}

	public String getWebRootPath() {
		String result = null;
		for (int i = 0; i < modules.length && result == null; i++)
			if ("<default>".equals(getModuleName(modules[i])))  //$NON-NLS-1$
				result = modules[i].getAttributeValue("root"); //$NON-NLS-1$
		return result;
	}

	public String[] getJavaSources() {
		ArrayList<String> list = new ArrayList<String>();
		String path = null;
		for (int i = 0; i < modules.length; ++i) {
			path = getJavaSource(modules[i]);
			if (path!=null) list.add(path.replace('\\', '/'));
		}
		return list.toArray(new String[list.size()]);
	}

	private String getModuleName(XModelObject module) {
		String n = module.getAttributeValue("name"); //$NON-NLS-1$
		return (n.length() == 0) ? "<default>" : n; //$NON-NLS-1$
	}

	private String getJavaSource(XModelObject module) {
		return module.getAttributeValue("java src"); //$NON-NLS-1$
	}

	public String[] getExistingSources() {
		return existingSources;
	}

	public void setAddLibraries(boolean b) {
		addLibraries = b;
	}

	public boolean getAddLibraries() {
		return addLibraries;
	}

	public void setServletVersion(String version) {
		this.servletVersion = version;
		registry.setServletVersion(version);
	}

	public String getServletVersion() {
		return servletVersion;
	}

	public void setTemplateVersion(String version) {
		this.templateVersion = version;
	}

	public String getTemplateVersion() {
		return templateVersion;
	}

	public boolean isLinkingToProjectOutsideWorkspace() {
		return isLinkingToProjectOutsideWorkspace;
	}

	public void setLinkingToProjectOutsideWorkspace(boolean b) {
		isLinkingToProjectOutsideWorkspace = b;
	}

	protected boolean isWebXMLUpToDate(String location) {
		if(location == null) return webXmlLocation == null;
		if(!location.equals(webXmlLocation)) return false;
		try {
			File f = new File(location);
			long last = f.isFile() ? f.lastModified() : -1;
			return webXMLTimeStamp == last;
		} catch (SecurityException e) {
			WebModelPlugin.getPluginLog().logError(e);
			return webXMLTimeStamp == -1;
		}
	}

	protected void loadWebXML(String body, String location) throws XModelException {
		String entity = getTarget().getModel().getEntityRecognizer().getEntityName("xml", body); //$NON-NLS-1$
		if(entity == null || !entity.startsWith("FileWebApp")) { //$NON-NLS-1$
			throw new XModelException(NLS.bind(WebUIMessages.FILE_ISNOT_RECOGNIZED, location));
		}
		webxml = getTarget().getModel().createModelObject(entity, null);
		webxml.setAttributeValue("name", "web"); //$NON-NLS-1$ //$NON-NLS-2$
		XModelObjectLoaderUtil.setTempBody(webxml, body);
		XModelObjectLoaderUtil.getObjectLoader(webxml).load(webxml);
		webxml.getChildren();

		if("yes".equals(webxml.getAttributeValue("isIncorrect"))) { //$NON-NLS-1$ //$NON-NLS-2$
			String[] errors = ((AbstractXMLFileImpl)webxml).getErrors();
			String error = (errors == null || errors.length == 0) ? "" : ": " + errors[0]; //$NON-NLS-1$ //$NON-NLS-2$
			String webXMLErrorMessage = NLS.bind(WebUIMessages.WEBDESCRIPTOR_FILE_IS_CORRUPTED, error); 
			throw new XModelException(webXMLErrorMessage);
		}
	}

	public abstract String getNatureID();	

	public boolean isServletVersionConsistentToWebXML() {
		if(webxml == null) return true;
		String entity = webxml.getModelEntity().getName();
		if("2.3".equals(servletVersion) && !entity.equals("FileWebApp")) return false; //$NON-NLS-1$ //$NON-NLS-2$
		if("2.4".equals(servletVersion) && !entity.equals("FileWebApp24")) return false; //$NON-NLS-1$ //$NON-NLS-2$
		if("2.5".equals(servletVersion) && !entity.equals("FileWebApp25")) return false; //$NON-NLS-1$ //$NON-NLS-2$
		return true;		
	}

	public void convertWebXML(boolean backup) throws XModelException {
		if(webxml == null) return;
		String entity = webxml.getModelEntity().getName();
		if("2.3".equals(servletVersion) && !entity.equals("FileWebApp")) { //$NON-NLS-1$ //$NON-NLS-2$
			convertWebXML("FileWebApp", backup); //$NON-NLS-1$
		} else if("2.4".equals(servletVersion) && !entity.equals("FileWebApp24")) { //$NON-NLS-1$ //$NON-NLS-2$
			convertWebXML("FileWebApp24", backup); //$NON-NLS-1$
		} else if("2.5".equals(servletVersion) && !entity.equals("FileWebApp25")) { //$NON-NLS-1$ //$NON-NLS-2$
			convertWebXML("FileWebApp25", backup); //$NON-NLS-1$
		}
	}	
	
	/**
	 * Returns servlet version corresponding to web.xml in imported project.
	 * @return
	 */	
	public String getWebXMLVersion() {
		return WebAppHelper.getServletVersion(webxml);
	}

	private void convertWebXML(String entity, boolean backup) throws XModelException {
		if(backup) backUp();
		XModelObject newweb = XModelObjectLoaderUtil.createValidObject(webxml.getModel(), entity);
		XAttribute[] as = newweb.getModelEntity().getAttributes();
		for (int i = 0; i < as.length; i++) {
			String n = as[i].getName();
			String xmlname = as[i].getXMLName();
			if(xmlname == null || xmlname.length() == 0) continue;
			XAttribute a = webxml.getModelEntity().getAttribute(n);
			if(a == null) continue;
			String v = webxml.getAttributeValue(n);
			if(v != null && !v.equals(a.getDefaultValue())) {
				newweb.setAttributeValue(n, v);
			}
		}
		XModelObject[] cs = webxml.getChildren();
		for (int i = 0; i < cs.length; i++) {
			String ent = cs[i].getModelEntity().getName();
			if(newweb.getModelEntity().getChild(ent) != null) {
				XModelObject o = newweb.getChildByPath(cs[i].getPathPart());
				if(o != null) {
					EnginesLoader.merge(o, cs[i]);
				} else {
					newweb.addChild(cs[i].copy());
				}
			} else if("WebAppJspConfig".equals(ent) || "WebAppFolderTaglibs".equals(ent)) { //$NON-NLS-1$ //$NON-NLS-2$
				XModelObject[] ts = cs[i].getChildren("WebAppTaglib"); //$NON-NLS-1$
				XModelObject jspConfig = WebAppHelper.getJSPConfig(newweb);
				for (int j = 0; j < ts.length; j++) jspConfig.addChild(ts[j]);
			} else if("WebAppTaglib".equals(ent)) { //$NON-NLS-1$
				XModelObject jspConfig = WebAppHelper.getJSPConfig(webxml);
				if(jspConfig == null) {
					//obsolete - it is a required child
					jspConfig = webxml.getModel().createModelObject("WebAppJspConfig", null); //$NON-NLS-1$
					newweb.addChild(jspConfig);
				}
				jspConfig.addChild(cs[i]);
			}
		}
		if(webXmlLocation == null) return;
		String text = ((FileAnyImpl)newweb).getAsText();
		FileUtil.writeFile(new File(webXmlLocation), text);
	}

	private void backUp() {
		if(webXmlLocation == null) return;
		File source = new File(webXmlLocation);
		File target = new File(source.getParentFile(), "web.xml." + getWebXMLVersion() + ".old"); //$NON-NLS-1$ //$NON-NLS-2$
		FileUtil.copyFile(source, target);
	}

}
