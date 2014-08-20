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
package org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard;

import java.beans.PropertyChangeEvent;

import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil.TwoColumns;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewListWizardPage extends NewIonicWidgetWizardPage {
	ListItemEditor list = new ListItemEditor(this, 1, 6);

	public NewListWizardPage() {
		super("newList", IonicWizardMessages.newListWizardTitle);
		setDescription(IonicWizardMessages.newListWizardDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		IFieldEditor delegate = IonicFieldEditorFactory.createDelegateHandleEditor();
		addEditor(delegate, parent);
		createIDEditor(parent, true);
		addID.setValue(Boolean.FALSE);

		createSeparator(parent);

		addEditor(IonicFieldEditorFactory.createListTypeEditor(), parent);
		TwoColumns columns = createTwoColumns(parent);
		addEditor(IonicFieldEditorFactory.createShowDeleteEditor(), columns.left());
		addEditor(IonicFieldEditorFactory.createShowReorderEditor(), columns.right());
		addEditor(IonicFieldEditorFactory.createCanSwipeEditor(), columns.left());
		addEditor(JQueryFieldEditorFactory.createSpan("span1", 3),columns.right());

		list.createControl(parent, WizardMessages.itemsLabel);

		addEditor(IonicFieldEditorFactory.createDeleteButtonEditor(), parent);
		addEditor(IonicFieldEditorFactory.createReorderButtonEditor(), parent);
		addEditor(IonicFieldEditorFactory.createOptionButtonEditor(), parent);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(list.isSwitching()) {
			return;
		}
		String name = evt.getPropertyName();
		String value = evt.getNewValue().toString();
		list.onPropertyChange(name, value);

		super.propertyChange(evt);
	}

}
