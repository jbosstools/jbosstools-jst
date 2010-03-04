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

package org.jboss.tools.jst.jsp.outline.cssdialog.cssselector;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSTreeNode;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.viewers.CSSSelectorTreeViewer;
import org.w3c.dom.css.CSSRule;

/**
 * 
 * @author yzhishko
 *
 */

@SuppressWarnings("unused")
public class CSSTreeDragListener implements DragSourceListener {

	private Table table;
	private Tree tree;
	private CSSSelectorPartComposite parent;
	private TreeViewer treeViewer;
	private TableViewer tableViewer;
	
	public CSSTreeDragListener (CSSSelectorPartComposite parent, TreeViewer treeViewer, TableViewer tableViewer){
		this.table = tableViewer.getTable();
		this.tree = treeViewer.getTree();
		this.treeViewer = treeViewer;
		this.tableViewer = tableViewer;
		this.parent = parent;
	}
	
	
	public void dragStart(DragSourceEvent event) {
		event.doit = tree.getSelectionCount() > 0;
	}

	public void dragSetData(DragSourceEvent event) {
		List<String> selectedItems = new ArrayList<String>(0);
		TreeItem[] selectedTreeItems = tree.getSelection();
		if (selectedTreeItems != null) {
			for (int i = 0; i < selectedTreeItems.length; i++) {
				selectedItems.add(selectedTreeItems[i].getData()
						.toString());
			}
		}

		event.data = CSSSelectorTreeViewer.CSS_SELECTOR_TREE_VIWER_ID;
	}

	public void dragFinished(DragSourceEvent event) {
		if (event.detail == DND.DROP_MOVE) {
			List<String> selectedItems = new ArrayList<String>(0);
			TreeItem[] selectedTreeItems = tree.getSelection();
			if (selectedTreeItems != null) {
				for (int i = 0; i < selectedTreeItems.length; i++) {
					if (((CSSTreeNode) selectedTreeItems[i].getData())
							.getCssResource() instanceof CSSRule) {
						selectedItems.add(selectedTreeItems[i]
								.getData().toString());
					}
				}
			}
			for (int i = 0; i < selectedItems.size(); i++) {
				tableViewer.add(selectedItems.get(i));
			}
			parent.updateStyles();
		}
	}

}
