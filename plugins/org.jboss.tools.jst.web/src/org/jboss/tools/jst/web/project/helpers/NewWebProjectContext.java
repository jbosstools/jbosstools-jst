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
package org.jboss.tools.jst.web.project.helpers;

import java.util.*;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.*;
import org.jboss.tools.jst.web.context.RegisterTomcatContext;

public abstract class NewWebProjectContext {
	public static final String ATTR_NAME                 = "name";
	public static final String ATTR_LOCATION             = "location";
	public static final String ATTR_USE_DEFAULT_LOCATION = "use default path";
	public static final String ATTR_VERSION              = "version";
	public static final String ATTR_TEMPLATE             = "template";
	public static final String ATTR_SERVLET_VERSION      = "servlet version";
	public static final String ATTR_REGISTER_WEB_CONTEXT = "register web context";
	
	public static final String PROPERTY_RUNTIME_TYPE     = "runtimeType";

	protected IWebProjectTemplate template = createTemplate();
	protected ProjectTemplate projectTemplateEdit;

	protected IProject project;
	protected String projectLocation;
	protected String version;
	protected String servletVersion;
	protected String projectTemplate;

	protected RegisterTomcatContext registry;
	
	public NewWebProjectContext() {
		registry = new RegisterTomcatContext(RegisterTomcatContext.PROJECT_MODE_NEW);
		initRegistry();
	}
	
	protected abstract IWebProjectTemplate createTemplate();

	protected void initRegistry() {
		registry.init();
	}

	public IPath getLocationPath() {
		return new Path(projectLocation);
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		if(this.project == null || !this.project.getName().equals(registry.getApplicationName())) {
			registry.setApplicationName(project.getName());
			registry.setProjectHandle(project);
		}
		this.project = project;
	}

	public Properties getActionProperties() {
		Properties result = new Properties();		
		result.setProperty(ATTR_NAME, project.getName());
		result.setProperty(ATTR_LOCATION, projectLocation);
		result.setProperty(ATTR_VERSION, version);
		result.setProperty(ATTR_TEMPLATE, projectTemplate);
		result.setProperty(ATTR_SERVLET_VERSION, servletVersion);
		if(registry.getRuntime() != null) {
			result.setProperty(PROPERTY_RUNTIME_TYPE, registry.getRuntime().getRuntimeType().getId());
		}
		if(projectTemplateEdit != null) {
			result.put("preprocessingProperties", projectTemplateEdit.getProperties());
		}
		return result;
	}

	public void setProjectLocation(String value) {
		projectLocation = value;
	}

	public void setProjectTemplate(String value) {
		if(projectTemplate != null && projectTemplate.equals(value)) return;
		projectTemplate = value;
		projectTemplateEdit = template.getProjectTemplate(version, projectTemplate);
	}

	public void setServletVersion(String value) {
		servletVersion = value;
		registry.setServletVersion(value);
	}

	public RegisterTomcatContext getRegisterTomcatContext() {
		return registry;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String value)	{
		if(version != null && version.equals(value)) return; 
		version = value;
		projectTemplateEdit = template.getProjectTemplate(version, projectTemplate);
	}
	
	public IWebProjectTemplate getTemplate() {
		return template;
	}
	
	public ProjectTemplate getProjectTemplate() {
		return projectTemplateEdit;
	}
	
}
