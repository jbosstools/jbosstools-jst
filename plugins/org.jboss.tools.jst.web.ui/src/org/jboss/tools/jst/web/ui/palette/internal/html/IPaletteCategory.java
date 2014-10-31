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

import java.util.List;

/**
 * Interface for palette subgroup 
 * 
 * @author Daniel Azarov
 *
 */
public interface IPaletteCategory {
	
	/**
	 * Returns parent element
	 * 
	 * @see IPaletteVersionGroup
	 * 
	 * @return palette version group
	 */
	public IPaletteVersionGroup getVersionGroup();
	
	/**
	 * Sets parent element 
	 * 
	 * @see IPaletteVersionGroup
	 * 
	 * @param IPaletteVersionGroup
	 */
	public void setVersionGroup(IPaletteVersionGroup versionGroup);
	
	/**
	 * Returns list of child elements
	 * 
	 * @see IPaletteItem
	 * 
	 * @return list of IPaletteItem elements
	 */
	public List<IPaletteItem> getItems();
}
