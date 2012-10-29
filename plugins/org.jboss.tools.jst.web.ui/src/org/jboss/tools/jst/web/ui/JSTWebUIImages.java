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
