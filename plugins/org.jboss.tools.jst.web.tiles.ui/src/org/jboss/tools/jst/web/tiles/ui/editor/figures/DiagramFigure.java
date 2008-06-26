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
package org.jboss.tools.jst.web.tiles.ui.editor.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;

import org.jboss.tools.jst.web.tiles.ui.editor.edit.TilesDiagramEditPart;

public class DiagramFigure extends FreeformLayer implements IFigure {

	private TilesDiagramEditPart editPart;
	
	public DiagramFigure(TilesDiagramEditPart editPart){
		super();
		this.editPart = editPart;
		setLayoutManager(new TilesDiagramLayout(editPart.getTilesModel().getOptions()));
		setBorder(new MarginBorder(5));
		setBackgroundColor(ColorConstants.white);
		setOpaque(true);
	}
	
	public TilesDiagramEditPart getEditPart(){
		return editPart;
	}

}
