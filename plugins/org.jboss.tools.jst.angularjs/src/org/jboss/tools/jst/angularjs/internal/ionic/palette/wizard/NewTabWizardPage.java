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
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewTabWizardPage extends NewIonicWidgetWizardPage {

	public NewTabWizardPage() {
		super("newTab", IonicWizardMessages.newTabTitle);
		setDescription(IonicWizardMessages.newTabDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		IFieldEditor title = JQueryFieldEditorFactory.createTitleEditor(IonicWizardMessages.tabTitleDescription);
		title.setValue("Title");
		addEditor(title, parent);

		createIDEditor(parent, true);
		addID.setValue(Boolean.FALSE);

		addEditor(JQueryFieldEditorFactory.createURLEditor(), parent);

		createSeparator(parent);

		addEditor(IonicFieldEditorFactory.createIconEditor(JQueryConstants.ATTR_ICON), parent);
		addEditor(IonicFieldEditorFactory.createIconOnEditor(), parent);
		addEditor(IonicFieldEditorFactory.createIconOffEditor(), parent);

		createSeparator(parent);

		addEditor(IonicFieldEditorFactory.createBadgeEditor(), parent);
		addEditor(IonicFieldEditorFactory.createBadgeStyleEditor(), parent);
		addEditor(IonicFieldEditorFactory.createOnSelectEditor(), parent);
		addEditor(IonicFieldEditorFactory.createOnDeselectEditor(), parent);
		addEditor(IonicFieldEditorFactory.createNgClickEditor(ATTR_NG_CLICK), parent);

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String name = evt.getPropertyName();
//
		super.propertyChange(evt);
	}

}