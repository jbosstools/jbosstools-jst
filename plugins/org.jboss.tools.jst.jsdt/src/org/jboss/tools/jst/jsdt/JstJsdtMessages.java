/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.jsdt;

import org.eclipse.osgi.util.NLS;

public class JstJsdtMessages  extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.jst.jsdt.messages"; //$NON-NLS-1$

	public static String MISSING_ANGULAR_MODULE_MESSAGE_TITLE;
	public static String SKIP_BUTTON_LABEL;
	public static String TURN_ANGULAR_MODULE_ON;
	public static String TURN_ANGULAR_MODULE_ON_TEXT;
	public static String DONT_SHOW_CHECKER_DIALOG;
	
	public static String AngularModuleConfigurationProperties_title;
	public static String AngularModuleConfigurationProperties_desc;
	public static String AngularModuleConfigurationProperties_doNotShowDialog_label;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, JstJsdtMessages.class);
	}

	private JstJsdtMessages() {
	}
}