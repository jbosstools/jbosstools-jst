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
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.loaders.EntityRecognizerContext;
import org.jboss.tools.common.model.loaders.impl.SerializingLoader;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;

public abstract class ImportWebDirProjectContext extends ImportWebProjectContext {
	public static final String PAGE_NAME = "WebPrjAdoptStepName"; //$NON-NLS-1$
	public static final String PAGE_FOLDERS = "WebPrjAdoptStepFolders"; //$NON-NLS-1$
	
	public static final String ATTR_NAME = "name"; //$NON-NLS-1$
	public static final String ATTR_LOCATON = "web.xml location"; //$NON-NLS-1$
	public static final String ATTR_LINK = "link"; //$NON-NLS-1$
	public static final String ATTR_CLASSES = "classes"; //$NON-NLS-1$
	
	public static final String ATTR_LIB = "lib"; //$NON-NLS-1$
	public static final String ATTR_ADD_LIB = "add libraries"; //$NON-NLS-1$
	public static final String ATTR_VERSION = "version"; //$NON-NLS-1$
	public static final String ATTR_SERVLET_VERSION = "servlet version"; //$NON-NLS-1$

	public static final String ATTR_BUILD = "build"; //$NON-NLS-1$

	public static final String ATTR_MODULE_NAME = ATTR_NAME;
	public static final String ATTR_MODULE_URI = "URI"; //$NON-NLS-1$
	
	protected String initialName = null;
	protected String initialLocation = null;
	protected String initialLocationErrorMessage = null;
	protected String webXMLErrorMessage = null;
	
	
	public ImportWebDirProjectContext(XModelObject target) {
		setTarget(target);
		initRegistry();
	}
	
	protected void initRegistry() {}
	
	public void setWebXmlLocation(String location) {
		location = location.replace('\\', '/');
		if(isWebXMLUpToDate(location)) return;
		webXmlLocation = location;
		webXMLTimeStamp = -1;
		initialLocationErrorMessage = null;
		webXMLErrorMessage = null;
		modules = new XModelObject[0];
		File f = new File(location);
		if(!f.isFile())	{
			webXMLErrorMessage = WebUIMessages.FILE_DOESNOT_EXIST;
			return;
		}
		webXMLTimeStamp = f.lastModified();
		String body = FileUtil.readFile(f);
		String entity = getTarget().getModel().getEntityRecognizer().getEntityName(new EntityRecognizerContext("xml", body)); //$NON-NLS-1$
		if(entity == null || !entity.startsWith("FileWebApp")) { //$NON-NLS-1$
			webXMLErrorMessage = WebUIMessages.FILE_ISNOT_RECOGNIZED_AS_WEBDESCRIPTOR_FILE;
			return;
		}
		try {
			loadWebXML(body, location);
		} catch (XModelException e) {
			//Do not log this exception. It will be shown in wizard.
			webXMLErrorMessage = e.getMessage();
			return;
		}
		File webInfFile = f.getParentFile();
		try {
			webInfLocation = webInfFile.getCanonicalPath().replace('\\', '/');
		} catch (IOException e) {
			//Do not log this exception. It will be shown in wizard.
			webXMLErrorMessage = e.getMessage();
			return;
		}
		boolean w_i = webInfFile.getName().equals("WEB-INF"); //$NON-NLS-1$
		String lib = webInfLocation + "/lib"; //$NON-NLS-1$
		if(new File(lib).isDirectory()) setLibLocation(lib);
		String cls = webInfLocation + "/classes"; //$NON-NLS-1$
		if(w_i || new File(cls).isDirectory()) setClassesLocation(cls); 
		modules = createAdoptContext().createModulesInfo(webxml, webInfFile);
		createAllModules();
		setProjectJavaSrc();
		if(webxml != null) {
			String sv = WebAppHelper.getServletVersion(webxml);
			if(sv != null && sv.length() > 0) {
				setServletVersion(sv);
			}
		}
	}

	protected void setProjectJavaSrc() {
		IProject project = getProjectHandle();
		String[] src = EclipseResourceUtil.getJavaProjectSrcLocations(project);
		existingSources = src;
		if(src.length > 0) for (int i = 0; i < modules.length; i++) {
			if(modules[i].getAttributeValue("java src").length() == 0) //$NON-NLS-1$
			  modules[i].setAttributeValue("java src", src[0]); //$NON-NLS-1$
		}
		String out = EclipseResourceUtil.getJavaProjectOutputLocation(project);
		if(out != null) setClassesLocation(out);
	}

	public String getPexFileName() {
		return null;
	}
	
	public void setPexFileName(String value) {
	}

	public String getSuggestedProjectLocation() {
		String webRootPath = getWebRootPath();
		File f = new File(webRootPath);
		File fc = f.getParentFile();
		while(fc != null && fc.isDirectory()) {
			if(projectName.equals(fc.getName())) return fc.getAbsolutePath();
			fc = fc.getParentFile();
		}
		if(f.getName().equals("WebContent")) { //$NON-NLS-1$
			return f.getParent();
		}
		return webRootPath;
	}
	
	public boolean canFinish() {
		if(!(webXmlLocation != null && !"".equals(webXmlLocation) && modules.length > 0)) return false; //$NON-NLS-1$
		String message = getModulesErrorMessage(getAllModules(), null);
		if(message == null) message = registry.getErrorMessage();
		return (message == null);
	}
	
	public void setAdoptProjectContext(AdoptWebProjectContext context) {
		webInfLocation = context.getWebInfLocation();
		webXmlLocation = context.getWebXMLLocation();
		classesLocation = context.getClassesPath();
		libLocation = context.getLibPath();
		buildXmlLocation = context.getBuildPath();
		modules = context.getModules();
	}
	
	public void setInitialName(String n) {
		initialName = n;
		setProjectName(n);
		setApplicationName(n);
	}
	
	public boolean isInitialized() {
		return initialName != null;
	}
	
	public void setInitialLocation(String initialLocation) {
		this.initialLocation = initialLocation;
		if(initialLocation != null) { 
			setWebXmlLocation(initialLocation);
			initialLocationErrorMessage = webXMLErrorMessage;
		}
	}

	public String getInitialLocation() {
		return initialLocation;
	}
	
	public String getInitialLocationErrorMessage() {
		return initialLocationErrorMessage;
	}
	
	public String getWebXMLErrorMessage() {
		return webXMLErrorMessage;
	}
	
	protected String getProjectLocation() {
		IProject p = getProjectHandle();
		return (p == null || p.getLocation() == null) ? null : p.getLocation().toString();
	}
	
	public String getModulesErrorMessage(XModelObject[] modules, XModelObject selected) {
		return null;
	}
	
	public boolean createConfigFile(String path) {
		return false;		
	}
	
	protected void createConfigFile(File f, String entity) {
		XModelObject c = XModelObjectLoaderUtil.createValidObject(webxml.getModel(), entity);
		String text = ((SerializingLoader)XModelObjectLoaderUtil.getObjectLoader(c)).serializeObject(c);
		FileUtil.writeFile(f, text);
		try {		
			IProject p = getProjectHandle();		
			if(p.exists() && p.isAccessible()) p.refreshLocal(IProject.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			WebModelPlugin.getPluginLog().logError(e);
		}
	}
	
	public abstract String getNatureID();	
	protected abstract AdoptWebProjectContext createAdoptContext();
	public abstract void addSupportDelta(Properties p);
	public abstract void rollbackSupportDelta();
	public abstract void commitSupportDelta();
	
}
