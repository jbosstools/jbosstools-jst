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
package org.jboss.tools.jst.web.kb.internal.taglib.html.jq;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttribute;
import org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttributeProvider;

/**
 * @author Alexey Kazakov
 */
public abstract class JQueryMobileAttrProvider extends HtmlAttributeProvider {

	protected static final String DATA_ROLE = "data-role";

	protected static final AttributeData DATA_ROLE_HEADER = new AttributeData(DATA_ROLE, HEADER);
	protected static final AttributeData DATA_ROLE_FOOTER = new AttributeData(DATA_ROLE, FOOTER);
	protected static final AttributeData DATA_ROLE_NAVBAR = new AttributeData(DATA_ROLE, NAVBAR);
	protected static final AttributeData DATA_ROLE_LISTVIEW = new AttributeData(DATA_ROLE, LISTVIEW);
	protected static final AttributeData DATA_ROLE_PANEL = new AttributeData(DATA_ROLE, PANEL);

	protected static final AttributeData DATA_POSITION_FIXED = new AttributeData("data-position", "fixed");

	protected static final AttributeData DATA_REL_POPUP = new AttributeData("data-rel", "popup");

	protected static final AttributeData DATA_ROLE_TABLE = new AttributeData(DATA_ROLE, TABLE);
	
	public static final String[] ENUM_ICON_VALUES = new String[] { 
			"alert", "arrow-d", "arrow-l", "arrow-r", "arrow-u", "back", "bars",
			"check", "custom", "delete", "forward", "gear", "grid", "home", 
			"info", "minus", "plus", "refresh", "search", "star"};
	public static final String[] ENUM_THEME = new String[] { "a", "b",
			"c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
			"p", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
	public static final String[] DATA_REL_ENUM = new String[]{"back", "dialog", "external", "popup", "close"};
	public static final String[] DATA_MODE_ENUM = new String[]{"columntoggle", "reflow"};
	public static final String[] DATA_PRIORITY_ENUM = new String[]{"1", "2", "3", "4", "5", "6"};

	public static final String[] DATA_POSITION_ENUM = new String[]{"left", "right"};
	public static final String[] DATA_DISPLAY_ENUM = new String[]{"reveal", "overlay", "push"};

	protected static final HtmlAttribute DATA_CORNERS_ATTRIBUTE = new HtmlAttribute("data-corners", "", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_ICON_ATTRIBUTE = new HtmlAttribute("data-icon", "", ENUM_ICON_VALUES);
	protected static final HtmlAttribute DATA_ICONPOS_ATTRIBUTE = new HtmlAttribute("data-iconpos", "", new String[]{"left", "right",	"top", "bottom", "notext"});
	protected static final HtmlAttribute DATA_ICONSHADOW_ATTRIBUTE = new HtmlAttribute("data-iconshadow", "", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_INLINE_ATTRIBUTE = new HtmlAttribute("data-inline", "", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_MINI_ATTRIBUTE = new HtmlAttribute("data-mini", "Compact sized version", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_SHADOW_ATTRIBUTE = new HtmlAttribute("data-shadow", "", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_THEME_ATTRIBUTE = new HtmlAttribute("data-theme", "", ENUM_THEME);
	protected static final HtmlAttribute DATA_INPUT_THEME_ATTRIBUTE = new HtmlAttribute("data-theme", "swatch letter (a-z) - Added to the form element", ENUM_THEME);

	protected static final HtmlAttribute DATA_ROLE_NONE_ATTRIBUTE = new DataRoleAttribute("Prevents auto-enhancement to use native control");
	protected static final HtmlAttribute DATA_ROLE_LIST_ATTRIBUTE = new HtmlAttribute("data-role", "", new String[]{"list-divider"});
	protected static final HtmlAttribute DATA_COLLAPSED_ATTRIBUTE = new HtmlAttribute("data-collapsed", "", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_COLLAPSED_ICON_ATTRIBUTE = new HtmlAttribute("data-collapsed-icon", "", ENUM_ICON_VALUES);
	protected static final HtmlAttribute DATA_CONTENT_THEME_ATTRIBUTE = new HtmlAttribute("data-content-theme", "", ENUM_THEME);
	protected static final HtmlAttribute DATA_EXPANDED_ICON_ATTRIBUTE = new HtmlAttribute("data-expanded-icon", "", ENUM_ICON_VALUES);
	protected static final HtmlAttribute DATA_INSET_ATTRIBUTE = new HtmlAttribute("data-inset", "", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_TYPE_ATTRIBUTE = new HtmlAttribute("data-type", "For horizontal or vertical item alignment", new String[]{"horizontal", "vertical"});
	protected static final HtmlAttribute DATA_CLOSE_BTN_ATTRIBUTE = new HtmlAttribute("data-close-btn", "", new String[]{"left", "right", "none"});
	protected static final HtmlAttribute DATA_CLOSE_BTN_TEXT_ATTRIBUTE = new HtmlAttribute("data-close-btn-text", "Text for the close button, dialog only", new String[]{});
	protected static final HtmlAttribute DATA_DOM_CACHE_ATTRIBUTE = new HtmlAttribute("data-dom-cache", "", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_OVERLAY_THEME_ATTRIBUTE = new HtmlAttribute("data-overlay-theme", "", ENUM_THEME);
	protected static final HtmlAttribute DATA_TITLE_ATTRIBUTE = new HtmlAttribute("data-title", "Title used when page is shown", new String[]{});
	protected static final HtmlAttribute DATA_ENHANCE_ATTRIBUTE = new HtmlAttribute("data-enhance", "Any DOM elements inside a data-enhance=\"false\" container, save for data-role=\"page|dialog\" elements, will be ignored during initial enhancement and subsequent create events provided that the $.mobile.ignoreContentEnabled flag is set prior to the enhancement (eg in a mobileinit binding)", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_AJAX_ATTRIBUTE = new HtmlAttribute("data-ajax", "Any link or form element inside data-ajax=\"false\" containers will be ignored by the framework's navigation functionality when $.mobile.ignoreContentEnabled is set to true", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_DISABLED_PAGE_ZOOM_ATTRIBUTE = new HtmlAttribute("data-disable-page-zoom", "User-scaling-ability for pages with fixed toolbars", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_FULLSCREEN_ATTRIBUTE = new HtmlAttribute("data-fullscreen", "Setting toolbars over the page-content", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_TAP_TOGGLE_ATTRIBUTE = new HtmlAttribute("data-tap-toggle", "Ability to toggle toolbar-visibility on user tap/click", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute FIXED_TOOLBAR_DATA_TRANSITION_ATTRIBUTE = new HtmlAttribute("data-transition", "Show/hide-transition when a tap/click occurs", new String[]{"slide", "fade", "none"});
	protected static final HtmlAttribute DATA_UPDATE_PAGE_PADDING_ATTRIBUTE = new HtmlAttribute("data-update-page-padding", "Have the page top and bottom padding updated on resize, transition, \"updatelayout\" events (the framework always updates the padding on the \"pageshow\" event).", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_VISIBLE_ON_PAGE_SHOW_ATTRIBUTE = new HtmlAttribute("data-visible-on-page-show", "Toolbar-visibility when parent page is shown", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_TRACK_THEME_ATTRIBUTE = new HtmlAttribute("data-track-theme", "swatch letter (a-z) - Added to the form element", ENUM_THEME);
	protected static final HtmlAttribute DATA_ID_ATTRIBUTE = new HtmlAttribute("data-id", "Unique ID. Required for persistent footers", new String[]{});
	protected static final HtmlAttribute DATA_POSITION_ATTRIBUTE = new HtmlAttribute("data-position", "", new String[]{"fixed"});
	protected static final HtmlAttribute PANEL_DATA_POSITION_ATTRIBUTE = new HtmlAttribute("data-position", "The position of the panel on the screen.  The default value of the attribute is left, meaning it will appear from the left edge of the screen. Specify data-position=\"right\" for it to appear from the right edge instead", DATA_POSITION_ENUM);
	protected static final HtmlAttribute DATA_DISPLAY_ATTRIBUTE = new HtmlAttribute("data-display", "The display mode of the panel.  The value of the attribute defaults to reveal, meaning the panel will sit under the page and reveal as the page slides away. Specify data-display=\"overlay\" for the panel to appear on top of the page contents. A third mode, data-display=\"push\" animates both the panel and page at the same time.", DATA_DISPLAY_ENUM);
	protected static final HtmlAttribute DATA_POSITION_FIXED_ATTRIBUTE = new HtmlAttribute("data-position-fixed", "\"false\" is the default value of this attribute", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_SWIPE_CLOSE_ATTRIBUTE = new HtmlAttribute("data-swipe-close", "Clicking the link that opened the panel, swiping left or right, or tapping the Esc key will close the panel. To turn off the swipe-to-close behavior, add the data-swipe-close=\"false\" attribute to the panel. \"right\" is the default value of this attribute", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_DIRECTION_ATTRIBUTE = new HtmlAttribute("data-direction", "Reverse transition animation (only for page or dialog)", new String[]{"reverse"});
	protected static final HtmlAttribute DATA_PREFETCH_ATTRIBUTE = new HtmlAttribute("data-prefetch", "", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_REL_ATTRIBUTE = new HtmlAttribute("data-rel", "<b>back</b> - To move one step back in history<br/><b>dialog</b> - To open link styled as dialog, not tracked in history<br/><b>external</b> - For linking to another domain<br/><b>popup</b> - For opening a popup", DATA_REL_ENUM, new String[]{"To move one step back in history", "To open link styled as dialog, not tracked in history", "For linking to another domain", "For opening a popup", "For closing a panel"});
	protected static final HtmlAttribute LINK_DATA_TRANSITION_ATTRIBUTE = new HtmlAttribute("data-transition", "", new String[]{"fade", "flip", "flow", "pop", "slide", "slidedown", "slidefade", "slideup", "turn", "none"});
	protected static final HtmlAttribute DATA_POSITION_TO_ATTRIBUTE = new HtmlAttribute("data-position-to", "<b>origin</b> - Centers the popup over the link that opens it jQuery<br/><b>JQuery selector</b> - Centers the popup over the specified element<br/><b>window</b> - Centers the popup in the window", new String[]{"origin", "window"}, new String[]{"Centers the popup over the link that opens it jQuery", "Centers the popup in the window"});
	protected static final HtmlAttribute DATA_AUTODIVIDERS_ATTRIBUTE = new HtmlAttribute("data-autodividers", "", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_COUNT_THEME_ATTRIBUTE = new HtmlAttribute("data-count-theme", "", ENUM_THEME);
	protected static final HtmlAttribute DATA_DIVIDER_THEME_ATTRIBUTE = new HtmlAttribute("data-divider-theme", "", ENUM_THEME);
	protected static final HtmlAttribute DATA_SELECT_DIVIDER_THEME_ATTRIBUTE = new HtmlAttribute("data-divider-theme", "swatch letter (a-z) - Default <b>\"b\"</b> - Only applicable if <i>optgroup</i> support is used in non-native selects", ENUM_THEME);
	protected static final HtmlAttribute DATA_FILTER_ATTRIBUTE = new HtmlAttribute("data-filter", "", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_FILTER_PLACEHOLDER_ATTRIBUTE = new HtmlAttribute("data-filter-placeholder", "", new String[]{});
	protected static final HtmlAttribute DATA_FILTER_THEME_ATTRIBUTE = new HtmlAttribute("data-filter-theme", "", ENUM_THEME);
	protected static final HtmlAttribute DATA_HEADER_THEME_ATTRIBUTE = new HtmlAttribute("data-header-theme", "", ENUM_THEME);
	protected static final HtmlAttribute DATA_SPLIT_ICON_ATTRIBUTE = new HtmlAttribute("data-split-icon", "", ENUM_ICON_VALUES);
	protected static final HtmlAttribute DATA_SPLIT_THEME_ATTRIBUTE = new HtmlAttribute("data-split-theme", "", ENUM_THEME);
	protected static final HtmlAttribute DATA_FILTERTEXT_ATTRIBUTE = new HtmlAttribute("data-filtertext", "Filter by this value instead of inner text", new String[]{});
	protected static final HtmlAttribute DATA_ADD_BACK_BTN_ATTRIBUTE = new HtmlAttribute("data-add-back-btn", "Auto add back button, header only", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_BACK_BTN_TEXT_ATTRIBUTE = new HtmlAttribute("data-back-btn-text", "", new String[]{});
	protected static final HtmlAttribute DATA_BACK_BTN_THEME_ATTRIBUTE = new HtmlAttribute("data-back-btn-theme", "", ENUM_THEME);
	protected static final HtmlAttribute DATA_URL_ATTRIBUTE = new HtmlAttribute("data-url", "Value for updating the URL, instead of the url used to request the page", new String[]{});
	protected static final HtmlAttribute DATA_DISMISSIBLE_ATTRIBUTE = new HtmlAttribute("data-dismissible", " If set to false prevents popup closing by clicking outside of the popup or pressing the Escape key", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute PANEL_DATA_DISMISSIBLE_ATTRIBUTE = new HtmlAttribute("data-dismissible", " If set to \"false\" prevents panel closing by clicking outside of the panel or pressing the Escape key. \"true\" is the default value of this attribute", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_TOLERANCE_ATTRIBUTE = new HtmlAttribute("data-tolerance", "Distance from the edges of the window (top, right, bottom, left)", new String[]{});
	protected static final HtmlAttribute POPUP_DATA_REL_ATTRIBUTE = new HtmlAttribute("data-rel", "popup - For opening a popup", new String[]{"popup"});
	protected static final HtmlAttribute POPUP_DATA_TRANSITION_ATTRIBUTE = new HtmlAttribute("data-transition", "", new String[]{"fade", "flip", "flow", "pop", "slide", "slidedown", "slidefade", "slideup", "turn", "none"});
	protected static final HtmlAttribute DATA_NATIVE_MENU_ATTRIBUTE = new HtmlAttribute("data-native-menu", "", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_PLACEHOLDER_ATTRIBUTE = new HtmlAttribute("data-placeholder", "Can be set on option element of a non-native select", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_HIGHLIGHT_ATTRIBUTE = new HtmlAttribute("data-highlight", "Adds an active state fill on track to handle", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_CLEAR_BTN_ATTRIBUTE = new HtmlAttribute("data-clear-btn", "Adds a clear button", ENUM_TRUE_FALSE);
	protected static final HtmlAttribute DATA_CLEAR_BTN_TEXT_ATTRIBUTE = new HtmlAttribute("data-clear-btn-text", "Text for the close button. Default: <b>\"clear text\"</b>", new String[]{});
	protected static final HtmlAttribute DATA_MODE_ATTRIBUTE = new HtmlAttribute("data-mode", "Column chooser mode", DATA_MODE_ENUM);
	protected static final HtmlAttribute DATA_COLUMN_BTN_TEXT_ATTRIBUTE = new HtmlAttribute("data-column-btn-text", "The color chooser button's text. \"Columns...\" is used by default", new String[]{});
	protected static final HtmlAttribute DATA_COLUMN_BTN_THEME_ATTRIBUTE = new HtmlAttribute("data-column-btn-theme", "The color chooser button's theme", ENUM_THEME);
	protected static final HtmlAttribute DATA_COLUMN_POPUP_THEME_ATTRIBUTE = new HtmlAttribute("data-column-popup-theme", "", ENUM_THEME);
	protected static final HtmlAttribute DATA_PRIORITY_ATTRIBUTE = new HtmlAttribute("data-priority", "Makes the column available in the column chooser menu", DATA_PRIORITY_ENUM);

	private static Set<String> ALL_ATTRIBUTES;

	private static void addAttributes(Set<String> result, HtmlAttribute... attributes) {
		for (HtmlAttribute attribute : attributes) {
			result.add(attribute.getName());
		}
	}

	public static Set<String> getAllAttributes() {
		if(ALL_ATTRIBUTES==null) {
			Set<String> attributes = new HashSet<String>();
			addAttributes(attributes, AButtonAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, ButtonAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, CheckboxAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, CollapsibleAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, CollapsibleAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, CollapsibleSetAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, CollapsibleSetAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, ContentAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, ControlgroupAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, ControlgroupAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, DialogAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, DialogAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, EnhancementAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, FieldcontainAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, FixedToolbarAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, FlipToggleSwitchAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, FlipToggleSwitchAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, FooterHeaderAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, FooterHeaderAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, LinkAttributeProvider.DATA_ATTRIBUTES);
			addAttributes(attributes, ListViewAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, ListViewAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, ListviewItemAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, NavbarAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, NavbarAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, PageAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, PageAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, PanelAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, PanelAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, PopupAnchorAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, PopupAnchorAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, PopupAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, PopupAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, RadioButtonAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, SelectAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, SliderAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, TableAttributeProvider.CONDITIONAL_ATTRIBUTES);
			addAttributes(attributes, TableAttributeProvider.REQUIRED_ATTRIBUTES);
			addAttributes(attributes, TextInputAndTextareaAttributeProvider.ATTRIBUTES);
			addAttributes(attributes, ThAttributeProvider.ATTRIBUTES);
			ALL_ATTRIBUTES = Collections.unmodifiableSet(attributes);
		}
		return ALL_ATTRIBUTES;
	}

	protected boolean checkDataRole(String role) {
		Map<String, String> attributes = query.getAttributes();
		if(attributes==null) {
			return false;
		}
		String dataRole = attributes.get(DATA_ROLE);
		return role.equalsIgnoreCase(dataRole);
	}
}