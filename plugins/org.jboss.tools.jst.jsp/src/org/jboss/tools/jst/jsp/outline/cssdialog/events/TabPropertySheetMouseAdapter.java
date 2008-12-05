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
package org.jboss.tools.jst.jsp.outline.cssdialog.events;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.outline.cssdialog.FontFamilyDialog;
import org.jboss.tools.jst.jsp.outline.cssdialog.ImageSelectionDialog;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.CSSConstants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.ImageCombo;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Util;
import org.jboss.tools.jst.jsp.outline.cssdialog.parsers.ColorParser;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.TabPropertySheetControl;

/**
 * Listener for tree in tab property sheet
 *
 * @author Evgeny Zheleznyakov
 */
public class TabPropertySheetMouseAdapter extends MouseAdapter {

    private static int GRID_NUM_COLUMNS = 2;
    private static int GRID_MARGIN_HEIGHT = 0;
    private static int GRID_MARGIN_WIDTH = 0;
    private static int GRID_HORIZONTAL_SPASING = 0;
    private Tree tree;
    private TreeEditor editor;
    private Combo combo = null;
    private ImageCombo colorCombo = null;
    private HashMap<String, ArrayList<String>> comboMap;
    private HashMap<String, ArrayList<String>> elementsMap;

    private TabPropertySheetControl tabPropertySheetControl;

    private ArrayList<ManualChangeStyleListener> listeners = new ArrayList<ManualChangeStyleListener>();

    /**
     * Constructor.
     *
     * @param tree
     * @param elementsMap
     * @param comboMap
     * @param tabPropertySheetControl
     */
    public TabPropertySheetMouseAdapter(Tree tree, HashMap<String, ArrayList<String>> elementsMap,
        HashMap<String, ArrayList<String>> comboMap, TabPropertySheetControl tabPropertySheetControl) {
        this.tabPropertySheetControl = tabPropertySheetControl;
        //this.cssDialog = dialog;
        this.tree = tree;
        this.comboMap = comboMap;
        this.elementsMap = elementsMap;

        editor = new TreeEditor(tree);
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
    }

    /**
     * Invoke then mouse pressed
     */
    public void mouseDown(MouseEvent event) {
        for (int i = 0; i < tree.getColumnCount(); i++) {
            tree.getColumn(i).pack();
        }
        Control old = editor.getEditor();
        if (old != null) {
            old.dispose();
        }

        Point pt = new Point(event.x, event.y);

        final TreeItem item = tree.getItem(pt);
        if (item != null) {
            if (elementsMap.keySet().contains(item.getText(Constants.FIRST_COLUMN).trim())) {
                return;
            }
            if (comboMap.keySet().contains(item.getText(Constants.FIRST_COLUMN).trim())) {
                createCombo(item, Constants.SECOND_COLUMN);
            } else {
                createText(item, Constants.SECOND_COLUMN);
            }
        }
    }

    /**
     * Method for create combo editor
     *
     * @param item Tree item for editing
     * @param column Number of column for editing
     */
    private void createCombo(final TreeItem item, int column) {
        final Composite panel = new Composite(tree, SWT.NONE);
        GridLayout grid = new GridLayout();
        grid.numColumns = GRID_NUM_COLUMNS;
        grid.marginHeight = GRID_MARGIN_HEIGHT;
        grid.marginWidth = GRID_MARGIN_WIDTH;
        grid.horizontalSpacing = GRID_HORIZONTAL_SPASING;
        panel.setLayout(grid);

        Button btn = null;

        boolean color = false;

        // color element
        if (item.getText(Constants.FIRST_COLUMN).trim().indexOf(CSSConstants.COLOR) != Constants.DONT_CONTAIN) {
            color = true;

            // create combo for color
            colorCombo = new ImageCombo(panel, SWT.BORDER);

            HashMap<String, String> colorMap = ColorParser.getInstance().getMap();
            for (String key : colorMap.keySet()) {
                RGB rgb = Util.getColor(key);
                colorCombo.add(colorMap.get(key), rgb);
            }

            if (colorCombo.indexOf(item.getText(Constants.SECOND_COLUMN).trim().toLowerCase()) != Constants.DONT_CONTAIN) {
                colorCombo.select(colorCombo.indexOf(item.getText(Constants.SECOND_COLUMN).trim()
                                                         .toLowerCase()));
            } else {
                colorCombo.setText(item.getText(Constants.SECOND_COLUMN).trim());
            }

            colorCombo.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent event) {
                        item.setText(Constants.SECOND_COLUMN, colorCombo.getText());
                        panel.dispose();
                        tabPropertySheetControl.updateData(true);
                        if (!tabPropertySheetControl.isUpdateDataFromStyleAttributes()) {
                        	notifyListeners();
                        }
                    }
                });
            colorCombo.addModifyListener(new ModifyListener() {
                    public void modifyText(ModifyEvent event) {
                        item.setText(Constants.SECOND_COLUMN, colorCombo.getText());
                        tabPropertySheetControl.updateData(true);
                        if (!tabPropertySheetControl.isUpdateDataFromStyleAttributes()) {
                        	notifyListeners();
                        }
                    }
                });

            // create color button
            btn = new Button(panel, SWT.NONE);

            ImageDescriptor imageDes = JspEditorPlugin.getImageDescriptor(Constants.IMAGE_COLOR_FILE_LOCATION);
            Image colorImage = imageDes.createImage();
            btn.setImage(colorImage);
            btn.addDisposeListener(new DisposeListener() {
                    public void widgetDisposed(DisposeEvent e) {
                        Button button = (Button) e.getSource();
                        button.getImage().dispose();
                    }
                });
            btn.setToolTipText(JstUIMessages.COLOR_DIALOG_TITLE);
            btn.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent event) {
                        RGB startRgb = Util.getColor(item.getText(Constants.SECOND_COLUMN).toLowerCase().trim());
                        if (startRgb == null) {
                            startRgb = Constants.RGB_BLACK;
                        }

                        ColorDialog colorDialog = new ColorDialog(tree.getShell());
                        colorDialog.setRGB(startRgb);
                        colorDialog.setText(JstUIMessages.COLOR_DIALOG_TITLE);

                        RGB rgb = colorDialog.open();
                        if (rgb != null) {
                            String str = Util.createColorString(rgb);
                            if (ColorParser.getInstance().getMap().get(str) != null) {
                                item.setText(Constants.SECOND_COLUMN, ColorParser.getInstance().getMap().get(str));
                            } else {
                                item.setText(Constants.SECOND_COLUMN, str);
                            }
                            tabPropertySheetControl.updateData(true);
                            if (!tabPropertySheetControl.isUpdateDataFromStyleAttributes()) {
                            	notifyListeners();
                            }
                        }

                        panel.dispose();
                    }
                });
        } else if (Util.containFolder(item.getText(Constants.FIRST_COLUMN).trim())) {
            combo = new Combo(panel, SWT.NONE);

            // create chooser button
            btn = new Button(panel, SWT.NONE);

            ImageDescriptor imageDes = JspEditorPlugin.getImageDescriptor(Constants.IMAGE_FOLDER_FILE_LOCATION);
            Image chooserImage = imageDes.createImage();
            btn.setImage(chooserImage);
            btn.addDisposeListener(new DisposeListener() {
                    public void widgetDisposed(DisposeEvent e) {
                        Button button = (Button) e.getSource();
                        button.getImage().dispose();
                    }
                });
            btn.setToolTipText(JstUIMessages.IMAGE_DIALOG_MESSAGE);
            btn.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent event) {
                    	IAdaptable project = Util.getCurrentProject();

                        ImageSelectionDialog dialog = new ImageSelectionDialog(tree.getShell(),
                                new WorkbenchLabelProvider(), new WorkbenchContentProvider());
                        dialog.setTitle(JstUIMessages.IMAGE_DIALOG_TITLE);
                        dialog.setMessage(JstUIMessages.IMAGE_DIALOG_MESSAGE);
                        dialog.setEmptyListMessage(JstUIMessages.IMAGE_DIALOG_EMPTY_MESSAGE);
                        dialog.setAllowMultiple(false);
                        dialog.setInput(project);

                        if (dialog.open() == ImageSelectionDialog.OK) {
                            IFile file = (IFile) dialog.getFirstResult();
                            String value = file.getFullPath().toString();
                            item.setText(Constants.SECOND_COLUMN, value);
                            panel.dispose();
                            tabPropertySheetControl.updateData(true);
                            if (!tabPropertySheetControl.isUpdateDataFromStyleAttributes()) {
                            	notifyListeners();
                            }
                        }
                    }
                });
        } else {
            combo = new Combo(panel, SWT.NONE);
        }
        // add items
        if (!color) {
            ArrayList<String> list = comboMap.get(item.getText(Constants.FIRST_COLUMN).trim());

            for (String str : list) {
                combo.add(str);
            }
            if (btn != null) {
                btn.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
            }

            combo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
            combo.setFocus();
            if ((combo.indexOf(item.getText(column)) == Constants.DONT_CONTAIN) &&
                    !item.getText(column).equals(Constants.EMPTY)) {
                combo.setText(item.getText(column));
            } else {
                combo.select(combo.indexOf(item.getText(column)));
            }

            // Add a listener to set the selected item back into the cell
            final int col = column;
            combo.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent event) {
                        item.setText(col, combo.getText());
                        panel.dispose();
                        tabPropertySheetControl.updateData(true);
                        if (!tabPropertySheetControl.isUpdateDataFromStyleAttributes()) {
                        	notifyListeners();
                        }
                    }
                });
            combo.addModifyListener(new ModifyListener() {
                    public void modifyText(ModifyEvent event) {
                        item.setText(col, combo.getText());
                        if (!tabPropertySheetControl.isUpdateDataFromStyleAttributes()) {
                        	notifyListeners();
                        }
                    }
                });
        } else {
            colorCombo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
            btn.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
            colorCombo.setFocus();
        }

        // Compute the width for the editor
        // Also, compute the column width, so that the dropdown fits
        editor.minimumWidth = panel.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        editor.minimumHeight = panel.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;

        tree.getColumn(column).setWidth(editor.minimumWidth);

        // Set the focus on the dropdown and set into the editor
        editor.setEditor(panel, item, column);
    }

    /**
     * Method for create text editor
     *
     * @param item Tree item for editing
     * @param column Number of column for editing
     */
    private void createText(final TreeItem item, int column) {
        final Composite panel = new Composite(tree, SWT.NONE);
        GridLayout grid = new GridLayout();
        grid.numColumns = GRID_NUM_COLUMNS;
        grid.marginHeight = GRID_MARGIN_HEIGHT;
        grid.marginWidth = GRID_MARGIN_WIDTH;
        grid.horizontalSpacing = GRID_HORIZONTAL_SPASING;
        panel.setLayout(grid);

        Button btn = null;

        final Text text = new Text(panel, SWT.BORDER);
        if (item.getText(Constants.FIRST_COLUMN).trim().equalsIgnoreCase(CSSConstants.FONT_FAMILY)) {
            btn = new Button(panel, SWT.NONE);

            ImageDescriptor imageDes = JspEditorPlugin.getImageDescriptor(Constants.IMAGE_FONT_FILE_LOCATION);
            Image fontImage = imageDes.createImage();
            btn.setImage(fontImage);
            btn.addDisposeListener(new DisposeListener() {
                    public void widgetDisposed(DisposeEvent e) {
                        Button button = (Button) e.getSource();
                        button.getImage().dispose();
                    }
                });
            btn.setToolTipText(JstUIMessages.FONT_FAMILY_TIP);
            btn.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent event) {
                        FontFamilyDialog dialog = new FontFamilyDialog(tree.getShell(),
                                item.getText(Constants.SECOND_COLUMN));
                        if (dialog.open() == Window.OK) {
                            item.setText(Constants.SECOND_COLUMN, dialog.getFontFamily());
                            panel.dispose();
                            tabPropertySheetControl.updateData(true);
                            if (!tabPropertySheetControl.isUpdateDataFromStyleAttributes()) {
                            	notifyListeners();
                            }
                        }
                    }
                });
        }

        if (btn != null) {
            btn.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        }

        text.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        // Compute the width for the editor
        // Also, compute the column width, so that the dropdown fits
        editor.minimumWidth = panel.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        editor.minimumHeight = panel.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;

        tree.getColumn(column).setWidth(editor.minimumWidth);

        // Set the focus on the dropdown and set into the editor
        editor.setEditor(panel, item, column);

        // Transfer any text from the cell to the Text control, set the color to match this row, select the text,
        // and set focus to the control
        text.setText(item.getText(column));
        text.setFocus();

        // Add a handler to transfer the text back to the cell any time it's modified
        final int col = column;
        text.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent event) {
                    // Set the text of the editor's control back into the cell
                    item.setText(col, text.getText());
                    tabPropertySheetControl.updateData(true);
                    if (!tabPropertySheetControl.isUpdateDataFromStyleAttributes()) {
                    	notifyListeners();
                    }
                }
            });
    }

    /**
     * Add ManualChangeStyleListener object.
     *
     * @param listener ManualChangeStyleListener object to be added
     */
    public void addManualChangeStyleListener(ManualChangeStyleListener listener) {
        listeners.add(listener);
    }

    /**
     * Gets an array of ChangeStyleListener object.
     *
     * @return an array of ChangeStyleListener object
     */
    public ManualChangeStyleListener[] getManualChangeStyleListeners() {
        return listeners.toArray(new ManualChangeStyleListener[listeners.size()]);
    }

    /**
     * Remove ManualChangeStyleListener object passed by parameter.
     *
     * @param listener ManualChangeStyleListener object to be removed
     */
    public void removeManualChangeStyleListener(ManualChangeStyleListener listener) {
        listeners.remove(listener);
    }

    /**
     * Method is used to notify all subscribed listeners about any changes within style attribute map.
     */
    private void notifyListeners() {
        ChangeStyleEvent event = new ChangeStyleEvent(this);
        for (ManualChangeStyleListener listener : listeners) {
            listener.styleChanged(event);
        }
    }
}
