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
package org.jboss.tools.jst.jsp.drop.treeviewer.ui;

import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

public final class TreeViewerMessages extends NLS {

	private static final String BUNDLE_NAME = "org.jboss.tools.jst.jsp.drop.treeviewer.ui.TreeViewerMessages";//$NON-NLS-1$

	private TreeViewerMessages() {
		// Do not instantiate
	}

	public static String BundlesNameResourceElement_name;
	public static String BundlesPropertiesResourceElement_name;
	public static String EnumerationResourceElement_name;
	public static String JsfVariablesResourceElement_name;
	public static String ManagedBeanMethodResourceElement_name;
	public static String ManagedBeansPropertiesResourceElement_name;
	public static String ViewActionsResorceElement_name;

	static {
		NLS.initializeMessages(BUNDLE_NAME, TreeViewerMessages.class);
	}
	

	/**
	 * @deprecated use the string fields
	 * @return
	 */
	public static ResourceBundle getResourceBundle() {
		return ResourceBundle.getBundle(BUNDLE_NAME);
	}

}