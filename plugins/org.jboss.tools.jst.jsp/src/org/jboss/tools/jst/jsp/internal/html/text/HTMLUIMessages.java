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
package org.jboss.tools.jst.jsp.internal.html.text;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

public class HTMLUIMessages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.jst.jsp.internal.html.text.HTMLUIMessages";//$NON-NLS-1$
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

	// Ignore Attribute Name Patterns
	public static String IgnoreAttributeNames;
	public static String IgnoreAttributeNamesPattern;
	public static String BadIgnoreAttributeNamesPattern;
	public static String DoNotValidateAttribute;
	public static String DoNotValidateAllAttributes;
	public static String DoNotValidateAttributeAddInfo;
	public static String DoNotValidateAllAttributesAddInfo;
	public static String DoNotValidateAttributeNeedWTPSR2;
	public static String DoNotValidateAllAttributesNeedWTPSR2;
	public static String DoNotValidateAttributeAddInfoNeedWTPSR2;
	public static String DoNotValidateAllAttributesAddInfoNeedWTPSR2;
	public static String RequiresWTPSR2;
}