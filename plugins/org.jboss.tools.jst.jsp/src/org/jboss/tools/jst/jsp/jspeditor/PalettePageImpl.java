/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.jsp.jspeditor;

import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.ui.views.palette.IPalettePageAdapter;
import org.jboss.tools.common.model.ui.views.palette.PaletteCreator;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class PalettePageImpl extends Page implements PalettePage, IPalettePageAdapter {
	PaletteCreator paletteCreator = new PaletteCreator(this);

	public PalettePageImpl() {}

    public void init(IPageSite pageSite) {
    	super.init(pageSite);
    	paletteCreator.initActionBars();
    }

    public void createControl(Composite parent) {
    	paletteCreator.createPartControlImpl(parent);
	}

	public Control getControl() {
		return paletteCreator.getControl();
	}

	public void setFocus() {
		paletteCreator.setFocus();
	}

	public void insertIntoEditor(XModelObject macro) {
		paletteCreator.insertIntoEditor(macro);
	}

    public void dispose() {
    	super.dispose();
    	paletteCreator.dispose();
    }

	public IActionBars getActionBars() {
		return getSite() == null ? null : getSite().getActionBars();
	}

	public boolean isEnabled() {
		return true;
	}

	public void setContentDescription(String description) {
		
	}

	public IWorkbenchPage getPage() {
		return getSite().getPage();
	}

}
