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
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewJQueryWidgetWizardPage.TwoColumns;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewPanelWizardPage extends NewJQueryWidgetWizardPage {
	ListEditor items = new ListEditor(this, 1, 8){
		public void updateEnablement() {
			boolean addList = isTrue(EDITOR_ID_ADD_LIST);
			setEditorEnablement(addList);
		}
	};

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

		TwoColumns columns = createTwoColumns(parent);

		IFieldEditor fixed = JQueryFieldEditorFactory.createFixedPositionEditor(WizardDescriptions.panelFixedPosition);
		addEditor(fixed, columns.left());

		IFieldEditor dismissable = JQueryFieldEditorFactory.createDismissableEditor(WizardDescriptions.panelDismissable);
		addEditor(dismissable, columns.right());

		IFieldEditor swipe = JQueryFieldEditorFactory.createSwipeCloseEditor();
		addEditor(swipe, columns.left());

		IFieldEditor span = JQueryFieldEditorFactory.createSpan("span", 3);
		addEditor(span, columns.right());

		createSeparator(parent);

		IFieldEditor addList = JQueryFieldEditorFactory.createAddListEditor();
		addEditor(addList, parent);

		items.createControl(parent, WizardMessages.itemsLabel);

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor(getVersion());
		addEditor(theme, parent, true);
	}

	@Override
	protected int getPreferredBrowser() {
		return isLinux ? super.getPreferredBrowser() : SWT.WEBKIT;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(items.isSwitching) {
			return;
		}
		String name = evt.getPropertyName();
		String value = evt.getNewValue().toString();
		if(items.onPropertyChange(name, value)) {
		}
		
		items.updateEnablement();

		super.propertyChange(evt);
	}

}
