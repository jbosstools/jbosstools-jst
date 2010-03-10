/*******************************************************************************
 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.dnd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.CSSSelectorPartComposite;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.viewers.CSSSelectorTableViewer;

/**
 * 
 * @author yzhishko
 *
 */

@SuppressWarnings("unused")
public class CSSTableDragListener implements DragSourceListener {

	private Table table;
	private Tree tree;
	private CSSSelectorPartComposite parent;
	private TreeViewer treeViewer;
	private TableViewer tableViewer;
	
	public CSSTableDragListener (CSSSelectorPartComposite parent, TreeViewer treeViewer, TableViewer tableViewer){
		this.table = tableViewer.getTable();
		this.tree = treeViewer.getTree();
		this.treeViewer = treeViewer;
		this.tableViewer = tableViewer;
		this.parent = parent;
	}
	
	public void dragStart(DragSourceEvent event) {
		event.doit = table.getSelectionCount() > 0;
	}

	public void dragSetData(DragSourceEvent event) {
		List<String> selectedItems = new ArrayList<String>(0);
		TableItem[] selectedTableItems = table.getSelection();
		if (selectedTableItems != null) {
			for (int i = 0; i < selectedTableItems.length; i++) {
				selectedItems.add(selectedTableItems[i].getData()
						.toString());
			}
		}
		event.data = CSSSelectorTableViewer.CSS_SELECTOR_TABLE_VIWER_ID;
	}

	public void dragFinished(DragSourceEvent event) {
		if (event.detail == DND.DROP_MOVE) {
			TableItem[] selectedTableItems = table.getSelection();
			if (selectedTableItems != null && selectedTableItems.length > 0) {
				for (int i = 0; i < selectedTableItems.length; i++) {
					tableViewer.remove(selectedTableItems[i].getData());
				}
			}
			parent.updateStyles();
		}
	}

}
