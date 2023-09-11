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
package org.jboss.tools.jst.web.ui.palette;

import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.FocusListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.internal.ui.palette.editparts.DrawerEditPart;
import org.eclipse.gef.palette.PaletteDrawer;


/**
 * @author Daniel
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CustomDrawerEditPart extends DrawerEditPart {
	
	public CustomDrawerEditPart(PaletteDrawer drawer) {
		super(drawer);
	}
	public IFigure createFigure() {
		CustomDrawerFigure fig = new CustomDrawerFigure(getViewer().getControl()) {
			public IFigure getToolTip() {
				return createToolTip();
			}
		};
		fig.setExpanded(getModel().isInitiallyOpen());
		fig.setPinned(getModel().isInitiallyPinned());
		fig.getCollapseToggle().setRequestFocusEnabled(true);
		fig.getCollapseToggle().addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent fe) {
				getViewer().select(CustomDrawerEditPart.this);
			}
			public void focusLost(FocusEvent fe) {
			}
		});
		return fig;
	}
}
