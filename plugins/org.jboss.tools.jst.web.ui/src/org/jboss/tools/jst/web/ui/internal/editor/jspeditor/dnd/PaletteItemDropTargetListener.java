/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.internal.editor.jspeditor.dnd;

import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.jboss.tools.common.model.ui.editors.dnd.DropData;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPTextEditor;
import org.jboss.tools.jst.web.ui.palette.internal.PaletteItemTransfer;
import org.jboss.tools.jst.web.ui.palette.internal.html.IPaletteItem;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Text;

/**
 * 
 * @author Daniel Azarov
 *
 */
public class PaletteItemDropTargetListener extends DropTargetAdapter implements TransferDropTargetListener {
	private JSPTextEditor editor;
	int lastpos = -1;

	int lastdetail = -1;
	
	private int operation;
	private PaletteItemDropCommand command=null;
	
	public PaletteItemDropTargetListener(JSPTextEditor editor){
		this.editor = editor;
	}

	@Override
	public void dragEnter(DropTargetEvent event) {
		lastpos = -1;
		lastdetail = operation = event.detail;
		if(!PaletteItemTransfer.getInstance().isSupportedType(event.currentDataType)){
			event.detail = DND.DROP_NONE;
			return;
		}
		IPaletteItem item = PaletteItemTransfer.getInstance().getPaletteItem();
		command = new PaletteItemDropCommand(item, false);
	}

	@Override
	public void dragLeave(DropTargetEvent event) {
	}

	@Override
	public void dragOperationChanged(DropTargetEvent event) {
		lastdetail = operation = event.detail;
	}

	@Override
	public void dragOver(DropTargetEvent event) {
		// show current cursor position during the drag JBIDE-13791 & JBIDE-14562
		event.feedback = DND.FEEDBACK_SCROLL | DND.FEEDBACK_SELECT;
		//IPaletteItem item = PaletteItemTransfer.getInstance().getPaletteItem();
		int position = editor.getDropPosition(event.x, event.y, command);
		int effectPosition = position;
		if (editor.getTextViewer() instanceof ITextViewerExtension5) {
		    ITextViewerExtension5 ext = (ITextViewerExtension5) editor.getTextViewer();
		    int off = ext.modelOffset2WidgetOffset(position);
		    if (off >= 0) {
		    	effectPosition = off;
		    }
		}
		editor.getDropEffect().setNewOffset(effectPosition);
		if (lastpos == position && position >= 0) {
			//position = lastpos;
			event.detail = lastdetail = operation;
			return;
		}
		
		lastpos = position;
		IndexedRegion region = editor.getModel().getIndexedRegion(position);
		if (region instanceof ElementImpl) {
			ElementImpl jspElement = (ElementImpl) region;
			NamedNodeMap attributes = jspElement.getAttributes();
			if (position == jspElement.getStartOffset()
					|| position == jspElement.getEndStartOffset()) {
				event.detail = lastdetail = operation;
				return;
			}
			
			event.detail = lastdetail = DND.DROP_NONE;
		} else if (region instanceof Text) {
			event.detail = lastdetail = operation;
		} else if (region instanceof DocumentType) {
			event.detail = lastdetail = DND.DROP_NONE;
			return;
		} else if (region == null) {
			// new place
			event.detail = lastdetail = operation;
		}
	}

	@Override
	public void drop(DropTargetEvent event) {
		int offset = editor.getDropPosition(event.x, event.y, command);
        editor.selectAndReveal(offset, 0);
        DropData dropData = new DropData(PaletteItemTransfer.PALETTE_ITEM, "",
				editor.getEditorInput(), editor.getTextViewer(),
				editor.getSelectionProvider());
		
		command.setDoNotShowDialog(event.detail == DND.DROP_COPY);
		command.setTagProposalFactory(JSPTagProposalFactory.getInstance());
		command.execute(dropData);
	}

	@Override
	public void dropAccept(DropTargetEvent event) {
		event.detail = lastdetail;
	}

	@Override
	public Transfer getTransfer() {
		return PaletteItemTransfer.getInstance();
	}

	@Override
	public boolean isEnabled(DropTargetEvent event) {
		return PaletteItemTransfer.getInstance().isSupportedType(event.currentDataType);
	}
}
