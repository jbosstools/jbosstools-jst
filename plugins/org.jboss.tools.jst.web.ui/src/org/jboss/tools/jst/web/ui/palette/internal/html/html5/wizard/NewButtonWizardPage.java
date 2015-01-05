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
package org.jboss.tools.jst.web.ui.palette.internal.html.html5.wizard;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jboss.tools.common.model.ui.editors.dnd.ValidationException;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil.TwoColumns;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.wizard.NewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewButtonWizardPage extends NewHTMLWidgetWizardPage {

	public NewButtonWizardPage() {
		super("newButton", WizardMessages.newButtonWizardTitle);
		setDescription(WizardMessages.newHTMLFormButtonWizardDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		IFieldEditor type = JQueryFieldEditorFactory.createFormButtonTypeEditor();
		addEditor(type, parent);

		IFieldEditor label = JQueryFieldEditorFactory.createFormButtonValueEditor();
		addEditor(label, parent);

		createIDEditor(parent, true);
		createSeparator(parent);

		TwoColumns columns = createTwoColumns(parent);

		addEditor(JQueryFieldEditorFactory.createDisabledEditor(), columns.left());
		addEditor(JQueryFieldEditorFactory.createAutofocusEditor(), columns.right());

		IFieldEditor form = JQueryFieldEditorFactory.createFormReferenceEditor();
		addEditor(form, parent);

		Group g = parent == null ? null : LayoutUtil.createGroup(parent, "Override Form"); 
		addEditor(HTMLFieldEditorFactory.createButtonFormActionEditor(), g);
		addEditor(HTMLFieldEditorFactory.createButtonFormMethodEditor(), g);
	}

	@Override
	public void validate() throws ValidationException {
		String type = getEditorValue(JQueryConstants.EDITOR_ID_FORM_BUTTON_TYPE);
		boolean isSubmit = (BUTTON_TYPE_SUBMIT.equals(type));
		setEnabled(ATTR_FORM_ACTION, isSubmit);
		setEnabled(ATTR_FORM_METHOD, isSubmit);

		super.validate();
	}
}
