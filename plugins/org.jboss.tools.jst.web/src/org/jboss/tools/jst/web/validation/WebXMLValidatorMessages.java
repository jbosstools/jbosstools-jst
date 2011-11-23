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
package org.jboss.tools.jst.web.validation;

import org.eclipse.osgi.util.NLS;

/**
 * @author Viacheslav Kabanovich
 */
public class WebXMLValidatorMessages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.jst.web.validation.messages"; //$NON-NLS-1$

	public static String WEB_XML_PLUGIN_NO_MESSAGE;

	public static String VALIDATING_RESOURCE;
	public static String VALIDATING_PROJECT;

	public static String PATH_EMPTY;
	public static String PATH_NOT_EXISTS;
	public static String PATH_NOT_TAGLIB;
	public static String PATH_NOT_ICON;
	public static String PATH_NOT_PAGE;

	public static String CLASS_NOT_EXISTS;
	public static String CLASS_NOT_IMPLEMENTS;
	public static String CLASS_NOT_EXTENDS;
	public static String CLASS_NOT_VALID;

	public static String EMPTY;
	public static String SERVLET_NOT_EXISTS;
	public static String FILTER_NOT_EXISTS;
	public static String ROLE_NOT_EXISTS;


	static {
		NLS.initializeMessages(BUNDLE_NAME, WebXMLValidatorMessages.class);
	}
}
