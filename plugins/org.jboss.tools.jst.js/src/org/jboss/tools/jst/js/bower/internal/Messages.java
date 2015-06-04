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
	public static String BowerPreferencePage_NodeLocationLabel;
	public static String BowerPreferencePage_NotInstalledError;
	public static String BowerPreferencePage_NotSpecifiedNodeWarning;
	public static String BowerPreferencePage_NotSpecifiedBowerWarning;
	public static String BowerPreferencePage_NotValidNodeError;
	public static String BowerPreferencePage_NotValidBowerError;
	public static String ErrorHandler_NodeNotDefinedTitle;
	public static String ErrorHandler_NodeNotDefinedMessage;
	public static String ErrorHandler_BowerNotDefinedTitle;
	public static String ErrorHandler_BowerNotDefinedMessage;
	public static String ErrorHandler_LaunchErrorTitle;
	public static String ErrorHandler_LaunchErrorMessage;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
