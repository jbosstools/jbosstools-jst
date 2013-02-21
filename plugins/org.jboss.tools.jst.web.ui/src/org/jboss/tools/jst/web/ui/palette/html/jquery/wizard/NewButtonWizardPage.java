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

import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.common.model.ui.editors.dnd.ValidationException;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewButtonWizardPage extends AbstractNewHTMLWidgetWizardPage implements JQueryConstants {

	public NewButtonWizardPage() {
		super("newButton", WizardMessages.newButtonWizardTitle);
		setDescription(WizardMessages.newButtonWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		IFieldEditor label = JQueryFieldEditorFactory.createLabelEditor();
		label.setValue("Link button");
		addEditor(label, parent);

		IFieldEditor url = JQueryFieldEditorFactory.createURLEditor();
		addEditor(url, parent);
		
		IFieldEditor action = JQueryFieldEditorFactory.createActionEditor();
		addEditor(action, parent);
		expandCombo(action);

		IFieldEditor disabled = JQueryFieldEditorFactory.createDisabledEditor();
		addEditor(disabled, parent);

		createSeparator(parent);

		IFieldEditor mini = JQueryFieldEditorFactory.createMiniEditor();
		addEditor(mini, parent);

		IFieldEditor inline = JQueryFieldEditorFactory.createInlineEditor();
		addEditor(inline, parent);

		createSeparator(parent);
	
		IFieldEditor icon = JQueryFieldEditorFactory.createIconEditor();
		addEditor(icon, parent);
		expandCombo(icon);

		IFieldEditor iconpos = JQueryFieldEditorFactory.createIconPositionEditor();
		addEditor(iconpos, parent);
		expandCombo(iconpos);

		IFieldEditor icononly = JQueryFieldEditorFactory.createIconOnlyEditor();
		addEditor(icononly, parent);

		createSeparator(parent);
	
		IFieldEditor theme = JQueryFieldEditorFactory.createDataThemeEditor();
		addEditor(theme, parent);
		expandCombo(theme);

		final Shell shell = parent.getShell();
		
		shell.addShellListener(new ShellAdapter() {
			public void shellActivated(ShellEvent e) {
				Rectangle r = shell.getBounds();
				r.height += 90;
				shell.setBounds(r);
				shell.removeShellListener(this);
			}
		});
		
	}

	public void validate() throws ValidationException {
		boolean icononly = TRUE.equals(getEditorValue(EDITOR_ID_ICON_ONLY));
		IFieldEditor iconpos = getEditor(EDITOR_ID_ICON_POS);
		iconpos.setEnabled(!icononly);
	}
}
