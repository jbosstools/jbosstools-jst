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
import org.jboss.tools.common.model.ui.views.palette.IPositionCorrector;

/**
 * Interface for palette item
 * 
 * @author Daniel Azarov
 *
 */
public interface IPaletteItem {
	
	/**
	 * Returns id, format : /<groupName>/<version>/<itemName>
	 * 
	 * @return
	 */
	public String getId();
	
	/**
	 * Returns parent palette element
	 * 
	 *  @see IPaletteCategory
	 *  
	 * @return IPaletteCategory
	 */
	public IPaletteCategory getCategory();
	
	/**
	 * Sets parent palette element
	 * 
	 * @see IPaletteCategory
	 * 
	 * @param category
	 */
	public void setCategory(IPaletteCategory category);
	
	/**
	 * Return name of palette item
	 * 
	 * @return name
	 */
	public String getName();
	
	/**
	 * Returns tool tip text for palette item
	 * 
	 * @return tooltip
	 */
	public String getTooltip();
	
	/**
	 * Returns list of key words of palette item
	 * 
	 * @return keyword list
	 */
	public List<String> getKeywords();
	
	/**
	 * Returns space separated string of keywords
	 * 
	 * @return keywords
	 */
	public String getKeywordsAsString();
	
	/**
	 * Returns true if palette item needs code format after inserting into editor
	 * 
	 * @return reformat
	 */
	public boolean isReformat();
	
	/**
	 * Returns palette item start text
	 * 
	 * @return start text
	 */
	public String getStartText();
	
	/**
	 * Returns palette item end text
	 * 
	 * @return end text
	 */
	public String getEndText();
	
	/**
	 * Returns images descriptor for palette item image
	 * 
	 * @see ImageDescriptor
	 * 
	 * @return ImageDescriptor
	 */
	public ImageDescriptor getImageDescriptor();

	/**
	 * Returns true if palette item has a wizard class
	 * 
	 * 
	 * @return IDropWizard
	 */
	public boolean hasWizard();
	
	/**
	 * Returns palette item wizard
	 * 
	 * @see IPaletteItemWizard
	 * 
	 * @return IDropWizard
	 */
	public IPaletteItemWizard createWizard();
	
	/**
	 * Returns palette item position corrector
	 * 
	 * @see IPositionCorrector
	 * 
	 * @return IPositionCorrector
	 */
	public IPositionCorrector createPositionCorrector();
	
	/**
	 * Returns Count Index
	 * last called item has max count index
	 * 
	 * @return
	 */
	public long getCountIndex();
	
	/**
	 * Sets Count Index, only for initialization
	 * 
	 * @param count
	 */
	public void setCountIndex(long count);
	
	/**
	 * Returns number of calls
	 * 
	 * @return
	 */
	public long getNumberOfCalls();

	/**
	 * Sets number of calls, only for initialization
	 * 
	 * @param numberOfCalls
	 */
	public void setNumberOfCalls(long numberOfCalls);
	
	/**
	 * This method is used to increment counts when item is used
	 * 
	 */
	public void called();
}
