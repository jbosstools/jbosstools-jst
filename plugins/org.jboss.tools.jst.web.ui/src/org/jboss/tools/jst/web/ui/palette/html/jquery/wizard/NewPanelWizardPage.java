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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewPanelWizardPage extends NewJQueryWidgetWizardPage {

	public NewPanelWizardPage() {
		super("newPanel", WizardMessages.newPanelWizardTitle);
		setDescription(WizardMessages.newPanelWizardDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		createIDEditor(parent, false);

		IFieldEditor display = JQueryFieldEditorFactory.createPanelDisplayEditor();
		addEditor(display, parent);

		IFieldEditor position = JQueryFieldEditorFactory.createPanelPositionEditor();
		addEditor(position, parent);

		IFieldEditor fixed = JQueryFieldEditorFactory.createFixedPositionEditor(WizardDescriptions.panelFixedPosition);
		addEditor(fixed, parent);

		IFieldEditor dismissable = JQueryFieldEditorFactory.createDismissableEditor(WizardDescriptions.panelDismissable);
		addEditor(dismissable, parent);

		IFieldEditor swipe = JQueryFieldEditorFactory.createSwipeCloseEditor();
		addEditor(swipe, parent);

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor(getVersion());
		addEditor(theme, parent, true);
	}

	@Override
	protected int getPreferredBrowser() {
		return isLinux ? super.getPreferredBrowser() : SWT.WEBKIT;
	}

}
