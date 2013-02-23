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

import org.jboss.tools.common.ui.widget.editor.CompositeEditor;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.common.ui.widget.editor.SwtFieldEditorFactory;
import org.jboss.tools.common.ui.widget.editor.TextFieldEditor;
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
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_THEME, WizardMessages.themeLabel, toList(values), "", true);
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

	public static IFieldEditor createOffLabelEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_OFF, WizardMessages.offLabelLabel, "Off");
	}

	public static IFieldEditor createOnLabelEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_ON, WizardMessages.onLabelLabel, "On");
	}

	public static IFieldEditor createIDEditor() {
		CompositeEditor editor = (CompositeEditor)SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_ID, WizardMessages.idLabel, "");
		TextFieldEditor text = (TextFieldEditor)editor.getEditors().get(1);
		text.setMessage("Generate");
		return editor; 
	}

	public static IFieldEditor createTitleEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_TITLE, WizardMessages.titleLabel, "");
	}

	static String[] LAYOUT_LIST = {LAYOUT_HORIZONTAL, LAYOUT_VERTICAL};

	public static IFieldEditor createLayoutEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				EDITOR_ID_LAYOUT, 
				WizardMessages.layoutLabel, 
				toList(LAYOUT_LIST), LAYOUT_HORIZONTAL, false);
	}

	public static IFieldEditor createURLEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_URL, WizardMessages.urlLabel, "");
	}

	public static IFieldEditor createDisabledEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_DISABLED, WizardMessages.disabledLabel, false);
	}

	public static IFieldEditor createInlineEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_INLINE, WizardMessages.inlineLabel, false);
	}

	public static IFieldEditor createIconOnlyEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_ICON_ONLY, WizardMessages.iconOnlyLabel, false);
	}

	public static IFieldEditor createIconEditor() {
		String[] values = new String[]{"", "arrow-l", "arrow-r", "arrow-u", "arrow-d", 
				"delete", "plus", "minus", "check", "gear", "refresh", 
				"forward", "back", "grid", "star", "alert", "info",
				"home", "search"};
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_ICON, WizardMessages.iconLabel, toList(values), "", true);
	}

	public static IFieldEditor createIconPositionEditor() {
		String[] values = new String[]{"", "right", "top", "bottom"};
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_ICON_POS, WizardMessages.iconposLabel, toList(values), "", true);
	}

	public static IFieldEditor createActionEditor() {
		String[] values = new String[]{"", 
				WizardMessages.actionDialogLabel, 
				WizardMessages.actionPopupLabel,
				WizardMessages.actionBackLabel,
				WizardMessages.actionExternalLabel};
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_ACTION, WizardMessages.actionLabel, toList(values), "", true);
	}


	static String[] TRANSITION_LIST = {"", 
		TRANSITION_FADE, TRANSITION_POP, TRANSITION_FLIP, TRANSITION_TURN, 
		TRANSITION_FLOW, TRANSITION_SLIDEFADE, TRANSITION_SLIDEDOWN,
		TRANSITION_SLIDE, TRANSITION_SLIDEUP, TRANSITION_NONE};

	//For Open dialog
	public static IFieldEditor createTransitionEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				EDITOR_ID_TRANSITION, 
				WizardMessages.transitionLabel, 
				toList(TRANSITION_LIST), "", false);
	}

	static String[] CLOSE_BUTTON_LIST = {"", CLOSE_RIGHT, CLOSE_NONE};

	//For Dialog
	public static IFieldEditor createCloseButtonEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				EDITOR_ID_CLOSE_BUTTON, 
				WizardMessages.closeButtonPositionLabel, 
				toList(CLOSE_BUTTON_LIST), "", false);
	}

	static List<String> toList(String[] values) {
		List<String> list = new ArrayList<String>();
		for (String s: values) list.add(s);
		return list;
	}

}
