/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.web.validation;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.jboss.tools.common.preferences.SeverityPreferences;
import org.jboss.tools.jst.web.WebModelPlugin;

/**
 * @author Viacheslav Kabanovich
 */
public class WebXMLPreferences extends SeverityPreferences {

	public static final Set<String> SEVERITY_OPTION_NAMES = new HashSet<String>();

	private static WebXMLPreferences INSTANCE = new WebXMLPreferences();

	//Class References group
	public static final String INVALID_EXCEPTION_TYPE = INSTANCE.createSeverityOption("invalidExceptionType"); //$NON-NLS-1$
	public static final String INVALID_FILTER_CLASS = INSTANCE.createSeverityOption("invalidFilterClass"); //$NON-NLS-1$
	public static final String INVALID_LISTENER_CLASS = INSTANCE.createSeverityOption("invalidListenerClass"); //$NON-NLS-1$
	public static final String INVALID_MESSAGE_DESTINATION_TYPE = INSTANCE.createSeverityOption("invalidMessageDestinationType"); //$NON-NLS-1$
	public static final String INVALID_RES_TYPE = INSTANCE.createSeverityOption("invalidResType"); //$NON-NLS-1$
	public static final String INVALID_SERVLET_CLASS = INSTANCE.createSeverityOption("invalidServletClass"); //$NON-NLS-1$

	//Resource References group
	public static final String INVALID_ERROR_PAGE_REF = INSTANCE.createSeverityOption("invalidErrorPageRef"); //$NON-NLS-1$
	public static final String INVALID_FORM_ERROR_PAGE_REF = INSTANCE.createSeverityOption("invalidFormErrorPageRef"); //$NON-NLS-1$
	public static final String INVALID_FORM_LOGIN_PAGE_REF = INSTANCE.createSeverityOption("invalidFormLoginPageRef"); //$NON-NLS-1$
	public static final String INVALID_JSP_FILE_REF = INSTANCE.createSeverityOption("invalidJspFileRef"); //$NON-NLS-1$
	public static final String INVALID_ICON_REF = INSTANCE.createSeverityOption("invalidIconRef"); //$NON-NLS-1$
	public static final String INVALID_TAGLIB_REF = INSTANCE.createSeverityOption("invalidTaglibRef"); //$NON-NLS-1$
	public static final String INVALID_WELCOME_FILE_REF = INSTANCE.createSeverityOption("invalidWelcomeFileRef"); //$NON-NLS-1$

	//Object References group
	public static final String INVALID_SERVLET_REF = INSTANCE.createSeverityOption("invalidServletRef"); //$NON-NLS-1$
	public static final String INVALID_FILTER_REF = INSTANCE.createSeverityOption("invalidFilterRef"); //$NON-NLS-1$
	public static final String INVALID_ROLE_REF = INSTANCE.createSeverityOption("invalidRoleRef"); //$NON-NLS-1$
	
	/**
	 * @return the only instance of CDIPreferences
	 */
	public static WebXMLPreferences getInstance() {
		return INSTANCE;
	}

	private WebXMLPreferences() {
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.common.preferences.SeverityPreferences#createSeverityOption(java.lang.String)
	 */
	@Override
	protected String createSeverityOption(String shortName) {
		String name = getPluginId() + ".validator.problem." + shortName; //$NON-NLS-1$
		SEVERITY_OPTION_NAMES.add(name);
		return name;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.common.preferences.SeverityPreferences#getPluginId()
	 */
	@Override
	protected String getPluginId() {
		return WebModelPlugin.PLUGIN_ID;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.common.preferences.SeverityPreferences#getSeverityOptionNames()
	 */
	@Override
	protected Set<String> getSeverityOptionNames() {
		return SEVERITY_OPTION_NAMES;
	}

	public static boolean shouldValidateCore(IProject project) {
		return true;
	}

	public static boolean isValidationEnabled(IProject project) {
		return INSTANCE.isEnabled(project);
	}

	public static int getMaxNumberOfProblemMarkersPerFile(IProject project) {
		return INSTANCE.getMaxNumberOfProblemMarkersPerResource(project);
	}
}