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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.common.ui.widget.editor.ButtonFieldEditor;
import org.jboss.tools.common.ui.widget.editor.CheckBoxFieldEditor;
import org.jboss.tools.common.ui.widget.editor.CompositeEditor;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.common.ui.widget.editor.LabelFieldEditor;
import org.jboss.tools.common.ui.widget.editor.SwtFieldEditorFactory;
import org.jboss.tools.common.ui.widget.editor.TextFieldEditor;
import org.jboss.tools.common.ui.widget.editor.ButtonFieldEditor.ButtonPressedAction;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.WizardDescriptions;
import org.jboss.tools.jst.web.ui.palette.html.wizard.HTMLConstants;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class HTMLFieldEditorFactory implements HTMLConstants {

	/**
	 * Used in New Context context.
	 * @return checkbox field editor unselected by default
	 */
	public static IFieldEditor createAddScriptTemplateEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_ADD_SCRIPT_TEMPLATE, 
				WizardMessages.addScriptTemplate, false,
				WizardDescriptions.canvasAddScriptTemplate);
	}

	/**
	 * Used in New Datalist wizard.
	 * @return text field editor for option label
	 */
	public static IFieldEditor createLabelEditor() {
		return createLabelEditor(WizardDescriptions.optionLabel);
	}

	/**
	 * Used in New Datalist and new Menuitem wizard.
	 * @return text field editor for option label
	 */
	public static IFieldEditor createLabelEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_LABEL, WizardMessages.labelLabel, "",
				description);
	}

	public static final String EDITOR_ID_MENU_LABEL = "menu-label";
	/**
	 * Used in New Datalist, New Menu and New Menuitem wizard.
	 * @return text field editor for option label
	 */
	public static IFieldEditor createLabelEditor(String editorID, String description) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(editorID, WizardMessages.labelLabel, "",
				description);
	}

	/**
	 * Used in New Datalist wizard.
	 * @return text efield editor for option value
	 */
	public static IFieldEditor createValueEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_VALUE, WizardMessages.valueLabel, "",
				WizardDescriptions.optionValue);
	}

	/**
	 * Used in New Datalist wizard.
	 * @return checkbox field editor unselected by default
	 */
	public static IFieldEditor createAddInputEditor(ButtonPressedAction action) {
		CompositeEditor editor = new CompositeEditor(TAG_INPUT, WizardMessages.addInput, false);
		ButtonFieldEditor b = new ButtonFieldEditor(TAG_INPUT, action, false);
		editor.addFieldEditors(new IFieldEditor[]{new LabelFieldEditor(TAG_INPUT, WizardMessages.addInput, WizardDescriptions.datalistAddInput),
				new CheckBoxFieldEditor(TAG_INPUT, WizardMessages.addInput,Boolean.valueOf(false)),
				b});
		return editor;
	}

	/**
	 * Used in New Table wizard.
	 * @return 
	 */
	public static IFieldEditor createCaptionEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(TAG_CAPTION, WizardMessages.captionLabel, "",
				WizardDescriptions.tableCaption);
	}

	static String[] TABLE_KIND_LIST = {TABLE_KIND_SIMPLE, TABLE_KIND_ADVANCED};
	static String[] TABLE_KIND_LABEL_LIST = {WizardMessages.tableKindSimpleLabel, WizardMessages.tableKindAdvancedLabel};

	/**
	 * Used in New Table wizard.
	 * @return
	 */
	public static IFieldEditor createTableKindEditor() {
		return SwtFieldEditorFactory.INSTANCE.createRadioEditor(
				EDITOR_ID_TABLE_KIND, 
				WizardMessages.kindLabel, 
				toList(TABLE_KIND_LABEL_LIST), 
				toList(TABLE_KIND_LIST), 
				TABLE_KIND_ADVANCED,
				WizardDescriptions.tableKind);
	}

	/**
	 * Used in New Table wizard.
	 * @return 
	 */
	public static IFieldEditor createAddHeaderEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(TAG_THEAD, 
				WizardMessages.addHeader, true,
				WizardDescriptions.tableAddHeader);
	}

	/**
	 * Used in New Table wizard.
	 * @return 
	 */
	public static IFieldEditor createAddFooterEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(TAG_TFOOT, 
				WizardMessages.addFooter, true,
				WizardDescriptions.tableAddFooter);
	}

	/**
	 * Used in New Table wizard.
	 * @return 
	 */
	public static IFieldEditor createColumnFootContentEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_FOOTER_CONTENT, WizardMessages.footContentLabel, "",
				WizardDescriptions.tableFootContent);
	}

	static List<String> toList(String[] values) {
		List<String> list = new ArrayList<String>();
		for (String s: values) list.add(s);
		return list;
	}

	public static IFieldEditor createInputListEditor(ButtonPressedAction action) {
		CompositeEditor editor = new CompositeEditor(ATTR_LIST, WizardMessages.listLabel, "");
		ButtonFieldEditor b = new ButtonFieldEditor(ATTR_LIST, action, "");
		b.setDescription(WizardDescriptions.inputCreateDatalist);
		editor.addFieldEditors(new IFieldEditor[]{new LabelFieldEditor(ATTR_LIST, WizardMessages.listLabel, WizardDescriptions.inputList),
				new TextFieldEditor(ATTR_LIST, WizardMessages.listLabel, ""),
				b});
		return editor;
	}

	public static IFieldEditor createEditSelectedMenuitemEditor(ButtonPressedAction action) {
		CompositeEditor editor = new CompositeEditor("EditMenuitem", WizardMessages.listLabel, "");
		ButtonFieldEditor b = new ButtonFieldEditor("EditMenuitem", action, "");
		b.setDescription(WizardDescriptions.inputCreateDatalist);
		editor.addFieldEditors(new IFieldEditor[]{new LabelFieldEditor("EditMenuitem", "", ""),
				new LabelFieldEditor("EditMenuitem", "", ""),
				b});
		return editor;
	}

	/**
	 * Used in New Button wizard.
	 * @return
	 */
	public static IFieldEditor createButtonFormActionEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_FORM_ACTION, WizardMessages.actionLabel, "",
				WizardDescriptions.buttonFormAction);
	}

	static String INHERITED_FROM_FORM = "default";

	static String[] METHOD_LIST = {INHERITED_FROM_FORM, METHOD_GET, METHOD_POST};

	/**
	 * Used in New Button wizard.
	 * @return
	 */
	public static IFieldEditor createButtonFormMethodEditor() {
		return SwtFieldEditorFactory.INSTANCE.createRadioEditor(
				ATTR_FORM_METHOD, 
				WizardMessages.methodLabel, 
				toList(METHOD_LIST), 
				toList(METHOD_LIST), 
				INHERITED_FROM_FORM,
				WizardDescriptions.buttonFormMethod);
	}

	static String[] MENUITEM_TYPE_LIST = {MENUITEM_TYPE_CHECKBOX, MENUITEM_TYPE_COMMAND, MENUITEM_TYPE_RADIO};

	/**
	 * Used in New Menuitem wizard.
	 * @return
	 */
	public static IFieldEditor createMenuitemTypeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createRadioEditor(
				ATTR_TYPE, 
				WizardMessages.typeLabel, 
				toList(MENUITEM_TYPE_LIST), 
				toList(MENUITEM_TYPE_LIST), 
				MENUITEM_TYPE_COMMAND,
				WizardDescriptions.menuitemType);
	}

	/**
	 * Used in New Menuitem wizard.
	 * @return
	 */
	public static IFieldEditor createDefaultEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_DEFAULT, 
				WizardMessages.defaultLabel, false,
				WizardDescriptions.menuitemDefault);
	}

	/**
	 * Used in New Menuitem wizard.
	 * @return
	 */
	public static IFieldEditor createIconEditor(IFile context) {
		return JQueryFieldEditorFactory.createBrowseWorkspaceImageEditor(ATTR_ICON, WizardMessages.iconLabel, context, WizardDescriptions.menuitemIcon);
	}

	/**
	 * Used in New Menuitem wizard.
	 * @return text field editor for option label
	 */
	public static IFieldEditor createRadiogroupEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_RADIOGROUP, WizardMessages.radiogroupLabel, "",
				WizardDescriptions.menuitemRadiogroup);
	}

	public static final String EDITOR_ID_MENU_TYPE = "menu-type";

	static String[] MENU_TYPES = {
		"", MENU_TYPE_CONTEXT, MENU_TYPE_LIST, MENU_TYPE_TOOLBAR
	};
	/**
	 * Used in New Menu wizard.
	 * @return text field editor for option label
	 */
	public static IFieldEditor createMenuTypeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_MENU_TYPE, 
				WizardMessages.typeLabel, toList(MENU_TYPES), "", false,
				WizardDescriptions.menuType);
	}


	/**
	 * Used in New List wizard.
	 * @return text efield editor for option value
	 */
	public static IFieldEditor createListValueEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_VALUE, WizardMessages.valueLabel, "",
				WizardDescriptions.listValue);
	}

	public static final String EDITOR_ID_ORDERED = "ordered-list"; 

	/**
	 * Used in New List context.
	 * @return checkbox field editor selected by default
	 */
	public static IFieldEditor createOrderedListEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_ORDERED, 
				WizardMessages.orderedLabel, true,
				WizardDescriptions.listOrdered);
	}

	static String[] ORDERED_LIST_TYPE_LIST = {"", "1", "A", "a", "I", "i"};

	/**
	 * Used in New List wizard.
	 * @return
	 */
	public static IFieldEditor createOrderedListTypeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(ATTR_TYPE, WizardMessages.typeLabel, toList(ORDERED_LIST_TYPE_LIST), "", false,
				WizardDescriptions.listType);
	}

	/**
	 * Used in New List wizard.
	 * @return
	 */
	public static IFieldEditor createOrderedListStartEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_START, WizardMessages.startLabel, "",
				WizardDescriptions.listStart);
	}

	/**
	 * Used in New List context.
	 * @return checkbox field editor unselected by default
	 */
	public static IFieldEditor createOrderedListReversedEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_REVERSED, 
				WizardMessages.reversedLabel, false,
				WizardDescriptions.listReversed);
	}
}
