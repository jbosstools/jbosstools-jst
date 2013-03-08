/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
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
import org.eclipse.swt.widgets.TabFolder;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewFooterWizardPage extends AbstractNewHTMLWidgetWizardPage implements JQueryConstants {
	ButtonsEditor buttons = new ButtonsEditor(this);

	public NewFooterWizardPage() {
		super("newFooter", WizardMessages.newFooterWizardTitle);
		setDescription(WizardMessages.newFooterWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		buttons.buttons[0].label = "Add";
		buttons.buttons[1].label = "Up";
		buttons.buttons[2].label = "Down";
		buttons.buttons[3].label = "Remove";
		buttons.buttons[0].icon = "plus";
		buttons.buttons[1].icon = "arrow-u";
		buttons.buttons[2].icon = "arrow-d";
		buttons.buttons[3].icon = "delete";
		IFieldEditor title = JQueryFieldEditorFactory.createTitleEditor();
		title.setValue("");
		addEditor(title, parent);

		Composite[] columns = NewRangeSliderWizardPage.createTwoColumns(parent);
		Composite left = columns[0];
		Composite right = columns[1];

		IFieldEditor fixed = JQueryFieldEditorFactory.createFixedPositionEditor();
		addEditor(fixed, left);
		
		IFieldEditor fullScreen = JQueryFieldEditorFactory.createFullScreenEditor();
		addEditor(fullScreen, right);
		
		IFieldEditor number = JQueryFieldEditorFactory.createItemsNumberEditor(WizardMessages.numberOfItemsLabel, 0, 4);
		addEditor(number); //Control is created by buttons editor.
		
		IFieldEditor arrangement = JQueryFieldEditorFactory.createArragementEditor();
		addEditor(arrangement); //Control is created by buttons editor.

		IFieldEditor iconpos = JQueryFieldEditorFactory.createIconPositionEditor();
		addEditor(iconpos); //Control is created by buttons editor.
		IFieldEditor icononly = JQueryFieldEditorFactory.createIconOnlyEditor();
		addEditor(icononly); //Control is created by buttons editor.

		TabFolder tab = buttons.createItemsFolder(parent, "Buttons");
		
		Composite p1 = new Composite(tab, SWT.NONE);
		buttons.control = p1;
		p1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout(3, false);
		p1.setLayout(layout);

		IFieldEditor label = JQueryFieldEditorFactory.createLabelEditor();
		addEditor(label, p1);

		IFieldEditor url = JQueryFieldEditorFactory.createURLEditor();
		addEditor(url, p1);

		IFieldEditor icon = JQueryFieldEditorFactory.createIconEditor();
		addEditor(icon, p1);

		getEditor(EDITOR_ID_NUMBER_OF_ITEMS).setValue("3");

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		addEditor(theme, parent);
		expandCombo(theme);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(buttons.isSwitching) {
			return;
		}
		String name = evt.getPropertyName();
		String value = evt.getNewValue().toString();
		if(EDITOR_ID_LABEL.equals(name)) {
			buttons.getSelected().label = value;
		} else if(EDITOR_ID_URL.equals(name)) {
			buttons.getSelected().url = value;
		} else if(EDITOR_ID_ICON.equals(name)) {
			buttons.getSelected().icon = value;
			getEditor(EDITOR_ID_ICON_POS).setEnabled(buttons.hasIcons());
		} else if(EDITOR_ID_NUMBER_OF_ITEMS.equals(name)) {
			buttons.setNumber(Integer.parseInt(value));
		}

		boolean hasIcons = buttons.hasIcons();
		boolean icononly = TRUE.equals(getEditorValue(EDITOR_ID_ICON_ONLY));
		if(getEditor(EDITOR_ID_ICON_POS) != null) {
			getEditor(EDITOR_ID_ICON_POS).setEnabled(hasIcons && !icononly);
		}
		if(getEditor(EDITOR_ID_ICON_ONLY) != null) {
			getEditor(EDITOR_ID_ICON_ONLY).setEnabled(hasIcons);
		}

		boolean isFixed = TRUE.equals(getEditorValue(EDITOR_ID_FIXED_POSITION));
		if(getEditor(EDITOR_ID_FULL_SCREEN) != null) {
			getEditor(EDITOR_ID_FULL_SCREEN).setEnabled(isFixed);
		}
		
		super.propertyChange(evt);
	}

	protected int getAdditionalHeight() {
		return 125;
	}

}
