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
package org.jboss.tools.jst.web.ui.operation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.common.model.ServiceDialog;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.model.util.XMLUtil;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.web.context.IImportWebProjectContext;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.w3c.dom.Element;

public abstract class WebProjectAdoptOperation extends WebNatureOperation {
	protected IImportWebProjectContext context;

	public WebProjectAdoptOperation(IImportWebProjectContext context) {
		super(context.getProjectHandle(), new Path(context.getSuggestedProjectLocation()), context.getRegisterServerContext(), new Properties());
		setProperty(WebNatureOperation.ANT_BUILD_XML_ID, context.getBuildXmlLocation().replace('\\', '/'));
		setProperty(WebNatureOperation.JAVA_CLASSES_LOCATION_ID, context.getClassesLocation().replace('\\', '/'));
		setProperty(WebNatureOperation.JAVA_SOURCES_LOCATION_ID, context.getJavaSources());
		setProperty(JAVA_CREATE_SOURCE_FOLDERS_ID, Boolean.FALSE);
		setProperty(WebNatureOperation.WEB_INF_LIBRARY_LOCATION_ID, context.getLibLocation().replace('\\', '/'));
		setProperty(WebNatureOperation.PROJECT_NAME_ID, context.getProjectName());
		setProperty(WebNatureOperation.PROJECT_LOCATION_ID, context.getSuggestedProjectLocation().replace('\\', '/'));
		setProperty(WebNatureOperation.SERVLET_VERSION_ID, context.getServletVersion());
		setProperty(WebNatureOperation.WEB_INF_LOCATION_ID, context.getWebInfLocation().replace('\\', '/'));
		setProperty(WebNatureOperation.WEB_CONTENT_LOCATION_ID, context.getWebRootPath().replace('\\', '/'));
		setProperty(WebNatureOperation.WEB_XML_LOCATION_ID, context.getWebXmlLocation().replace('\\', '/'));
		setProperty(WebNatureOperation.RUNTIME_NAME, context.getRegisterServerContext().getRuntimeName());
		this.context = context;
	}
	
	protected boolean isLinkingToProjectOutsideWorkspace() {
		return context.isLinkingToProjectOutsideWorkspace();
	}
		
	protected boolean hasJavaSource() {
		String[] s = context.getJavaSources();
		if(s.length == 0) return false;
		for (int i = 0; i < s.length; i++) {
			if(s[i] != null && s[i].length() > 0) return true;
		}
		return false;
	}
	
	protected void preCreateWebNature() throws CoreException {
		setWorkspaceHome(getProperty(WebNatureOperation.WEB_INF_LOCATION_ID));
		copyLibraries();
	}

	protected void createWebNature() throws CoreException {
//		setWorkspaceHome(getProperty(WebNatureOperation.WEB_INF_LOCATION_ID));
//		copyLibraries();
		EclipseResourceUtil.addNatureToProject(getProject(), getNatureID());
		IModelNature nature = (IModelNature)getProject().getNature(getNatureID());
		model = nature.getModel(); // model is XModel
		try	{
			execute();
			String sv = context.getServletVersion();
			if(sv == null || sv.length() == 0) sv = getDefaultServletVersion();
			model.changeObjectAttribute(model.getByPath("Web"), "servlet version", sv); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (XModelException ex) {
			WebUiPlugin.getPluginLog().logError(ex);
		}
	}
	
	protected void postCreateWebNature() {
		
	}

	protected void copyLibraries() {}
	
	protected String getDefaultServletVersion() {
		return ""; //$NON-NLS-1$
	}
	
	protected abstract void execute() throws XModelException;

	void setWorkspaceHome(String path) {
		String relativePath = FileUtil.getRelativePath(getProject().getLocation().toString(), path);
		if (relativePath != null) path = "." + relativePath; //$NON-NLS-1$
		Element element = XMLUtil.createDocumentElement("struts"); //$NON-NLS-1$
		element = XMLUtil.createElement(element, "workspace_home"); //$NON-NLS-1$
		element.appendChild(element.getOwnerDocument().createTextNode(path));

		OutputFormat format = new OutputFormat("xml", "UTF-8", true); //$NON-NLS-1$ //$NON-NLS-2$
		format.setLineSeparator("\n"); //$NON-NLS-1$
		format.setIndent(2);

		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(getProject().getLocation().toString() + "/" + IModelNature.PROJECT_TEMP); //$NON-NLS-1$
			XMLSerializer ser = new XMLSerializer(fileWriter, format);
			ser.asDOMSerializer();
			ser.serialize(element);	
		} catch (IOException ex) {
			WebUiPlugin.getPluginLog().logError(ex);
		} finally {
			try {
				if (fileWriter != null) fileWriter.close();
			} catch (IOException ex) {
				WebUiPlugin.getPluginLog().logError(ex);
			}
		}
	}
	
	protected boolean checkOverwrite() {
		IProject project = getProject();
		if(project == null) return false;
		if(project.exists()) return true;
		String projectLocation = getProperty(PROJECT_LOCATION_ID);
		if(isLinkingToProjectOutsideWorkspace()) {
			String root = ModelPlugin.getWorkspace().getRoot().getLocation().toString().replace('\\', '/');
			if(!projectLocation.replace('\\','/').startsWith(root + "/")) { //$NON-NLS-1$
				projectLocation = root + "/" + project.getName(); //$NON-NLS-1$
			}
		}
		IPath path = new Path(projectLocation);
		String dots = null;
		for (int i = 0; i < dotFilesList.length && dots == null; i++) {
			File file = path.append(dotFilesList[i]).toFile();
			if (file.exists()) dots = dotFilesList[i];
		}
		if(dots == null) return true;

		ServiceDialog d = PreferenceModelUtilities.getPreferenceModel().getService();
		String message = MessageFormat.format(
	        Messages.ADOPT_WILL_OVERWRITE_DOT_FILES_MESSAGE,new Object[]{dots} 
		);			
		int q = d.showDialog(Messages.WebProjectAdoptOperation_Warning, message, new String[]{Messages.WebProjectAdoptOperation_Continue, Messages.WebProjectAdoptOperation_Cancel}, null, ServiceDialog.WARNING);
		if(q != 0) return false;
		
		if(!checkClearWorkspace()) return false;
		
		return true;
	}
	
	protected boolean checkClearWorkspace() {
		if(getProject().exists()) return true;
		
		String location = getProperty(PROJECT_LOCATION_ID).replace('\\', '/');
		String root = ModelPlugin.getWorkspace().getRoot().getLocation().toString().replace('\\', '/');
		String wsProjectLocation = (root + "/" + getProject().getName()).replace('\\', '/');; //$NON-NLS-1$
		if(location.equals(wsProjectLocation) || location.startsWith(wsProjectLocation + "/")) return true; //$NON-NLS-1$
		File wsf = new File(wsProjectLocation); 
		if(!wsf.isDirectory()) return true;
		File[] cs = wsf.listFiles();
		if(cs == null || cs.length == 0) return true;
		
		ServiceDialog d = PreferenceModelUtilities.getPreferenceModel().getService();
		String message = MessageFormat.format(
		    Messages.ADOPT_WILL_CLEAR_WORKSPACE_MESSAGE,new Object[]{getProject().getName()} 
		);			
		int q = d.showDialog(Messages.WebProjectAdoptOperation_Warning, message, new String[]{Messages.WebProjectAdoptOperation_Continue, Messages.WebProjectAdoptOperation_Cancel}, null, ServiceDialog.WARNING);
		if(q != 0) return false;
		
		for (int i = 0; i < cs.length; i++) {
			FileUtil.remove(cs[i]);
		}
		return true;
	}

	protected void removeDotFiles(IPath projectLocation) {
		if(isLinkingToProjectOutsideWorkspace()) {
			return;
		} else {
			super.removeDotFiles(projectLocation);
		}
	}
	
}
