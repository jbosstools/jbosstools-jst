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

import java.util.List;

import org.jboss.tools.common.model.XModelObject;

public class PaletteRoot extends org.eclipse.gef.palette.PaletteRoot implements PaletteXModelObject {
	private XModelObject xobject;
	private PaletteModel model;

	public PaletteRoot(XModelObject xobject) {
		this.xobject = xobject;
	}
	
	public XModelObject getXModelObject() {
		return xobject;
	}
	
	public PaletteCategory findCategory(XModelObject xobject) {
		if (xobject == null) return null;

		List v = getChildren();
		if (v != null) {
			int max = v.size();
			for (int i = 0; i < max; i++) {
				PaletteCategory cat = (PaletteCategory)v.get(i);
				if (cat.getXModelObject() == xobject)
					return cat; 
			}
		}
		return null;
	}

	public void setPaletteModel(PaletteModel model) {
		this.model = model;
	}

	public PaletteModel getPaletteModel() {
		return model;
	}
}
