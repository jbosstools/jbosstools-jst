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
package org.jboss.tools.jst.web.debug;

import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

public final class DebugMessages extends NLS {

	private static final String BUNDLE_NAME = "org.jboss.tools.jst.web.debug.DebugMessages";//$NON-NLS-1$

	private DebugMessages() {
		// Do not instantiate
	}

	public static String ActionEnterBreakpoint_name;
	public static String ActionFormPopulateBreakpoint_name;
	public static String ActionFormValidateBreakpoint_name;
	public static String ActionForwardBreakpoint_name;
	public static String ActionTilesDefinitionForwardBreakpoint_name;
	public static String ActionIncludeBreakpoint_name;
	public static String ActionExceptionBreakpoint_name;
	public static String GlobalExceptionBreakpoint_name;
	public static String GlobalForwardBreakpoint_name;
	public static String TilesDefinitionGlobalForwardBreakpoint_name;
	public static String PageEnterBreakpoint_name;
	public static String JspLineBreakpoint_name;

	static {
		NLS.initializeMessages(BUNDLE_NAME, DebugMessages.class);
	}

	/**
	 * @deprecated use the string fields of the NLS subclass
	 */
	public static ResourceBundle getResourceBundle() {
		return ResourceBundle.getBundle(BUNDLE_NAME);
	}

}