/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.tools.common.ui.CommonUIImages;

public class JSTWebUIImages extends CommonUIImages{
	private static String WIZARDS_PATH         = "wizards/"; //$NON-NLS-1$
	
	public static String CSS_FILE_IMAGE    = WIZARDS_PATH + "CSSFileWizBan.png"; //$NON-NLS-1$
	public static String DATASOURCE_IMAGE    = WIZARDS_PATH + "DatasourceWizBan.png"; //$NON-NLS-1$
	public static String TLD_FILE_IMAGE    = WIZARDS_PATH + "TLDFileWizBan.png"; //$NON-NLS-1$
	public static String WEB_DESCRIPTOR_IMAGE    = WIZARDS_PATH + "WebDescriptorWizBan.png"; //$NON-NLS-1$
	public static String TILES_FILE_IMAGE    = WIZARDS_PATH + "TilesFileWizBan.png"; //$NON-NLS-1$
	public static String PAGE_IMAGE    = WIZARDS_PATH + "PageWizBan.png"; //$NON-NLS-1$
	public static String DIALOG_IMAGE    = WIZARDS_PATH + "DialogWizBan.png"; //$NON-NLS-1$
	public static String POPUP_IMAGE    = WIZARDS_PATH + "PopupWizBan.png"; //$NON-NLS-1$
	public static String HEADER_IMAGE    = WIZARDS_PATH + "HeaderWizBan.png"; //$NON-NLS-1$
	public static String FOOTER_IMAGE    = WIZARDS_PATH + "FooterWizBan.png"; //$NON-NLS-1$
	public static String NAVBAR_IMAGE    = WIZARDS_PATH + "NavbarWizBan.png"; //$NON-NLS-1$
	public static String BUTTON_IMAGE    = WIZARDS_PATH + "ButtonWizBan.png"; //$NON-NLS-1$
	public static String GROUP_BUTTON_IMAGE    = WIZARDS_PATH + "ButtonsWizBan.png"; //$NON-NLS-1$
	public static String GRID_IMAGE    = WIZARDS_PATH + "GridWizBan.png"; //$NON-NLS-1$
	public static String COLLAPSIBLE_IMAGE    = WIZARDS_PATH + "CollapsibleWizBan.png"; //$NON-NLS-1$
	public static String COLLAPSIBLE_SET_IMAGE    = WIZARDS_PATH + "CollapsibleSetWizBan.png"; //$NON-NLS-1$
	public static String TOGGLE_IMAGE    = WIZARDS_PATH + "ToggleWizBan.png"; //$NON-NLS-1$
	public static String RADIO_IMAGE    = WIZARDS_PATH + "RadioWizBan.png"; //$NON-NLS-1$
	public static String CHECKBOX_IMAGE    = WIZARDS_PATH + "CheckboxWizBan.png"; //$NON-NLS-1$
	public static String GROUP_CHECKBOX_IMAGE    = WIZARDS_PATH + "GroupCheckboxWizBan.png"; //$NON-NLS-1$
	public static String LISTVIEW_IMAGE    = WIZARDS_PATH + "ListviewWizBan.png"; //$NON-NLS-1$
	public static String LINK_IMAGE    = WIZARDS_PATH + "LinkWizBan.png"; //$NON-NLS-1$
	public static String RANGE_SLIDER_IMAGE    = WIZARDS_PATH + "RangeSliderWizBan.png"; //$NON-NLS-1$
	public static String TEXT_INPUT_IMAGE    = WIZARDS_PATH + "TextInputWizBan.png"; //$NON-NLS-1$
	public static String SELECT_MENU_IMAGE    = WIZARDS_PATH + "SelectWizBan.png"; //$NON-NLS-1$
	public static String PANEL_IMAGE    = WIZARDS_PATH + "PanelWizBan.png"; //$NON-NLS-1$
	public static String TABLE_IMAGE    = WIZARDS_PATH + "TableWizBan.png"; //$NON-NLS-1$

	private static JSTWebUIImages INSTANCE;
	
	static {
		try {
			INSTANCE = new JSTWebUIImages(new URL(WebUiPlugin.getDefault().getBundle().getEntry("/"), "images/xstudio/")); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (MalformedURLException e) {
			// do nothing
			WebUiPlugin.getPluginLog().logError(e);
		}
	}
	
	public static JSTWebUIImages getInstance() {
		return INSTANCE;
	}
	
	protected JSTWebUIImages(URL registryUrl, JSTWebUIImages parent){
		super(registryUrl, parent);
	}
	
	protected JSTWebUIImages(URL url){
		this(url,null);		
	}
}
