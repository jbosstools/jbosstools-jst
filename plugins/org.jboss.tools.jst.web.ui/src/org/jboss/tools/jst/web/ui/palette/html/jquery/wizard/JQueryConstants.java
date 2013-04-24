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

import org.jboss.tools.jst.web.ui.palette.html.wizard.HTMLConstants;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public interface JQueryConstants extends HTMLConstants {
	public String ATTR_DATA_AUTODIVIDERS = "data-autodividers";
	public String ATTR_DATA_CLEAR_BTN = "data-clear-btn";
	public String ATTR_DATA_CLOSE_BTN = "data-close-btn";
	public String ATTR_DATA_COLLAPSED = "data-collapsed";
	public String ATTR_DATA_COLLAPSED_ICON = "data-collapsed-icon";
	public String ATTR_DATA_CONTENT_THEME = "data-content-theme";
	public String ATTR_DATA_DISPLAY = "data-display";
	public String ATTR_DATA_EXPANDED_ICON = "data-expanded-icon";
	public String ATTR_DATA_FILTER = "data-filter";
	public String ATTR_DATA_FULL_SCREEN = "data-fullscreen";
	public String ATTR_DATA_HIGHLIGHT = "data-highlight";
	public String ATTR_DATA_ICON = "data-icon";
	public String ATTR_DATA_ICONPOS = "data-iconpos";
	public String ATTR_DATA_INLINE = "data-inline";
	public String ATTR_DATA_INSET = "data-inset";
	public String ATTR_DATA_MINI = "data-mini";
	public String ATTR_DATA_OVERLAY_THEME = "data-overlay-theme";
	public String ATTR_DATA_POSITION = "data-position";
	public String ATTR_DATA_POSITION_FIXED = "data-position-fixed";
	public String ATTR_DATA_REL = "data-rel";
	public String ATTR_DATA_ROLE = "data-role";
	public String ATTR_DATA_SWIPE_CLOSE = "data-swipe-close";
	public String ATTR_DATA_THEME = "data-theme";
	public String ATTR_DATA_TRACK_THEME = "data-track-theme";
	public String ATTR_DATA_DIVIDER_THEME = "data-divider-theme";
	public String ATTR_DATA_TRANSITION = "data-transition";
	public String ATTR_DATA_TYPE = "data-type";
	public String ATTR_DATA_CORNERS = "data-corners";
	public String ATTR_DATA_POSITION_TO = "data-position-to";

	public String ATTR_DATA_MIN = "min";
	public String ATTR_DATA_MAX = "max";
	public String ATTR_DATA_STEP = "step";
	public String ATTR_DATA_VALUE = "value";
	public String ATTR_PLACEHOLDER = "placeholder";
	public String ATTR_DATA_DISMISSABLE = "data-dismissible";
	public String ATTR_DATA_SHADOW = "data-shadow";

	public String TRUE = "true";
	public String FALSE = "false";
	public String TYPE_CHECKBOX = "checkbox";
	public String TYPE_RADIO = "radio";
	public String TYPE_RANGE = "range";
	public String TYPE_TEXT = "text";
	public String TYPE_TEXTAREA = "textarea";
	public String TYPE_SEARCH = "search";
	public String TYPE_PASSWORD = "password";
	public String TYPE_FILE = "file";
	public String TYPE_NUMBER = "number";
	public String TYPE_URL = "url";
	public String TYPE_COLOR = "color";
	public String TYPE_EMAIL = "email";
	public String TYPE_TEL = "tel";
	public String TYPE_DATE = "date";
	public String TYPE_MONTH = "month";
	public String TYPE_WEEK = "week";
	public String TYPE_TIME = "time";
	public String TYPE_DATETIME = "datetime";

	public String CLASS_CUSTOM = "custom";
	public String CLASS_DISABLED = "ui-disabled";
	public String CLASS_HIDDEN_ACCESSIBLE = "ui-hidden-accessible";
	public String CLASS_HIDE_LABEL = "ui-hide-label";
	public String CLASS_BUTTON_RIGHT = "ui-btn-right";
	public String CLASS_BUTTON_LEFT = "ui-btn-left";
	public String CLASS_BAR = "ui-bar";
	public String CLASS_CONTENT = "ui-content";

	public String POSITION_FIXED = "fixed";
	public String POSITION_TO_WINDOW = "window";
	public String POSITION_TO_ORIGIN = "origin";

	public String POSITION_LEFT = "left";
	public String POSITION_RIGHT = "right";

	public String DISPLAY_OVERLAY = "overlay";
	public String DISPLAY_REVEAL = "reveal";
	public String DISPLAY_PUSH = "push";

	public String ROLE_BUTTON = "button";
	public String ROLE_COLLAPSIBLE = "collapsible";
	public String ROLE_CONTENT = "content";
	public String ROLE_DIALOG = "dialog";
	public String ROLE_FIELDCONTAIN = "fieldcontain";
	public String ROLE_FOOTER = "footer";
	public String ROLE_GROUP = "controlgroup";
	public String ROLE_HEADER = "header";
	public String ROLE_LISTVIEW = "listview";
	public String ROLE_DIVIDER = "list-divider";
	public String ROLE_NAVBAR = "navbar";
	public String ROLE_PAGE = "page";
	public String ROLE_POPUP = "popup";
	public String ROLE_RANGE_SLIDER = "rangeslider";
	public String ROLE_SLIDER = "slider";

	public String DATA_TYPE_HORIZONTAL = "horizontal";

	public String LAYOUT_HORIZONTAL = "Horizontal";
	public String LAYOUT_VERTICAL = "Vertical";

	public String TRANSITION_FADE = "fade"; //default
	public String TRANSITION_POP = "pop";
	public String TRANSITION_FLIP = "flip";
	public String TRANSITION_TURN = "turn";
	public String TRANSITION_FLOW = "flow";
	public String TRANSITION_SLIDEFADE = "slidefade";
	public String TRANSITION_SLIDEDOWN = "slidedown";
	public String TRANSITION_SLIDE = "slide";
	public String TRANSITION_SLIDEUP = "slideup";
	public String TRANSITION_NONE = "none";

	public String DATA_REL_DIALOG = "dialog";
	public String DATA_REL_POPUP = "popup";
	public String DATA_REL_BACK = "back";
	public String DATA_REL_EXTERNAL = "external";
	public String CLOSE_LEFT = "left";
	public String CLOSE_RIGHT = "right";
	public String CLOSE_NONE = "none";

	public String ARRAGEMENT_DEFAULT = "Default";
	public String ARRAGEMENT_GROUPED = "Grouped";
	public String ARRAGEMENT_NAVBAR = "Navbar";

	public String ICONPOS_NOTEXT = "notext";

	public String EDITOR_ID_TITLE = "title";
	public String EDITOR_ID_LABEL = "label";
	public String EDITOR_ID_LEGEND = "legend";
	public String EDITOR_ID_OPTION = "option";
	public String EDITOR_ID_MINI = "mini";
	public String EDITOR_ID_CHECKED = "checked";
	public String EDITOR_ID_SELECTED = "selected";
	public String EDITOR_ID_THEME = "theme";
	public String EDITOR_ID_TRACK_THEME = "track-theme";
	public String EDITOR_ID_CONTENT_THEME = "content-theme";
	public String EDITOR_ID_DIVIDER_THEME = "divider-theme";

	public String EDITOR_ID_NUMBERED = "numbered";
	public String EDITOR_ID_READ_ONLY = "read-only";
	public String EDITOR_ID_AUTODIVIDERS = "autodividers";
	public String EDITOR_ID_SEARCH_FILTER = "search-filter";
	public String EDITOR_ID_INSET = "inset";

	public String EDITOR_ID_OFF = "off";
	public String EDITOR_ID_ON = "on";
	public String EDITOR_ID_ID = "id";
	public String EDITOR_ID_LAYOUT = "layout";
	public String EDITOR_ID_URL = "url";
	public String EDITOR_ID_TRANSITION = "transition";
	public String EDITOR_ID_CLOSE_BUTTON = "close";

	public String EDITOR_ID_ACTION = "action";
	public String EDITOR_ID_DISABLED = "disabled";
	public String EDITOR_ID_DIVIDER = "divider";
	public String EDITOR_ID_ICON = "icon";
	public String EDITOR_ID_ICON_ONLY = "icon-only";
	public String EDITOR_ID_ICON_POS = "icon-pos";
	public String EDITOR_ID_CORNERS = "corners";
	public String EDITOR_ID_COLLAPSED_ICON = "collapsed-icon";
	public String EDITOR_ID_EXPANDED_ICON = "expanded-icon";
	public String EDITOR_ID_INLINE = "inline";
	public String EDITOR_ID_RANGE = "range";
	public String EDITOR_ID_MIN = "min";
	public String EDITOR_ID_MAX = "max";
	public String EDITOR_ID_STEP = "step";
	public String EDITOR_ID_VALUE = "value";
	public String EDITOR_ID_RVALUE = "right-value";
	public String EDITOR_ID_HIGHLIGHT = "highlight";
	public String EDITOR_ID_HIDE_LABEL = "hide-label";
	public String EDITOR_ID_ADD_HEADER = "add-header";
	public String EDITOR_ID_HEADER_TITLE = "header-title";
	public String EDITOR_ID_ADD_FOOTER = "add-footer";
	public String EDITOR_ID_FOOTER_TITLE = "footer-title";
	public String EDITOR_ID_TEXT_TYPE = "text-type";
	public String EDITOR_ID_CLEAR_INPUT = "clear-input";
	public String EDITOR_ID_PLACEHOLDER = "placeholder";
	public String EDITOR_ID_FIXED_POSITION = "fixed";
	public String EDITOR_ID_FULL_SCREEN = "full-screen";
	public String EDITOR_ID_LEFT_BUTTON = "left-button";
	public String EDITOR_ID_RIGHT_BUTTON = "right-button";
	public String EDITOR_ID_LEFT_BUTTON_LABEL = "left-button-label";
	public String EDITOR_ID_LEFT_BUTTON_URL = "left-button-uri";
	public String EDITOR_ID_LEFT_BUTTON_ICON = "left-button-icon";
	public String EDITOR_ID_RIGHT_BUTTON_LABEL = "right-button-label";
	public String EDITOR_ID_RIGHT_BUTTON_URL = "right-button-uri";
	public String EDITOR_ID_RIGHT_BUTTON_ICON = "right-button-icon";
	public String EDITOR_ID_NUMBER_OF_ITEMS = "number-of-items";
	public String EDITOR_ID_ARRAGEMENT = "arragement";
	public String EDITOR_ID_GRID_COLUMNS = "grid-columns";
	public String EDITOR_ID_GRID_ROWS = "grid-rows";
	public String EDITOR_ID_DISMISSABLE = "dismissable";
	public String EDITOR_ID_SHADOW = "shadow";
	public String EDITOR_ID_PADDING = "padding";
	public String EDITOR_ID_OVERLAY = "overlay";
	public String EDITOR_ID_POSITION_TO = "position-to";
	public String EDITOR_ID_INFO_STYLED = "info-styled";
	public String EDITOR_ID_SWIPE_CLOSE = "swipe-close";

	public String EDITOR_ID_HEADER = "header";
	public String EDITOR_ID_COLLAPSED = "collapsed";
	public String EDITOR_ID_FIELD_SET = "field-set";

	public String EDITOR_ID_PANEL_POSITION = "panel-position";
	public String EDITOR_ID_DISPLAY = "display";

}
