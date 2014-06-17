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
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil.TwoColumns;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewFooterWizardPage extends NewJQueryWidgetWizardPage {
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
		IFieldEditor title = JQueryFieldEditorFactory.createTitleEditor(WizardDescriptions.footerTitle);
		title.setValue("");
		addEditor(title, parent);

		createIDEditor(parent, true);

		TwoColumns columns = createTwoColumns(parent);

		IFieldEditor fixed = JQueryFieldEditorFactory.createFixedPositionEditor(WizardDescriptions.footerFixedPosition);
		addEditor(fixed, columns.left());
		
		IFieldEditor fullScreen = JQueryFieldEditorFactory.createFullScreenEditor(WizardDescriptions.footerFullScreen);
		addEditor(fullScreen, columns.right());

		setEnabled(EDITOR_ID_FULL_SCREEN, false);

		Composite panel = buttons.createControl(parent, "Buttons");

		IFieldEditor arrangement = JQueryFieldEditorFactory.createArragementEditor();
		addEditor(arrangement, panel);

		IFieldEditor barpos = JQueryFieldEditorFactory.createBarPositionEditor();
		addEditor(barpos, panel);

		columns = createTwoColumns(panel);
		if(parent != null) {
			GridLayout l = (GridLayout)columns.left().getLayout();
			l.marginBottom = 2;
			columns.left().setLayout(l);
		}

		IFieldEditor iconpos = JQueryFieldEditorFactory.createIconPositionEditor();
		addEditor(iconpos, columns.left(), true);

		IFieldEditor icononly = JQueryFieldEditorFactory.createIconOnlyEditor();
		addEditor(icononly, columns.right());

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor(getVersion());
		addEditor(theme, parent, true);

		updateBarEnablement();
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
		boolean icononly = isTrue(EDITOR_ID_ICON_ONLY);
		setEnabled(EDITOR_ID_ICON_POS, hasIcons && !icononly);
		setEnabled(EDITOR_ID_ICON_ONLY, hasIcons);

		boolean isFixed = TRUE.equals(getEditorValue(EDITOR_ID_FIXED_POSITION));
		setEnabled(EDITOR_ID_FULL_SCREEN, isFixed);

		updateBarEnablement();
		super.propertyChange(evt);
	}

	void updateBarEnablement() {
		boolean isArrangementEnabled = buttons.getNumber() > 0;
		setEnabled(EDITOR_ID_ARRAGEMENT, isArrangementEnabled);
		boolean isBarPosEnabled = isArrangementEnabled &&
				(ARRAGEMENT_GROUPED.equals(getEditorValue(EDITOR_ID_ARRAGEMENT))
					|| (!ARRAGEMENT_NAVBAR.equals(getEditorValue(EDITOR_ID_ARRAGEMENT)) && buttons.getNumber() == 1));
		setEnabled(EDITOR_ID_BAR_POSITION, isBarPosEnabled);
		
	}

}
