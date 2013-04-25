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
package org.jboss.tools.jst.web.kb.internal.taglib.jq;

import java.util.Map;

import org.jboss.tools.jst.web.kb.internal.taglib.AbstractAttributeProvider;

/**
 * @author Alexey Kazakov
 */
public abstract class JQueryMobileAttrProvider extends AbstractAttributeProvider {

	protected static final String DATA_ROLE = "data-role";

	public static final String BUTTON = "button";
	public static final String COLLAPSIBLE = "collapsible";
	public static final String COLLAPSIBLE_SET = "collapsible-set";
	public static final String CONTROLGROUP = "controlgroup";
	public static final String DIALOG = "dialog";
	public static final String CONTENT = "content";
	public static final String FIELDCONTENT = "fieldcontain";
	public static final String SUBMIT = "submit";
	public static final String RESET = "reset";
	public static final String TYPE = "type";
	public static final String INPUT = "input";
	public static final String CHECKBOX = "checkbox";
	public static final String DIV = "div";
	public static final String HEADER = "header";
	public static final String FOOTER = "footer";
	public static final String NAVBAR = "navbar";
	public static final String SLIDER = "slider";
	public static final String LISTVIEW = "listview";
	public static final String PAGE = "page";
	public static final String TABLE = "table";
	public static final String POPUP = "popup";
	public static final String RADIO = "radio";
	public static final String RANGE = "range";
	public static final String HIDEN = "hiden";
	public static final String TEXTAREA = "textarea";
	public static final String PANEL = "panel";

	protected static final AttributeData TYPE_BUTTON = new AttributeData(TYPE, BUTTON);
	protected static final AttributeData TYPE_SUBMIT = new AttributeData(TYPE, SUBMIT);
	protected static final AttributeData TYPE_RESET = new AttributeData(TYPE, RESET);
	protected static final AttributeData TYPE_CHECKBOX = new AttributeData(TYPE, CHECKBOX);
	protected static final AttributeData TYPE_RADIO = new AttributeData(TYPE, RADIO);
	protected static final AttributeData TYPE_RANGE = new AttributeData(TYPE, RANGE);
	protected static final AttributeData TYPE_HIDEN = new AttributeData(TYPE, HIDEN);

	protected static final AttributeData DATA_ROLE_HEADER = new AttributeData(DATA_ROLE, HEADER);
	protected static final AttributeData DATA_ROLE_FOOTER = new AttributeData(DATA_ROLE, FOOTER);
	protected static final AttributeData DATA_ROLE_NAVBAR = new AttributeData(DATA_ROLE, NAVBAR);
	protected static final AttributeData DATA_ROLE_LISTVIEW = new AttributeData(DATA_ROLE, LISTVIEW);
	protected static final AttributeData DATA_ROLE_PANEL = new AttributeData(DATA_ROLE, PANEL);

	protected static final AttributeData DATA_POSITION_FIXED = new AttributeData("data-position", "fixed");

	protected static final AttributeData DATA_REL_POPUP = new AttributeData("data-rel", "popup");

	protected static final AttributeData DATA_ROLE_TABLE = new AttributeData(DATA_ROLE, TABLE);
	
	public static final String[] ENUM_TRUE_FALSE = new String[] { "true",
			"false" };
	public static final String[] ENUM_ICON_VALUES = new String[] { "home",
			"delete", "plus", "arrow-u", "arrow-d", "check", "gear", "grid",
			"star", "custom", "arrow-r", "arrow-l", "minus", "refresh",
			"forward", "back", "alert", "info", "search" };
	public static final String[] ENUM_THEME = new String[] { "a", "b",
			"c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
			"p", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
	public static final String[] DATA_REL_ENUM = new String[]{"back", "dialog", "external", "popup", "close"};
	public static final String[] DATA_MODE_ENUM = new String[]{"columntoggle", "reflow"};
	public static final String[] DATA_PRIORITY_ENUM = new String[]{"1", "2", "3", "4", "5", "6"};

	public static final String[] DATA_POSITION_ENUM = new String[]{"left", "right"};
	public static final String[] DATA_DISPLAY_ENUM = new String[]{"reveal", "overlay", "push"};

	protected static final JQueryMobileAttribute DATA_CORNERS_ATTRIBUTE = new JQueryMobileAttribute("data-corners", "", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_ICON_ATTRIBUTE = new JQueryMobileAttribute("data-icon", "", ENUM_ICON_VALUES);
	protected static final JQueryMobileAttribute DATA_ICONPOS_ATTRIBUTE = new JQueryMobileAttribute("data-iconpos", "", new String[]{"left", "right",	"top", "bottom", "notext"});
	protected static final JQueryMobileAttribute DATA_ICONSHADOW_ATTRIBUTE = new JQueryMobileAttribute("data-iconshadow", "", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_INLINE_ATTRIBUTE = new JQueryMobileAttribute("data-inline", "", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_MINI_ATTRIBUTE = new JQueryMobileAttribute("data-mini", "Compact sized version", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_SHADOW_ATTRIBUTE = new JQueryMobileAttribute("data-shadow", "", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_THEME_ATTRIBUTE = new JQueryMobileAttribute("data-theme", "", ENUM_THEME);
	protected static final JQueryMobileAttribute DATA_INPUT_THEME_ATTRIBUTE = new JQueryMobileAttribute("data-theme", "swatch letter (a-z) - Added to the form element", ENUM_THEME);

	protected static final JQueryMobileAttribute DATA_ROLE_NONE_ATTRIBUTE = new DataRoleAttribute("Prevents auto-enhancement to use native control");
	protected static final JQueryMobileAttribute DATA_ROLE_LIST_ATTRIBUTE = new JQueryMobileAttribute("data-role", "", new String[]{"list-divider"});
	protected static final JQueryMobileAttribute DATA_COLLAPSED_ATTRIBUTE = new JQueryMobileAttribute("data-collapsed", "", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_COLLAPSED_ICON_ATTRIBUTE = new JQueryMobileAttribute("data-collapsed-icon", "", ENUM_ICON_VALUES);
	protected static final JQueryMobileAttribute DATA_CONTENT_THEME_ATTRIBUTE = new JQueryMobileAttribute("data-content-theme", "", ENUM_THEME);
	protected static final JQueryMobileAttribute DATA_EXPANDED_ICON_ATTRIBUTE = new JQueryMobileAttribute("data-expanded-icon", "", ENUM_ICON_VALUES);
	protected static final JQueryMobileAttribute DATA_INSET_ATTRIBUTE = new JQueryMobileAttribute("data-inset", "", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_TYPE_ATTRIBUTE = new JQueryMobileAttribute("data-type", "For horizontal or vertical item alignment", new String[]{"horizontal", "vertical"});
	protected static final JQueryMobileAttribute DATA_CLOSE_BTN_ATTRIBUTE = new JQueryMobileAttribute("data-close-btn", "", new String[]{"left", "right", "none"});
	protected static final JQueryMobileAttribute DATA_CLOSE_BTN_TEXT_ATTRIBUTE = new JQueryMobileAttribute("data-close-btn-text", "Text for the close button, dialog only", new String[]{});
	protected static final JQueryMobileAttribute DATA_DOM_CACHE_ATTRIBUTE = new JQueryMobileAttribute("data-dom-cache", "", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_OVERLAY_THEME_ATTRIBUTE = new JQueryMobileAttribute("data-overlay-theme", "", ENUM_THEME);
	protected static final JQueryMobileAttribute DATA_TITLE_ATTRIBUTE = new JQueryMobileAttribute("data-title", "Title used when page is shown", new String[]{});
	protected static final JQueryMobileAttribute DATA_ENHANCE_ATTRIBUTE = new JQueryMobileAttribute("data-enhance", "Any DOM elements inside a data-enhance=\"false\" container, save for data-role=\"page|dialog\" elements, will be ignored during initial enhancement and subsequent create events provided that the $.mobile.ignoreContentEnabled flag is set prior to the enhancement (eg in a mobileinit binding)", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_AJAX_ATTRIBUTE = new JQueryMobileAttribute("data-ajax", "Any link or form element inside data-ajax=\"false\" containers will be ignored by the framework's navigation functionality when $.mobile.ignoreContentEnabled is set to true", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_DISABLED_PAGE_ZOOM_ATTRIBUTE = new JQueryMobileAttribute("data-disable-page-zoom", "User-scaling-ability for pages with fixed toolbars", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_FULLSCREEN_ATTRIBUTE = new JQueryMobileAttribute("data-fullscreen", "Setting toolbars over the page-content", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_TAP_TOGGLE_ATTRIBUTE = new JQueryMobileAttribute("data-tap-toggle", "Ability to toggle toolbar-visibility on user tap/click", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute FIXED_TOOLBAR_DATA_TRANSITION_ATTRIBUTE = new JQueryMobileAttribute("data-transition", "Show/hide-transition when a tap/click occurs", new String[]{"slide", "fade", "none"});
	protected static final JQueryMobileAttribute DATA_UPDATE_PAGE_PADDING_ATTRIBUTE = new JQueryMobileAttribute("data-update-page-padding", "Have the page top and bottom padding updated on resize, transition, \"updatelayout\" events (the framework always updates the padding on the \"pageshow\" event).", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_VISIBLE_ON_PAGE_SHOW_ATTRIBUTE = new JQueryMobileAttribute("data-visible-on-page-show", "Toolbar-visibility when parent page is shown", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_TRACK_THEME_ATTRIBUTE = new JQueryMobileAttribute("data-track-theme", "swatch letter (a-z) - Added to the form element", ENUM_THEME);
	protected static final JQueryMobileAttribute DATA_ID_ATTRIBUTE = new JQueryMobileAttribute("data-id", "Unique ID. Required for persistent footers", new String[]{});
	protected static final JQueryMobileAttribute DATA_POSITION_ATTRIBUTE = new JQueryMobileAttribute("data-position", "", new String[]{"fixed"});
	protected static final JQueryMobileAttribute PANEL_DATA_POSITION_ATTRIBUTE = new JQueryMobileAttribute("data-position", "The position of the panel on the screen.  The default value of the attribute is left, meaning it will appear from the left edge of the screen. Specify data-position=\"right\" for it to appear from the right edge instead", DATA_POSITION_ENUM);
	protected static final JQueryMobileAttribute DATA_DISPLAY_ATTRIBUTE = new JQueryMobileAttribute("data-display", "The display mode of the panel.  The value of the attribute defaults to reveal, meaning the panel will sit under the page and reveal as the page slides away. Specify data-display=\"overlay\" for the panel to appear on top of the page contents. A third mode, data-display=\"push\" animates both the panel and page at the same time.", DATA_DISPLAY_ENUM);
	protected static final JQueryMobileAttribute DATA_POSITION_FIXED_ATTRIBUTE = new JQueryMobileAttribute("data-position-fixed", "\"false\" is the default value of this attribute", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_SWIPE_CLOSE_ATTRIBUTE = new JQueryMobileAttribute("data-swipe-close", "Clicking the link that opened the panel, swiping left or right, or tapping the Esc key will close the panel. To turn off the swipe-to-close behavior, add the data-swipe-close=\"false\" attribute to the panel. \"right\" is the default value of this attribute", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_DIRECTION_ATTRIBUTE = new JQueryMobileAttribute("data-direction", "Reverse transition animation (only for page or dialog)", new String[]{"reverse"});
	protected static final JQueryMobileAttribute DATA_PREFETCH_ATTRIBUTE = new JQueryMobileAttribute("data-prefetch", "", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_REL_ATTRIBUTE = new JQueryMobileAttribute("data-rel", "<b>back</b> - To move one step back in history<br/><b>dialog</b> - To open link styled as dialog, not tracked in history<br/><b>external</b> - For linking to another domain<br/><b>popup</b> - For opening a popup", DATA_REL_ENUM, new String[]{"To move one step back in history", "To open link styled as dialog, not tracked in history", "For linking to another domain", "For opening a popup", "For closing a panel"});
	protected static final JQueryMobileAttribute LINK_DATA_TRANSITION_ATTRIBUTE = new JQueryMobileAttribute("data-transition", "", new String[]{"fade", "flip", "flow", "pop", "slide", "slidedown", "slidefade", "slideup", "turn", "none"});
	protected static final JQueryMobileAttribute DATA_POSITION_TO_ATTRIBUTE = new JQueryMobileAttribute("data-position-to", "<b>origin</b> - Centers the popup over the link that opens it jQuery<br/><b>JQuery selector</b> - Centers the popup over the specified element<br/><b>window</b> - Centers the popup in the window", new String[]{"origin", "window"}, new String[]{"Centers the popup over the link that opens it jQuery", "Centers the popup in the window"});
	protected static final JQueryMobileAttribute DATA_AUTODIVIDERS_ATTRIBUTE = new JQueryMobileAttribute("data-autodividers", "", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_COUNT_THEME_ATTRIBUTE = new JQueryMobileAttribute("data-count-theme", "", ENUM_THEME);
	protected static final JQueryMobileAttribute DATA_DIVIDER_THEME_ATTRIBUTE = new JQueryMobileAttribute("data-divider-theme", "", ENUM_THEME);
	protected static final JQueryMobileAttribute DATA_SELECT_DIVIDER_THEME_ATTRIBUTE = new JQueryMobileAttribute("data-divider-theme", "swatch letter (a-z) - Default <b>\"b\"</b> - Only applicable if <i>optgroup</i> support is used in non-native selects", ENUM_THEME);
	protected static final JQueryMobileAttribute DATA_FILTER_ATTRIBUTE = new JQueryMobileAttribute("data-filter", "", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_FILTER_PLACEHOLDER_ATTRIBUTE = new JQueryMobileAttribute("data-filter-placeholder", "", new String[]{});
	protected static final JQueryMobileAttribute DATA_FILTER_THEME_ATTRIBUTE = new JQueryMobileAttribute("data-filter-theme", "", ENUM_THEME);
	protected static final JQueryMobileAttribute DATA_HEADER_THEME_ATTRIBUTE = new JQueryMobileAttribute("data-header-theme", "", ENUM_THEME);
	protected static final JQueryMobileAttribute DATA_SPLIT_ICON_ATTRIBUTE = new JQueryMobileAttribute("data-split-icon", "", ENUM_ICON_VALUES);
	protected static final JQueryMobileAttribute DATA_SPLIT_THEME_ATTRIBUTE = new JQueryMobileAttribute("data-split-theme", "", ENUM_THEME);
	protected static final JQueryMobileAttribute DATA_FILTERTEXT_ATTRIBUTE = new JQueryMobileAttribute("data-filtertext", "Filter by this value instead of inner text", new String[]{});
	protected static final JQueryMobileAttribute DATA_ADD_BACK_BTN_ATTRIBUTE = new JQueryMobileAttribute("data-add-back-btn", "Auto add back button, header only", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_BACK_BTN_TEXT_ATTRIBUTE = new JQueryMobileAttribute("data-back-btn-text", "", new String[]{});
	protected static final JQueryMobileAttribute DATA_BACK_BTN_THEME_ATTRIBUTE = new JQueryMobileAttribute("data-back-btn-theme", "", ENUM_THEME);
	protected static final JQueryMobileAttribute DATA_URL_ATTRIBUTE = new JQueryMobileAttribute("data-url", "Value for updating the URL, instead of the url used to request the page", new String[]{});
	protected static final JQueryMobileAttribute DATA_DISMISSIBLE_ATTRIBUTE = new JQueryMobileAttribute("data-dismissible", " If set to false prevents popup closing by clicking outside of the popup or pressing the Escape key", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute PANEL_DATA_DISMISSIBLE_ATTRIBUTE = new JQueryMobileAttribute("data-dismissible", " If set to \"false\" prevents panel closing by clicking outside of the panel or pressing the Escape key. \"true\" is the default value of this attribute", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_TOLERANCE_ATTRIBUTE = new JQueryMobileAttribute("data-tolerance", "Distance from the edges of the window (top, right, bottom, left)", new String[]{});
	protected static final JQueryMobileAttribute POPUP_DATA_REL_ATTRIBUTE = new JQueryMobileAttribute("data-rel", "popup - For opening a popup", new String[]{"popup"});
	protected static final JQueryMobileAttribute POPUP_DATA_TRANSITION_ATTRIBUTE = new JQueryMobileAttribute("data-transition", "", new String[]{"fade", "flip", "flow", "pop", "slide", "slidedown", "slidefade", "slideup", "turn", "none"});
	protected static final JQueryMobileAttribute DATA_NATIVE_MENU_ATTRIBUTE = new JQueryMobileAttribute("data-native-menu", "", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_PLACEHOLDER_ATTRIBUTE = new JQueryMobileAttribute("data-placeholder", "Can be set on option element of a non-native select", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_HIGHLIGHT_ATTRIBUTE = new JQueryMobileAttribute("data-highlight", "Adds an active state fill on track to handle", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_CLEAR_BTN_ATTRIBUTE = new JQueryMobileAttribute("data-clear-btn", "Adds a clear button", ENUM_TRUE_FALSE);
	protected static final JQueryMobileAttribute DATA_CLEAR_BTN_TEXT_ATTRIBUTE = new JQueryMobileAttribute("data-clear-btn-text", "Text for the close button. Default: <b>\"clear text\"</b>", new String[]{});
	protected static final JQueryMobileAttribute DATA_MODE_ATTRIBUTE = new JQueryMobileAttribute("data-mode", "Column chooser mode", DATA_MODE_ENUM);
	protected static final JQueryMobileAttribute DATA_COLUMN_BTN_TEXT_ATTRIBUTE = new JQueryMobileAttribute("data-column-btn-text", "The color chooser button's text. \"Columns...\" is used by default", new String[]{});
	protected static final JQueryMobileAttribute DATA_COLUMN_BTN_THEME_ATTRIBUTE = new JQueryMobileAttribute("data-column-btn-theme", "The color chooser button's theme", ENUM_THEME);
	protected static final JQueryMobileAttribute DATA_COLUMN_POPUP_THEME_ATTRIBUTE = new JQueryMobileAttribute("data-column-popup-theme", "", ENUM_THEME);
	protected static final JQueryMobileAttribute DATA_PRIORITY_ATTRIBUTE = new JQueryMobileAttribute("data-priority", "Makes the column available in the column chooser menu", DATA_PRIORITY_ENUM);

	protected boolean checkDataRole(String role) {
		Map<String, String> attributes = query.getAttributes();
		if(attributes==null) {
			return false;
		}
		String dataRole = attributes.get(DATA_ROLE);
		return role.equalsIgnoreCase(dataRole);
	}
}