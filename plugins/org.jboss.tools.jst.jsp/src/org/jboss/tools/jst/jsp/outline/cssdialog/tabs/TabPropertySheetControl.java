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
package org.jboss.tools.jst.jsp.outline.cssdialog.tabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.ManualChangeStyleListener;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.StyleAttributes;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.TabPropertySheetMouseAdapter;

/**
 * Class for creating Property sheet tab
 *
 * @author Evgeny Zheleznyakov
 */
public class TabPropertySheetControl extends BaseTabControl {

	private String[] columns = new String[] { "Attribute", "Value" };
    private Tree tree;

    private TabPropertySheetMouseAdapter propertySheetMouseAdapter = null;

    /**
     * Constructor for creating controls
     *
     * @param composite The parent composite for tab
     * @param elementMap
     * @param comboMap
     * @param styleAttributes the StyleAttributes object
     */
    public TabPropertySheetControl(Composite parent, HashMap<String, ArrayList<String>> elementMap,
        HashMap<String, ArrayList<String>> comboMap, StyleAttributes styleAttributes) {
        super(parent, SWT.NONE);
        this.styleAttributes = styleAttributes;
        setLayout(new FillLayout());

        tree = new Tree(this, SWT.NONE);
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);

        // Create NUM columns
        for (int i = 0; i < columns.length; i++) {
            TreeColumn column = new TreeColumn(tree, SWT.LEFT | SWT.COLOR_BLACK);
            column.setText(columns[i]);
        }

        Set<String> set = elementMap.keySet();
        for (String str : set) {
            TreeItem item = new TreeItem(tree, SWT.NONE);
            item.setText(str);
//            item.setFont(Constants.FIRST_COLUMN, JFaceResources.getFontRegistry().get(JFaceResources.TEXT_FONT));

            ArrayList<String> list = elementMap.get(str);
            for (String strList : list) {
                TreeItem subItem = new TreeItem(item, SWT.NONE);
                subItem.setText(Constants.FIRST_COLUMN, strList);
            }
        }

        updateData(false);

        propertySheetMouseAdapter = new TabPropertySheetMouseAdapter(tree, elementMap, comboMap, this);
        tree.addMouseListener(propertySheetMouseAdapter);
        for (int i = 0; i < tree.getColumnCount(); i++) {
            tree.getColumn(i).pack();
        }
    }

    /**
     * Method for get data in controls (if param equal true ), or set data (if param equal false).
     *
     * @param update
     */
    public void updateData(boolean update) {
        if (update) {
            for (int i = 0; i < tree.getItemCount(); i++) {
                for (int j = 0; j < tree.getItem(i).getItemCount(); j++) {
                    if (tree.getItem(i).getItem(j).getText(Constants.SECOND_COLUMN) == null) {
                        styleAttributes.removeAttribute(tree.getItem(i).getItem(j)
                                                            .getText(Constants.FIRST_COLUMN));
                    } else if (tree.getItem(i).getItem(j).getText(Constants.SECOND_COLUMN).trim()
                                       .equals(Constants.EMPTY)) {
                        styleAttributes.removeAttribute(tree.getItem(i).getItem(j)
                                                            .getText(Constants.FIRST_COLUMN));
                    } else {
                        styleAttributes.addAttribute(tree.getItem(i).getItem(j)
                                                         .getText(Constants.FIRST_COLUMN),
                            tree.getItem(i).getItem(j).getText(Constants.SECOND_COLUMN));
                    }
                }
            }
        } else {
        	updateDataFromStyleAttributes = true;
            for (int i = 0; i < tree.getItemCount(); i++) {
                for (int j = 0; j < tree.getItem(i).getItemCount(); j++) {
                    tree.getItem(i).getItem(j).setText(Constants.SECOND_COLUMN, Constants.EMPTY);
                }
            }

            Set<String> set = styleAttributes.keySet();
            for (String str : set) {
                for (int i = 0; i < tree.getItemCount(); i++) {
                    for (int j = 0; j < tree.getItem(i).getItemCount(); j++) {
                        if (tree.getItem(i).getItem(j).getText(Constants.FIRST_COLUMN).equals(str)) {
                            tree.getItem(i).getItem(j)
                                .setText(Constants.SECOND_COLUMN, styleAttributes.getAttribute(str));
                        }
                    }
                }
            }
            setExpanded();
            updateDataFromStyleAttributes = false;
        }
    }

    /**
     * Set expanded item for not empty css attributes.
     */
    private void setExpanded() {
        TreeItem item = null;
        for (int i = 0; i < tree.getItemCount(); i++) {
            tree.getItem(i).setExpanded(false);
        }
        Set<String> set = styleAttributes.keySet();
        for (String attr : set) {
            if ((item = find(attr)) != null) {
                item.setExpanded(true);
            }
        }
        for (int i = 0; i < tree.getColumnCount(); i++) {
            tree.getColumn(i).pack();
        }
    }

    /**
     * Find tree item for expand
     *
     * @param attr Name of css attributes
     * @return Tree item which will expand
     */
    private TreeItem find(String attr) {
        TreeItem item = null;
        TreeItem subItem = null;
        for (int i = 0; i < tree.getItemCount(); i++) {
            item = tree.getItem(i);
            for (int j = 0; j < item.getItemCount(); j++) {
                subItem = item.getItem(j);
                if (subItem.getText().equals(attr)) {
                    return item;
                }
            }
        }

        return null;
    }

    /**
     * Add ManualChangeStyleListener object.
     *
     * @param listener ChangeStyleListener object to be added
     */
    public void addManualChangeStyleListener(ManualChangeStyleListener listener) {
    	if (propertySheetMouseAdapter != null) {
    		propertySheetMouseAdapter.addManualChangeStyleListener(listener);
    	}
    }
}
