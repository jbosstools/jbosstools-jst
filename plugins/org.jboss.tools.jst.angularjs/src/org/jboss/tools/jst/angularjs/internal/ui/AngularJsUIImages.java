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
package org.jboss.tools.jst.angularjs.internal.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.tools.common.ui.CommonUIImages;
import org.jboss.tools.jst.angularjs.AngularJsPlugin;

public class AngularJsUIImages extends CommonUIImages {
	private static String WIZARDS_PATH = "wizards/"; //$NON-NLS-1$
	
	public static final String CONTENT_IMAGE = WIZARDS_PATH + "ContentWizBan.png"; //$NON-NLS-1$
	public static final String CHECKBOX_IMAGE = WIZARDS_PATH + "IonicCheckboxWizBan.png"; //$NON-NLS-1$
	public static final String NAVIGATION_IMAGE = WIZARDS_PATH + "IonicNavigationWizBan.png"; //$NON-NLS-1$
	public static final String RADIO_IMAGE = WIZARDS_PATH + "IonicRadioWizBan.png"; //$NON-NLS-1$
	public static final String REFRESHER_IMAGE = WIZARDS_PATH + "IonicRefresherWizBan.png"; //$NON-NLS-1$
	public static final String TOGGLE_IMAGE = WIZARDS_PATH + "IonicToggleWizBan.png"; //$NON-NLS-1$
	public static final String LIST_IMAGE = WIZARDS_PATH + "ListWizBan.png"; //$NON-NLS-1$
	public static final String SCROLL_IMAGE = WIZARDS_PATH + "ScrollWizBan.png"; //$NON-NLS-1$
	public static final String SIDEMENU_IMAGE = WIZARDS_PATH + "SideMenuWizBan.png"; //$NON-NLS-1$
	public static final String SLIDEBOX_IMAGE = WIZARDS_PATH + "SlideBoxWizBan.png"; //$NON-NLS-1$
	public static final String TABS_IMAGE = WIZARDS_PATH + "IonicTabsWizBan.png"; //$NON-NLS-1$
	public static final String TAB_IMAGE = WIZARDS_PATH + "IonicTabWizBan.png"; //$NON-NLS-1$
	public static final String BUTTON_IMAGE = WIZARDS_PATH + "IonicButtonWizBan.png"; //$NON-NLS-1$
	public static final String TEXT_INPUT_IMAGE = WIZARDS_PATH + "IonicTextInputWizBan.png"; //$NON-NLS-1$


	private static AngularJsUIImages INSTANCE;
	
	static {
		try {
			INSTANCE = new AngularJsUIImages(new URL(AngularJsPlugin.getDefault().getBundle().getEntry("/"), "images/")); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (MalformedURLException e) {
			// do nothing
			AngularJsPlugin.getDefault().logError(e);
		}
	}
	
	public static AngularJsUIImages getInstance() {
		return INSTANCE;
	}
	
	protected AngularJsUIImages(URL registryUrl, AngularJsUIImages parent){
		super(registryUrl, parent);
	}
	
	protected AngularJsUIImages(URL url){
		this(url,null);		
	}
}
