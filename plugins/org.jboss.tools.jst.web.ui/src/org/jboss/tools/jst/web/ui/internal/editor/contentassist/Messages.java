/******************************************************************************* 
 * Copyright (c) 2011-2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.internal.editor.contentassist;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author jeremy
 *
 */
public class Messages  extends NLS {

	private static final String BUNDLE_NAME = "org.jboss.tools.jst.web.ui.internal.editor.contentassist.messages";//$NON-NLS-1$

	private Messages() {
		// Do not instantiate
	}

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	public static String NO_JAVADOC;
	public static String JspEditorPlugin_additionalInfo_affordance;
	
	public static String JavadocHover_back;
	public static String JavadocHover_back_toElement_toolTip;
	public static String JavadocHover_forward;
	public static String JavadocHover_forward_toElement_toolTip;
	public static String JavadocHover_forward_toolTip;
	public static String JavadocHover_openDeclaration;
	public static String JavadocHover_showInJavadoc;
}