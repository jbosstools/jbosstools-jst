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
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.MessageUtil;
import org.jboss.tools.jst.jsp.outline.cssdialog.parsers.BaseListener;
import org.jboss.tools.jst.jsp.outline.cssdialog.parsers.CSSElementsParser;
import org.jboss.tools.jst.jsp.outline.cssdialog.parsers.ComboParser;
import org.jboss.tools.jst.jsp.outline.cssdialog.parsers.Parser;
import org.jboss.tools.jst.jsp.outline.cssdialog.parsers.ParserListener;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.TabBackgroundControl;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.TabBoxesControl;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.TabPropertySheetControl;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.TabQuickEditControl;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.TabTextControl;
import org.xml.sax.Attributes;

/**
 * Class for CSS editor dialog
 * 
 * @author Dzmitry Sakovich (dsakovich@exadel.com)
 */
public class CSSDialog extends Dialog {
    
    private static int MIN_HEIGHT_FOR_BROWSER = 60;

    private static int TAB_TEXT_FONT_NUMBER = 0;
    private static int TAB_QUICK_EDIT_NUMBER = 4;

    private static int SIZE_NULL = 0;

    private static int FIRST_SELECTION = 0;

    private static String NODE_NAME_ELEMENTS = "elements";
    private static String NODE_NAME_VALUE = "value";
    private static String NODE_NAME_ELEMENT = "element";
    private static String NODE_ATTRIBUTE_NAME = "name";

    private Browser browser = null;

    private String oldStyle;
    private String newStyle;

    private TabItem lastSelectedTab = null;

    private TabTextControl tabTextControl;
    private TabBackgroundControl tabBackgroundControl;
    private TabBoxesControl tabBoxesControl;
    private TabPropertySheetControl tabPropertySheetControl;
    private TabQuickEditControl tabQuickEditControl;

    private TabItem tabTextFont;
    private TabItem tabBackground;
    private TabItem tabBoxes;
    private TabItem tabPropertySheet;
    private TabItem tabQuickEdit;

    private HashMap<String, ArrayList<String>> comboMap = new HashMap<String, ArrayList<String>>();
    private HashMap<String, ArrayList<String>> elementsMap = new HashMap<String, ArrayList<String>>();
    private HashMap<String, String> attributesMap = new HashMap<String, String>();

    public CSSDialog(final Shell parentShell, String oldStyle) {
	super(parentShell);
	setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX
		| SWT.APPLICATION_MODAL);
	this.oldStyle = oldStyle;

	ComboParser comboParser = new ComboParser();
	comboParser.setListener(new BaseListener(comboMap) {

	    private ArrayList<String> list = null;

	    public void startElement(String uri, String localName,
		    String nodeName, Attributes attrs) {

		if (nodeName.trim().equalsIgnoreCase(NODE_NAME_ELEMENTS))
		    return;

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

	    public void startElement(String uri, String localName,
		    String nodeName, Attributes attrs) {

		if (nodeName.trim().equalsIgnoreCase(NODE_NAME_ELEMENTS))
		    return;

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

    }

    /**
     * Getter for newStyle attribute
     * 
     * @return
     */
    public String getNewStyle() {
	return newStyle;
    }

    /**
     * Setter for newStyle attribute
     * 
     * @param newStyle
     */
    public void setNewStyle(String newStyle) {
	this.newStyle = newStyle;
    }

    /**
     * Method for creating dialog area
     * 
     * @param parent
     */
    protected Control createDialogArea(final Composite parent) {
		
	ParserListener listener = new ParserListener(attributesMap);
	Parser parser = new Parser(elementsMap);
	parser.addListener(listener);
	parser.parse(oldStyle);

	final Composite composite = (Composite) super.createDialogArea(parent);

	final GridData gridData = new GridData(GridData.FILL, GridData.FILL,
		true, true);

	composite.setLayoutData(gridData);
	GridData gd = new GridData(GridData.FILL_BOTH
		| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);

	final TabFolder tabFolder = new TabFolder(composite, SWT.NONE);
	tabFolder.setLayoutData(gd);

	// ------------------------Browser-----------------------------------

	browser = new Browser(composite, SWT.BORDER);
	GridData gridBrowser = new GridData(GridData.FILL_BOTH);
	gridBrowser.minimumHeight = MIN_HEIGHT_FOR_BROWSER;
	browser.setLayoutData(gridBrowser);
	//browser.se
	setStyleForPreview();
	// ------------------------------------------------------------------

	// Create each tab and set its text, tool tip text,
	tabTextFont = new TabItem(tabFolder, SWT.NONE);
	tabTextFont.setText(MessageUtil.getString("TEXT_FONT_TAB_NAME"));
	tabTextFont.setToolTipText(MessageUtil.getString("TEXT_FONT_TAB_NAME"));
	tabTextFont.setControl(createTabTextControl(tabFolder));
	lastSelectedTab = tabTextFont;

	tabBackground = new TabItem(tabFolder, SWT.NONE);
	tabBackground.setText(MessageUtil.getString("BACKGROUND_TAB_NAME"));
	tabBackground.setToolTipText(MessageUtil
		.getString("BACKGROUND_TAB_NAME"));
	tabBackground.setControl(createTabBackgroundControl(tabFolder));

	tabBoxes = new TabItem(tabFolder, SWT.NONE);
	tabBoxes.setText(MessageUtil.getString("BOXES_TAB_NAME"));
	tabBoxes.setToolTipText(MessageUtil.getString("BOXES_TAB_NAME"));
	tabBoxes.setControl(createTabBoxesControl(tabFolder));

	tabPropertySheet = new TabItem(tabFolder, SWT.NONE);
	tabPropertySheet.setText(MessageUtil
		.getString("PROPERTY_SHEET_TAB_NAME"));
	tabPropertySheet.setToolTipText(MessageUtil
		.getString("PROPERTY_SHEET_TAB_NAME"));
	tabPropertySheet.setControl(createTabPropertySheetControl(tabFolder));

	tabFolder.setSelection(TAB_TEXT_FONT_NUMBER);
	if (attributesMap.size() > SIZE_NULL) {
	    tabQuickEdit = new TabItem(tabFolder, SWT.NONE);
	    tabQuickEdit.setText(MessageUtil.getString("QUICK_EDIT_TAB-NAME"));
	    tabQuickEdit.setToolTipText(MessageUtil
		    .getString("QUICK_EDIT_TAB-NAME"));
	    tabQuickEdit.setControl(createTabQuickEditContol(tabFolder));
	    tabFolder.setSelection(TAB_QUICK_EDIT_NUMBER);
	    lastSelectedTab = tabQuickEdit;
	}
	tabFolder.addSelectionListener(new SelectionAdapter() {

	    public void widgetSelected(SelectionEvent se) {

		
		if (tabFolder.getSelection()[FIRST_SELECTION] == tabQuickEdit) {
		    tabQuickEditControl.dispose();
		    tabQuickEdit
			    .setControl(createTabQuickEditContol(tabFolder));
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
		} else if (tabFolder.getSelection()[FIRST_SELECTION] == tabPropertySheet) {
		    tabPropertySheetControl.updateData(false);
		    lastSelectedTab = tabPropertySheet;
		}
	    }
	});

	return composite;
    }

    /**
     * Method for setting title for dialog
     * 
     * @param newShell
     */
    protected void configureShell(Shell newShell) {
	super.configureShell(newShell);
	newShell.setText(MessageUtil.getString("CSS_DIALOG_TITLE"));
    }

    protected void okPressed() {

	if (lastSelectedTab == tabTextFont) {
	    tabTextControl.updateData(true);
	} else if (lastSelectedTab == tabBackground) {
	    tabBackgroundControl.updateData(true);
	} else if (lastSelectedTab == tabBoxes) {
	    tabBoxesControl.updateData(true);
	} else if (lastSelectedTab == tabPropertySheet) {
	    tabPropertySheetControl.updateData(true);
	}
	StringBuffer buf = new StringBuffer();
	Set<Entry<String, String>> set = attributesMap.entrySet();
	for (Map.Entry<String, String> me : set) {
	    buf.append(me.getKey() + Constants.COLON_STRING + me.getValue()
		    + Constants.SEMICOLON_STRING);
	}
	setNewStyle(buf.toString());
	super.okPressed();
    }

    /**
     * Method for creating text tab
     * 
     * @param tabFolder
     * @return composite
     */
    private Control createTabTextControl(TabFolder tabFolder) {

	ScrolledComposite sc = new ScrolledComposite(tabFolder, SWT.H_SCROLL
		| SWT.V_SCROLL);

	sc.setExpandHorizontal(true);
	sc.setExpandVertical(true);

	tabTextControl = new TabTextControl(sc, comboMap, attributesMap, this);
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
	ScrolledComposite sc = new ScrolledComposite(tabFolder, SWT.H_SCROLL
		| SWT.V_SCROLL);

	sc.setExpandHorizontal(true);
	sc.setExpandVertical(true);

	tabBackgroundControl = new TabBackgroundControl(sc, comboMap,
		attributesMap, this);
	sc.setContent(tabBackgroundControl);
	sc.setMinSize(tabBackgroundControl
		.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	return sc;
    }

    /**
     * Method for creating boxes tab
     * 
     * @param tabFolder
     * @return composite
     */
    private Control createTabBoxesControl(TabFolder tabFolder) {

	ScrolledComposite sc = new ScrolledComposite(tabFolder, SWT.H_SCROLL
		| SWT.V_SCROLL);

	sc.setExpandHorizontal(true);
	sc.setExpandVertical(true);

	tabBoxesControl = new TabBoxesControl(sc, comboMap, attributesMap, this);
	sc.setContent(tabBoxesControl);
	sc.setMinSize(tabBoxesControl.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	return sc;
    }

    /**
     * 
     * Method for creating property sheet tab
     * 
     * @param tabFolder
     * @return
     */
    private Control createTabPropertySheetControl(TabFolder tabFolder) {
	tabPropertySheetControl = new TabPropertySheetControl(tabFolder,
		elementsMap, comboMap, attributesMap, this);
	return tabPropertySheetControl;
    }

    /**
     * 
     * Method for creating quick edit tab
     * 
     * @param tabFolder
     * @return
     */
    private Control createTabQuickEditContol(TabFolder tabFolder) {

	ScrolledComposite sc = new ScrolledComposite(tabFolder, SWT.H_SCROLL
		| SWT.V_SCROLL);

	sc.setExpandHorizontal(true);
	sc.setExpandVertical(true);

	tabQuickEditControl = new TabQuickEditControl(sc, comboMap,
		attributesMap, this);
	sc.setContent(tabQuickEditControl);

	sc
		.setMinSize(tabQuickEditControl.computeSize(SWT.DEFAULT,
			SWT.DEFAULT));
	return sc;
    }

    @Override
    protected IDialogSettings getDialogBoundsSettings() {
	return JspEditorPlugin.getDefault().getDialogSettings();
    }

    /**
     * 
     * Set style for preview
     */
    public void setStyleForPreview() {

	String styleForSpan = "";
	String html = "";

	Set<String> keySet = attributesMap.keySet();

	for (String key : keySet)
	    styleForSpan += key + Constants.COLON_STRING
		    + attributesMap.get(key) + Constants.SEMICOLON_STRING;

	html = Constants.OPEN_SPAN_TAG + styleForSpan
		+ Constants.TEXT_FOR_PREVIEW + Constants.CLOSE_SPAN_TAG;
	browser.setText(html);
    }
}