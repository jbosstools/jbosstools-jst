/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.model;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteDrawer;

import org.jboss.tools.common.model.XModelObject;

public class PaletteCategory extends PaletteDrawer implements PaletteXModelObject {
	private XModelObject xobject;

	public PaletteCategory(XModelObject xobject, boolean open) {
		super(null);
		setXModelObject(xobject);
		if (open)
			setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
		else
			setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
		setDrawerType(PaletteEntry.PALETTE_TYPE_UNKNOWN);
	}
	
	public XModelObject getXModelObject() {
		return xobject;
	}
	
	public void setXModelObject(XModelObject xobject) {
		this.xobject = xobject;
		String label = xobject.getAttributeValue("name"); //$NON-NLS-1$
		XModelObject p = xobject.getParent();
		while(p != null && (PaletteModelHelper.isGroup(p) || PaletteModelHelper.isSubGroup(p))) {
			String parentName = p.getAttributeValue("name");
			if(!"Mobile".equals(parentName)) { //$NON-NLS-1$
				label = parentName + " " + label; //$NON-NLS-1$ //$NON-NLS-2$
			}
			p = p.getParent();
		}
		setLabel(label);
	}

}
