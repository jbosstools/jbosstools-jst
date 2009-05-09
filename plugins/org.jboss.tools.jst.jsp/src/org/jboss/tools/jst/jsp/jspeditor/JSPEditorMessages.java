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
package org.jboss.tools.jst.jsp.jspeditor;

import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

public final class JSPEditorMessages extends NLS {

	private static final String BUNDLE_NAME = "org.jboss.tools.jst.jsp.jspeditor.JSPEditorMessages";//$NON-NLS-1$

	private JSPEditorMessages() {
		// Do not instantiate
	}

	public static String JSPMultiPageEditor_TabLabel_VisualSource;
	public static String JSPMultiPageEditor_TabLabel_Source;
	public static String JSPMultiPageEditor_TabLabel_Preview;

	static {
		NLS.initializeMessages(BUNDLE_NAME, JSPEditorMessages.class);
	}

	/**
	 * @deprecated use the string fields of the NLS subclass
	 */
	public static ResourceBundle getResourceBundle() {
		return ResourceBundle.getBundle(BUNDLE_NAME);
	}

}