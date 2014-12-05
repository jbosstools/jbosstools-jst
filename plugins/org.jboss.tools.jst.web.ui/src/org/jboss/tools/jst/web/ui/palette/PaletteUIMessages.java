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
package org.jboss.tools.jst.web.ui.palette;

import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;



public class PaletteUIMessages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.jst.web.ui.palette.messages";//$NON-NLS-1$
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, PaletteUIMessages.class);
	}
	public static String PALETTE_EDITOR;
	public static String SHOW_HIDE_TABS;
	public static String SHOW_HIDE;
	public static String IMPORT_TLD;
	public static String IMPORT;
	public static String PALETTE_FILTER_MESSAGE;
	public static String PALETTE_FILTER_TOOLTIP;
	
	private PaletteUIMessages() {
	}
	
	/**
	 * @deprecated use the fields of PaletteUIMessages instead
	 */
	static final ResourceBundle getBundle() {
		return ResourceBundle.getBundle(BUNDLE_NAME);
	}

}
