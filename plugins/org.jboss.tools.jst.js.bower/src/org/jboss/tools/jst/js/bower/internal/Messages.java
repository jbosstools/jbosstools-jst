/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *       Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.jst.js.bower.internal;

import org.eclipse.osgi.util.NLS;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = Messages.class.getName().toString().toLowerCase();

	public static String BowerPreferencePage_BowerLocationLabel;
	public static String BowerPreferencePage_NotSpecifiedBowerWarning;
	public static String BowerPreferencePage_NotValidBowerError;

	public static String BowerErrorHandler_BowerNotDefinedTitle;
	public static String BowerErrorHandler_BowerNotDefinedMessage;
	
	public static String BowerLaunchConfigurationTab_launchMainTabName;
	public static String BowerLaunchConfigurationTab_addParameter;
	public static String BowerLaunchConfigurationTab_editParameter;
	public static String BowerLaunchConfigurationTab_buttonAdd;
	public static String BowerLaunchConfigurationTab_buttonEdit;
	public static String BowerLaunchConfigurationTab_buttonRemove;
	public static String BowerLaunchConfigurationTab_Author;
	public static String BowerLaunchConfigurationTab_Authors;
	public static String BowerLaunchConfigurationTab_Ignore;
	public static String BowerLaunchConfigurationTab_Properties;
	public static String BowerLaunchConfigurationTab_Name;
	public static String BowerLaunchConfigurationTab_Version;
	public static String BowerLaunchConfigurationTab_License;
	public static String BowerLaunchConfigurationTab_useDefaulConfiguration;
	public static String BowerLaunchConfigurationTab_baseDirectory;
	public static String BowerLaunchConfigurationTab_browseWorkspace;
	public static String BowerLaunchConfigurationTab_rootFolderSelection;
	
	public static String BowerInitWizard_pageName;
	public static String BowerInitWizard_pageTitle;
	public static String BowerInitWizard_pageDescription;
	public static String BowerInitWizard_windowTitle;
	public static String BowerInitWizard_errorDirNotDefiened;
	public static String BowerInitWizard_errorDirNotExist;
	public static String BowerInitWizard_errorBowerJsonAlreadyExist;
	public static String BowerInitWizard_addAuthor;
	public static String BowerInitWizard_editAuthor;
	public static String BowerInitWizard_addIgnore;
	public static String BowerInitWizard_editIgnore;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
	
}
