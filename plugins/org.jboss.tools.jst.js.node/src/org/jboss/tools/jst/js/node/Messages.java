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
package org.jboss.tools.jst.js.node;

import org.eclipse.osgi.util.NLS;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = Messages.class.getName().toString().toLowerCase();
	
	public static String PreferenceDescription_JavaScriptTools;

	public static String NodePreferencePage_NotSpecifiedNodeWarning;
	public static String NodePreferencePage_NotValidNodeError;
	public static String NodePreferencePage_NodeLocationLabel;
	
	public static String ErrorHandler_LaunchErrorTitle;
	public static String ErrorHandler_LaunchErrorMessage;
	public static String ErrorHandler_NodeNotDefinedTitle;
	public static String ErrorHandler_NodeNotDefinedMessage;

	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
	
}
