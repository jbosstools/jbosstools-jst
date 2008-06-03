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

import org.jboss.tools.jst.web.project.version.ProjectVersions;

public interface IWebProjectTemplate {
	public String getTemplatesBase();
	public String getProjectTemplatesLocation();
	public ProjectVersions getProjectVersions();
	public String[] getVersionList();
	public String[] getLibraries(String version);
	public String getProjectTemplatesLocation(String version);
	public String[] getTemplateList(String version);
	public String getDefaultVersion();
	public String getDefaultTemplate(String version);
	public ProjectTemplate getProjectTemplate(String version, String name);
}
