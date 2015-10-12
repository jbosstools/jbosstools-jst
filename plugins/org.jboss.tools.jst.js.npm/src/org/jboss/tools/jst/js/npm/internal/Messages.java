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
package org.jboss.tools.jst.js.npm.internal;

import org.eclipse.osgi.util.NLS;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = Messages.class.getName().toString().toLowerCase();

	public static String NpmPreferencePage_NpmLocationLabel;
	public static String NpmPreferencePage_NotSpecifiedNpmWarning;
	public static String NpmPreferencePage_NotValidNpmError;

	public static String NpmErrorHandler_NpmNotDefinedTitle;
	public static String NpmErrorHandler_NpmNotDefinedMessage;
	
	public static String NpmInitWizard_WindowTitle;
	public static String NpmInitWizard_LaunchMainTabName;
	public static String NpmInitWizard_UseDefaulConfiguration;
	public static String NpmInitWizard_RootFolderSelection;
	public static String NpmInitWizard_BaseDirectory;
	public static String NpmInitWizard_BrowseWorkspace;
	
	public static String NpmInitWizard_ButtonAdd;
	public static String NpmInitWizard_ButtonEdit;
	public static String NpmInitWizard_ButtonRemove;
	
	public static String NpmInitWizard_Name;
	public static String NpmInitWizard_Version;
	public static String NpmInitWizard_License;
	public static String NpmInitWizard_Description;
	public static String NpmInitWizard_Main;
	public static String NpmInitWizard_Author;
	public static String NpmInitWizard_Properties;
	public static String NpmInitWizard_PageName;
	public static String NpmInitWizard_PageTitle;
	public static String NpmInitWizard_PageDescription;
	
	public static String NpmInitWizard_Scripts;
	public static String NpmInitWizard_ScriptName;
	public static String NpmInitWizard_ScriptValue;
	public static String NpmInitWizard_ScriptPopUpName;
	public static String NpmInitWizard_ScriptPopUpValue;
	public static String NpmInitWizard_AddScript;
	public static String NpmInitWizard_EditScript;
	
	public static String NpmInitWizard_ErrorDirNotDefiened;
	public static String NpmInitWizard_ErrorDirNotExist;
	public static String NpmInitWizard_ErrorPackageJsonAlreadyExist;

	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
	
}
