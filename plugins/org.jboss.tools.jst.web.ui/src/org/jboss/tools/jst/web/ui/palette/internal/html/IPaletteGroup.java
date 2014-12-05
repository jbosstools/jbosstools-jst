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

import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.jst.web.kb.taglib.IHTMLLibraryVersion;
import org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer;
import org.jboss.tools.jst.web.ui.palette.model.IPaletteModel;

/**
 * Interface for palette group 
 * 
 * @author Daniel Azarov
 *
 */
public interface IPaletteGroup {
	/**
	 * Returns palette group name
	 * 
	 * @return name
	 */
	public String getName();
	
	/**
	 * Returns array of versions of child elements
	 * 
	 * @see IHTMLLibraryVersion
	 * 
	 * @return versions
	 */
	public IHTMLLibraryVersion[] getVersions();
	
	/**
	 * Returns list of child palette elements
	 * 
	 * @see IPaletteVersionGroup
	 * 
	 * @return list of IPaletteVersionGroup elements
	 */
	public List<IPaletteVersionGroup> getPaletteVersionGroups();
	
	/**
	 * Returns tag library recognizer
	 * 
	 * @see ITagLibRecognizer
	 * 
	 * @return ITagLibRecognizer
	 */
	public ITagLibRecognizer getRecognizer();
	
	/**
	 * Returns images descriptor for palette group image
	 * 
	 * @see ImageDescriptor
	 * 
	 * @return ImageDescriptor
	 */
	public ImageDescriptor getImageDescriptor();
	
	/**
	 * Returns parent palette model
	 * 
	 * @see IPaletteModel
	 * 
	 * @return IPaletteModel
	 */
	public IPaletteModel getPaletteModel();
	
	/**
	 * Sets parent palette model
	 * 
	 * @see IPaletteModel
	 * 
	 * @param model
	 */
	public void setPaletteModel(IPaletteModel model);
	
	/**
	 * Returns selected palette version group
	 * 
	 * @see IPaletteVersionGroup
	 * 
	 * @return IPaletteVersionGroup
	 */
	public IPaletteVersionGroup getSelectedVersionGroup();
	
	/**
	 * Sets version name of selected palette version group
	 *  
	 * @param version
	 */
	public void setSelectedVersion(IHTMLLibraryVersion version);

	/**
	 * Returns last palette version group
	 * 
	 * @see IPaletteVersionGroup
	 * 
	 * @return IPaletteVersionGroup
	 */
	public IPaletteVersionGroup getLastVersionGroup();
	
	/**
	 * Returns false if no need to load the group while the palette is being initialized 
	 */
	public boolean isEnabled();
}
