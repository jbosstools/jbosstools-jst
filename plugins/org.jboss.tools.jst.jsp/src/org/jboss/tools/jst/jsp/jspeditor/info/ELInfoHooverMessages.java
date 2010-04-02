/******************************************************************************* 
 * Copyright (c) 2010 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.jsp.jspeditor.info;

import org.eclipse.osgi.util.NLS;

public class ELInfoHooverMessages extends NLS {

	private static final String BUNDLE_NAME= ELInfoHooverMessages.class.getName();

	private ELInfoHooverMessages() {
		// Do not instantiate
	}

	public static String ELInfoHover_noAttachments;			//
	public static String ELInfoHover_noAttachedJavadoc;		//
	public static String ELInfoHover_noAttachedJavaSource; 	//
	public static String ELInfoHover_noInformation;			//
	public static String ELInfoHover_error_gettingJavadoc; 	//	

	static {
		NLS.initializeMessages(BUNDLE_NAME, ELInfoHooverMessages.class);
	}
}
