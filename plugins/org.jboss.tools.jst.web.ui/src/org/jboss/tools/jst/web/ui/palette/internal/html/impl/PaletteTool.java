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
package org.jboss.tools.jst.web.ui.palette.internal.html.impl;

import org.eclipse.gef.palette.ToolEntry;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteItem;
/**
 * 
 * @author Daniel Azarov
 *
 */
public class PaletteTool extends ToolEntry {
	private IPaletteItem item;

	public PaletteTool(IPaletteItem item) {
		super(item.getName(), item.getName(), item.getImageDescriptor(), item.getImageDescriptor());
		this.item = item;
	}
	
	public IPaletteItem getPaletteItem(){
		return item;
	}

}
