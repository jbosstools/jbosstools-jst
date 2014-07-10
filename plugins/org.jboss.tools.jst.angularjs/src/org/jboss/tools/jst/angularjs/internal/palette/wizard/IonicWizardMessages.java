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

	public static String newScrollWizardTitle;
	public static String newScrollWizardDescription;

	public static String alignTitleLabel;
	public static String barColorLabel;
	public static String delegateHandleLabel;
	public static String directionLabel;
	public static String hasBouncingLabel;
	public static String minZoomLabel;
	public static String maxZoomLabel;
	public static String ngClickLabel;
	public static String noTapScrollLabel;
	public static String onrefreshLabel;
	public static String onscrollLabel;
	public static String onscrollCompleteLabel;
	public static String overflowScrollLabel;
	public static String paddingLabel;
	public static String pagingLabel;
	public static String scrollbar_xLabel;
	public static String scrollbar_yLabel;
	public static String scrollLabel;
	public static String startYLabel;
	public static String subheaderLabel;
	public static String zoomingLabel;

	public static String contentDelegateHandleDescription;
	public static String contentDirectionDescription;
	public static String contentOnscrollDescription;
	public static String contentOnscrollCompleteDescription;
	public static String contentOverflowScrollDescription;
	public static String contentPaddingDescription;
	public static String contentScrollbar_xDescription;
	public static String contentScrollbar_yDescription;
	public static String contentScrollDescription;
	public static String contentStartYDescription;
	public static String headerAlignTitleDescription;
	public static String headerNoTapScrollDescription;
	public static String ngClickDescription;
	public static String scrollHasBouncingDescription;
	public static String scrollMaxZoomDescription;
	public static String scrollMinZoomDescription;
	public static String scrollPagingDescription;
	public static String scrollRefreshDescription;
	public static String scrollZoomingDescription;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, IonicWizardMessages.class);
	}

	private IonicWizardMessages() {
	}
}