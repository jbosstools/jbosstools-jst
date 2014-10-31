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
package org.jboss.tools.jst.web.ui.palette.model;

import org.jboss.tools.common.model.ui.views.palette.PaletteContents;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.PagePaletteContents;
/**
 * 
 * Common interface for old and new palette models
 * 
 * @author Daniel Azarov
 *
 */
public interface IPaletteModel {
	public static final String VERSION_PREFIX = "version:";
	public static final String TYPE_HTML5 = PaletteContents.TYPE_MOBILE;
	public static final String TYPE_JSF = PaletteContents.TYPE_JSF;
	
	/**
	 * Returns palette model type
	 * 
	 * @return type
	 */
	public String getType();
	
	/**
	 * Returns palette model root
	 * 
	 * @see org.eclipse.gef.palette.PaletteRoot
	 * 
	 * @return root
	 */
	public org.eclipse.gef.palette.PaletteRoot getPaletteRoot();
	
	/**
	 * Sets page palette contents
	 * 
	 * @see PagePaletteContents
	 * 
	 * @param paletteContents
	 */
	public void setPaletteContents(PagePaletteContents paletteContents);
	
	/**
	 * Returns page palette contents
	 * 
	 * @see PagePaletteContents
	 * 
	 * @return contents
	 */
	public PagePaletteContents getPaletteContents();
	
	/**
	 * Loads palette model
	 */
	public void load();
	
	/**
	 * Returns preferred expanded category
	 * 
	 * @return preferred expanded category
	 */
	public String getPreferredExpandedCategory();
	
	/**
	 * Method supposed to be called on category expand state change 
	 * 
	 * @param name
	 * @param state
	 */
	public void onCategoryExpandChange(String name, boolean state);
}
