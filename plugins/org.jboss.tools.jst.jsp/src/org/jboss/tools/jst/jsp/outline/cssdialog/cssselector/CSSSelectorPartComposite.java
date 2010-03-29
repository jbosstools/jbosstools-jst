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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.StyleAttributes;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.dnd.CSSTableDragAdapter;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.dnd.CSSTableDropAdapter;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.dnd.CSSTreeDragAdapter;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSContainer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSJSPRecognizer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSRuleContainer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSSelectorTableModel;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSSelectorTreeModel;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSStyleSheetContainer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model.CSSTreeNode;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.selection.CSSClassSelectionChangedEvent;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.selection.CSSSelectionEventManager;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.selection.ICSSClassSelectionChangedListener;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.viewers.CSSSelectorFilter;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.viewers.CSSSelectorTableViewer;
import org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.viewers.CSSSelectorTreeViewer;

/**
 * 
 * @author yzhishko
 * 
 */

public class CSSSelectorPartComposite extends Composite implements
		SelectionListener {

	private static final int VIEWER_HEIGHT = 500;
	private static final int BUTTOND_WIDTH = 50;
	private static final int VIEWER_WIDTH = 175;

	/** Existing font family */
	private Composite moveButtonsContainer;
	private Composite tableButtonsContainer;
	private Button rightButton;
	private Button leftButton;
	private Button upButton;
	private Button downButton;
	private String setClasses;
	private CSSSelectorTreeViewer allCSSStyleClassViewer;
	private CSSSelectorTableViewer selectedClassesTableViewer;
	private Map<String, Map<String, String>> allCSSClassStyles;
	private StyleAttributes styleAttributes;
	private CSSSelectorTreeModel styleClassTreeModel;
	private CSSSelectorFilter filter;
	private List<ICSSClassSelectionChangedListener> changedListeners = new ArrayList<ICSSClassSelectionChangedListener>(
			0);

	public CSSSelectorPartComposite(StyleAttributes styleAttributes,
			Composite parentComposite, String setClasses) {
		super(parentComposite, SWT.BORDER);
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
		String[] selectedClasses = parseSetClasses();
		selectedClassesTableViewer.setModel(new CSSSelectorTableModel(
				selectedClasses));
		CSSJSPRecognizer recognizer = new CSSJSPRecognizer(
				(JSPMultiPageEditor) editorPart);
		if (recognizer.parseCSS() == CSSJSPRecognizer.VOID_RESULT) {
			return;
		}
		styleClassTreeModel = recognizer.getCssStyleClassTreeModel();
		allCSSClassStyles = recognizer.getCSSStyleMap();
		allCSSStyleClassViewer.setModel(styleClassTreeModel);
		updateStyles();
	}

	private void initDND() {
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };

		allCSSStyleClassViewer.addDragSupport(DND.DROP_MOVE, types,
				new CSSTreeDragAdapter(this, allCSSStyleClassViewer,
						selectedClassesTableViewer));

		selectedClassesTableViewer.addDragSupport(DND.DROP_MOVE, types,
				new CSSTableDragAdapter(this, allCSSStyleClassViewer,
						selectedClassesTableViewer));

		allCSSStyleClassViewer.addDropSupport(DND.DROP_MOVE, types,
				new DropTargetAdapter() {
					@Override
					public void drop(DropTargetEvent event) {
						if (event.data == null
								|| event.data
										.equals(CSSSelectorTreeViewer.CSS_SELECTOR_TREE_VIWER_ID)) {
							event.detail = DND.DROP_NONE;
							return;
						}
					}
				});

		selectedClassesTableViewer.addDropSupport(DND.DROP_MOVE, types,
				new CSSTableDropAdapter(this, allCSSStyleClassViewer,
						selectedClassesTableViewer));

	}

	private void initListeners() {

		allCSSStyleClassViewer
				.addDoubleClickListener(new IDoubleClickListener() {

					public void doubleClick(DoubleClickEvent event) {
						handleAddClass();
					}
				});

		selectedClassesTableViewer
				.addDoubleClickListener(new IDoubleClickListener() {

					public void doubleClick(DoubleClickEvent event) {
						handleRemoveClass();
					}

				});

		allCSSStyleClassViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {

					public void selectionChanged(
							final SelectionChangedEvent event) {
						if (!CSSSelectionEventManager.getInstance()
								.isHandleSelection()) {
							CSSSelectionEventManager.getInstance()
									.setHandleSelection(true);
							return;
						}
						if (!event.getSelection().isEmpty()) {
							rightButton.setEnabled(true);
							leftButton.setEnabled(false);
						} else {
							rightButton.setEnabled(false);
							leftButton.setEnabled(false);
						}
						selectedClassesTableViewer.getTable().deselectAll();
						checkForTableMove(event);
						if (event.getSelection().isEmpty()) {
							return;
						}
						Display.getCurrent().asyncExec(new Runnable() {

							public void run() {
								fireClassSelectionChanged(CSSSelectionEventManager
										.getInstance()
										.createTreeSelectionChangedEvent(
												event,
												allCSSStyleClassViewer
														.getModel()));
							}
						});
					}
				});

		selectedClassesTableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {

					public void selectionChanged(
							final SelectionChangedEvent event) {
						if (!CSSSelectionEventManager.getInstance()
								.isHandleSelection()) {
							CSSSelectionEventManager.getInstance()
									.setHandleSelection(true);
							return;
						}
						if (!event.getSelection().isEmpty()) {
							rightButton.setEnabled(false);
							leftButton.setEnabled(true);
						} else {
							rightButton.setEnabled(false);
							leftButton.setEnabled(false);
						}
						checkForTableMove(event);
						allCSSStyleClassViewer.getTree().deselectAll();
						if (event.getSelection().isEmpty()) {
							return;
						}
						Display.getCurrent().asyncExec(new Runnable() {

							public void run() {
								fireClassSelectionChanged(CSSSelectionEventManager
										.getInstance()
										.createTableSelectionChangedEvent(
												event,
												allCSSStyleClassViewer
														.getModel()));
							}
						});
					}
				});

		allCSSStyleClassViewer.getTree().addKeyListener(new KeyListener() {

			public void keyReleased(KeyEvent e) {
				if (SWT.ARROW_RIGHT == e.keyCode && SWT.ALT == e.stateMask) {
					handleAddClass();
				}
			}

			public void keyPressed(KeyEvent e) {
			}
		});

		selectedClassesTableViewer.getTable().addKeyListener(new KeyListener() {

			public void keyReleased(KeyEvent e) {
				if (SWT.ARROW_LEFT == e.keyCode && SWT.ALT == e.stateMask) {
					handleRemoveClass();
				}
			}

			public void keyPressed(KeyEvent e) {
			}
		});

		rightButton.addSelectionListener(this);
		leftButton.addSelectionListener(this);
		upButton.addSelectionListener(this);
		downButton.addSelectionListener(this);
	}

	private void checkForTableMove(SelectionChangedEvent event) {
		if (event.getSelection().isEmpty()) {
			upButton.setEnabled(false);
			downButton.setEnabled(false);
			return;
		}
		int[] selectionIndices = selectedClassesTableViewer.getTable()
				.getSelectionIndices();
		if (selectionIndices.length == 0) {
			upButton.setEnabled(false);
			downButton.setEnabled(false);
			return;
		}
		if (!isSequencedIndices(selectionIndices)) {
			upButton.setEnabled(true);
			downButton.setEnabled(true);
		} else {
			if (selectionIndices[0] != 0
					&& (selectionIndices[selectionIndices.length - 1] != selectedClassesTableViewer
							.getTable().getItemCount() - 1)) {
				upButton.setEnabled(true);
				downButton.setEnabled(true);
			} else if (selectionIndices[0] != 0) {
				downButton.setEnabled(false);
				upButton.setEnabled(true);
			} else if (selectionIndices[selectionIndices.length - 1] != selectedClassesTableViewer
					.getTable().getItemCount() - 1) {
				downButton.setEnabled(true);
				upButton.setEnabled(false);
			} else if (selectionIndices[0] == 0
					&& (selectionIndices[selectionIndices.length - 1] == selectedClassesTableViewer
							.getTable().getItemCount() - 1)) {
				upButton.setEnabled(false);
				downButton.setEnabled(false);
			}
		}
	}

	private boolean isSequencedIndices(int[] indices) {
		for (int i = 0; i < indices.length - 1; i++) {
			if (indices[i + 1] - indices[i] != 1)
				return false;
		}
		return true;
	}

	private void initControls() {
		allCSSStyleClassViewer = new CSSSelectorTreeViewer(this, SWT.MULTI
				| SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		filter = new CSSSelectorFilter();
		allCSSStyleClassViewer.addFilter(filter);
		moveButtonsContainer = new Composite(this, SWT.NONE);
		rightButton = new Button(moveButtonsContainer, SWT.PUSH);
		leftButton = new Button(moveButtonsContainer, SWT.PUSH);
		selectedClassesTableViewer = new CSSSelectorTableViewer(this, SWT.MULTI
				| SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		tableButtonsContainer = new Composite(this, SWT.NONE);
		upButton = new Button(tableButtonsContainer, SWT.PUSH);
		downButton = new Button(tableButtonsContainer, SWT.PUSH);
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
		upButton.setToolTipText(JstUIMessages.CSS_MOVE_UP_CSS_CLASS_TIP);

		ImageDescriptor upDesc = JspEditorPlugin
				.getImageDescriptor(Constants.IMAGE_UP_FILE_LOCATION);
		Image upImage = upDesc.createImage();
		upButton.setImage(upImage);
		upButton.setEnabled(false);
		upButton.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				Button button = (Button) e.getSource();
				button.getImage().dispose();
			}
		});
		downButton.setToolTipText(JstUIMessages.CSS_MOVE_DOWN_CSS_CLASS_TIP);

		ImageDescriptor downDesc = JspEditorPlugin
				.getImageDescriptor(Constants.IMAGE_DOWN_FILE_LOCATION);
		Image downImage = downDesc.createImage();
		downButton.setImage(downImage);
		downButton.setEnabled(false);
		downButton.addDisposeListener(new DisposeListener() {
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
			handleRemoveClass();
		} else if (ob.equals(rightButton)) {
			handleAddClass();
		} else if (ob.equals(upButton)) {
			handleMoveUp();
		} else if (ob.equals(downButton)) {
			handleMoveDown();
		} else if (ob.equals(allCSSStyleClassViewer.getTree())) {
			selectedClassesTableViewer.setSelection(new StructuredSelection());
		} else if (ob.equals(selectedClassesTableViewer.getTable())) {
			allCSSStyleClassViewer.setSelection(new StructuredSelection());
		}
	}

	private void handleMoveDown() {
		StructuredSelection selection = (StructuredSelection) selectedClassesTableViewer
				.getSelection();
		Object[] selectedItems = selection.toArray();
		int[] selectedIndices = selectedClassesTableViewer.getTable()
				.getSelectionIndices();
		int itemsCount = selectedClassesTableViewer.getTable().getItemCount();
		int selectionLength = selectedIndices.length;
		for (int i = 0; i < selectionLength; i++) {
			CSSSelectionEventManager.getInstance().setHandleSelection(false);
			selectedClassesTableViewer.remove(selectedItems[selectionLength - 1
					- i]);
			if (selectedIndices[selectionLength - 1 - i] == itemsCount - 1) {
				TableItem item = new TableItem(selectedClassesTableViewer
						.getTable(), selectedClassesTableViewer.getTable()
						.getStyle(), itemsCount - 1);
				item.setData(selectedItems[selectionLength - 1 - i]);
				itemsCount--;
			} else {
				TableItem item = new TableItem(selectedClassesTableViewer
						.getTable(), selectedClassesTableViewer.getTable()
						.getStyle(),
						selectedIndices[selectionLength - 1 - i] + 1);
				item.setData(selectedItems[selectionLength - 1 - i]);
			}
		}
		selectedClassesTableViewer.refresh();
		selectedClassesTableViewer.setSelection(new StructuredSelection(
				selectedItems));
		updateStyles();
	}

	private void handleMoveUp() {
		StructuredSelection selection = (StructuredSelection) selectedClassesTableViewer
				.getSelection();
		Object[] selectedItems = selection.toArray();
		int[] selectedIndices = selectedClassesTableViewer.getTable()
				.getSelectionIndices();
		CSSSelectionEventManager.getInstance().setHandleSelection(false);
		selectedClassesTableViewer.remove(selection.toArray());
		for (int i = 0; i < selectedIndices.length; i++) {
			if (selectedIndices[i] == i) {
				TableItem item = new TableItem(selectedClassesTableViewer
						.getTable(), selectedClassesTableViewer.getTable()
						.getStyle(), i);
				item.setData(selectedItems[i]);
			} else {
				TableItem item = new TableItem(selectedClassesTableViewer
						.getTable(), selectedClassesTableViewer.getTable()
						.getStyle(), selectedIndices[i] - 1);
				item.setData(selectedItems[i]);
			}
		}
		selectedClassesTableViewer.refresh();
		selectedClassesTableViewer.setSelection(new StructuredSelection(
				selectedItems));
		updateStyles();
	}

	public void widgetSelected(SelectionEvent e) {
		Object ob = e.getSource();
		if (ob.equals(leftButton)) {
			handleRemoveClass();
		} else if (ob.equals(rightButton)) {
			handleAddClass();
		} else if (ob.equals(upButton)) {
			handleMoveUp();
		} else if (ob.equals(downButton)) {
			handleMoveDown();
		}
	}

	private void handleRemoveClass() {
		TableItem[] selectedItems = selectedClassesTableViewer.getTable()
				.getSelection();
		if (selectedItems != null && selectedItems.length > 0) {
			List<String> itemsToRemove = new ArrayList<String>(0);
			for (int i = 0; i < selectedItems.length; i++) {
				itemsToRemove.add(selectedItems[i].getData().toString());
			}
			selectedClassesTableViewer.remove(itemsToRemove.toArray());
			updateStyles();
		}
	}

	private void handleAddClass() {
		Set<String> itemsToMove = new LinkedHashSet<String>(0);
		TreeItem[] selectedItems = allCSSStyleClassViewer.getTree()
				.getSelection();
		if (selectedItems != null && selectedItems.length > 0) {
			for (int i = 0; i < selectedItems.length; i++) {
				TreeItem item = selectedItems[i];
				CSSContainer container = ((CSSTreeNode) item.getData())
						.getCSSContainer();
				CSSTreeNode treeNode = (CSSTreeNode) item.getData();
				if ((container instanceof CSSStyleSheetContainer)) {
					List<CSSTreeNode> children = treeNode.getChildren();
					for (int j = 0; j < children.size(); j++) {
						itemsToMove.add(children.get(j).toString());
					}
				} else if (container instanceof CSSRuleContainer) {
					itemsToMove.add(treeNode.toString());
				}
			}
			selectedClassesTableViewer.add(itemsToMove.toArray());
			updateStyles();
			allCSSStyleClassViewer.setSelection(new StructuredSelection());
		}
	}

	private void createLayout() {
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		setLayout(gridLayout);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		setLayoutData(gridData);

		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = VIEWER_HEIGHT;
		gridData.widthHint = VIEWER_WIDTH;
		allCSSStyleClassViewer.getTree().setLayoutData(gridData);

		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = VIEWER_HEIGHT;
		gridData.widthHint = VIEWER_WIDTH;
		selectedClassesTableViewer.getTable().setLayoutData(gridData);

		final GridLayout moveBtnContGridLayout = new GridLayout();
		moveBtnContGridLayout.numColumns = 1;
		moveButtonsContainer.setLayout(moveBtnContGridLayout);

		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.widthHint = BUTTOND_WIDTH;
		rightButton.setLayoutData(gridData);

		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.widthHint = BUTTOND_WIDTH;
		leftButton.setLayoutData(gridData);

		final GridLayout tableBtnContGridLayout = new GridLayout();
		tableBtnContGridLayout.numColumns = 1;
		tableButtonsContainer.setLayout(tableBtnContGridLayout);
	}

	public void updateStyles() {
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

	private void fireClassSelectionChanged(
			final CSSClassSelectionChangedEvent event) {
		for (int i = 0; i < changedListeners.size(); i++) {
			final ICSSClassSelectionChangedListener listener = changedListeners
					.get(i);
			SafeRunner.run(new SafeRunnable() {
				public void run() throws Exception {
					for (int i = 0; i < changedListeners.size(); i++) {
						listener.classSelectionChanged(event);
					}
				}
			});
		}
	}

	public synchronized void addCSSClassSelectionChangedListener(
			ICSSClassSelectionChangedListener listener) {
		changedListeners.add(listener);
	}

	public synchronized void removeCSSClassSelectionChangedListener(
			ICSSClassSelectionChangedListener listener) {
		changedListeners.remove(listener);
	}

	@Override
	public void dispose() {
		for (int i = 0; i < changedListeners.size(); i++) {
			removeCSSClassSelectionChangedListener(changedListeners.get(i));
		}
		changedListeners = null;
		super.dispose();
	}

}
