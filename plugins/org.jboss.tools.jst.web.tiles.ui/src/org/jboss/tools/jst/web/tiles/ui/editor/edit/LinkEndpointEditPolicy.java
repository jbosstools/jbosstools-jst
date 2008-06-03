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
package org.jboss.tools.jst.web.tiles.ui.editor.edit;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;

import org.jboss.tools.jst.web.tiles.ui.editor.figures.FigureFactory;

public class LinkEndpointEditPolicy
	extends org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy  {
	private List JSFhandles=null;
		

	private void removeJSFHandles() {
		if (JSFhandles == null)
			return;
		IFigure layer = getLayer(LayerConstants.HANDLE_LAYER);
		for (int i = 0; i < JSFhandles.size(); i++)
			layer.remove((IFigure) JSFhandles.get(i));
		JSFhandles = null;
	}

	protected void addSelectionHandles() {
		super.addSelectionHandles();
		getConnectionFigure().setForegroundColor(FigureFactory.selectedColor);
	}

	protected PolylineConnection getConnectionFigure() {
		return (PolylineConnection) ((GraphicalEditPart) getHost()).getFigure();
	}

	protected void removeSelectionHandles() {
		super.removeSelectionHandles();
		removeJSFHandles();
		getConnectionFigure().setForegroundColor(FigureFactory.normalColor);
	}

}