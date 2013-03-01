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

import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.jboss.tools.common.model.ui.dnd.ModelTransfer;
import org.jboss.tools.common.model.ui.views.palette.IPalettePageAdapter;
import org.jboss.tools.jst.web.ui.palette.model.PaletteItem;
import org.jboss.tools.jst.web.ui.palette.model.PaletteModel;

public class PaletteViewer extends org.eclipse.gef.ui.palette.PaletteViewer {

	private IPalettePageAdapter viewPart;
	PaletteDragSourceListener listener;

    static Transfer[] transferTypes = new Transfer[] {
            TextTransfer.getInstance(),ModelTransfer.getInstance() };

	public PaletteViewer(IPalettePageAdapter viewPart, PaletteModel model) {
		this.viewPart = viewPart;
		if(model.getType().equals(PaletteModel.TYPE_MOBILE)){
			setEditPartFactory(new MobilePaletteEditPartFactory());
		}else{
			setEditPartFactory(new CustomPaletteEditPartFactory());
		}
	}
	
	void addDragStartSupport() {
        listener = new PaletteDragSourceListener(this);
		final DragSource dragSource = new DragSource(getControl(), DND.DROP_COPY | DND.DROP_MOVE);
		dragSource.setTransfer(transferTypes);
		dragSource.addDragListener(listener);		
	}

	public void setActiveTool(ToolEntry tool) {
		EditPart part = getFocusEditPart();
		if (part instanceof GraphicalEditPart) {
			IFigure fig = ((GraphicalEditPart)part).getFigure();
			if (fig instanceof Clickable) {
				part.setSelected(EditPart.SELECTED_NONE);
				((Clickable)fig).getModel().setArmed(false);
				((Clickable)fig).getModel().setPressed(false);
				((Clickable)fig).getModel().setSelected(false);
			}
		}
		super.setActiveTool(null);

		if (tool instanceof PaletteItem && !listener.isDragging()) {
			insertIntoEditor((PaletteItem)tool);
		}
	}

	private void insertIntoEditor(PaletteItem item) {
		viewPart.insertIntoEditor(item.getXModelObject());
	}

	public void setEnabled(boolean enabled) {
		EditPart root = getContents();
		List v = root.getChildren();
		if (v != null) {
			int max = v.size();
			for (int i = 0; i < max; i++) {
				setCategoryEnabled((EditPart)v.get(i), enabled);
			}
		}
	}

	private void setCategoryEnabled(EditPart part, boolean enabled) {
		List v = part.getChildren();
		if (v != null) {
			int max = v.size();
			for (int i = 0; i < max; i++) {
				Object obj = v.get(i);
				if( obj instanceof GraphicalEditPart) {
					IFigure fig = ((GraphicalEditPart)obj).getFigure();

					if (fig instanceof Clickable) {
						((Clickable)fig).setEnabled(enabled);
					}
				}
			}
		}
	}
}
