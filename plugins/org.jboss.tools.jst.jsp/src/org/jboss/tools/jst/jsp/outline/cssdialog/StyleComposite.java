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
package org.jboss.tools.jst.jsp.outline.cssdialog;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.CSSModel;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.ManualChangeStyleListener;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.StyleAttributes;
import org.jboss.tools.jst.jsp.outline.cssdialog.parsers.BaseListener;
import org.jboss.tools.jst.jsp.outline.cssdialog.parsers.CSSElementsParser;
import org.jboss.tools.jst.jsp.outline.cssdialog.parsers.ComboParser;
import org.jboss.tools.jst.jsp.outline.cssdialog.parsers.Parser;
import org.jboss.tools.jst.jsp.outline.cssdialog.parsers.ParserListener;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.TabBackgroundControl;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.TabBoxesControl;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.TabPreviewControl;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.TabPropertySheetControl;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.TabQuickEditControl;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.TabTextControl;
import org.xml.sax.Attributes;

/**
 * Class for creating style tabs
 *
 * @author Igor Zhukov (izhukov@exadel.com)
 */
public class StyleComposite extends Composite {

    private static String NODE_NAME_ELEMENTS = "elements";
    private static String NODE_NAME_VALUE = "value";
    private static String NODE_NAME_ELEMENT = "element";
    private static String NODE_ATTRIBUTE_NAME = "name";

    private static int TAB_TEXT_FONT_NUMBER = 0;
    private static int FIRST_SELECTION = 0;
    private static int SIZE_NULL = 0;

    private String newStyle;
    private String oldStyle;

    private TabTextControl tabTextControl;
    private TabBackgroundControl tabBackgroundControl;
    private TabBoxesControl tabBoxesControl;
    private TabPropertySheetControl tabPropertySheetControl;
    private TabQuickEditControl tabQuickEditControl;
    private TabPreviewControl tabPreviewControl;

    private TabFolder tabFolder;
    private TabItem tabTextFont;
    private TabItem tabBackground;
    private TabItem tabBoxes;
    private TabItem tabPropertySheet;
    private TabItem tabQuickEdit;
    private TabItem tabPreview;
    private TabItem lastSelectedTab = null;
    private StyleAttributes styleAttributes;
    private Parser parser;
    private HashMap<String, ArrayList<String>> comboMap = new HashMap<String, ArrayList<String>>();
    private HashMap<String, ArrayList<String>> elementsMap = new HashMap<String, ArrayList<String>>();

    private boolean showPreviewTab = false;
    private CSSModel cssModel = null;

    /**
     * StyleComposite constructor.
     *
     * @param parent Composite object
     * @param styleAttributes StyleAttributes objects
     * @param oldStyle String value
     */
    public StyleComposite(Composite parent, StyleAttributes styleAttributes, String oldStyle) {
        super(parent, SWT.NONE);
        this.oldStyle = oldStyle;
        this.styleAttributes = styleAttributes;
        createTabs();
    }

    /**
     * Gets updated style.
     *
     * @return new style value
     */
    public String getNewStyle() {
        return newStyle;
    }

    /**
     * Update new style in accordance with the style attribute values.
     */
    public void updateStyle() {
        if (lastSelectedTab == tabTextFont) {
            tabTextControl.updateData(true);
        } else if (lastSelectedTab == tabBackground) {
            tabBackgroundControl.updateData(true);
        } else if (lastSelectedTab == tabBoxes) {
            tabBoxesControl.updateData(true);
        } else if (lastSelectedTab == tabPropertySheet) {
            tabPropertySheetControl.updateData(true);
        }
        // update newStyle value
        newStyle = styleAttributes.getStyle();
    }

    /**
     * Method is used to create it child subtabs.
     */
    private void createTabs() {
        ComboParser comboParser = new ComboParser();
        comboParser.setListener(new BaseListener(comboMap) {
                private ArrayList<String> list = null;

                public void startElement(String uri, String localName, String nodeName, Attributes attrs) {
                    if (nodeName.trim().equalsIgnoreCase(NODE_NAME_ELEMENTS)) {
                        return;
                    }

                    if (!nodeName.trim().equalsIgnoreCase(NODE_NAME_VALUE)) {
                        String name = nodeName;
                        list = new ArrayList<String>();
                        map.put(name, list);
                    } else {
                        list.add(attrs.getValue(NODE_ATTRIBUTE_NAME));
                    }
                }
            });
        comboParser.parse();

        CSSElementsParser cssParser = new CSSElementsParser();
        cssParser.setListener(new BaseListener(elementsMap) {
                private ArrayList<String> list = null;

                public void startElement(String uri, String localName, String nodeName, Attributes attrs) {
                    if (nodeName.trim().equalsIgnoreCase(NODE_NAME_ELEMENTS)) {
                        return;
                    }

                    if (!nodeName.trim().equalsIgnoreCase(NODE_NAME_ELEMENT)) {
                        String name = nodeName;
                        list = new ArrayList<String>();
                        map.put(name, list);
                    } else {
                        list.add(attrs.getValue(NODE_ATTRIBUTE_NAME));
                    }
                }
            });
        cssParser.parse();

        // HashMap<String, String> attributesMap = new HashMap<String, String>();
        ParserListener listener = new ParserListener(styleAttributes);
        parser = new Parser(elementsMap);
        parser.addListener(listener);
        parser.parse(this.oldStyle);

        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        setLayout(gridLayout);
        setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

        tabFolder = new TabFolder(this, SWT.NONE);
        tabFolder.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

        // Create each tab and set its text, tool tip text,
        tabTextFont = new TabItem(tabFolder, SWT.NONE);
        tabTextFont.setText(JstUIMessages.TEXT_FONT_TAB_NAME);
        tabTextFont.setToolTipText(tabTextFont.getText());
        tabTextFont.setControl(createTabTextControl(tabFolder));
        lastSelectedTab = tabTextFont;

        tabBackground = new TabItem(tabFolder, SWT.NONE);
        tabBackground.setText(JstUIMessages.BACKGROUND_TAB_NAME);
        tabBackground.setToolTipText(JstUIMessages.BACKGROUND_TAB_NAME);
        tabBackground.setControl(createTabBackgroundControl(tabFolder));

        tabBoxes = new TabItem(tabFolder, SWT.NONE);
        tabBoxes.setText(JstUIMessages.BOXES_TAB_NAME);
        tabBoxes.setToolTipText(JstUIMessages.BOXES_TAB_NAME);
        tabBoxes.setControl(createTabBoxesControl(tabFolder));

        tabPropertySheet = new TabItem(tabFolder, SWT.NONE);
        tabPropertySheet.setText(JstUIMessages.PROPERTY_SHEET_TAB_NAME);
        tabPropertySheet.setToolTipText(JstUIMessages.PROPERTY_SHEET_TAB_NAME);
        tabPropertySheet.setControl(createTabPropertySheetControl(tabFolder));

        if (showPreviewTab) {
        	tabPreview = new TabItem(tabFolder, SWT.NONE);
        	tabPreview.setText(JstUIMessages.PREVIEW_SHEET_TAB_NAME);
        	tabPreview.setToolTipText(JstUIMessages.PREVIEW_SHEET_TAB_NAME);
        	tabPreview.setControl(createPreviewContol(tabFolder));
        }

        tabFolder.setSelection(TAB_TEXT_FONT_NUMBER);

        tabQuickEdit = new TabItem(tabFolder, SWT.NONE);
      	tabQuickEdit.setText(JstUIMessages.QUICK_EDIT_TAB_NAME);
      	tabQuickEdit.setToolTipText(JstUIMessages.QUICK_EDIT_TAB_NAME);
      	tabQuickEdit.setControl(createTabQuickEditContol(tabFolder));
 
      	if (styleAttributes.getAttributeMap().size() > SIZE_NULL) {       
            tabFolder.setSelection(tabQuickEdit);
            lastSelectedTab = tabQuickEdit;
        }

        tabFolder.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent se) {
                    if (tabFolder.getSelection()[FIRST_SELECTION] == tabQuickEdit) {
                        tabQuickEditControl.dispose();
                        tabQuickEdit.setControl(createTabQuickEditContol(tabFolder));
                        lastSelectedTab = tabQuickEdit;
                    } else if (tabFolder.getSelection()[FIRST_SELECTION] == tabTextFont) {
                        tabTextControl.updateData(false);
                        lastSelectedTab = tabTextFont;
                    } else if (tabFolder.getSelection()[FIRST_SELECTION] == tabBackground) {
                        tabBackgroundControl.updateData(false);
                        lastSelectedTab = tabBackground;
                    } else if (tabFolder.getSelection()[FIRST_SELECTION] == tabBoxes) {
                        tabBoxesControl.updateData(false);
                        lastSelectedTab = tabBoxes;
                    } else if (tabFolder.getSelection()[FIRST_SELECTION] == tabPreview) {
                    	if(!styleAttributes.isValid()){
                    		//mareshkau, if styles attributes not valid we cann't create
                    		//correct preview
                    		return;
                    	}
                    	cssModel.setCSS(styleAttributes.getCssSelector(), styleAttributes);
                    	tabPreviewControl.selectEditorArea(styleAttributes.getCssSelector(), 0);
                        lastSelectedTab = tabPreview;
                    } else if (tabFolder.getSelection()[FIRST_SELECTION] == tabPropertySheet) {
                    	// TODO: changes on property sheet tag should reflect changes to another tabs
                    	tabPropertySheetControl.updateData(false);
                        lastSelectedTab = tabPropertySheet;
                    }
                }
            });
    }

    /**
     * Method for creating text tab
     *
     * @param tabFolder
     * @return composite
     */
    private Control createTabTextControl(TabFolder tabFolder) {
        ScrolledComposite sc = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);

        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);

        tabTextControl = new TabTextControl(sc, comboMap, styleAttributes);
        sc.setContent(tabTextControl);
        sc.setMinSize(tabTextControl.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        return sc;
    }

    /**
     * Method for creating background tab
     *
     * @param tabFolder
     * @return composite
     */
    private Control createTabBackgroundControl(TabFolder tabFolder) {
        ScrolledComposite sc = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);

        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);

        tabBackgroundControl = new TabBackgroundControl(sc, comboMap, styleAttributes);
        sc.setContent(tabBackgroundControl);
        sc.setMinSize(tabBackgroundControl.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        return sc;
    }

    /**
     * Method for creating boxes tab
     *
     * @param tabFolder
     * @return composite
     */
    private Control createTabBoxesControl(TabFolder tabFolder) {
        ScrolledComposite sc = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);

        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);

        tabBoxesControl = new TabBoxesControl(sc, comboMap, styleAttributes);
        sc.setContent(tabBoxesControl);
        sc.setMinSize(tabBoxesControl.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        return sc;
    }

    /**
     * Method for creating quick edit tab
     *
     * @param tabFolder
     * @return
     */
    private Control createPreviewContol(TabFolder tabFolder) {
        ScrolledComposite sc = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);

        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);

        tabPreviewControl = new TabPreviewControl(sc, styleAttributes);
        sc.setContent(tabPreviewControl);

        sc.setMinSize(tabPreviewControl.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        return sc;
    }

    /**
     * Method for creating property sheet tab
     *
     * @param tabFolder
     * @return
     */
    private Control createTabPropertySheetControl(TabFolder tabFolder) {
    	tabPropertySheetControl = new TabPropertySheetControl(tabFolder, elementsMap, comboMap, styleAttributes);

        return tabPropertySheetControl;
    }

    /**
     * Method for creating quick edit tab
     *
     * @param tabFolder
     * @return
     */
    private Control createTabQuickEditContol(TabFolder tabFolder) {
        ScrolledComposite sc = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);

        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);

        ManualChangeStyleListener[] listeners = null;
        if (tabQuickEditControl != null) {
        	listeners = tabQuickEditControl.getManualChangeStyleListeners();
        }
        tabQuickEditControl = new TabQuickEditControl(sc, comboMap, styleAttributes);
        if (listeners != null && listeners.length > 0) {
        	tabQuickEditControl.addManualChangeStyleListener(listeners[0]);
        }
        sc.setContent(tabQuickEditControl);

        sc.setMinSize(tabQuickEditControl.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        return sc;
    }

    /**
     * Clear whole style composite component.
     */
    public void clearStyleComposite(String cssSelector) {
        styleAttributes.clear();
        styleAttributes.setCssSelector(cssSelector);

        tabBackgroundControl.updateData(false);
        tabBoxesControl.updateData(false);
        tabPropertySheetControl.updateData(false);
        tabTextControl.updateData(false);

        if (tabQuickEdit != null) {
            tabQuickEditControl.updateData();
        }
    }

    /**
     * Recreate style composite tab widgets.
     *
     * @param style CSS style
     */
    public void recreateStyleComposite(String style, String cssSelector) {
        styleAttributes.clear();
        styleAttributes.setCssSelector(cssSelector);
        parser.parse(style);

        tabBackgroundControl.updateData(false);
        tabBoxesControl.updateData(false);
        tabPropertySheetControl.updateData(false);
        tabTextControl.updateData(false);

        if (styleAttributes.getAttributeMap().size() > SIZE_NULL) {
            if ((tabQuickEdit == null) || tabQuickEdit.isDisposed()) {
                tabQuickEdit = new TabItem(tabFolder, SWT.NONE);
                tabQuickEdit.setText(JstUIMessages.QUICK_EDIT_TAB_NAME);
                tabQuickEdit.setToolTipText(JstUIMessages.QUICK_EDIT_TAB_NAME);
                tabQuickEdit.setControl(createTabQuickEditContol(tabFolder));
                ManualChangeStyleListener[] listeners = tabBackgroundControl.getManualChangeStyleListeners();
                if (listeners != null && listeners.length > 0) {
                	tabQuickEditControl.addManualChangeStyleListener(listeners[0]);
                }
            } else {
                // update quick edit
                tabQuickEditControl.updateData();
            }

            tabFolder.setSelection(tabQuickEdit);
            lastSelectedTab = tabQuickEdit;
        } else {
            if (tabQuickEdit != null && !tabQuickEdit.isDisposed()) {
//            	tabQuickEdit.dispose();
            	tabQuickEditControl.updateData();            	
            }
            tabFolder.redraw();
        }
    }

    /**
     * Method is used to update preview tab with corresponding CSS file.
     *
     * @param cssModel model associated with the file that should be displayed in preview tab
     */
    public void initPreview(CSSModel cssModel) {
        if (showPreviewTab && ((tabPreview == null) || tabPreview.isDisposed())) {
        	tabPreview = new TabItem(tabFolder, SWT.NONE);
        	tabPreview.setText(JstUIMessages.PREVIEW_SHEET_TAB_NAME);
        	tabPreview.setToolTipText(JstUIMessages.PREVIEW_SHEET_TAB_NAME);
        	tabPreview.setControl(createPreviewContol(tabFolder));
        }
    	if (tabPreviewControl != null) {
    		tabPreviewControl.initPreview(cssModel);
    		if (lastSelectedTab == tabPreview) {
            	cssModel.setCSS(styleAttributes.getCssSelector(), styleAttributes);
    			tabPreviewControl.selectEditorArea(styleAttributes.getCssSelector(), 0);
    		}
    	}
    }

    /**
     * Method is used to update preview selection area.
     *
     * @param selector CSS selector
     */
    public void updatePreview(String selector) {
    	if (tabPreviewControl != null && lastSelectedTab == tabPreview) {
    		cssModel.setCSS(selector, styleAttributes);
        	tabPreviewControl.selectEditorArea(selector, 0);
    	}
    }

    /**
     * Method is used to revert preview to before-saved state.
     */
    public void revertPreview() {
        if (showPreviewTab && ((tabPreview == null) || tabPreview.isDisposed())) {
        	tabPreview = new TabItem(tabFolder, SWT.NONE);
        	tabPreview.setText(JstUIMessages.PREVIEW_SHEET_TAB_NAME);
        	tabPreview.setToolTipText(JstUIMessages.PREVIEW_SHEET_TAB_NAME);
        	tabPreview.setControl(createPreviewContol(tabFolder));
        }
    	if (tabPreviewControl != null) {
    		tabPreviewControl.doRevertToSaved();
        	tabPreviewControl.selectEditorArea(styleAttributes.getCssSelector(), 0);
    	}
    }

	/**
	 * @param showPreviewTab the showPreviewTab to set
	 */
	public void setShowPreviewTab(boolean showPreviewTab) {
		this.showPreviewTab = showPreviewTab;
	}

	/**
	 * @param cssModel the cssModel to set
	 */
	public void setCSSModel(CSSModel cssModel) {
		this.cssModel = cssModel;
	}

	/**
	 * @return the showPreviewTab
	 */
	public boolean isShowPreviewTab() {
		return showPreviewTab;
	}

    /**
     * Add ManualChangeStyleListener object.
     *
     * @param listener ChangeStyleListener object to be added
     */
    public void addManualChangeStyleListener(ManualChangeStyleListener listener) {
        tabBackgroundControl.addManualChangeStyleListener(listener);
        tabBoxesControl.addManualChangeStyleListener(listener);
        tabPropertySheetControl.addManualChangeStyleListener(listener);
        tabTextControl.addManualChangeStyleListener(listener);
        if (tabQuickEditControl != null) {
        	tabQuickEditControl.addManualChangeStyleListener(listener);
        }
    }
}
