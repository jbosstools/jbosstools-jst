/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.angularjs.internal.palette.wizard;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class IonicWizardMessages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.jst.angularjs.internal.palette.wizard.messages"; //$NON-NLS-1$

	public static String newContentWizardTitle;
	public static String newContentWizardDescription;

	public static String alignTitleLabel;
	public static String barColorLabel;
	public static String ngClickLabel;
	public static String noTapScrollLabel;
	public static String subheaderLabel;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, IonicWizardMessages.class);
	}

	private IonicWizardMessages() {
	}
}