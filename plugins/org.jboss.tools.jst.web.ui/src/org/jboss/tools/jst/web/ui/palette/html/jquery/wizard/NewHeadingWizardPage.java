/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.html.jquery.wizard;

import java.beans.PropertyChangeEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewHeadingWizardPage extends NewJQueryWidgetWizardPage {

	public NewHeadingWizardPage() {
		super("newHeading", WizardMessages.newHeadingTitle);
		setDescription(WizardMessages.newHeadingDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		IFieldEditor mode = JQueryFieldEditorFactory.createHeadingLayoutEditor();
		addEditor(mode, parent);

		createIDEditor(parent, true);

		Group headingPanel = null;
		if(parent != null) {
			headingPanel = new Group(parent,SWT.BORDER);
			headingPanel.setText("Heading");
			GridData d = new GridData(GridData.FILL_HORIZONTAL);
			d.horizontalSpan = 3;
			headingPanel.setLayoutData(d);
			headingPanel.setLayout(new GridLayout(3, false));
		}

		IFieldEditor title = JQueryFieldEditorFactory.createTitleEditor("");
		addEditor(title, headingPanel);

		IFieldEditor size = JQueryFieldEditorFactory.createHeadingSizeEditor();
		addEditor(size, headingPanel);

		IFieldEditor corners = JQueryFieldEditorFactory.createCornersEditor();
		addEditor(corners, headingPanel);

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor(getVersion());
		addEditor(theme, headingPanel, true);

		Group contentPanel = null;
		if(parent != null) {
			contentPanel = new Group(parent,SWT.BORDER);
			contentPanel.setText("Content");
			GridData d = new GridData(GridData.FILL_HORIZONTAL);
			d.horizontalSpan = 3;
			contentPanel.setLayoutData(d);
			contentPanel.setLayout(new GridLayout(3, false));	
		}

		IFieldEditor contentCorners = JQueryFieldEditorFactory.createHeadingContentCornersEditor();
		addEditor(contentCorners, contentPanel, true);

		IFieldEditor contenttheme = JQueryFieldEditorFactory.createHeadingContentThemeEditor();
		addEditor(contenttheme, contentPanel, true);

		theme.setValue("a");
		contenttheme.setValue("a");
		title.setValue("Heading");

		updateEnablement();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		updateEnablement();

		super.propertyChange(evt);
	}

	protected void updateEnablement() {
		if(left == null) return;
		String layout = getEditorValue(EDITOR_ID_HEADING_LAYOUT);
		boolean isAttached = HEADING_LAYOUT_ATTACHED.equals(layout);
		getEditor(EDITOR_ID_HEADING_SIZE).setEnabled(!isAttached);
		boolean isCombined = HEADING_LAYOUT_COMBINED.equals(layout);
		getEditor(EDITOR_ID_HEADING_CONTENT_THEME).setEnabled(!isCombined);
		getEditor(EDITOR_ID_HEADING_CONTENT_CORNERS).setEnabled(!isCombined);
	}

	@Override
	protected int getPreferredBrowser() {
		return isLinux ? super.getPreferredBrowser() : SWT.WEBKIT;
	}

}
