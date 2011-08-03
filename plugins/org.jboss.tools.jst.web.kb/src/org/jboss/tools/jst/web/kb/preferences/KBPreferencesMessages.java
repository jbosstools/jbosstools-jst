/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.web.kb.preferences;

import org.eclipse.osgi.util.NLS;

/**
 * @author Viacheslav Kabanovich
 */
public class KBPreferencesMessages extends NLS {
	private static final String BUNDLE_NAME = KBPreferencesMessages.class.getName();

	public static String KB_DESCRIPTION;

	public static String KB_SETTINGS_PREFERENCE_PAGE_KB_SUPPORT;
	public static String KB_VALIDATOR_PREFERENCE_PAGE_KB_VALIDATOR;
	
	public static String KBValidationConfigurationBlock_common_description;
	public static String KBValidationConfigurationBlock_section_config;
	public static String KBValidationConfigurationBlock_pb_requiredKbCapabilitiesAreMissing_label;
	public static String KBValidationConfigurationBlock_pb_kbCapabilitiesAreNotEnabledInJavaModule_label;

	static {
		NLS.initializeMessages(BUNDLE_NAME, KBPreferencesMessages.class);
	}
}