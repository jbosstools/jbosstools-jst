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

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewFooterWizardPage extends AbstractNewHTMLWidgetWizardPage implements JQueryConstants {
	ButtonsEditor buttons = new ButtonsEditor(this, 0, 4);

	public NewFooterWizardPage() {
		super("newFooter", WizardMessages.newFooterWizardTitle);
		setDescription(WizardMessages.newFooterWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		buttons.setLabel(0, "Add");
		buttons.setLabel(1, "Up");
		buttons.setLabel(2, "Down");
		buttons.setLabel(3, "Remove");
		buttons.setIcon(0, "plus");
		buttons.setIcon(1, "arrow-u");
		buttons.setIcon(2, "arrow-d");
		buttons.setIcon(3, "delete");
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

		Composite panel = buttons.createControl(parent, "Buttons");

		IFieldEditor arrangement = JQueryFieldEditorFactory.createArragementEditor();
		addEditor(arrangement, panel);

		columns = NewRangeSliderWizardPage.createTwoColumns(panel);
		GridLayout l = (GridLayout)columns[0].getLayout();
		l.marginBottom = 2;
		columns[0].setLayout(l);
		left = columns[0];
		right = columns[1];

		IFieldEditor iconpos = JQueryFieldEditorFactory.createIconPositionEditor();
		addEditor(iconpos, left, true);

		IFieldEditor icononly = JQueryFieldEditorFactory.createIconOnlyEditor();
		addEditor(icononly, right);

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		addEditor(theme, parent, true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(buttons.isSwitching) {
			return;
		}
		String name = evt.getPropertyName();
		String value = evt.getNewValue().toString();
		buttons.onPropertyChange(name, value);

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

}
