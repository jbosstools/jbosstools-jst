/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.internal.html.text;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

public class HTMLUIMessages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.jst.web.ui.internal.html.text.HTMLUIMessages";//$NON-NLS-1$
	private static ResourceBundle fResourceBundle;
	
	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, HTMLUIMessages.class);
	}
	
	private HTMLUIMessages() {
		// cannot create new instance of this class
	}
	
	public static ResourceBundle getResourceBundle() {
		try {
			if (fResourceBundle == null)
				fResourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
		}
		catch (MissingResourceException x) {
			fResourceBundle = null;
		}
		return fResourceBundle;
	}

	// Ignore Element Name Patterns
	public static String IgnoreElementNames;
	public static String IgnoreElementNamesPattern;
	public static String BadIgnoreElementNamesPattern;
	public static String DoNotValidateElement;
	public static String DoNotValidateAllElements;
	public static String DoNotValidateElementAddInfo;
	public static String DoNotValidateAllElementsAddInfo;
	public static String DoNotValidateElementNeedWTPSR2;
	public static String DoNotValidateAllElementsNeedWTPSR2;
	public static String DoNotValidateElementAddInfoNeedWTPSR2;
	public static String DoNotValidateAllElementsAddInfoNeedWTPSR2;
	public static String RequiresWTPSR2;
}