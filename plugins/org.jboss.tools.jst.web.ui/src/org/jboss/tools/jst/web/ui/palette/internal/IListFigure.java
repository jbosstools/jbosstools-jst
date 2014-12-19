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
package org.jboss.tools.jst.web.ui.palette.internal;

import org.eclipse.draw2d.IFigure;

/**
 * Interface for using with ListPopUp
 * 
 * Default implementation is ListFigure
 * 
 * @see ListPopUp
 * @see ListFigure
 * 
 * @author Daniel Azarov
 *
 */
public interface IListFigure extends IFigure{
	/**
	 * Returns array of string that will be shown in popup menu
	 * 
	 * @return
	 */
	public String[] getValues();
	
	/**
	 * Returns current selected value 
	 * 
	 * @return
	 */
	public String getSelected();
	
	/**
	 * Sets current selected value 
	 * 
	 * @param value
	 */
	public void setSelected(String value);
}
