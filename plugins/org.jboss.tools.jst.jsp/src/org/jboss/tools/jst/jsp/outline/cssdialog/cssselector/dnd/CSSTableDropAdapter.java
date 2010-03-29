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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.CSSSelectorPartComposite;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSContainer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSRuleContainer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSStyleSheetContainer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSTreeNode;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.selection.CSSSelectionEventManager;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.viewers.CSSSelectorTableViewer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.viewers.CSSSelectorTreeViewer;

/**
 * 
 * @author yzhishko
 * 
 */

public class CSSTableDropAdapter extends DropTargetAdapter {

	private TableViewer tableViewer;
	private CSSSelectorPartComposite parent;
	private TreeViewer treeViewer;

	public CSSTableDropAdapter(CSSSelectorPartComposite parent,
			TreeViewer treeViewer, TableViewer tableViewer) {
		this.treeViewer = treeViewer;
		this.tableViewer = tableViewer;
		this.parent = parent;
	}

	@Override
	public void drop(DropTargetEvent event) {
		if (event.data == null) {
			event.detail = DND.DROP_NONE;
		} else if (event.data
				.equals(CSSSelectorTableViewer.CSS_SELECTOR_TABLE_VIWER_ID)) {
			handleTableSelectionMove(event);
			event.detail = DND.DROP_NONE;
		} else if (event.data
				.equals(CSSSelectorTreeViewer.CSS_SELECTOR_TREE_VIWER_ID)) {
			handleTreeSelectionMove(event);
			event.detail = DND.DROP_NONE;
		}
	}

	protected void handleTreeSelectionMove(DropTargetEvent event) {
		Set<String> itemsToMove = new LinkedHashSet<String>(0);
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
						itemsToMove.add(((CSSTreeNode) children.get(j))
								.toString());
					}
				} else if (container instanceof CSSRuleContainer) {
					itemsToMove.add(treeNode.toString());
				}
			}
			filterExistingItems(itemsToMove);
			if (itemsToMove.size() != 0) {
				if (event.item == null) {
					handleEndInsertFromTree(itemsToMove, event);
				} else {
					handleMiddleInsertFromTree(itemsToMove, event);
				}
				parent.updateStyles();
			}
			treeViewer.setSelection(new StructuredSelection());
		}
	}

	protected void filterExistingItems(Set<String> itemsSet) {
		Object[] items = itemsSet.toArray();
		for (int i = 0; i < items.length; i++) {
			if (isContain(items[i])) {
				itemsSet.remove(items[i]);
			}
		}
	}

	private boolean isContain(Object element) {
		TableItem[] items = tableViewer.getTable().getItems();
		if (items != null) {
			for (int i = 0; i < items.length; i++) {
				if (element.equals(items[i].getData())) {
					return true;
				}
			}
		}
		return false;
	}

	protected void handleEndInsertFromTree(Set<String> itemsToMove,
			DropTargetEvent event) {
		tableViewer.add(itemsToMove.toArray(new String[0]));
		tableViewer.getTable().setFocus();
		tableViewer.setSelection(new StructuredSelection(itemsToMove
				.toArray(new String[0])), true);
	}

	protected void handleMiddleInsertFromTree(Set<String> itemsToMove,
			DropTargetEvent event) {
		String[] selectedItems = itemsToMove.toArray(new String[0]);
		TableItem item = (TableItem) event.item;
		Point pt = Display.getCurrent().map(null, tableViewer.getTable(),
				event.x, event.y);
		Rectangle bounds = item.getBounds();
		int itemIndex = getItemIndex(item);
		if (pt.y < bounds.y + bounds.height / 2) {
			for (int i = 0; i < selectedItems.length; i++) {
				itemIndex = getItemIndex(item);
				TableItem tableItem = new TableItem(tableViewer.getTable(),
						tableViewer.getTable().getStyle(), itemIndex);
				tableItem.setData(selectedItems[i]);
			}
			tableViewer.refresh();
			tableViewer.setSelection(new StructuredSelection(selectedItems),
					true);
		} else if (pt.y >= bounds.y + bounds.height / 2) {
			for (int i = 0; i < selectedItems.length; i++) {
				itemIndex = getItemIndex(item);
				itemIndex = itemIndex + i + 1;
				if (itemIndex > tableViewer.getTable().getItemCount()) {
					itemIndex = tableViewer.getTable().getItemCount();
				}
				TableItem tableItem = new TableItem(tableViewer.getTable(),
						tableViewer.getTable().getStyle(), itemIndex);
				tableItem.setData(selectedItems[i]);
			}
			tableViewer.refresh();
			tableViewer.setSelection(new StructuredSelection(selectedItems),
					true);
		}
	}

	@Override
	public void dragOver(DropTargetEvent event) {
		event.feedback = DND.FEEDBACK_SCROLL;
		if (event.item != null) {
			TableItem item = (TableItem) event.item;
			Point pt = Display.getCurrent().map(null, tableViewer.getTable(),
					event.x, event.y);
			Rectangle bounds = item.getBounds();
			if (pt.y < bounds.y + bounds.height / 3) {
				event.feedback |= DND.FEEDBACK_INSERT_BEFORE;
			} else if (pt.y > bounds.y + 2 * bounds.height / 3) {
				event.feedback |= DND.FEEDBACK_INSERT_AFTER;
			} else {
				event.feedback |= DND.FEEDBACK_SELECT;
			}
		}
	}

	protected void handleTableSelectionMove(DropTargetEvent event) {
		if (event.item == null) {
			handleTableEndInsert(event);
		} else {
			handleTableMiddleInsert(event);
		}
		parent.updateStyles();
	}

	private int getItemIndex(TableItem item) {
		TableItem[] tableItems = tableViewer.getTable().getItems();
		for (int i = 0; i < tableItems.length; i++) {
			if (item.getData().equals(tableItems[i].getData())) {
				return i;
			}
		}
		return 0;
	}

	protected void handleTableEndInsert(DropTargetEvent event) {
		List<String> selectedCSSNames = getSelectedItems();
		CSSSelectionEventManager.getInstance().setHandleSelection(false);
		tableViewer.remove(selectedCSSNames.toArray(new String[0]));
		tableViewer.add(selectedCSSNames.toArray(new String[0]));
		tableViewer.setSelection(new StructuredSelection(selectedCSSNames),
				true);
	}

	protected void handleTableMiddleInsert(DropTargetEvent event) {
		TableItem item = (TableItem) event.item;
		Point pt = Display.getCurrent().map(null, tableViewer.getTable(),
				event.x, event.y);
		Rectangle bounds = item.getBounds();
		int itemIndex = getItemIndex(item);
		List<String> selectedItems = getSelectedItems();
		if (selectedItems.contains(item.getData().toString())) {
			return;
		}
		if (pt.y < bounds.y + bounds.height / 3) {
			for (int i = 0; i < selectedItems.size(); i++) {
				CSSSelectionEventManager.getInstance()
						.setHandleSelection(false);
				tableViewer.remove(selectedItems.get(i));
				itemIndex = getItemIndex(item);
				TableItem tableItem = new TableItem(tableViewer.getTable(),
						tableViewer.getTable().getStyle(), itemIndex);
				tableItem.setData(selectedItems.get(i));
			}
			tableViewer.refresh();
			tableViewer.setSelection(new StructuredSelection(selectedItems),
					true);
		} else if (pt.y > bounds.y + 2 * bounds.height / 3) {
			for (int i = 0; i < selectedItems.size(); i++) {
				CSSSelectionEventManager.getInstance()
						.setHandleSelection(false);
				tableViewer.remove(selectedItems.get(i));
				itemIndex = getItemIndex(item);
				itemIndex = itemIndex + i + 1;
				if (itemIndex > tableViewer.getTable().getItemCount()) {
					itemIndex = tableViewer.getTable().getItemCount();
				}
				TableItem tableItem = new TableItem(tableViewer.getTable(),
						tableViewer.getTable().getStyle(), itemIndex);
				tableItem.setData(selectedItems.get(i));
			}
			tableViewer.refresh();
			tableViewer.setSelection(new StructuredSelection(selectedItems),
					true);
		}
	}

	private List<String> getSelectedItems() {
		List<String> selectedNamesList = new ArrayList<String>(0);
		TableItem[] selectedItems = tableViewer.getTable().getSelection();
		if (selectedItems == null) {
			return selectedNamesList;
		}
		for (int i = 0; i < selectedItems.length; i++) {
			selectedNamesList.add(selectedItems[i].getData().toString());
		}
		return selectedNamesList;
	}

}
