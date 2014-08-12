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
		return SwtFieldEditorFactory.INSTANCE.createComboEditor(editorID, WizardMessages.iconLabel, IonicIconFactory.getInstance().getIcons(), "", true,
				IonicWizardMessages.tabIconDescription);
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
		return SwtFieldEditorFactory.INSTANCE.createTextEditor(ATTR_ON_REFRESH, IonicWizardMessages.onrefreshLabel, "",
				IonicWizardMessages.scrollRefreshDescription);
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

}

