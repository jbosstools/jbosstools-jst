/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.internal;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteItem;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteTool;

public class PaletteItemDragSourceListener extends DragSourceAdapter  implements TransferDragSourceListener {
	EditPartViewer viewer;
	private boolean dragging = false;

	public PaletteItemDragSourceListener(EditPartViewer viewer) {
		this.viewer = viewer;
	}
	
	public boolean isDragging() {
		return dragging;
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		IPaletteItem item = getPaletteItem();
		if(item == null){
			event.doit = false;
		}
		dragging = true;
		PaletteItemTransfer.getInstance().setPaletteItem(item);
	}
	
	@Override
	public void dragSetData(DragSourceEvent event) {
		if(PaletteItemTransfer.getInstance().isSupportedType(event.dataType)){
			IPaletteItem item = getPaletteItem();
			event.data = item;
		}
	}

	@Override
	public void dragFinished(DragSourceEvent event) { 
		PaletteItemTransfer.getInstance().setPaletteItem(null);
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				dragging = false;
			}
		});
	}
	
	private IPaletteItem getPaletteItem() {
		List selection = viewer.getSelectedEditParts();
		if (selection.size() == 1) {
			EditPart editpart = (EditPart) selection.get(0);
			Object model = editpart.getModel();
			if (model instanceof PaletteTool)
				return ((PaletteTool) model).getPaletteItem();
		}
		return null;
	}

	@Override
	public Transfer getTransfer() {
		return PaletteItemTransfer.getInstance();
	}
}
