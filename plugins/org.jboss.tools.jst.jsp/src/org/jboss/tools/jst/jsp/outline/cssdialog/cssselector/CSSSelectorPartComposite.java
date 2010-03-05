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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.StyleAttributes;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSJSPRecognizer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSSelectorTableModel;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSSelectorTreeModel;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSTreeNode;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.viewers.CSSSelectorFilter;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.viewers.CSSSelectorTableViewer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.viewers.CSSSelectorTreeViewer;
import org.w3c.dom.css.CSSRule;

/**
 * 
 * @author yzhishko
 * 
 */

public class CSSSelectorPartComposite extends Composite implements
		SelectionListener {

	private static final int LIST_HEIGHT = 500;
	private static final int BUTTOND_WIDTH = 50;
	private static final int LIST_WIDTH = 175;

	/** Existing font family */
	Composite buttonsContainer;
	private Button rightButton;
	private Button leftButton;
	private String setClasses;
	private CSSSelectorTreeViewer allCSSStyleClassViewer;
	private CSSSelectorTableViewer selectedClassesTableViewer;
	private Map<String, Map<String, String>> allCSSClassStyles;
	private StyleAttributes styleAttributes;
	private CSSSelectorTreeModel styleClassTreeModel;
	private CSSSelectorFilter filter;

	public CSSSelectorPartComposite(StyleAttributes styleAttributes,
			Composite parentComposite, String setClasses) {
		super(parentComposite, SWT.NONE);
		this.setClasses = setClasses;
		this.styleAttributes = styleAttributes;
		creatSelectorPart();
	}

	private void creatSelectorPart() {
		initControls();
		createLayout();
		initListeners();
		initDND();
		initDefaultContent();
	}

	private void initDefaultContent() {
		IEditorPart editorPart = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (!(editorPart instanceof JSPMultiPageEditor)) {
			return;
		}
		CSSJSPRecognizer recognizer = new CSSJSPRecognizer(
				(JSPMultiPageEditor) editorPart);
		if (recognizer.parseCSS() == CSSJSPRecognizer.VOID_RESULT) {
			return;
		}
		styleClassTreeModel = recognizer.getCssStyleClassTreeModel();
		allCSSClassStyles = recognizer.getCSSStyleMap();
		allCSSStyleClassViewer.setModel(styleClassTreeModel);
		String[] selectedClasses = parseSetClasses();
		selectedClassesTableViewer.setModel(new CSSSelectorTableModel(
				selectedClasses));
		updateStyles();
	}

	private void initDND() {
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
	
		allCSSStyleClassViewer.addDragSupport(DND.DROP_MOVE, types,
				new CSSTreeDragListener(this, allCSSStyleClassViewer,
						selectedClassesTableViewer));

		selectedClassesTableViewer.addDragSupport(DND.DROP_MOVE, types,
				new CSSTableDragListener(this, allCSSStyleClassViewer,
						selectedClassesTableViewer));
		
		allCSSStyleClassViewer.addDropSupport(DND.DROP_MOVE, types, new DropTargetAdapter(){
			@Override
			public void drop(DropTargetEvent event) {
				if (event.data == null || event.data.equals(CSSSelectorTreeViewer.CSS_SELECTOR_TREE_VIWER_ID)) {
					event.detail = DND.DROP_NONE;
					return;
				}
			}
		});
		
		selectedClassesTableViewer.addDropSupport(DND.DROP_MOVE, types, new DropTargetAdapter(){
			@Override
			public void drop(DropTargetEvent event) {
				if (event.data == null || event.data.equals(CSSSelectorTableViewer.CSS_SELECTOR_TABLE_VIWER_ID)) {
					event.detail = DND.DROP_NONE;
					return;
				}
			}
		});

	}

	private void initListeners() {

		final Tree tree = allCSSStyleClassViewer.getTree();
		final Table table = selectedClassesTableViewer.getTable();
		allCSSStyleClassViewer
				.addDoubleClickListener(new IDoubleClickListener() {

					public void doubleClick(DoubleClickEvent event) {
						TreeItem[] selectedItems = tree.getSelection();
						if (selectedItems != null && selectedItems.length > 0) {
							TreeItem selectedItem = selectedItems[selectedItems.length - 1];
							CSSTreeNode treeNode = (CSSTreeNode) selectedItem
									.getData();
							if (!(treeNode.getCssResource() instanceof CSSRule)) {
								if (allCSSStyleClassViewer
										.getExpandedState(treeNode)) {
									allCSSStyleClassViewer.collapseToLevel(
											treeNode, 1);
									return;
								}
								allCSSStyleClassViewer.expandToLevel(treeNode,
										1);
								return;
							}
							selectedClassesTableViewer.add(treeNode.toString());
							updateStyles();
						}
					}
				});

		tree.addMouseListener(new MouseAdapter() {

			public void mouseDown(MouseEvent e) {
				if (tree.getSelectionCount() > 0) {
					selectedClassesTableViewer.getTable().deselectAll();
					leftButton.setEnabled(false);
					rightButton.setEnabled(true);
				}
			}
		});

		selectedClassesTableViewer
				.addDoubleClickListener(new IDoubleClickListener() {

					public void doubleClick(DoubleClickEvent event) {
						TableItem[] selectedItems = table.getSelection();
						if (selectedItems != null && selectedItems.length > 0) {
							TableItem selectedItem = selectedItems[selectedItems.length - 1];
							selectedClassesTableViewer.remove(selectedItem
									.getData());
							updateStyles();
						}
					}

				});

		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(MouseEvent e) {
				if (table.getSelectionCount() > 0) {
					tree.deselectAll();
					rightButton.setEnabled(false);
					leftButton.setEnabled(true);
				}
			}

		});

		rightButton.addSelectionListener(this);
		leftButton.addSelectionListener(this);
	}

	private void initControls() {
		allCSSStyleClassViewer = new CSSSelectorTreeViewer(this, SWT.MULTI
				| SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		filter = new CSSSelectorFilter();
		allCSSStyleClassViewer.addFilter(filter);
		buttonsContainer = new Composite(this, SWT.NONE);
		rightButton = new Button(buttonsContainer, SWT.PUSH);
		leftButton = new Button(buttonsContainer, SWT.PUSH);
		selectedClassesTableViewer = new CSSSelectorTableViewer(this, SWT.MULTI
				| SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		rightButton.setToolTipText(JstUIMessages.CSS_ADD_CSS_CLASS_TIP);
		ImageDescriptor rightDesc = JspEditorPlugin
				.getImageDescriptor(Constants.IMAGE_RIGHT_FILE_LOCATION);
		Image rightImage = rightDesc.createImage();
		rightButton.setImage(rightImage);
		rightButton.setEnabled(false);
		rightButton.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				Button button = (Button) e.getSource();
				button.getImage().dispose();
			}
		});

		leftButton.setToolTipText(JstUIMessages.CSS_REMOVE_CSS_CLASS_TIP);

		ImageDescriptor leftDesc = JspEditorPlugin
				.getImageDescriptor(Constants.IMAGE_LEFT_FILE_LOCATION);
		Image leftImage = leftDesc.createImage();
		leftButton.setImage(leftImage);
		leftButton.setEnabled(false);
		leftButton.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				Button button = (Button) e.getSource();
				button.getImage().dispose();
			}
		});
	}

	private String[] parseSetClasses() {
		if (setClasses == null || setClasses.length() == 0) {
			return new String[0];
		}
		StringTokenizer stringTokenizer = new StringTokenizer(
				setClasses.trim(), " ", false); //$NON-NLS-1$
		java.util.List<String> list = new ArrayList<String>(0);
		while (stringTokenizer.hasMoreTokens()) {
			String token = stringTokenizer.nextToken();
			list.add(token.trim());
		}
		return list.toArray(new String[0]);
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		Object ob = e.getSource();
		if (ob.equals(leftButton)) {
			handleLeftButtonSelection();
		} else if (ob.equals(rightButton)) {
			handleRightButtonSelection();
		} else if (ob.equals(allCSSStyleClassViewer.getTree())) {
			selectedClassesTableViewer.getTable().deselectAll();
			leftButton.setEnabled(false);
			rightButton.setEnabled(true);
		} else if (ob.equals(selectedClassesTableViewer.getTable())) {
			allCSSStyleClassViewer.getTree().deselectAll();
			rightButton.setEnabled(false);
			leftButton.setEnabled(true);
		}
	}

	public void widgetSelected(SelectionEvent e) {
		Object ob = e.getSource();
		if (ob.equals(leftButton)) {
			handleLeftButtonSelection();
		} else if (ob.equals(rightButton)) {
			handleRightButtonSelection();
		}
	}

	private void handleLeftButtonSelection() {
		TableItem[] selectedItems = selectedClassesTableViewer.getTable()
				.getSelection();

		for (int i = 0; i < selectedItems.length; i++) {
			selectedClassesTableViewer.remove(selectedItems[i].getData());
		}
		updateStyles();
	}

	private void handleRightButtonSelection() {
		TreeItem[] selectedItems = allCSSStyleClassViewer.getTree()
				.getSelection();
		if (selectedItems != null && selectedItems.length > 0) {
			for (int i = 0; i < selectedItems.length; i++) {
				TreeItem item = selectedItems[i];
				if (!(((CSSTreeNode) item.getData()).getCssResource() instanceof CSSRule)) {
					continue;
				}
				selectedClassesTableViewer.add(((CSSTreeNode) item.getData())
						.toString());
			}
		}
		updateStyles();
	}

	private void createLayout() {
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		setLayout(gridLayout);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		setLayoutData(gridData);

		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = LIST_HEIGHT;
		gridData.widthHint = LIST_WIDTH;
		allCSSStyleClassViewer.getTree().setLayoutData(gridData);

		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = LIST_HEIGHT;
		gridData.widthHint = LIST_WIDTH;
		selectedClassesTableViewer.getTable().setLayoutData(gridData);

		final GridLayout btmContGridLayout = new GridLayout();
		btmContGridLayout.numColumns = 1;
		buttonsContainer.setLayout(btmContGridLayout);

		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.widthHint = BUTTOND_WIDTH;
		rightButton.setLayoutData(gridData);

		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.widthHint = BUTTOND_WIDTH;
		leftButton.setLayoutData(gridData);

	}

	void updateStyles() {
		String[] selectedItems = getItemsFromSelectedTable();
		styleAttributes.clear();
		if (filter == null) {
			filter = new CSSSelectorFilter();
			allCSSStyleClassViewer.addFilter(filter);
		}
		filter.removeAllFilters();
		if (selectedItems != null && selectedItems.length != 0) {
			selectedItems = getSortedStyleClasses(selectedItems);
			for (int i = 0; i < selectedItems.length; i++) {
				if (selectedItems[i] != null) {
					styleAttributes.putAll(allCSSClassStyles
							.get(selectedItems[i]));
					filter.addFilterName(selectedItems[i]);
				}
			}
		}
		allCSSStyleClassViewer.refresh();
		allCSSStyleClassViewer.getTree().deselectAll();
		selectedClassesTableViewer.getTable().deselectAll();
		rightButton.setEnabled(false);
		leftButton.setEnabled(false);
	}

	public String getCSSStyleClasses() {
		StringBuilder stringBuilder = new StringBuilder(""); //$NON-NLS-1$
		String[] selectedItems = getItemsFromSelectedTable();
		if (selectedItems != null && selectedItems.length != 0) {
			for (int i = 0; i < selectedItems.length - 1; i++) {
				stringBuilder.append(selectedItems[i] + " "); //$NON-NLS-1$
			}
			stringBuilder.append(selectedItems[selectedItems.length - 1]);
		}
		return stringBuilder.toString();
	}

	private String[] getSortedStyleClasses(String[] unsortedClasses) {
		String[] sortedStyleClasses = new String[unsortedClasses.length];
		if (allCSSClassStyles != null) {
			Set<String> keySet = allCSSClassStyles.keySet();
			int iter = 0;
			for (Iterator<String> iterator = keySet.iterator(); iterator
					.hasNext();) {
				String styleClassName = iterator.next();
				for (int i = 0; i < unsortedClasses.length; i++) {
					if (unsortedClasses[i].equals(styleClassName)) {
						sortedStyleClasses[iter] = styleClassName;
						iter++;
						continue;
					}
				}
			}
		}
		return sortedStyleClasses;
	}

	private String[] getItemsFromSelectedTable() {
		TableItem[] selectedTableItems = selectedClassesTableViewer.getTable()
				.getItems();
		java.util.List<String> selectedItemsList = new ArrayList<String>(0);
		if (selectedTableItems != null) {
			for (int i = 0; i < selectedTableItems.length; i++) {
				selectedItemsList.add((String) selectedTableItems[i].getData());
			}
		}
		return selectedItemsList.toArray(new String[0]);
	}

}
