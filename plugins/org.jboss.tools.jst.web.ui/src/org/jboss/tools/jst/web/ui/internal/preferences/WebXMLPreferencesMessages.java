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
package org.jboss.tools.jst.web.ui.internal.preferences;

import org.eclipse.osgi.util.NLS;

/**
 * @author Viacheslav Kabanovich
 */
public class WebXMLPreferencesMessages extends NLS {
	private static final String BUNDLE_NAME = WebXMLPreferencesMessages.class.getName();

	// Validator Preference page
	public static String WebXMLValidatorConfigurationBlock_common_description;

	// Section Name
	public static String WebXMLValidatorConfigurationBlock_section_classreferences;
	public static String WebXMLValidatorConfigurationBlock_pb_invalidExceptionType_label;
	public static String WebXMLValidatorConfigurationBlock_pb_invalidFilterClass_label;
	public static String WebXMLValidatorConfigurationBlock_pb_invalidListenerClass_label;
	public static String WebXMLValidatorConfigurationBlock_pb_invalidMessageDestinationType_label;
	public static String WebXMLValidatorConfigurationBlock_pb_invalidResType_label;
	public static String WebXMLValidatorConfigurationBlock_pb_invalidServletClass_label;

	public static String WebXMLValidatorConfigurationBlock_section_resourcereferences;
	public static String WebXMLValidatorConfigurationBlock_pb_invalidErrorPageRef_label;
	public static String WebXMLValidatorConfigurationBlock_pb_invalidFormErrorPageRef_label;
	public static String WebXMLValidatorConfigurationBlock_pb_invalidFormLoginPageRef_label;
	public static String WebXMLValidatorConfigurationBlock_pb_invalidJspFileRef_label;
	public static String WebXMLValidatorConfigurationBlock_pb_invalidIconRef_label;
	public static String WebXMLValidatorConfigurationBlock_pb_invalidTaglibRef_label;
	public static String WebXMLValidatorConfigurationBlock_pb_invalidWelcomeFileRef_label;

	public static String WebXMLValidatorConfigurationBlock_section_objectreferences;
	public static String WebXMLValidatorConfigurationBlock_pb_invalidServletRef_label;
	public static String WebXMLValidatorConfigurationBlock_pb_invalidFilterRef_label;
	public static String WebXMLValidatorConfigurationBlock_pb_invalidRoleRef_label;

	public static String PREFERENCE_PAGE_WEB_XML_VALIDATOR;

	static {
		NLS.initializeMessages(BUNDLE_NAME, WebXMLPreferencesMessages.class);
	}
}