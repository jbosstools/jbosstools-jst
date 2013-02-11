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

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.common.ui.widget.editor.SwtFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class JQueryFieldEditorFactory implements JQueryConstants {

	public static IFieldEditor createLabelEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_LABEL, WizardMessages.labelLabel, "");
	}

	public static IFieldEditor createDataThemeEditor() {
		String[] values = new String[]{"", "a", "b", "c", "d", "e"};
		List<String> list = new ArrayList<String>();
		for (String s: values) list.add(s);
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_THEME, WizardMessages.themeLabel, list, "", true);
	}

	public static IFieldEditor createMiniEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_MINI, WizardMessages.miniLabel, false);
	}

	public static IFieldEditor createNumberedEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_NUMBERED, WizardMessages.numberedLabel, false);
	}

	public static IFieldEditor createReadonlyEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_READ_ONLY, WizardMessages.readonlyLabel, false);
	}

	public static IFieldEditor createAutodividersEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_AUTODIVIDERS, WizardMessages.autodividersLabel, false);
	}

	public static IFieldEditor createSearchFilterEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_SEARCH_FILTER, WizardMessages.searchFilterLabel, false);
	}

	public static IFieldEditor createInsetEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_INSET, WizardMessages.insetLabel, false);
	}

}
