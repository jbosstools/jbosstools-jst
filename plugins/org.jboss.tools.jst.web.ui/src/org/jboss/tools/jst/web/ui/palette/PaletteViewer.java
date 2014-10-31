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
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jboss.tools.common.model.ui.dnd.ModelTransfer;
import org.jboss.tools.common.model.ui.editors.dnd.DropData;
import org.jboss.tools.common.model.ui.views.palette.IPalettePageAdapter;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPTextEditor;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.dnd.JSPTagProposalFactory;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.dnd.PaletteItemDropCommand;
import org.jboss.tools.jst.web.ui.palette.internal.PaletteItemDragSourceListener;
import org.jboss.tools.jst.web.ui.palette.internal.PaletteItemTransfer;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteItem;
import org.jboss.tools.jst.web.ui.palette.internal.html.impl.PaletteTool;
import org.jboss.tools.jst.web.ui.palette.model.IPaletteModel;
import org.jboss.tools.jst.web.ui.palette.model.PaletteItem;

public class PaletteViewer extends org.eclipse.gef.ui.palette.PaletteViewer {

	private IPalettePageAdapter viewPart;
	DragSourceListener listener;
	private IPaletteModel model;

    static Transfer[] transferTypes = new Transfer[] {
            TextTransfer.getInstance(), ModelTransfer.getInstance() };
    static Transfer[] html5TransferTypes = new Transfer[] {
    	PaletteItemTransfer.getInstance() };

	public PaletteViewer(IPalettePageAdapter viewPart, IPaletteModel model) {
		this.viewPart = viewPart;
		this.model = model;
		if(model.getType().equals(IPaletteModel.TYPE_HTML5)){
			setEditPartFactory(new MobilePaletteEditPartFactory());
		}else{
			setEditPartFactory(new CustomPaletteEditPartFactory());
		}
	}
	
	void addDragStartSupport() {
		DragSource dragSource = new DragSource(getControl(), DND.DROP_COPY | DND.DROP_MOVE);
		if(model.getType().equals(IPaletteModel.TYPE_HTML5)){
			dragSource.setTransfer(html5TransferTypes);
			listener = new PaletteItemDragSourceListener(this);
		}else{
			dragSource.setTransfer(transferTypes);
			listener = new PaletteDragSourceListener(this);
		}
		dragSource.addDragListener(listener);
	}
	
	private boolean isDragging(){
		if(listener instanceof PaletteItemDragSourceListener){
			return ((PaletteItemDragSourceListener) listener).isDragging();
		}else if(listener instanceof PaletteDragSourceListener){
			return ((PaletteDragSourceListener) listener).isDragging();
		}
		return false;
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

		if(!isDragging()){
			if (tool instanceof PaletteItem) {
				insertIntoEditor((PaletteItem)tool);
			}else if(tool instanceof PaletteTool){
				insertIntoEditor((PaletteTool)tool);
			}
		}
	}

	private void insertIntoEditor(PaletteTool tool) {
		IPaletteItem item = tool.getPaletteItem();
		ITextEditor editor = viewPart.getActiveTextEditor();
		if(editor instanceof JSPMultiPageEditor){
			DropData dropData = new DropData(PaletteItemTransfer.PALETTE_ITEM, "",
					editor.getEditorInput(), ((JSPMultiPageEditor)editor).getJspEditor().getTextViewer(),
					editor.getSelectionProvider());
				//dropData.setValueProvider(createAttributeDescriptorValueProvider());
			
			//dropData.setAttributeName(dropContext.getAttributeName());
			PaletteItemDropCommand command = new PaletteItemDropCommand(item, false);
			command.setTagProposalFactory(JSPTagProposalFactory.getInstance());
			command.execute(dropData);
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
