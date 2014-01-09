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
import org.eclipse.swt.widgets.Group;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileVersion;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil.TwoColumns;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewPopupWizardPage extends NewJQueryWidgetWizardPage {

	public NewPopupWizardPage() {
		super("newPopup", WizardMessages.newPopupWizardTitle);
		setDescription(WizardMessages.newPopupWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		Group windowPanel = new Group(parent,SWT.BORDER);
		windowPanel.setText("Popup Window");
		GridData d = new GridData(GridData.FILL_HORIZONTAL);
		d.horizontalSpan = 3;
		windowPanel.setLayoutData(d);
		windowPanel.setLayout(new GridLayout(3, false));		

		createIDEditor(windowPanel, false);

		IFieldEditor close = JQueryFieldEditorFactory.createClosePopupButtonEditor();
		addEditor(close, windowPanel, true);
		
		TwoColumns columns = createTwoColumns(windowPanel);

		IFieldEditor dismissable = JQueryFieldEditorFactory.createDismissableEditor(WizardDescriptions.popupDismissable);
		addEditor(dismissable, columns.left());

		IFieldEditor shadow = JQueryFieldEditorFactory.createShadowEditor();
		addEditor(shadow, columns.right());

		IFieldEditor padding = JQueryFieldEditorFactory.createPaddingEditor();
		addEditor(padding, columns.left());

		IFieldEditor corners = JQueryFieldEditorFactory.createCornersEditor();
		addEditor(corners, columns.right());
		
		if(getVersion() != JQueryMobileVersion.JQM_1_3) {
			IFieldEditor arrow = JQueryFieldEditorFactory.createArrowEditor();
			addEditor(arrow, columns.left());
			addEditor(JQueryFieldEditorFactory.createSpan("popup-arrow", 3), columns.right());
		}
		
		IFieldEditor theme = JQueryFieldEditorFactory.createPopupThemeEditor(getVersion());
		addEditor(theme, windowPanel, true);

		IFieldEditor overlay = JQueryFieldEditorFactory.createOverlayEditor(getVersion());
		addEditor(overlay, windowPanel, true);

		IFieldEditor popupButton = JQueryFieldEditorFactory.createPopupButtonEditor();
		addEditor(popupButton, parent);

		Composite buttonPanel = new Composite(parent, SWT.BORDER);
		d = new GridData(GridData.FILL_HORIZONTAL);
		d.horizontalSpan = 3;
		buttonPanel.setLayoutData(d);
		buttonPanel.setLayout(new GridLayout(3, false));		

		IFieldEditor label = JQueryFieldEditorFactory.createLabelEditor();
		label.setValue("Popup");
		addEditor(label, buttonPanel);

		IFieldEditor info = JQueryFieldEditorFactory.createInfoStyledEditor();
		addEditor(info, buttonPanel);

		IFieldEditor transition = JQueryFieldEditorFactory.createTransitionEditor(WizardDescriptions.popupTransition);
		addEditor(transition, buttonPanel, true);

		IFieldEditor positionTo = JQueryFieldEditorFactory.createPositionToEditor();
		addEditor(positionTo, buttonPanel, true);

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(EDITOR_ID_POPUP_BUTTON.equals(evt.getPropertyName())) {
			boolean enabled = isTrue(EDITOR_ID_POPUP_BUTTON);
			setEnabled(EDITOR_ID_LABEL, enabled);
			setEnabled(EDITOR_ID_INFO_STYLED, enabled);
			setEnabled(EDITOR_ID_TRANSITION, enabled);
			setEnabled(EDITOR_ID_POSITION_TO, enabled);
		}
		super.propertyChange(evt);
		if(EDITOR_ID_SHADOW.equals(evt.getPropertyName()) 
				&& !isTrue(EDITOR_ID_SHADOW)
				&& getEditorValue(EDITOR_ID_THEME).length() == 0
				) {
			setEditorValue(EDITOR_ID_THEME, "none");
		}
	}

}
