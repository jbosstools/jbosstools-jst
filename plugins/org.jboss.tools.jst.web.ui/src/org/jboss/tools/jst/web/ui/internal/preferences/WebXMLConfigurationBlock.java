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

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.internal.ui.preferences.OptionsConfigurationBlock.Key;
import org.eclipse.jdt.internal.ui.wizards.IStatusChangeListener;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;
import org.jboss.tools.common.preferences.SeverityPreferences;
import org.jboss.tools.common.ui.preferences.SeverityConfigurationBlock;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.validation.WebXMLPreferences;

/**
 * @author Viacheslav Kabanovich
 */
public class WebXMLConfigurationBlock extends SeverityConfigurationBlock {

	private static final String SETTINGS_SECTION_NAME = "WebXMLConfigurationBlock"; //$NON-NLS-1$

	private static SectionDescription SECTION_CLASS_REFERENCES = new SectionDescription(
		WebXMLPreferencesMessages.WebXMLValidatorConfigurationBlock_section_classreferences,
		new String[][]{
			{WebXMLPreferences.INVALID_EXCEPTION_TYPE, WebXMLPreferencesMessages.WebXMLValidatorConfigurationBlock_pb_invalidExceptionType_label},
			{WebXMLPreferences.INVALID_SERVLET_CLASS, WebXMLPreferencesMessages.WebXMLValidatorConfigurationBlock_pb_invalidServletClass_label},
			{WebXMLPreferences.INVALID_FILTER_CLASS, WebXMLPreferencesMessages.WebXMLValidatorConfigurationBlock_pb_invalidFilterClass_label},
			{WebXMLPreferences.INVALID_LISTENER_CLASS, WebXMLPreferencesMessages.WebXMLValidatorConfigurationBlock_pb_invalidListenerClass_label},
			{WebXMLPreferences.INVALID_MESSAGE_DESTINATION_TYPE, WebXMLPreferencesMessages.WebXMLValidatorConfigurationBlock_pb_invalidMessageDestinationType_label},
			{WebXMLPreferences.INVALID_RES_TYPE, WebXMLPreferencesMessages.WebXMLValidatorConfigurationBlock_pb_invalidResType_label},
		},
		WebModelPlugin.PLUGIN_ID
	);

	private static SectionDescription SECTION_RESOURCE_REFERENCES = new SectionDescription(
		WebXMLPreferencesMessages.WebXMLValidatorConfigurationBlock_section_resourcereferences,
		new String[][]{
			{WebXMLPreferences.INVALID_ERROR_PAGE_REF, WebXMLPreferencesMessages.WebXMLValidatorConfigurationBlock_pb_invalidErrorPageRef_label},
			{WebXMLPreferences.INVALID_FORM_ERROR_PAGE_REF, WebXMLPreferencesMessages.WebXMLValidatorConfigurationBlock_pb_invalidFormErrorPageRef_label},
			{WebXMLPreferences.INVALID_FORM_LOGIN_PAGE_REF, WebXMLPreferencesMessages.WebXMLValidatorConfigurationBlock_pb_invalidFormLoginPageRef_label},
			{WebXMLPreferences.INVALID_JSP_FILE_REF, WebXMLPreferencesMessages.WebXMLValidatorConfigurationBlock_pb_invalidJspFileRef_label},
			{WebXMLPreferences.INVALID_ICON_REF, WebXMLPreferencesMessages.WebXMLValidatorConfigurationBlock_pb_invalidIconRef_label},
			{WebXMLPreferences.INVALID_TAGLIB_REF, WebXMLPreferencesMessages.WebXMLValidatorConfigurationBlock_pb_invalidTaglibRef_label},
			{WebXMLPreferences.INVALID_WELCOME_FILE_REF, WebXMLPreferencesMessages.WebXMLValidatorConfigurationBlock_pb_invalidWelcomeFileRef_label},
		},
		WebModelPlugin.PLUGIN_ID
	);

	private static SectionDescription SECTION_OBJECT_REFERENCES = new SectionDescription(
		WebXMLPreferencesMessages.WebXMLValidatorConfigurationBlock_section_objectreferences,
		new String[][]{
			{WebXMLPreferences.INVALID_SERVLET_REF, WebXMLPreferencesMessages.WebXMLValidatorConfigurationBlock_pb_invalidServletRef_label},
			{WebXMLPreferences.INVALID_FILTER_REF, WebXMLPreferencesMessages.WebXMLValidatorConfigurationBlock_pb_invalidFilterRef_label},
			{WebXMLPreferences.INVALID_ROLE_REF, WebXMLPreferencesMessages.WebXMLValidatorConfigurationBlock_pb_invalidRoleRef_label},
		},
		WebModelPlugin.PLUGIN_ID
	);

	public static SectionDescription[] ALL_SECTIONS = new SectionDescription[]{
		SECTION_CLASS_REFERENCES,
		SECTION_RESOURCE_REFERENCES,
		SECTION_OBJECT_REFERENCES,
	};

	private static Key[] getKeys() {
		ArrayList<Key> keys = new ArrayList<Key>();
		keys.add(ENABLE_BLOCK_KEY);
		for (SectionDescription s: ALL_SECTIONS) {
			s.collectKeys(keys);
		}
		keys.add(MAX_NUMBER_OF_PROBLEMS_KEY);
		return keys.toArray(new Key[0]);
	}

	protected final static Key ENABLE_BLOCK_KEY = getKey(WebModelPlugin.PLUGIN_ID, SeverityPreferences.ENABLE_BLOCK_PREFERENCE_NAME);

	@Override
	protected Key getEnableBlockKey() {
		return ENABLE_BLOCK_KEY;
	}

	private static final Key MAX_NUMBER_OF_PROBLEMS_KEY = getKey(WebModelPlugin.PLUGIN_ID, SeverityPreferences.MAX_NUMBER_OF_MARKERS_PREFERENCE_NAME);

	@Override
	protected Key getMaxNumberOfProblemsKey() {
		return MAX_NUMBER_OF_PROBLEMS_KEY;
	}

	public WebXMLConfigurationBlock(IStatusChangeListener context,
			IProject project, IWorkbenchPreferenceContainer container) {
		super(context, project, getKeys(), container);
	}

	@Override
	protected SectionDescription[] getAllSections() {
		return ALL_SECTIONS;
	}

	@Override
	protected String getCommonDescription() {
		return WebXMLPreferencesMessages.WebXMLValidatorConfigurationBlock_common_description;
	}

	@Override
	protected IDialogSettings getDialogSettings() {
		return WebUiPlugin.getDefault().getDialogSettings().getSection(SETTINGS_SECTION_NAME);
	}

	@Override
	protected String getQualifier() {
		return WebModelPlugin.PLUGIN_ID;
	}
}