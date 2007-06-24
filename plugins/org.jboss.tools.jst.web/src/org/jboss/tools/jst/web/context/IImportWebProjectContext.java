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

import org.eclipse.core.resources.IProject;

import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;

public interface IImportWebProjectContext {
	public String getProjectName();
	public IProject getProjectHandle();
	public String getApplicationName();
	public String getWebInfLocation();
	public String getWebRootPath();
	public String getWebXmlLocation();
	public XModelObject[] getModules();
	public String getClassesLocation();
	public String[] getJavaSources();
	public String[] getExistingSources();
	public String getLibLocation();
	public String getBuildXmlLocation();
	public boolean getAddLibraries();
	public String getServletVersion();
	public String getTemplateVersion();
	public String getSuggestedProjectLocation();
	public RegisterTomcatContext getRegisterTomcatContext();
	public String SERVLET_VERSION_WARNING = WebUIMessages.SERVLET_VERSION_WARNING;
	public boolean isServletVersionConsistentToWebXML();
	public void convertWebXML(boolean backup);
}
