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
public class NewTableWizardPage extends AbstractNewHTMLWidgetWizardPage implements JQueryConstants {
	ColumnEditor columns = new ColumnEditor(this, 1, 6);

	public NewTableWizardPage() {
		super("newTable", WizardMessages.newTableTitle);
		setDescription(WizardMessages.newTableDescription);
	}

	protected void createFieldPanel(Composite parent) {
		IFieldEditor modeEditor = JQueryFieldEditorFactory.createTableModeEditor();
		addEditor(modeEditor, parent);

		IFieldEditor id = JQueryFieldEditorFactory.createIDEditor();
		addEditor(id, parent);

		columns.createControl(parent, WizardMessages.columnsLabel);

		Composite[] columns = NewRangeSliderWizardPage.createTwoColumns(parent);
		Composite left = columns[0];
		Composite right = columns[1];

		IFieldEditor responsive = JQueryFieldEditorFactory.createResponsiveEditor();
		addEditor(responsive, left);

		IFieldEditor stripes = JQueryFieldEditorFactory.createStripesEditor();
		addEditor(stripes, right);

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		addEditor(theme, parent, true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(columns.isSwitching) {
			return;
		}
		String name = evt.getPropertyName();
		String value = evt.getNewValue().toString();
		columns.onPropertyChange(name, value);

		super.propertyChange(evt);
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
