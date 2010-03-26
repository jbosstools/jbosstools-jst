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
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.CSSSelectorPartComposite;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSContainer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSRuleContainer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSStyleSheetContainer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSTreeNode;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.viewers.CSSSelectorTreeViewer;
import org.w3c.dom.css.CSSRule;

/**
 * 
 * @author yzhishko
 * 
 */

@SuppressWarnings("unused")
public class CSSTreeDragAdapter implements DragSourceListener {

	private Table table;
	private Tree tree;
	private CSSSelectorPartComposite parent;
	private TreeViewer treeViewer;
	private TableViewer tableViewer;

	public CSSTreeDragAdapter(CSSSelectorPartComposite parent,
			TreeViewer treeViewer, TableViewer tableViewer) {
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
				selectedItems.add(selectedTreeItems[i].getData().toString());
			}
		}

		event.data = CSSSelectorTreeViewer.CSS_SELECTOR_TREE_VIWER_ID;
	}

	public void dragFinished(DragSourceEvent event) {
		if (event.detail == DND.DROP_MOVE) {
			Set<CSSTreeNode> itemsToMove = new LinkedHashSet<CSSTreeNode>(0);
			TreeItem[] selectedItems = treeViewer.getTree().getSelection();
			if (selectedItems != null && selectedItems.length > 0) {
				for (int i = 0; i < selectedItems.length; i++) {
					TreeItem item = selectedItems[i];
					CSSContainer container = ((CSSTreeNode) item.getData())
							.getCSSContainer();
					CSSTreeNode treeNode = (CSSTreeNode) item.getData();
					if ((container instanceof CSSStyleSheetContainer)) {
						List<CSSTreeNode> children = treeNode.getChildren();
						for (int j = 0; j < children.size(); j++) {
							itemsToMove.add(children.get(j));
						}
					} else if (container instanceof CSSRuleContainer) {
						itemsToMove.add(treeNode);
					}
				}
				for (Iterator<CSSTreeNode> iterator = itemsToMove.iterator(); iterator
						.hasNext();) {
					CSSTreeNode cssTreeNode = (CSSTreeNode) iterator.next();
					tableViewer.add(cssTreeNode.toString());
				}
				parent.updateStyles();
			}
		}
	}

}
