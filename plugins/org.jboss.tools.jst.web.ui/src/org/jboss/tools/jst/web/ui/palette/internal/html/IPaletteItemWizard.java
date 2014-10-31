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
package org.jboss.tools.jst.web.ui.palette.internal.html;

import org.jboss.tools.common.model.ui.editors.dnd.IDropWizard;

/**
 * Interface for palette item wizards
 * 
 * @see IDropWizard
 * @see IPaletteItem
 * 
 * @author Daniel Azarov
 *
 */
public interface IPaletteItemWizard extends IDropWizard {
	/**
	 * Sets palette item
	 * 
	 * @see IPaletteItem
	 * 
	 * @param paletteItem
	 */
	public void setPaletteItem(IPaletteItem paletteItem);
	
	/**
	 * Returns palette item
	 * 
	 * @see IPaletteItem
	 * 
	 * @return IPaletteItem
	 */
	public IPaletteItem getPaletteItem();
}
