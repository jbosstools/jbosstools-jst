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
package org.jboss.tools.jst.web.ui.palette.html.jquery.wizard;

import java.beans.PropertyChangeEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil;
import org.jboss.tools.jst.web.ui.internal.properties.advanced.LayoutUtil.TwoColumns;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewTabsWizardPage extends NewJQueryWidgetWizardPage {
	TabEditor tabs = new TabEditor(this, 1, 6);

	public NewTabsWizardPage() {
		super("newTabs", WizardMessages.newTabsTitle);
		setDescription(WizardMessages.newTabsDescription);
	}

	@Override
	protected void createFieldPanel(Composite parent) {
		IFieldEditor modeEditor = JQueryFieldEditorFactory.createTabsLayoutEditor();
		addEditor(modeEditor, parent);

		createIDEditor(parent, false);

		TwoColumns columns = LayoutUtil.createTwoColumns(parent);
		IFieldEditor collapsible = JQueryFieldEditorFactory.createTabsCollapsibleEditor();
		addEditor(collapsible, columns.left());

		IFieldEditor collapsed = JQueryFieldEditorFactory.createTabsCollapsedEditor();
		addEditor(collapsed, columns.right());

		IFieldEditor animated = JQueryFieldEditorFactory.createTabsAnimatedEditor();
		addEditor(animated, columns.left());

		IFieldEditor span = JQueryFieldEditorFactory.createSpan("tabs", 3);
		addEditor(span, columns.right());
		
		IFieldEditor activation = JQueryFieldEditorFactory.createTabsActivationEditor();
		addEditor(activation, parent);

		tabs.createControl(parent, WizardMessages.itemsLabel);

		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor(getVersion());
		addEditor(theme, parent, true);

		updateEnablement();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(tabs.isSwitching) {
			return;
		}
		String name = evt.getPropertyName();
		String value = evt.getNewValue().toString();
		if(tabs.onPropertyChange(name, value)) {
			if(EDITOR_ID_TABS_ACTIVE.equals(name)) {
				tabs.onActiveModified();
			}
		}

		updateEnablement();

		super.propertyChange(evt);
	}

	@Override
	protected int getPreferredBrowser() {
		return isLinux ? super.getPreferredBrowser() : SWT.WEBKIT;
	}

	protected void updateEnablement() {
		boolean isCollapsible = isTrue(EDITOR_ID_TABS_COLLAPSIBLE);
		getEditor(EDITOR_ID_COLLAPSED).setEnabled(isCollapsible);
		boolean isCollapsed = isCollapsible && isTrue(EDITOR_ID_COLLAPSED);
		getEditor(EDITOR_ID_TABS_ACTIVE).setEnabled(!isCollapsed);
	}

}
