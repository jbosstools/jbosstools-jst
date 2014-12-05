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

import org.jboss.tools.jst.web.kb.taglib.IHTMLLibraryVersion;

/**
 * Interface for palette version group
 * 
 * @author Daniel Azarov
 *
 */
public interface IPaletteVersionGroup {
	/**
	 * Return parent palette element
	 * 
	 * @see IPaletteGroup
	 * 
	 * @return IPaletteGroup
	 */
	public IPaletteGroup getGroup();
	
	/**
	 * Sets parent palette element
	 * 
	 * @see IPaletteGroup
	 * 
	 * @param group
	 */
	public void setGroup(IPaletteGroup group);
	
	/**
	 * Returns name of version
	 * 
	 * @see IHTMLLibraryVersion
	 * 
	 * @return version
	 */
	public IHTMLLibraryVersion getVersion();
	
	/**
	 * Returns list of child palette elements
	 * 
	 * @see IPaletteCategory
	 * 
	 * @return list of IPaletteCategory
	 */
	public List<IPaletteCategory> getCategories();
}
