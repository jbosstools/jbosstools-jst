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

import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.XModelObjectConstants;
import org.jboss.tools.common.model.options.SharableConstants;

public class PaletteCategory extends PaletteDrawer implements PaletteXModelObject {
	private XModelObject xobject;
	
	private String version = null;

	private String[] availableVersions = new String[0];

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
		String label = xobject.getAttributeValue(XModelObjectConstants.ATTR_NAME);
		XModelObject p = xobject.getParent();
		if(label.startsWith("version:")) {
			label = p.getAttributeValue(XModelObjectConstants.ATTR_NAME);
			p = p.getParent();
		}
		while(p != null && (PaletteModelHelper.isGroup(p) || PaletteModelHelper.isSubGroup(p))) {
			String parentName = p.getAttributeValue(XModelObjectConstants.ATTR_NAME);
			if(!SharableConstants.MOBILE_PALETTE_ROOT.equals(parentName)) {
				label = parentName + " " + label; //$NON-NLS-1$
			}
			p = p.getParent();
		}
		setLabel(label);
	}

	public String getVersion() {
		return version;
	}

	public String[] getAvailableVersions() {
		return availableVersions;
	}

	public void setVersion(String s) {
		version = s;
	}

	public void setAvailableVersions(String[] vs) {
		availableVersions = vs;
	}
}
