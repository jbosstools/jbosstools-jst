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

import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.internal.ui.palette.editparts.SliderPaletteEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.jboss.tools.jst.web.ui.palette.xpl.CustomPaletteToolbarLayout;
import org.jboss.tools.jst.web.ui.palette.xpl.DrawerAnimationController;


/**
 * @author Daniel
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CustomSliderPaletteEditPart extends SliderPaletteEditPart {
	public CustomSliderPaletteEditPart(PaletteRoot paletteRoot) {
		super(paletteRoot);
	}
	protected void registerVisuals() {
		super.registerVisuals();
		DrawerAnimationController controller = new DrawerAnimationController(
			((PaletteViewer)getViewer()).getPaletteViewerPreferences());
		getViewer().getEditPartRegistry().put(DrawerAnimationController.class, controller);
		ToolbarLayout layout = new CustomPaletteToolbarLayout(controller);
		getFigure().setLayoutManager(layout);
	}

}
