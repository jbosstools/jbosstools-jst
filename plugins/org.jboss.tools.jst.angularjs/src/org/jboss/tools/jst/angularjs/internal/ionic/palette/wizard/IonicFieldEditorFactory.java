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

import java.util.Arrays;
import java.util.List;

import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.common.ui.widget.editor.SwtFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryFieldEditorFactory;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.WizardDescriptions;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class IonicFieldEditorFactory implements IonicConstants {

	/**
	 * Used in New Tabs and Tab wizard.
	 * @return
	 */
	public static IFieldEditor createIconEditor(String editorID) {
		return createIconEditor(editorID, IonicWizardMessages.tabIconDescription);
	}

	public static IFieldEditor createIconEditor(String editorID, String description) {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(editorID, WizardMessages.iconLabel, IonicIconFactory.getInstance().getIcons(), "", true,
				description);
	}

	/**
	 * Used in New Tab wizard.
	 * @return
	 */
	public static IFieldEditor createIconOffEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(ATTR_ICON_OFF, IonicWizardMessages.iconOffLabel, IonicIconFactory.getInstance().getIcons(), "", true,
				IonicWizardMessages.tabIconOffDescription);
	}

	/**
	 * Used in New Tab wizard.
	 * @return
	 */
	public static IFieldEditor createIconOnEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(ATTR_ICON_ON, IonicWizardMessages.iconOnLabel, IonicIconFactory.getInstance().getIcons(), "", true,
				IonicWizardMessages.tabIconOnDescription);
	}

	/**
	 * Used in New Header wizard.
	 * @return
	 */
	public static IFieldEditor createSubheaderEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(CLASS_BAR_SUBHEADER, IonicWizardMessages.subheaderLabel, false,
				"");
	}

	/**
	 * Used in New Header wizard.
	 * @return
	 */
	public static IFieldEditor createNoTapScrollEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_NO_TAP_SCROLL, IonicWizardMessages.noTapScrollLabel, false,
				IonicWizardMessages.headerNoTapScrollDescription);
	}

	static String[] BAR_COLORS = {
		"", "bar-light", "bar-stable", "bar-positive", "bar-calm", "bar-balanced", "bar-energized", "bar-assertive", "bar-royal", "bar-dark",  
	};

	static List<String> BAR_COLOR_LIST = Arrays.asList(BAR_COLORS);

	/**
	 * Used in New Header wizard.
	 * @return
	 */
	public static IFieldEditor createBarColorEditor(String editorID) {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(editorID, IonicWizardMessages.barColorLabel, BAR_COLOR_LIST, "", true,
				"");
	}

	static String[] TABS_COLORS = {
		"", "tabs-light", "tabs-stable", "tabs-positive", "tabs-calm", "tabs-balanced", "tabs-energized", "tabs-assertive", "tabs-royal", "tabs-dark",  
	};

	static List<String> TABS_COLOR_LIST = Arrays.asList(TABS_COLORS);

	/**
	 * Used in New Tabs wizard.
	 * @return
	 */
	public static IFieldEditor createTabsColorEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_TABS_COLOR, IonicWizardMessages.barColorLabel, TABS_COLOR_LIST, "", true,
				"");
	}

	static List<String> ALIGN_TITLE_LIST = Arrays.asList(new String[]{"", "left", "center", "right"});

	/**
	 * Used in New Footer and Header wizards.
	 * @return
	 */
	public static IFieldEditor createAlignTitleEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(ATTR_ALIGN_TITLE, IonicWizardMessages.alignTitleLabel, ALIGN_TITLE_LIST, "", true,
				IonicWizardMessages.headerAlignTitleDescription);
	}

	/**
	 * Used in New Footer and Header wizard.
	 * @return
	 */
	public static IFieldEditor createNgClickEditor(String editorID) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(editorID, IonicWizardMessages.ngClickLabel, "",
				IonicWizardMessages.ngClickDescription);
	}

	/**
	 * Used in Content wizard.
	 * @return
	 */
	public static IFieldEditor createDelegateHandleEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_DELEGATE_HANDLE, IonicWizardMessages.delegateHandleLabel, "",
				IonicWizardMessages.contentDelegateHandleDescription);
	}

	static List<String> DIRECTIONS = Arrays.asList(new String[]{"", "x", "y", "xy"});

	/**
	 * Used in New Content wizard.
	 * @return
	 */
	public static IFieldEditor createDirectionEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(
				ATTR_DIRECTION, IonicWizardMessages.directionLabel, 
				DIRECTIONS, "", false, IonicWizardMessages.contentDirectionDescription);
	}

	/**
	 * Used in Content wizard.
	 * @return
	 */
	public static IFieldEditor createPaddingEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(ATTR_PADDING, 
				IonicWizardMessages.paddingLabel, Arrays.asList(new String[]{"", TRUE, FALSE}),
				"", false, IonicWizardMessages.contentPaddingDescription);
	}

	/**
	 * Used in Content wizard.
	 * @return
	 */
	public static IFieldEditor createScrollEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_SCROLL, IonicWizardMessages.scrollLabel, true,
				IonicWizardMessages.contentScrollDescription);
	}

	/**
	 * Used in Content wizard.
	 * @return
	 */
	public static IFieldEditor createOverflowScrollEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_OVERFLOW_SCROLL, IonicWizardMessages.overflowScrollLabel, false,
				IonicWizardMessages.contentOverflowScrollDescription);
	}

	/**
	 * Used in Content wizard.
	 * @return
	 */
	public static IFieldEditor createScrollbarXEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_SCROLLBAR_X, IonicWizardMessages.scrollbar_xLabel, true,
				IonicWizardMessages.contentScrollbar_xDescription);
	}

	/**
	 * Used in Content wizard.
	 * @return
	 */
	public static IFieldEditor createScrollbarYEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_SCROLLBAR_Y, IonicWizardMessages.scrollbar_yLabel, true,
				IonicWizardMessages.contentScrollbar_yDescription);
	}

	/**
	 * Used in Content wizard.
	 * @return
	 */
	public static IFieldEditor createStartYEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_START_Y, IonicWizardMessages.startYLabel, "",
				IonicWizardMessages.contentStartYDescription);
	}

	/**
	 * Used in Content wizard.
	 * @return
	 */
	public static IFieldEditor createOnScrollEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_ON_SCROLL, IonicWizardMessages.onscrollLabel, "",
				IonicWizardMessages.contentScrollDescription);
	}

	/**
	 * Used in Content wizard.
	 * @return
	 */
	public static IFieldEditor createOnScrollCompleteEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_ON_SCROLL_COMPLETE, IonicWizardMessages.onscrollCompleteLabel, "",
				IonicWizardMessages.contentOnscrollCompleteDescription);
	}

	/**
	 * Used in Scroll wizard.
	 * @return
	 */
	public static IFieldEditor createHasBouncingEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(ATTR_HAS_BOUNCING, 
				IonicWizardMessages.hasBouncingLabel, Arrays.asList(new String[]{"", TRUE, FALSE}),
				"", false, IonicWizardMessages.scrollHasBouncingDescription);
	}

	/**
	 * Used in Scroll wizard.
	 * @return
	 */
	public static IFieldEditor createPagingEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_PAGING, IonicWizardMessages.pagingLabel, false,
				IonicWizardMessages.scrollPagingDescription);
	}

	/**
	 * Used in Scroll wizard.
	 * @return
	 */
	public static IFieldEditor createZoomingEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_ZOOMING, IonicWizardMessages.zoomingLabel, false,
				IonicWizardMessages.scrollZoomingDescription);
	}

	/**
	 * Used in Scroll wizard.
	 * @return
	 */
	public static IFieldEditor createOnRefreshEditor() {
		return createOnRefreshEditor(IonicWizardMessages.scrollRefreshDescription);
	}

	/**
	 * Used in Scroll and Refresher wizard.
	 * @return
	 */
	public static IFieldEditor createOnRefreshEditor(String description) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_ON_REFRESH, IonicWizardMessages.onrefreshLabel, "",
				description);
	}

	/**
	 * Used in Scroll wizard.
	 * @return
	 */
	public static IFieldEditor createMaxZoomEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_MAX_ZOOM, IonicWizardMessages.maxZoomLabel, "",
				IonicWizardMessages.scrollMaxZoomDescription);
	}

	/**
	 * Used in Scroll wizard.
	 * @return
	 */
	public static IFieldEditor createMinZoomEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_MIN_ZOOM, IonicWizardMessages.minZoomLabel, "",
				IonicWizardMessages.scrollMinZoomDescription);
	}

	/**
	 * Used in New Footer wizard.
	 * @return
	 */
	public static IFieldEditor createSubfooterEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(CLASS_BAR_SUBFOOTER, IonicWizardMessages.subfooterLabel, false,
				"");
	}

	/**
	 * Used in New Tabs wizard.
	 * @return
	 */
	public static IFieldEditor createTabsIconPositionEditor() {
		String[] values = new String[]{"", "left", "top"};
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(JQueryConstants.EDITOR_ID_ICON_POS, WizardMessages.iconposLabel, Arrays.asList(values), "", true,
				WizardDescriptions.iconPosition);
	}

	/**
	 * Used in Tabs wizard.
	 * @return
	 */
	public static IFieldEditor createHideTabsEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(CLASS_TABS_ITEM_HIDE, IonicWizardMessages.hideTabbarLabel, false,
				IonicWizardMessages.tabsHideTabbarDescription);
	}

	/**
	 * Used in New Tab wizard.
	 * @return
	 */
	public static IFieldEditor createBadgeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_BADGE, IonicWizardMessages.badgeLabel, "",
				IonicWizardMessages.tabBadgeDescription);
	}

	static String[] BADGE_COLORS = {
		"", "badge-light", "badge-stable", "badge-positive", "badge-calm", "badge-balanced", "badge-energized", "badge-assertive", "badge-royal", "badge-dark",  
	};

	static List<String> BADGE_COLOR_LIST = Arrays.asList(BADGE_COLORS);

	/**
	 * Used in New Tab wizard.
	 * @return
	 */
	public static IFieldEditor createBadgeStyleEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(ATTR_BADGE_STYLE, IonicWizardMessages.badgeStyleLabel, BADGE_COLOR_LIST, "", true,
				"");
	}

	/**
	 * Used in New Tab wizard.
	 * @return
	 */
	public static IFieldEditor createOnDeselectEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_ON_DESELECT, IonicWizardMessages.ondeselectLabel, "",
				IonicWizardMessages.tabOnDeselectDescription);
	}

	/**
	 * Used in New Tab wizard.
	 * @return
	 */
	public static IFieldEditor createOnSelectEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_ON_SELECT, IonicWizardMessages.onselectLabel, "",
				IonicWizardMessages.tabOnSelectDescription);
	}

	/**
	 * Used in Slidebox wizard.
	 * @return
	 */
	public static IFieldEditor createDoesContinueEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_DOES_CONTINUE, IonicWizardMessages.doesContinueLabel, false,
				IonicWizardMessages.slideboxDoesContinueDescription);
	}

	/**
	 * Used in Slidebox wizard.
	 * @return
	 */
	public static IFieldEditor createAutoplayEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(ATTR_AUTO_PLAY, 
				IonicWizardMessages.autoplayLabel, Arrays.asList(new String[]{"", TRUE, FALSE}),
				"", false, IonicWizardMessages.slideboxAutoplayDescription);
	}

	/**
	 * Used in New Slidebox wizard.
	 * @return
	 */
	public static IFieldEditor createSlideIntervalEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_SLIDE_INTERVAL, IonicWizardMessages.slideInterval, "",
				IonicWizardMessages.slideboxSlideIntervalDescription);
	}

	/**
	 * Used in Slidebox wizard.
	 * @return
	 */
	public static IFieldEditor createShowPagerEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_SHOW_PAGER, IonicWizardMessages.showPagerLabel, true,
				IonicWizardMessages.slideboxShowPagerDescription);
	}

	/**
	 * Used in New Slidebox wizard.
	 * @return
	 */
	public static IFieldEditor createPagerClickEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_PAGER_CLICK, IonicWizardMessages.pagerClickLabel, "",
				IonicWizardMessages.slideboxPagerClickDescription);
	}

	/**
	 * Used in New Slidebox wizard.
	 * @return
	 */
	public static IFieldEditor createOnSlideChangedEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_ON_SLIDE_CHANGED, IonicWizardMessages.onslideChangedLabel, "",
				IonicWizardMessages.slideboxOnSlideChangedDescription);
	}

	/**
	 * Used in New Slidebox wizard.
	 * @return
	 */
	public static IFieldEditor createActiveSlideEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_ACTIVE_SLIDE, IonicWizardMessages.activeSlideLabel, "",
				IonicWizardMessages.slideboxActiveSlideDescription);
	}

	/**
	 * Used in New Checkbox, Toggle wizard.
	 * @return
	 */
	public static IFieldEditor createNgTrueValueEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_NG_TRUE_VALUE, IonicWizardMessages.ngTrueValueLabel, "",
				IonicWizardMessages.checkboxNgTrueValueDescription);
	}

	/**
	 * Used in New Checkbox, Toggle wizard.
	 * @return
	 */
	public static IFieldEditor createNgModelEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_NG_MODEL, IonicWizardMessages.ngModelLabel, "",
				IonicWizardMessages.ngModelDescription);
	}

	/**
	 * Used in New Checkbox, Toggle wizard.
	 * @return
	 */
	public static IFieldEditor createNgFalseValueEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_NG_FALSE_VALUE, IonicWizardMessages.ngFalseValueLabel, "",
				IonicWizardMessages.checkboxNgFalseValueDescription);
	}

	/**
	 * Used in New Checkbox, Toggle wizard.
	 * @return
	 */
	public static IFieldEditor createNgChangeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_NG_CHANGE, IonicWizardMessages.ngChangeLabel, "",
				IonicWizardMessages.checkboxNgChangeDescription);
	}

	static String[] TOGGLE_COLORS = {
		"", "toggle-light", "toggle-stable", "toggle-positive", "toggle-calm", "toggle-balanced", "toggle-energized", "toggle-assertive", "toggle-royal", "toggle-dark",  
	};

	static List<String> TOGGLE_COLOR_LIST = Arrays.asList(TOGGLE_COLORS);

	/**
	 * Used in New Tabs wizard.
	 * @return
	 */
	public static IFieldEditor createToggleColorEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(ATTR_TOGGLE_CLASS, IonicWizardMessages.toggleClassLabel, TOGGLE_COLOR_LIST, "", true,
				"");
	}

	/**
	 * Used in Radio wizard.
	 * @return
	 */
	public static IFieldEditor createIsNgValueEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_IS_NG_VALUE, IonicWizardMessages.isNgValue, true,
				IonicWizardMessages.ngValueDescription);
	}

	/**
	 * Used in New Side menus wizard.
	 * @return
	 */
	public static IFieldEditor createDragContentEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_DRAG_CONTENT, IonicWizardMessages.dragContentLabel, true,
				IonicWizardMessages.sideMenusDragContentDescription);
	}

	static String[] THRESHOLDS = {"", TRUE, FALSE, "0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50"};

	/**
	 * Used in New Side menus wizard.
	 * @return
	 */
	public static IFieldEditor createEdgeDragThresholdEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(ATTR_EDGE_DRAG_THRESHOLD, IonicWizardMessages.edgeDragThreshold, Arrays.asList(THRESHOLDS), "", true,
				IonicWizardMessages.sideMenusEdgeDragThresholdDescription);
	}

	/**
	 * Used in New Side menus wizard.
	 * @return
	 */
	public static IFieldEditor createLeftMenuEditor() {
		return JQueryFieldEditorFactory.createCheckboxEditor(EDITOR_ID_LEFT_MENU, IonicWizardMessages.leftMenuLabel, true, 3,
				"");
	}

	/**
	 * Used in New Side menus wizard.
	 * @return
	 */
	public static IFieldEditor createRightMenuEditor() {
		return JQueryFieldEditorFactory.createCheckboxEditor(EDITOR_ID_RIGHT_MENU, IonicWizardMessages.rightMenuLabel, true, 3,
				"");
	}

	/**
	 * Used in New Side menus wizard.
	 * @return
	 */
	public static IFieldEditor createAddToggleEditor(String id) {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(id, IonicWizardMessages.addMenuToggleLable, true,
				IonicWizardMessages.sideMenuAddMenuToggleDescription);
	}

	/**
	 * Used in New Side menus wizard.
	 * @return
	 */
	public static IFieldEditor createMenuWidthEditor(String id) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(id, IonicWizardMessages.widthLabel, "",
				IonicWizardMessages.sideMenuWidthDescription);
	}

	/**
	 * Used in New Side menus wizard.
	 * @return
	 */
	public static IFieldEditor createIsEnabledEditor(String id) {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(id, IonicWizardMessages.isEnabled, true,
				IonicWizardMessages.sideMenuIsEnabledDescription);
	}

	/**
	 * Used in New Side menus wizard.
	 * @return
	 */
	public static IFieldEditor createMenuTitleEditor(String id) {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(id, WizardMessages.titleLabel, "",
				"");
	}

	/**
	 * Used in New List wizard.
	 * @return
	 */
	public static IFieldEditor createShowDeleteEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_SHOW_DELETE, IonicWizardMessages.showDeleteLabel, false,
				IonicWizardMessages.listShowDeleteDescription);
	}

	/**
	 * Used in New List wizard.
	 * @return
	 */
	public static IFieldEditor createDeleteButtonEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_DELETE_BUTTON, IonicWizardMessages.deleteButtonLabel, true,
				IonicWizardMessages.listDeleteButtonDescription);
	}

	/**
	 * Used in New List wizard.
	 * @return
	 */
	public static IFieldEditor createShowReorderEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_SHOW_REORDER, IonicWizardMessages.showReorderLabel, false,
				IonicWizardMessages.listShowReorderDescription);
	}

	/**
	 * Used in New List wizard.
	 * @return
	 */
	public static IFieldEditor createReorderButtonEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(EDITOR_ID_REORDER_BUTTON, IonicWizardMessages.reorderButtonLabel, true,
				IonicWizardMessages.listReorderButtonDescription);
	}

	/**
	 * Used in New List wizard.
	 * @return
	 */
	public static IFieldEditor createCanSwipeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(ATTR_CAN_SWIPE, IonicWizardMessages.canSwipeLabel, true,
				IonicWizardMessages.listCanSwipeDescription);
	}

	static String[] LIST_TYPES = {"", /*"card",*/ "list-inset"};

	/**
	 * Used in New List wizard.
	 * @return
	 */
	public static IFieldEditor createListTypeEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(ATTR_TYPE, WizardMessages.typeLabel, Arrays.asList(LIST_TYPES), "", true,
				IonicWizardMessages.listTypeDescription);
	}

	/**
	 * Used in New List wizard.
	 * @return
	 */
	public static IFieldEditor createOptionButtonEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(EDITOR_ID_OPTION_BUTTON, IonicWizardMessages.optionButtonLabel, "",
				"");
	}

	static String[] ITEM_STYLES = {"", CLASS_ITEM_AVATAR, CLASS_ITEM_BODY, CLASS_ITEM_PLACEHOLDER, 
		CLASS_ITEM_THUMBNAIL_LEFT, CLASS_ITEM_THUMBNAIL_RIGHT};

	/**
	 * Used in New List wizard.
	 * @return
	 */
	public static IFieldEditor createItemStyleEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_ITEM_STYLE, IonicWizardMessages.itemStyleLabel, Arrays.asList(ITEM_STYLES), "", true,
				"");
	}

	/**
	 * Used in New Button wizard.
	 * @return
	 */
	public static IFieldEditor createButtonIconPositionEditor() {
		String[] values = new String[]{"left", "right"};
		return SwtFieldEditorFactory.INSTANCE.createRadioEditor(JQueryConstants.EDITOR_ID_ICON_POS, WizardMessages.iconposLabel, Arrays.asList(values), Arrays.asList(values), "left", 
				WizardDescriptions.iconPosition);
	}

	static String[] BUTTON_COLORS = {
		"", "button-light", "button-stable", "button-positive", "button-calm", "button-balanced", "button-energized", "button-assertive", "button-royal", "button-dark",  
	};

	static List<String> BUTTON_COLOR_LIST = Arrays.asList(BUTTON_COLORS);

	/**
	 * Used in New Button wizard.
	 * @return
	 */
	public static IFieldEditor createButtonColorEditor(String editorID) {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(editorID, IonicWizardMessages.barColorLabel, BUTTON_COLOR_LIST, "", true,
				"");
	}

	/**
	 * Used in New Button wizard.

	 * @return
	 */
	public static IFieldEditor createButtonTypeEditor() {
		String[] labels = new String[]{"button", "link"};
		String[] values = new String[]{TAG_BUTTON, TAG_A};
		return SwtFieldEditorFactory.INSTANCE.createRadioEditor(EDITOR_ID_BUTTON_TYPE, IonicWizardMessages.buttonTypeLabel, 
				Arrays.asList(labels), Arrays.asList(values), TAG_BUTTON, 
				"");
	}
	/**
	 * Used in New Button wizard.
	 * @return
	 */
	public static IFieldEditor createButtonSizeEditor() {
		String[] labels = new String[]{"normal", "small", "large"};
		String[] values = new String[]{"none", "button-small", "button-large"};
		return SwtFieldEditorFactory.INSTANCE.createRadioEditor(EDITOR_ID_BUTTON_SIZE, IonicWizardMessages.buttonSizeLabel, 
				Arrays.asList(labels), Arrays.asList(values), "none", 
				"");
	}

	/**
	 * Used in New Button wizard.
	 * @return
	 */
	public static IFieldEditor createButtonWidthEditor() {
		String[] labels = new String[]{"normal", "block", "full"};
		String[] values = new String[]{"none", "button-block", "button-full"};
		
		return SwtFieldEditorFactory.INSTANCE.createRadioEditor(EDITOR_ID_BUTTON_WIDTH, IonicWizardMessages.buttonWidthLabel, 
				Arrays.asList(labels), Arrays.asList(values), "none", 
				"");
	}

	/**
	 * Used in New Button wizard.
	 * @return
	 */
	public static IFieldEditor createButtonFillEditor() {
		String[] labels = new String[]{"normal", "outline", "clear"};
		String[] values = new String[]{"none", "button-outline", "button-clear"};
		return SwtFieldEditorFactory.INSTANCE.createRadioEditor(EDITOR_ID_BUTTON_FILL, IonicWizardMessages.buttonFillLabel, 
				Arrays.asList(labels), Arrays.asList(values), "none", 
				"");
	}

	static String[] INPUT_LABEL_STYLES = {"", "inline", "stacked", "floating"};

	/**
	 * Used in New Text Input wizard.
	 * @return
	 */
	public static IFieldEditor createInputLabelStyleEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(EDITOR_ID_INPUT_LABEL_STYLE, IonicWizardMessages.inputLabelStyleLabel, Arrays.asList(INPUT_LABEL_STYLES), "inline", false,
				"");
	}

	/**
	 * Used in Refresher wizard.
	 * @return
	 */
	public static IFieldEditor createOnPullingEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_ON_PULLING, IonicWizardMessages.onpullingLabel, "",
				IonicWizardMessages.refresherOnpullingDescription);
	}

	/**
	 * Used in New Refresher wizard.
	 * @return
	 */
	public static IFieldEditor createPullingIconEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(ATTR_PULLING_ICON, IonicWizardMessages.pullingIconLabel, IonicIconFactory.getInstance().getIcons(), "", true,
				IonicWizardMessages.refresherPullingIconDescription);
	}

	/**
	 * Used in New Refresher wizard.
	 * @return
	 */
	public static IFieldEditor createRefreshingIconEditor() {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(ATTR_REFRESHING_ICON, IonicWizardMessages.refreshingIconLabel, IonicIconFactory.getInstance().getIcons(), "", true,
				IonicWizardMessages.refresherRefreshingIconDescription);
	}

	/**
	 * Used in Refresher wizard.
	 * @return
	 */
	public static IFieldEditor createPullingTextEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_PULLING_TEXT, IonicWizardMessages.pullingTextLabel, "",
				IonicWizardMessages.refresherPullingTextDescription);
	}

	/**
	 * Used in Refresher wizard.
	 * @return
	 */
	public static IFieldEditor createRefreshingTextEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_REFRESHING_TEXT, IonicWizardMessages.refreshingTextLabel, "",
				IonicWizardMessages.refresherRefreshingTextDescription);
	}

	/**
	 * Used in New Navigation wizard.
	 * @return
	 */
	public static IFieldEditor createNavViewNameEditor() {
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_NAME, WizardMessages.nameLabel, "",
				IonicWizardMessages.navviewNameDescription);
	}

	static String[] ANIMATIONS = {
		"", "fade-in", "nav-title-slide-ios7", "no-animation", "reverse",
		"slide-in-left", "slide-in-right", "slide-in-up", "slide-left-right-ios7", 
		"slide-left-right", "slide-out-left", "slide-out-right", "slide-right-left-ios7",
		"slide-right-left"
	};

	static List<String> ANIMATION_LIST = Arrays.asList(ANIMATIONS);

	/**
	 * Used in New Button wizard.
	 * @return
	 */
	public static IFieldEditor createAnimationEditor(String editorID) {
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(editorID, IonicWizardMessages.animationLabel, ANIMATION_LIST, "", false,
				"");
	}

	/**
	 * Used in New List wizard.
	 * @return
	 */
	public static IFieldEditor createAddBackButtonEditor() {
		return SwtFieldEditorFactory.INSTANCE.createCheckboxEditor(TAG_ION_NAV_BACK_BUTTON, IonicWizardMessages.addBackButton, true,
				IonicWizardMessages.navbarAddBackButtonDescription);
	}
}

