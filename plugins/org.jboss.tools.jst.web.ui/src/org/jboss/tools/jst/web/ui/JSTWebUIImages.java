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
	private static String VIEWS_PATH         = "views/"; //$NON-NLS-1$
	
	public static final String CSS_FILE_IMAGE    = WIZARDS_PATH + "CSSFileWizBan.png"; //$NON-NLS-1$
	public static final String DATASOURCE_IMAGE    = WIZARDS_PATH + "DatasourceWizBan.png"; //$NON-NLS-1$
	public static final String TLD_FILE_IMAGE    = WIZARDS_PATH + "TLDFileWizBan.png"; //$NON-NLS-1$
	public static final String WEB_DESCRIPTOR_IMAGE    = WIZARDS_PATH + "WebDescriptorWizBan.png"; //$NON-NLS-1$
	public static final String TILES_FILE_IMAGE    = WIZARDS_PATH + "TilesFileWizBan.png"; //$NON-NLS-1$
	public static final String PAGE_IMAGE    = WIZARDS_PATH + "PageWizBan.png"; //$NON-NLS-1$
	public static final String DIALOG_IMAGE    = WIZARDS_PATH + "DialogWizBan.png"; //$NON-NLS-1$
	public static final String POPUP_IMAGE    = WIZARDS_PATH + "PopupWizBan.png"; //$NON-NLS-1$
	public static final String HEADER_IMAGE    = WIZARDS_PATH + "HeaderWizBan.png"; //$NON-NLS-1$
	public static final String FOOTER_IMAGE    = WIZARDS_PATH + "FooterWizBan.png"; //$NON-NLS-1$
	public static final String NAVBAR_IMAGE    = WIZARDS_PATH + "NavbarWizBan.png"; //$NON-NLS-1$
	public static final String BUTTON_IMAGE    = WIZARDS_PATH + "ButtonWizBan.png"; //$NON-NLS-1$
	public static final String FORM_BUTTON_IMAGE    = WIZARDS_PATH + "FormButtonWizBan.png"; //$NON-NLS-1$
	public static final String GROUP_BUTTON_IMAGE    = WIZARDS_PATH + "ButtonsWizBan.png"; //$NON-NLS-1$
	public static final String GRID_IMAGE    = WIZARDS_PATH + "GridWizBan.png"; //$NON-NLS-1$
	public static final String COLLAPSIBLE_IMAGE    = WIZARDS_PATH + "CollapsibleWizBan.png"; //$NON-NLS-1$
	public static final String COLLAPSIBLE_SET_IMAGE    = WIZARDS_PATH + "CollapsibleSetWizBan.png"; //$NON-NLS-1$
	public static final String TOGGLE_IMAGE    = WIZARDS_PATH + "ToggleWizBan.png"; //$NON-NLS-1$
	public static final String RADIO_IMAGE    = WIZARDS_PATH + "RadioWizBan.png"; //$NON-NLS-1$
	public static final String CHECKBOX_IMAGE    = WIZARDS_PATH + "CheckboxWizBan.png"; //$NON-NLS-1$
	public static final String GROUP_CHECKBOX_IMAGE    = WIZARDS_PATH + "GroupCheckboxWizBan.png"; //$NON-NLS-1$
	public static final String LISTVIEW_IMAGE    = WIZARDS_PATH + "ListviewWizBan.png"; //$NON-NLS-1$
	public static final String LINK_IMAGE    = WIZARDS_PATH + "LinkWizBan.png"; //$NON-NLS-1$
	public static final String RANGE_SLIDER_IMAGE    = WIZARDS_PATH + "RangeSliderWizBan.png"; //$NON-NLS-1$
	public static final String TEXT_INPUT_IMAGE    = WIZARDS_PATH + "TextInputWizBan.png"; //$NON-NLS-1$
	public static final String SELECT_MENU_IMAGE    = WIZARDS_PATH + "SelectWizBan.png"; //$NON-NLS-1$
	public static final String PANEL_IMAGE    = WIZARDS_PATH + "PanelWizBan.png"; //$NON-NLS-1$
	public static final String TABLE_IMAGE    = WIZARDS_PATH + "TableWizBan.png"; //$NON-NLS-1$
	public static final String TABS_IMAGE    = WIZARDS_PATH + "TabsWizBan.png"; //$NON-NLS-1$
	public static final String HEADING_IMAGE    = WIZARDS_PATH + "HeadingWizBan.png"; //$NON-NLS-1$
	public static final String FORM_IMAGE    = WIZARDS_PATH + "FormWizBan.png"; //$NON-NLS-1$
	public static final String IMG_IMAGE    = WIZARDS_PATH + "ImageWizBan.png"; //$NON-NLS-1$
	public static final String VIDEO_IMAGE    = WIZARDS_PATH + "VideoWizBan.png"; //$NON-NLS-1$
	public static final String AUDIO_IMAGE    = WIZARDS_PATH + "AudioWizBan.png"; //$NON-NLS-1$
	public static final String LABEL_IMAGE    = WIZARDS_PATH + "LabelWizBan.png"; //$NON-NLS-1$
	public static final String JS_CSS_IMAGE    = WIZARDS_PATH + "JS-CSSWizBan.png"; //$NON-NLS-1$
	public static final String CANVAS_IMAGE    = WIZARDS_PATH + "CanvasWizBan.png"; //$NON-NLS-1$
	
	public static final String DROP_DOWN_LIST_IMAGE    = VIEWS_PATH + "list.png"; //$NON-NLS-1$
	public static final String FILTER_IMAGE    = VIEWS_PATH + "filter.png"; //$NON-NLS-1$

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
