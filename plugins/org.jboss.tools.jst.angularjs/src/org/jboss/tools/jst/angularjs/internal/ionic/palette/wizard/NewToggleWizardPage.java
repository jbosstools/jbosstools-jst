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

import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil.TwoColumns;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.WizardDescriptions;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewToggleWizardPage extends NewIonicWidgetWizardPage {

	public NewToggleWizardPage() {
		super("newToggle", IonicWizardMessages.newToggleWizardTitle);
		setDescription(IonicWizardMessages.newToggleWizardDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		IFieldEditor label = JQueryFieldEditorFactory.createLabelEditor();
		label.setValue("Label");
		addEditor(label, parent);

		createIDEditor(parent, true);
		addID.setValue(Boolean.FALSE);

		addEditor(IonicFieldEditorFactory.createNgModelEditor(), parent);
		addEditor(JQueryFieldEditorFactory.createNameEditor(), parent);

		createSeparator(parent);

		TwoColumns columns = createTwoColumns(parent);
		addEditor(JQueryFieldEditorFactory.createCheckedEditor(WizardDescriptions.checkboxIsSelected), columns.left());
		addEditor(JQueryFieldEditorFactory.createDisabledEditor(), columns.right());
		addEditor(IonicFieldEditorFactory.createToggleColorEditor(), parent);

		createSeparator(parent);

		addEditor(IonicFieldEditorFactory.createNgTrueValueEditor(), parent);
		addEditor(IonicFieldEditorFactory.createNgFalseValueEditor(), parent);
		addEditor(IonicFieldEditorFactory.createNgChangeEditor(), parent);

	}

}
