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
package org.jboss.tools.jst.jsp.outline.cssdialog.common;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Utility class which helps with managing messages.
 * 
 * @author dsakovich@exadel.com
 */
public class MessageUtil {

    private static final String RESOURCE_BUNDLE = "org.jboss.tools.jst.jsp.outline.cssdialog.common.messages";//$NON-NLS-1$

    private static ResourceBundle fgResourceBundle = ResourceBundle
	    .getBundle(RESOURCE_BUNDLE);

    private MessageUtil() {
    }

    /**
     * Returns the formatted message for the given key in
     * 
     * @param key
     *                the resource name
     * @param args
     *                the message arguments
     * @return the string
     */
    public static String format(String key, Object[] args) {
	return MessageFormat.format(getString(key), args);
    }

    /**
     * Returns the resource object with the given key
     * 
     * @param key
     *                the resource name
     * @return the string
     */
    public static String getString(String key) {
	try {
	    return fgResourceBundle.getString(key);
	} catch (MissingResourceException e) {
	    // TODO Sakovich
	    return key;
	}
    }
}
