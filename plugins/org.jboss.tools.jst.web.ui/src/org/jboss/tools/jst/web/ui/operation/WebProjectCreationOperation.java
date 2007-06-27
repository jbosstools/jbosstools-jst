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
import java.text.MessageFormat;
import java.util.Properties;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.model.ServiceDialog;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelConstants;
import org.jboss.tools.common.model.XModelFactory;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.FileSystemsHelper;
import org.jboss.tools.common.model.filesystems.impl.FileSystemImpl;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.project.ProjectHome;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.context.RegisterTomcatContext;
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;
import org.jboss.tools.jst.web.project.helpers.IWebProjectTemplate;
import org.jboss.tools.jst.web.project.helpers.NewWebProjectContext;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

public abstract class WebProjectCreationOperation extends WebNatureOperation {
	protected IWebProjectTemplate template = createTemplate();
	protected XModel templateModel = null;

	public WebProjectCreationOperation(IProject project, IPath projectLocation, RegisterTomcatContext registry, Properties properties)	{
		super(project, projectLocation, registry, properties);
	}

	public WebProjectCreationOperation(NewWebProjectContext context) {
		this(context.getProject(), context.getLocationPath(), context.getRegisterTomcatContext(), context.getActionProperties());
		setProperty(WebNatureOperation.PROJECT_NAME_ID, context.getActionProperties().getProperty(NewWebProjectContext.ATTR_NAME));
		setProperty(WebNatureOperation.PROJECT_LOCATION_ID, context.getActionProperties().getProperty(NewWebProjectContext.ATTR_LOCATION));
		setProperty(WebNatureOperation.USE_DEFAULT_LOCATION_ID, context.getActionProperties().getProperty(NewWebProjectContext.ATTR_USE_DEFAULT_LOCATION));
		setProperty(WebNatureOperation.TEMPLATE_ID, context.getActionProperties().getProperty(NewWebProjectContext.ATTR_TEMPLATE));
		setProperty(WebNatureOperation.TEMPLATE_VERSION_ID, context.getActionProperties().getProperty(NewWebProjectContext.ATTR_VERSION));
		setProperty(WebNatureOperation.SERVLET_VERSION_ID, context.getActionProperties().getProperty(NewWebProjectContext.ATTR_SERVLET_VERSION));
		setProperty(WebNatureOperation.REGISTER_WEB_CONTEXT_ID, context.getActionProperties().getProperty(NewWebProjectContext.ATTR_REGISTER_WEB_CONTEXT));
		setProperty(WebNatureOperation.RUNTIME_NAME, context.getRegisterTomcatContext().getRuntimeName());
		setProperty(WebNatureOperation.JAVA_SOURCES_LOCATION_ID, getJavaSources());
	}
	
	protected abstract IWebProjectTemplate createTemplate();
	protected abstract void copyTemplate() throws Exception;

	protected void createWebNature() throws CoreException {
		Properties properties = this.getWizardPropertiesAsIs();
		properties.setProperty(NewWebProjectContext.ATTR_LOCATION, getProject().getLocation().toString());
		try	{
			createTemplateModel();
			copyTemplate();
		} catch (Exception e) {
			WebUiPlugin.getPluginLog().logError(e);
			String message = e.getMessage();
			if(message == null || message.length() == 0) message = e.getClass().getName(); 
			throw new CoreException(new Status(IStatus.ERROR, ModelUIPlugin.PLUGIN_ID, 1, message, e));
		}
		copyProjectFile(properties);
		EclipseResourceUtil.addNatureToProject(getProject(), getNatureID());
		IModelNature strutsProject = (IModelNature)getProject().getNature(getNatureID());
		model = strutsProject.getModel();
		XModelObject fso = FileSystemsHelper.getFileSystems(model);
		properties.setProperty("skipWizard", "yes");
		properties.setProperty("name", getProject().getName());
		XActionInvoker.invoke("CreateStrutsProject", fso, properties);
		
		XModelObject web = model.getByPath("Web");
		if (web != null && properties.containsKey(NewWebProjectContext.ATTR_SERVLET_VERSION))
			model.changeObjectAttribute(web, NewWebProjectContext.ATTR_SERVLET_VERSION, properties.getProperty("servlet version"));
		XModelObject webxml = WebAppHelper.getWebApp(model);
		if(webxml != null) {
			model.changeObjectAttribute(webxml, "display-name", "" + getProject().getName());
		}
	}
	
	protected void copyProjectFile(Properties p) {
		String templateFolder = template.getProjectTemplatesLocation(
				getProperty(TEMPLATE_VERSION_ID)) + "/" + 
				getProperty(TEMPLATE_ID) + "/";
		File sf =  new File(templateFolder + IModelNature.PROJECT_FILE);
		String tf = p.getProperty(NewWebProjectContext.ATTR_LOCATION) + "/" + IModelNature.PROJECT_FILE;
		if(sf.exists())	{
			FileUtil.copyFile(sf, new File(tf), true);
		} else {
			throw new RuntimeException("Project template must have model configuration file");
		}
///for wtp Dynamic Project
		adjustProjectFile(new File(tf));
	}
	private void adjustProjectFile(File f) {
		if(!isMultipleModulesProject()) return;
		
		String text = FileUtil.readFile(f);
		if(text == null || text.length() == 0) return;
		String match = "\"./WebContent/WEB-INF\"";
		int i = text.indexOf(match);
		if(i >= 0) {
			text = text.substring(0, i) + "\"./" + getProject().getName() + "/WebContent/WEB-INF\"" + text.substring(i + match.length());
		}
		
		match = "%redhat.workspace%/classes";
		i = text.indexOf(match);
		if(i >= 0) {
			String replace = "%redhat.workspace%/../../../.deployables/" + getProject().getName() + "/WEB-INF/classes";
			text = text.substring(0, i) + replace + text.substring(i + match.length());
		}
		
		FileUtil.writeFile(f, text);
	}
	
	protected void createTemplateModel() throws Exception {
		if(templateModel != null) return;
		String templateLocation = getTemplateLocation();
		Properties p = new Properties();
		p.putAll(System.getProperties());
		String workspace = new ProjectHome().getLocation(templateLocation);
		p.setProperty(XModelConstants.WORKSPACE, workspace);
		p.setProperty(IModelNature.ECLIPSE_PROJECT, templateLocation);
		templateModel = XModelFactory.getModel(p);
	}
	
	protected String getTemplateLocation() throws Exception {
		String fileName = template.getProjectTemplatesLocation(
				getProperty(TEMPLATE_VERSION_ID)) + "/" +
				getProperty(TEMPLATE_ID) + "/";
		try {
			return new File(fileName).getCanonicalPath();
		} catch (Exception e) {
			WebUiPlugin.getPluginLog().logError("Cannot find folder '" + fileName + "'", null);
			return fileName;
		}
	}

	public static final String WARNING_MESSAGE = "COD_MESSAGE";
	public static final String WARNING_TITLE   = "COD_TITLE";
	public static final String BTN_CANCEL      = "BTN_CANCEL";
	public static final String BTN_OK          = "BTN_OK";
	
	protected boolean checkOverwrite() {
		String location = getProperty(PROJECT_LOCATION_ID);
		
		if(location == null) return true;
		
		File targetFile = new File(location);
		File[] cs = (targetFile.exists()) ? targetFile.listFiles() : null;
		
		if(cs != null && cs.length > 0) {
			ServiceDialog dlg = PreferenceModelUtilities.getPreferenceModel().getService();
			
			ResourceBundle bundle = ResourceBundle.getBundle(WebProjectCreationOperation.class.getName());
			
			String message = MessageFormat.format(
			        bundle.getString(WARNING_MESSAGE),new Object[]{location}
				);			
			
			int selAction = dlg.showDialog(
				bundle.getString(WARNING_TITLE), 
				message, 
				new String[]{bundle.getString(BTN_OK),bundle.getString(BTN_CANCEL)}, 
				null, 
				ServiceDialog.WARNING
			);

			if(selAction != 0) return false; 
		}
		
		return true;
	}
	
	private String[] getJavaSources() {
		try {
			createTemplateModel();
		} catch (Exception e) {
			//ignore
		}
		if(templateModel != null) {
			XModelObject o = FileSystemsHelper.getFileSystem(templateModel, "src");
			if(o instanceof FileSystemImpl) {
				String s = ((FileSystemImpl)o).getAbsoluteLocation();
				File f = new File(s);
				if(f.exists()) {
					return new String[]{f.getName()};
				}
			}
		}
		return new String[0];
	}

}
