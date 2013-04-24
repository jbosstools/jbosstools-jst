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

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.common.ui.widget.editor.CheckBoxFieldEditor;
import org.jboss.tools.common.ui.widget.editor.CompositeEditor;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.common.ui.widget.editor.LabelFieldEditor;
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
		return createLabelEditor(EDITOR_ID_LABEL);
	}

	public static IFieldEditor createLabelEditor(String editorID) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(editorID, WizardMessages.labelLabel, "");
	}

	public static IFieldEditor createLegendEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_LEGEND, WizardMessages.legendLabel, "");
	}

	static String[] THEMES = {"", "a", "b", "c", "d", "e"};

	public static IFieldEditor createDataThemeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_THEME, WizardMessages.themeLabel, toList(THEMES), "", true);
	}

	public static IFieldEditor createDataTrackThemeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_TRACK_THEME, WizardMessages.trackThemeLabel, toList(THEMES), "", true);
	}

	public static IFieldEditor createDividerThemeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_DIVIDER_THEME, WizardMessages.dividerThemeLabel, toList(THEMES), "", true);
	}

	public static IFieldEditor createDataContentThemeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_CONTENT_THEME, WizardMessages.contentThemeLabel, toList(THEMES), "", true);
	}

	public static IFieldEditor createMiniEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_MINI, WizardMessages.miniLabel, false);
	}

	public static IFieldEditor createCheckedEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_CHECKED, WizardMessages.checkedLabel, false);
	}

	public static IFieldEditor createSelectedEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_SELECTED, WizardMessages.selectedLabel, false);
	}

	public static IFieldEditor createCornersEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_CORNERS, WizardMessages.cornersLabel, true);
	}

	public static IFieldEditor createNumberedEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_NUMBERED, WizardMessages.numberedLabel, false);
	}

	public static IFieldEditor createReadonlyEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_READ_ONLY, WizardMessages.readonlyLabel, false);
	}

	public static IFieldEditor createDividerEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_DIVIDER, WizardMessages.dividerLabel, false);
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

	public static IFieldEditor createCollapsibleHeaderEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_HEADER, WizardMessages.headerLabel, "");
	}

	static String[] LAYOUT_LIST = {LAYOUT_HORIZONTAL, LAYOUT_VERTICAL};

	public static IFieldEditor createLayoutEditor() {
		return SwtFieldEditorFactory.INSTANCE.createRadioEditor(
				EDITOR_ID_LAYOUT, 
				WizardMessages.layoutLabel, 
				toList(LAYOUT_LIST), 
				toList(LAYOUT_LIST), 
				LAYOUT_HORIZONTAL);
	}

	public static IFieldEditor createURLEditor() {
		return createURLEditor(EDITOR_ID_URL);
	}

	public static IFieldEditor createURLEditor(String editorID) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(editorID, WizardMessages.urlLabel, "");
	}

	public static IFieldEditor createDisabledEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_DISABLED, WizardMessages.disabledLabel, false);
	}

	public static IFieldEditor createInlineEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_INLINE, WizardMessages.inlineLabel, false);
	}

	public static IFieldEditor createCollapsedEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_COLLAPSED, WizardMessages.collapsedLabel, true);
	}

	public static IFieldEditor createFieldSetEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_FIELD_SET, WizardMessages.fieldSetLabel, true);
	}

	public static IFieldEditor createIconOnlyEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_ICON_ONLY, WizardMessages.iconOnlyLabel, false);
	}

	static String[] ICON_VALUES = {"", "arrow-l", "arrow-r", "arrow-u", "arrow-d", 
		"delete", "plus", "minus", "check", "gear", "refresh", 
		"forward", "back", "grid", "star", "alert", "info",
		"home", "search"};

	public static IFieldEditor createIconEditor() {
		return createIconEditor(EDITOR_ID_ICON);
	}

	public static IFieldEditor createIconEditor(String editorID) {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(editorID, WizardMessages.iconLabel, toList(ICON_VALUES), "", true);
	}

	public static IFieldEditor createCollapsedIconEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_COLLAPSED_ICON, WizardMessages.collapsedIconLabel, toList(ICON_VALUES), "", true);
	}

	public static IFieldEditor createExpandedIconEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_EXPANDED_ICON, WizardMessages.expandedIconLabel, toList(ICON_VALUES), "", true);
	}

	public static IFieldEditor createIconPositionEditor() {
		String[] values = new String[]{"", "left", "right", "top", "bottom"};
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

	public static IFieldEditor createRangeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_RANGE, WizardMessages.rangeLabel, false);
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

	static String[] CLOSE_BUTTON_LIST = {CLOSE_LEFT, CLOSE_RIGHT, CLOSE_NONE};

	//For Dialog
	public static IFieldEditor createCloseButtonEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				EDITOR_ID_CLOSE_BUTTON, 
				WizardMessages.closeButtonPositionLabel, 
				toList(CLOSE_BUTTON_LIST), CLOSE_LEFT, false);
	}

	static String[] CLOSE_POPUP_BUTTON_LIST = {CLOSE_NONE, CLOSE_LEFT, CLOSE_RIGHT};

	public static IFieldEditor createClosePopupButtonEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				EDITOR_ID_CLOSE_BUTTON, 
				WizardMessages.closeButtonPositionLabel, 
				toList(CLOSE_POPUP_BUTTON_LIST), CLOSE_NONE, false);
	}

	public static IFieldEditor createMinEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_MIN, WizardMessages.minLabel, "0");
	}

	public static IFieldEditor createMaxEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_MAX, WizardMessages.maxLabel, "100");
	}

	public static IFieldEditor createStepEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_STEP, WizardMessages.stepLabel, "");
	}

	public static IFieldEditor createValueEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_VALUE, WizardMessages.valueLabel, "");
	}

	public static IFieldEditor createRightValueEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_RVALUE, WizardMessages.rightValueLabel, "60");
	}

	public static IFieldEditor createHighlightEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_HIGHLIGHT, WizardMessages.highlightLabel, true);
	}

	public static IFieldEditor createHideLabelEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_HIDE_LABEL, WizardMessages.hideLabelLabel, false);
	}

	static List<String> toList(String[] values) {
		List<String> list = new ArrayList<String>();
		for (String s: values) list.add(s);
		return list;
	}

	public static IFieldEditor createAddHeaderEditor() {
		return createCheckboxEditor(EDITOR_ID_ADD_HEADER, WizardMessages.headerLabel, true, 1);
	}

	public static IFieldEditor createAddFooterEditor() {
		return createCheckboxEditor(EDITOR_ID_ADD_FOOTER, WizardMessages.footerLabel, true, 1);
	}

	public static IFieldEditor createHeaderTitleEditor() {
		return createTextEditor(EDITOR_ID_HEADER_TITLE, "Page Title", 2);
	}

	public static IFieldEditor createFooterTitleEditor() {
		return createTextEditor(EDITOR_ID_FOOTER_TITLE, "Page Footer", 2);
	}

	/**
	 * Creates checkbox editor with label as text at checkbox.
	 * This can serve as enabling label for a text editor.
	 * 
	 * @param name
	 * @param label
	 * @param defaultValue
	 * @param span
	 * @return
	 */
	public static IFieldEditor createCheckboxEditor(String name, final String label, boolean defaultValue, final int span) {
		return new CheckBoxFieldEditor(name,label,Boolean.valueOf(defaultValue)){
			public void doFillIntoGrid(Object parent) {
				Composite c = (Composite) parent;
				final Control[] controls = (Control[]) getEditorControls(c);
				Button button = (Button)controls[0];
				button.setText(label);
				GridData d = new GridData();
				d.horizontalSpan = span;
				button.setLayoutData(d);
			}
		};
	}

	/**
	 * Creates text editor without label.
	 * 
	 * @param name
	 * @param defaultValue
	 * @param span
	 * @return
	 */
	public static IFieldEditor createTextEditor(String name, String defaultValue, final int span) {
		return new TextFieldEditor(name,"",defaultValue) {
			public void doFillIntoGrid(Object parent) {
				Composite c = (Composite) parent;
				final Control[] controls = (Control[]) getEditorControls(c);
				Text text = (Text)controls[0];
				GridData d = new GridData(GridData.FILL_HORIZONTAL);
				d.horizontalSpan = span;
				text.setLayoutData(d);
			}
		};
	}

	/**
	 * Creates an invisible placeholder.
	 * 
	 * @param name
	 * @param span
	 * @return
	 */
	public static IFieldEditor createSpan(String name, final int span) {
		return new LabelFieldEditor(name, "X") {
			public void doFillIntoGrid(Object parent) {
				Composite c = (Composite) parent;
				Control[] controls = (Control[]) getEditorControls(c);
				Control text = controls[0];
				GridData d = new GridData();
				controls[0].setVisible(false);
				d.horizontalSpan = span;
				text.setLayoutData(d);
			}
		};
	}

	public static IFieldEditor createClearInputEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_CLEAR_INPUT, WizardMessages.clearInputLabel, true);
	}

	static String[] TEXT_TYPES = {
		TYPE_TEXT, TYPE_TEXTAREA, TYPE_SEARCH, TYPE_PASSWORD, TYPE_NUMBER,
		TYPE_FILE, TYPE_URL, TYPE_EMAIL, TYPE_TEL,
		TYPE_DATE, TYPE_TIME, TYPE_DATETIME, TYPE_MONTH, TYPE_WEEK,
		TYPE_COLOR
	};

	public static IFieldEditor createTextTypeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				EDITOR_ID_TEXT_TYPE, 
				WizardMessages.textTypeLabel, 
				toList(TEXT_TYPES), TYPE_TEXT, false);
	}

	public static IFieldEditor createPlaceholderEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_PLACEHOLDER, WizardMessages.placeholderLabel, "");
	}

	public static IFieldEditor createFixedPositionEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_FIXED_POSITION, WizardMessages.fixedPositionLabel, false);
	}

	public static IFieldEditor createFullScreenEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_FULL_SCREEN, WizardMessages.fullScreenLabel, false);
	}

	public static IFieldEditor createLeftButtonEditor() {
		return createCheckboxEditor(EDITOR_ID_LEFT_BUTTON, WizardMessages.leftButtonLabel, true, 3);
	}

	public static IFieldEditor createRightButtonEditor() {
		return createCheckboxEditor(EDITOR_ID_RIGHT_BUTTON, WizardMessages.rightButtonLabel, true, 3);
	}

	public static IFieldEditor createItemsNumberEditor(String label, int min, int max) {
		String[] numbers = new String[max - min + 1];
		for (int i = min; i <= max; i++) {
			numbers[i - min] = "" + i;
		}
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_NUMBER_OF_ITEMS, WizardMessages.numberOfItemsLabel, toList(numbers), "3", false);
	}

	public static IFieldEditor createArragementEditor() {
		String[] values = new String[]{ARRAGEMENT_DEFAULT, ARRAGEMENT_GROUPED, ARRAGEMENT_NAVBAR};
		return SwtFieldEditorFactory.INSTANCE.createRadioEditor(EDITOR_ID_ARRAGEMENT, WizardMessages.arragementLabel, 
				toList(values), toList(values), values[0]);
	}

	public static IFieldEditor createGridColumnsEditor() {
		String[] values = new String[]{"1", "2", "3", "4", "5"};
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_GRID_COLUMNS, WizardMessages.gridColumnsLabel, 
				toList(values), "3", false);
	}

	public static IFieldEditor createGridRowsEditor() {
		String[] values = new String[]{"1", "2", "3", "4", "5"};
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_GRID_ROWS, WizardMessages.gridRowsLabel, 
				toList(values), "3", false);
	}

	public static IFieldEditor createDismissableEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_DISMISSABLE, WizardMessages.dismissableLabel, true);
	}

	public static IFieldEditor createShadowEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_SHADOW, WizardMessages.shadowLabel, true);
	}

	public static IFieldEditor createPaddingEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_PADDING, WizardMessages.paddingLabel, true);
	}

	public static IFieldEditor createOverlayEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_OVERLAY, WizardMessages.overlayLabel, false);
	}

	public static IFieldEditor createInfoStyledEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_INFO_STYLED, WizardMessages.infoStyledLabel, false);
	}

	public static IFieldEditor createPositionToEditor() {
		String[] values = new String[]{"", POSITION_TO_WINDOW, POSITION_TO_ORIGIN};
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_POSITION_TO, WizardMessages.positionToLabel, 
				toList(values), "", true);
	}

	static String[] PANEL_POSITION_LIST = {POSITION_LEFT, POSITION_RIGHT};

	public static IFieldEditor createPanelPositionEditor() {
		return SwtFieldEditorFactory.INSTANCE.createRadioEditor(
				EDITOR_ID_PANEL_POSITION, 
				WizardMessages.panelPositionLabel, 
				toList(PANEL_POSITION_LIST), 
				toList(PANEL_POSITION_LIST), 
				POSITION_LEFT);
	}

	static String[] PANEL_DISPLAY_LIST = {DISPLAY_OVERLAY, DISPLAY_REVEAL, DISPLAY_PUSH};

	public static IFieldEditor createPanelDisplayEditor() {
		return SwtFieldEditorFactory.INSTANCE.createRadioEditor(
				EDITOR_ID_DISPLAY, 
				WizardMessages.displayLabel, 
				toList(PANEL_DISPLAY_LIST), 
				toList(PANEL_DISPLAY_LIST), 
				DISPLAY_REVEAL);
	}

	public static IFieldEditor createSwipeCloseEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_SWIPE_CLOSE, WizardMessages.swipeCloseLabel, true);
	}

}

