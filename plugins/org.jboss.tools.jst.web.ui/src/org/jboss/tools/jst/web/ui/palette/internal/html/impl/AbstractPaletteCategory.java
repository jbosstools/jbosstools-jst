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

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteCategory;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteItem;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteVersionGroup;

/**
 * Default implementation of palette category
 * 
 * @see IPaletteCategory
 * 
 * @author Daniel Azarov
 *
 */
public abstract class AbstractPaletteCategory implements IPaletteCategory {
	private ArrayList<IPaletteItem> items = new ArrayList<IPaletteItem>();
	private IPaletteVersionGroup versionGroup;
	
	@Override
	public List<IPaletteItem> getItems(){
		return items;
	}
	
	protected void add(IPaletteItem item){
		items.add(item);
		item.setCategory(this);
	}
	
	protected void remove(IPaletteItem item){
		items.remove(item);
	}
	
	protected void insertAfter(IPaletteItem itemToSearch, IPaletteItem itemToInsert){
		int index = items.indexOf(itemToSearch);
		if(index >= 0){
			items.add(index+1, itemToInsert);
		}
	}

	@Override
	public IPaletteVersionGroup getVersionGroup() {
		return versionGroup;
	}

	@Override
	public void setVersionGroup(IPaletteVersionGroup versionGroup) {
		this.versionGroup = versionGroup;
	}
}
