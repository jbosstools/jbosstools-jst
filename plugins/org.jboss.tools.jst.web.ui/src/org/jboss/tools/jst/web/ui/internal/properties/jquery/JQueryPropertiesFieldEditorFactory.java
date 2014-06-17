package org.jboss.tools.jst.web.ui.internal.properties.jquery;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.common.ui.widget.editor.SwtFieldEditorFactory;
import org.jboss.tools.jst.web.html.JQueryHTMLConstants;
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileVersion;
import org.jboss.tools.jst.web.ui.internal.properties.html.HTMLFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.WizardDescriptions;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class JQueryPropertiesFieldEditorFactory extends HTMLFieldEditorFactory implements JQueryHTMLConstants {

	static String[] ALL_DATA_ROLES = new String[]{"", 
		ROLE_BUTTON, ROLE_COLLAPSIBLE, ROLE_COLLAPSIBLE_SET,
		ROLE_CONTENT, ROLE_CONTROLGROUP, ROLE_DIALOG,
		ROLE_DIVIDER, ROLE_FIELDCONTAIN, ROLE_FOOTER,
		ROLE_GROUP, ROLE_HEADER, ROLE_LISTVIEW,
		ROLE_NAVBAR, ROLE_PAGE, ROLE_PANEL,
		ROLE_POPUP, ROLE_RANGE_SLIDER,
		ROLE_SLIDER, ROLE_TABLE
	};
	static String[] DIV_DATA_ROLES = new String[]{"", 
		ROLE_COLLAPSIBLE, ROLE_COLLAPSIBLE_SET,
		ROLE_CONTENT, ROLE_CONTROLGROUP, ROLE_DIALOG,
		ROLE_FIELDCONTAIN, ROLE_FOOTER,	ROLE_HEADER, ROLE_NAVBAR, 
		ROLE_PAGE, ROLE_PANEL, ROLE_POPUP
	};
	static String[] INPUT_DATA_ROLES = new String[]{"", ROLE_NONE};
	static String[] A_DATA_ROLES = new String[]{"",	ROLE_BUTTON};
	static String[] TABLE_DATA_ROLES = new String[]{"",	ROLE_TABLE};
	static String[] LIST_DATA_ROLES = new String[]{"",	ROLE_LISTVIEW};
	static String[] LI_DATA_ROLES = new String[]{"",	ROLE_DIVIDER};
	static String[] SELECT_DATA_ROLES = new String[]{"",	ROLE_SLIDER};

	public static IFieldEditor createDataRoleEditor(String elementName) {
		String[] values = TAG_DIV.equalsIgnoreCase(elementName) ? DIV_DATA_ROLES
				: TAG_INPUT.equalsIgnoreCase(elementName) ? INPUT_DATA_ROLES
				: TAG_TEXTAREA.equalsIgnoreCase(elementName) ? INPUT_DATA_ROLES
				: TAG_A.equalsIgnoreCase(elementName) ? A_DATA_ROLES
				: TAG_BUTTON.equalsIgnoreCase(elementName) ? A_DATA_ROLES
				: TAG_TABLE.equalsIgnoreCase(elementName) ? TABLE_DATA_ROLES
				: (TAG_UL.equalsIgnoreCase(elementName) || TAG_OL.equalsIgnoreCase(elementName)) ? LIST_DATA_ROLES				
				: TAG_LI.equalsIgnoreCase(elementName) ? LI_DATA_ROLES
				: TAG_SELECT.equalsIgnoreCase(elementName) ? SELECT_DATA_ROLES
				: ALL_DATA_ROLES;
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(ATTR_DATA_ROLE, "Data Role:", toList(values), "", false,
		"");
	}

	static String[] THEMES = JQueryFieldEditorFactory.THEMES;
	static String[] THEMES_1_4 = JQueryFieldEditorFactory.THEMES_1_4;
	static String[] POPUP_THEMES = JQueryFieldEditorFactory.POPUP_THEMES;
	static String[] POPUP_THEMES_1_4 = JQueryFieldEditorFactory.POPUP_THEMES_1_4;

	static String[] getThemes(JQueryMobileVersion version) {
		return (version == JQueryMobileVersion.JQM_1_3) ? THEMES : THEMES_1_4;
	}

	static String[] getPopupThemes(JQueryMobileVersion version) {
		return (version == JQueryMobileVersion.JQM_1_3) ? POPUP_THEMES : POPUP_THEMES_1_4;
	}

	/**
	 * Used in all jQuery Mobile widgets.
	 * @return
	 */
	public static IFieldEditor createDataThemeEditor(JQueryMobileVersion version, String role) {
		String[] values = ROLE_POPUP.equals(role) ? getPopupThemes(version) : getThemes(version);
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				ATTR_DATA_THEME, WizardMessages.themeLabel, toList(values), "", true, 
				"");
	}

	/**
	 * Used in Popup.
	 * @param description
	 * @return
	 */
	public static IFieldEditor createOverlayThemeEditor(JQueryMobileVersion version) {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				ATTR_DATA_OVERLAY_THEME, WizardMessages.overlayLabel, toList(getThemes(version)), "", true, 
				WizardDescriptions.popupOverlay);
	}

	/**
	 * Used in Table.
	 * @param description
	 * @return
	 */
	public static IFieldEditor createColumnButtonThemeEditor(JQueryMobileVersion version) {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				ATTR_DATA_COLUMN_BUTTON_THEME, WizardMessages.columnButtonThemeLabel, toList(getThemes(version)), "", true, 
				"");
	}

	/**
	 * Used in Table.
	 * @param description
	 * @return
	 */
	public static IFieldEditor createColumnPopupThemeEditor(JQueryMobileVersion version) {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				ATTR_DATA_COLUMN_POPUP_THEME, WizardMessages.columnPopupThemeLabel, toList(getPopupThemes(version)), "", true, 
				"");
	}

	/**
	 * Used in Listview.
	 * @param description
	 * @return
	 */
	public static IFieldEditor createThemeEditorForID(String id) {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				id, WizardMessages.themeLabel, toList(THEMES), "", true, 
			"");
	}

	/**
	 * Used in Popup and Panel.
	 * @param description
	 * @return
	 */
	public static IFieldEditor createDismissableEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_DATA_DISMISSABLE, "Dismissable:", true,
				description);
	}

	/**
	 * Popup
	 * @return
	 */
	public static IFieldEditor createShadowEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_DATA_SHADOW, WizardMessages.shadowLabel, true,
				WizardDescriptions.popupShadow);
	}

	/**
	 * Used in many components.
	 * @return
	 */
	public static IFieldEditor createMiniEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_DATA_MINI, WizardMessages.miniLabel, false,
				WizardDescriptions.widgetMini);
	}

	/**
	 * Used in Button and Select Menu.
	 * @return
	 */
	public static IFieldEditor createInlineEditor() {
		return JQueryFieldEditorFactory.createInlineEditor();
	}

	/**
	 * Used in Button, Popup and Select Menu.
	 * @return
	 */
	public static IFieldEditor createCornersEditor() {
		return JQueryFieldEditorFactory.createCornersEditor();
	}

	/**
	 * Used in Popup.
	 * @return
	 */
	public static IFieldEditor createPositionToEditor() {
		return JQueryFieldEditorFactory.createPositionToEditor();
	}

	/**
	 * Used in Button and Link.
	 * @return
	 */
	public static IFieldEditor createActionEditor() {
		String[] values = new String[]{"", 
				DATA_REL_DIALOG, 
				DATA_REL_POPUP,
				DATA_REL_BACK,
				DATA_REL_CLOSE,
				DATA_REL_EXTERNAL};
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(ATTR_DATA_REL, "Data Rel:", toList(values), "", false,
				"");
	}

	/**
	 * Used in Link button.
	 * @return
	 */
	public static IFieldEditor createTransitionEditor(String description) {
		return JQueryFieldEditorFactory.createTransitionEditor(description);
	}

	/**
	 * Used in Dialog and Page
	 * @return
	 */
	public static IFieldEditor createCloseButtonTextEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_DATA_CLOSE_BTN_TEXT, "Close Button Text:", "",
				"");
	}

	/**
	 * Used in Dialog and Page
	 * @return
	 */
	public static IFieldEditor createDataTitleEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_DATA_TITLE, "Title:", "",
				"");
	}

	/**
	 * Used in Page.
	 * @return
	 */
	public static IFieldEditor createAddBackButton() {
		return JQueryFieldEditorFactory.createBackButtonEditor();
	}


	/**
	 * Used in Page.
	 * @return
	 */
	public static IFieldEditor createBackButtonThemeEditor(JQueryMobileVersion version) {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				ATTR_DATA_BACK_BUTTON_THEME, WizardMessages.themeLabel, toList(getThemes(version)), "", true, 
				"");
	}

	/**
	 * Used in Page.
	 * @return
	 */
	public static IFieldEditor createBackButtonTextEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_DATA_BACK_BUTTON_TEXT, "Text:", "",
				"");
	}

	/**
	 * Used in Header and Footer.
	 * @return
	 */
	public static IFieldEditor createFixedPositionEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_DATA_POSITION, WizardMessages.fixedPositionLabel, false,
				description);
	}

	/**
	 * Used in Panel.
	 * @return
	 */
	public static IFieldEditor createFixedPositionEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_DATA_POSITION_FIXED, WizardMessages.fixedPositionLabel, false,
				WizardDescriptions.panelFixedPosition);
	}

	static List<String> toList(String[] values) {
		List<String> list = new ArrayList<String>();
		for (String s: values) list.add(s);
		return list;
	}

	/**
	 * Used in Button.
	 * @return
	 */
	public static IFieldEditor createIconShadowEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_DATA_ICON_SHADOW, WizardMessages.iconShadowLabel, true,
				WizardDescriptions.iconShadow);
	}

	/**
	 * Used in Link.
	 * @return
	 */
	public static IFieldEditor createReverseEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_DATA_DIRECTION, WizardMessages.transitionReverseLabel, false,
				WizardDescriptions.transitionReverse);
	}

	/**
	 * Used in Link.
	 * @return
	 */
	public static IFieldEditor createPrefetchEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_DATA_PREFETCH, WizardMessages.prefetchLabel, false,
				WizardDescriptions.prefetch);
	}

	/**
	 * Used in Link and containers.
	 * @return
	 */
	public static IFieldEditor createAjaxEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_DATA_AJAX, WizardMessages.ajaxLabel, false,
				WizardDescriptions.ajax);
	}

	/**
	 * Used in Link, Page and Dialog.
	 * @return
	 */
	public static IFieldEditor createDomCacheEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_DATA_DOM_CACHE, WizardMessages.domCacheLabel, false,
				WizardDescriptions.domCache);
	}

	/**
	 * Used in containers.
	 * @return
	 */
	public static IFieldEditor createEnhance() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_DATA_ENHANCE, WizardMessages.enhanceLabel, false,
				WizardDescriptions.enhance);
	}

	/**
	 * Used in Select.
	 * @return
	 */
	public static IFieldEditor createNativeMenuEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_DATA_NATIVE_MENU, WizardMessages.nativeMenuLabel, true,
				WizardDescriptions.nativeMenu);
	}

	/**
	 * Used in Select.
	 * @return
	 */
	public static IFieldEditor createDataPlaceholderEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_DATA_PLACEHOLDER, WizardMessages.placeholderLabel, false,
				WizardDescriptions.dataPlaceholder);
	}

	/**
	 * Used in Listview.
	 * @return
	 */
	public static IFieldEditor createFilterPlaceholderEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_DATA_FILTER_PLACEHOLDER, WizardMessages.placeholderLabel, "",
			"");
	}

	/**
	 * Used in fixed toolbars.
	 * @return
	 */
	public static IFieldEditor createDataIDEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_DATA_ID, WizardMessages.dataIDLabel, "",
				WizardDescriptions.dataID);
	}

	/**
	 * Used in Page.
	 * @return
	 */
	public static IFieldEditor createDataURLEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_DATA_URL, WizardMessages.dataURLLabel, "",
				WizardDescriptions.dataURL);
	}

	/**
	 * Used in Page.
	 * @return
	 */
	public static IFieldEditor createToleranceEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_DATA_TOLERANCE, WizardMessages.toleranceLabel, "",
				WizardDescriptions.pageTolerance);
	}

	/**
	 * Used in Table.
	 * @return
	 */
	public static IFieldEditor createColumnButtonTextEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_DATA_COLUMN_BUTTON_TEXT, WizardMessages.columnButtonTextLabel, "",
				"");
	}

	static String[] DATA_TYPE_LABLE_LIST = {WizardMessages.dataTypeHorizontalLabel, WizardMessages.dataTypeVerticalLabel};
	static String[] DATA_TYPE_LIST = {DATA_TYPE_HORIZONTAL, DATA_TYPE_VERTICAL};

	public static IFieldEditor createDataTypeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createRadioEditor(
				ATTR_DATA_TYPE, 
				WizardMessages.layoutLabel, 
				toList(DATA_TYPE_LABLE_LIST), 
				toList(DATA_TYPE_LIST), 
				DATA_TYPE_VERTICAL,
				WizardDescriptions.widgetLayout);
	}

	/**
	 * Used in Range Slider.
	 * @return
	 */
	public static IFieldEditor createHighlightEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_DATA_HIGHLIGHT, WizardMessages.highlightLabel, false,
				WizardDescriptions.rangeSliderHighlight);
	}

	/**
	 * Used in Page.
	 * @return
	 */
	public static IFieldEditor createClearButtonTextEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_DATA_CLEAR_BTN_TEXT, WizardMessages.clearButtonTextLable, "",
				"");
	}

}
