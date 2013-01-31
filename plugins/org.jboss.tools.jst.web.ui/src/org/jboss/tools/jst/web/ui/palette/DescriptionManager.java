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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.internal.text.html.HTMLTextPresenter;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.AbstractHoverInformationControlManager;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;

import org.jboss.tools.jst.web.ui.palette.model.PaletteItem;

public class DescriptionManager extends AbstractHoverInformationControlManager {
	private PaletteViewer viewer;

	public DescriptionManager(PaletteViewer viewer) {
		super(new DescriptionControlCreator());
		setSizeConstraints(60, 30, false, false);
		this.viewer = viewer;
	}

	protected void computeInformation() {
		org.eclipse.swt.graphics.Point p = getHoverEventLocation();
		EditPart part = viewer.findObjectAt(new Point(p.x, p.y));
		if (part instanceof GraphicalEditPart) {
			Object model = part.getModel();
			if (model instanceof PaletteItem) {
				IFigure fig = ((GraphicalEditPart)part).getFigure();
				org.eclipse.draw2d.geometry.Rectangle r = fig.getBounds();
				//fig.getParent().translateToAbsolute(r);
				setInformation(((PaletteItem)model).getHtmlDescription(), new Rectangle(p.x, p.y, r.width, r.height));
				return;
			}
		}
		setInformation(null, null);
	}
	
	static private class DescriptionControlCreator implements IInformationControlCreator {
		public IInformationControl createInformationControl(Shell parent) {
			return new DefaultInformationControl(parent, SWT.NONE, new HTMLTextPresenter(true));
		}
	}
}
