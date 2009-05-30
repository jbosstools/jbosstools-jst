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

import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.StyleAttributes;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.BaseTabControl;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.ICSSTabControl;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.TabBackgroundControl;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.TabBoxesControl;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.TabPropertySheetControl;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.TabQuickEditControl;
import org.jboss.tools.jst.jsp.outline.cssdialog.tabs.TabTextControl;

/**
 * Class for creating style tabs
 * 
 * @author Igor Zhukov (izhukov@exadel.com)
 */
public class StyleComposite extends Composite {

	private TabFolder tabFolder;
	private StyleAttributes styleAttributes;
	public static int DEFAULT_START_TAB = 4;

	/**
	 * StyleComposite constructor.
	 * 
	 * @param parent
	 *            Composite object
	 * @param styleAttributes
	 *            StyleAttributes objects
	 * @param oldStyle
	 *            String value
	 */
	public StyleComposite(Composite parent, StyleAttributes styleAttributes,
			DataBindingContext bindingContext) {
		super(parent, SWT.NONE);
		this.styleAttributes = styleAttributes;

		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		setLayout(gridLayout);
		setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				true, true));
		tabFolder.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				updateTab((TabItem) e.item);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// add text tab
		ScrolledComposite tabComposite = createTabComposite();

		BaseTabControl baseTabControl = new TabTextControl(tabComposite,
				styleAttributes, bindingContext);

		createTabItem(tabComposite, baseTabControl,
				JstUIMessages.TEXT_FONT_TAB_NAME,
				JstUIMessages.TEXT_FONT_TAB_NAME);

		tabComposite.setContent(baseTabControl);
		tabComposite.setMinSize(baseTabControl.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

		// add background tab
		tabComposite = createTabComposite();

		baseTabControl = new TabBackgroundControl(tabComposite,
				styleAttributes, bindingContext);

		createTabItem(tabComposite, baseTabControl,
				JstUIMessages.BACKGROUND_TAB_NAME,
				JstUIMessages.BACKGROUND_TAB_NAME);

		tabComposite.setContent(baseTabControl);
		tabComposite.setMinSize(baseTabControl.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

		// add boxes tab
		tabComposite = createTabComposite();

		baseTabControl = new TabBoxesControl(tabComposite, styleAttributes,
				bindingContext);

		createTabItem(tabComposite, baseTabControl,
				JstUIMessages.BOXES_TAB_NAME, JstUIMessages.BOXES_TAB_NAME);

		tabComposite.setContent(baseTabControl);
		tabComposite.setMinSize(baseTabControl.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

		// add propertie tab
		tabComposite = createTabComposite();
		baseTabControl = new TabPropertySheetControl(tabComposite,
				styleAttributes, bindingContext);

		createTabItem(tabComposite, baseTabControl,
				JstUIMessages.PROPERTY_SHEET_TAB_NAME,
				JstUIMessages.PROPERTY_SHEET_TAB_NAME);

		tabComposite.setContent(baseTabControl);
		tabComposite.setMinSize(baseTabControl.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

		// add quick edit tab
		tabComposite = createTabComposite();
		baseTabControl = new TabQuickEditControl(tabComposite, styleAttributes,
				bindingContext);

		createTabItem(tabComposite, baseTabControl,
				JstUIMessages.QUICK_EDIT_TAB_NAME,
				JstUIMessages.QUICK_EDIT_TAB_NAME);

		tabComposite.setContent(baseTabControl);
		tabComposite.setMinSize(baseTabControl.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));
		
		tabFolder.setSelection(DEFAULT_START_TAB);

	}

	/**
	 * Clear whole style composite component.
	 */
	public void clearStyleComposite() {
		styleAttributes.clear();

	}

	/**
	 * Clear whole style composite component.
	 */
	public void setStyleProperties(Map<String, String> properties) {
		styleAttributes.setStyleProperties(properties);

	}

	public TabFolder getTabFolder() {
		return tabFolder;
	}

	public TabItem createTabItem(Composite content, ICSSTabControl tabControl,
			String label, String tooltip) {

		TabItem item = new TabItem(tabFolder, SWT.NONE);
		item.setText(label);
		item.setToolTipText(tooltip);
		item.setControl(content);
		item.setData(tabControl);
		return item;

	}

	public ScrolledComposite createTabComposite() {

		ScrolledComposite scroll = new ScrolledComposite(tabFolder,
				SWT.H_SCROLL | SWT.V_SCROLL);

		scroll.setExpandHorizontal(true);
		scroll.setExpandVertical(true);

		return scroll;
	}

	public void selectTab(int index) {

		tabFolder.setSelection(index);
		TabItem item = tabFolder.getItem(index);
		updateTab(item);

	}

	public void updateCurrentTab() {
		updateTab(tabFolder.getSelection()[0]);
	}

	private void updateTab(TabItem item) {
		if (item.getData() instanceof ICSSTabControl)
			((ICSSTabControl) item.getData()).update();
	}

}
