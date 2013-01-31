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

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.internal.ui.palette.editparts.DrawerFigure;
import org.eclipse.swt.widgets.Control;


/**
 * @author Daniel
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CustomDrawerFigure extends DrawerFigure {
	public CustomDrawerFigure(final Control control) {
		super(control);
	}
	public Dimension getMinimumSize(int wHint, int hHint) {
		/*
		 * Fix related to Bug #35176
		 * The figure returns a minimum size that is of at least a certain height, so as to
		 * prevent each drawer from getting too small (in which case, the scrollbars cover up
		 * the entire available space).
		 */
		if (isExpanded()) {
			List children = getContentPane().getChildren();
			if (!children.isEmpty()) {
				Dimension result = getCollapseToggle().getPreferredSize(wHint, hHint).getCopy();
				result.height += getContentPane().getInsets().getHeight();
				IFigure child = (IFigure)children.get(0);
				int visible = children.size();
				if(visible > 10)visible = 10;
				result.height += /*Math.min(80,*/ child.getPreferredSize(wHint, -1).height*visible;//);
				return result.intersect(getPreferredSize(wHint, hHint));
			}
		}

		return super.getMinimumSize(wHint, hHint);
	}
	
}
