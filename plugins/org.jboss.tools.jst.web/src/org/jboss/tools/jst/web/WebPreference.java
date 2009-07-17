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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.tools.common.model.options.Preference;

public class WebPreference extends Preference
{
	private static List<WebPreference> webPreferenceList = new ArrayList<WebPreference>();
			
	public static final String OPTIONS_NEW_PROJECT_PATH = "%Options%/Struts Studio/Project/New Project"; //$NON-NLS-1$
	public static final String OPTIONS_IMPORT_PROJECT_PATH = "%Options%/Struts Studio/Project/Import Project"; //$NON-NLS-1$
	public static final String OPTIONS_RUNNING_PATH     = "%Options%/Struts Studio/Running"; //$NON-NLS-1$

	public static final WebPreference BROWSER_PATH		        = new WebPreference(OPTIONS_RUNNING_PATH, "Browser Path"); //$NON-NLS-1$
	public static final WebPreference BROWSER_PREFIX           = new WebPreference(OPTIONS_RUNNING_PATH, "Browser Prefix"); //$NON-NLS-1$

	public static final WebPreference DEFAULT_WTP_SERVER 		= new WebPreference(OPTIONS_RUNNING_PATH, "Default WTP Server"); //$NON-NLS-1$
	public static final WebPreference USE_DEFAULT_JVM   		= new WebPreference(OPTIONS_RUNNING_PATH, "Use Default Eclipse JVM"); //$NON-NLS-1$
	public static final WebPreference SERVER_JVM         		= new WebPreference(OPTIONS_RUNNING_PATH, "JVM"); //$NON-NLS-1$
	public static final WebPreference SERVER_WARNING         		= new WebPreference(OPTIONS_RUNNING_PATH, "show_warning"); //$NON-NLS-1$

	public static String ATTR_REGISTER_IN_SERVER = "Register Web Context in server.xml"; //$NON-NLS-1$

	public static final WebPreference DEFAULT_SERVLET_VERSION  = new WebPreference(OPTIONS_NEW_PROJECT_PATH, "Servlet Version"); //$NON-NLS-1$

	protected WebPreference(String optionPath, String attributeName) {
		super(optionPath, attributeName);
		webPreferenceList.add(this);
	}
	
	public static List getPreferenceList() {
		return Collections.unmodifiableList(webPreferenceList);	
	}

}
