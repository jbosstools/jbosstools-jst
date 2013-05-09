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
import org.jboss.tools.common.model.ui.editors.dnd.ValidationException;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewPanelWizardPage extends AbstractNewHTMLWidgetWizardPage implements JQueryConstants {

	public NewPanelWizardPage() {
		super("newPanel", WizardMessages.newPanelWizardTitle);
		setDescription(WizardMessages.newPanelWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		IFieldEditor id = JQueryFieldEditorFactory.createIDEditor();
		addEditor(id, parent);

		IFieldEditor display = JQueryFieldEditorFactory.createPanelDisplayEditor();
		addEditor(display, parent);

		IFieldEditor position = JQueryFieldEditorFactory.createPanelPositionEditor();
		addEditor(position, parent);

		IFieldEditor fixed = JQueryFieldEditorFactory.createFixedPositionEditor();
		addEditor(fixed, parent);

		IFieldEditor dismissable = JQueryFieldEditorFactory.createDismissableEditor();
		addEditor(dismissable, parent);

		IFieldEditor swipe = JQueryFieldEditorFactory.createSwipeCloseEditor();
		addEditor(swipe, parent);

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		addEditor(theme, parent, true);
	}

	public void validate() throws ValidationException {
		String id = getEditorValue(EDITOR_ID_ID);
		if(id != null && !getWizard().isIDAvailable(id)) {
			throw new ValidationException(WizardMessages.errorIDisUsed);
		}
	}

	protected int getPreferredBrowser() {
		return SWT.WEBKIT;
	}

}
