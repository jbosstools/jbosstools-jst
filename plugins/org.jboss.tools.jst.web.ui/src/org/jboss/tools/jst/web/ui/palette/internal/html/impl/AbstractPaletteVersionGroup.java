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
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteGroup;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteVersionGroup;
/**
 * Default implementation of palette version group
 * 
 * @see IPaletteVersionGroup
 * 
 * @author Daniel Azarov
 *
 */
public abstract class AbstractPaletteVersionGroup implements IPaletteVersionGroup {
	private ArrayList<IPaletteCategory> categories = new ArrayList<IPaletteCategory>();
	private IPaletteGroup group;
	
	protected void add(IPaletteCategory category){
		categories.add(category);
		category.setVersionGroup(this);
	}

	@Override
	public List<IPaletteCategory> getCategories() {
		return categories;
	}

	@Override
	public IPaletteGroup getGroup() {
		return group;
	}

	@Override
	public void setGroup(IPaletteGroup group) {
		this.group = group;
	}
}
