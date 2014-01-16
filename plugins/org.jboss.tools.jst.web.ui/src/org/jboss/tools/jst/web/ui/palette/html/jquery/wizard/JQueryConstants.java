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

import org.jboss.tools.jst.web.html.JQueryHTMLConstants;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public interface JQueryConstants extends JQueryHTMLConstants {
	public String LAYOUT_HORIZONTAL = "Horizontal";
	public String LAYOUT_VERTICAL = "Vertical";

	public String CLOSE_LEFT = "left";
	public String CLOSE_RIGHT = "right";
	public String CLOSE_NONE = "none";

	public String ARRAGEMENT_DEFAULT = "Default";
	public String ARRAGEMENT_GROUPED = "Grouped";
	public String ARRAGEMENT_NAVBAR = "Navbar";

	public String TOGGLE_KIND_CHECKBOX = "checkbox";
	public String TOGGLE_KIND_SELECT = "select";

	/**
	 * Palette category id.
	 */
	public String JQM_CATEGORY = "jQuery Mobile";

	public String EDITOR_ID_TITLE = TAG_TITLE;
	public String EDITOR_ID_LABEL = TAG_LABEL;
	public String EDITOR_ID_NAME = ATTR_NAME;
	public String EDITOR_ID_LEGEND = TAG_LEGEND;
	public String EDITOR_ID_OPTION = TAG_OPTION;
	public String EDITOR_ID_MINI = "mini";
	public String EDITOR_ID_CHECKED = CHECKED;
	public String EDITOR_ID_SELECTED = SELECTED;
	public String EDITOR_ID_THEME = "theme";
	public String EDITOR_ID_TRACK_THEME = ATTR_DATA_TRACK_THEME;
	public String EDITOR_ID_CONTENT_THEME = ATTR_DATA_CONTENT_THEME;
	public String EDITOR_ID_DIVIDER_THEME = ATTR_DATA_DIVIDER_THEME;

	public String EDITOR_ID_NUMBERED = "numbered";
	public String EDITOR_ID_READ_ONLY = ATTR_READONLY;
	public String EDITOR_ID_AUTODIVIDERS = ATTR_DATA_AUTODIVIDERS;
	public String EDITOR_ID_SEARCH_FILTER = ATTR_DATA_FILTER;
	public String EDITOR_ID_INSET = ATTR_DATA_INSET;

	public String EDITOR_ID_OFF = "off";
	public String EDITOR_ID_ON = "on";
	public String EDITOR_ID_ID = "id";
	public String EDITOR_ID_LAYOUT = "layout";
	public String EDITOR_ID_URL = "url";
	public String EDITOR_ID_SRC = ATTR_SRC;
	public String EDITOR_ID_TRANSITION = ATTR_DATA_TRANSITION;
	public String EDITOR_ID_CLOSE_BUTTON = ATTR_DATA_CLOSE_BTN;
	public String EDITOR_ID_ADD_ID = "add-id";
	public String EDITOR_ID_POSTER = ATTR_POSTER;

	public String EDITOR_ID_ACTION = "action";
	public String EDITOR_ID_FORM_ACTION = ATTR_ACTION;
	public String EDITOR_ID_FORM_METHOD = ATTR_METHOD;
	public String EDITOR_ID_AUTOCOMPLETE = ATTR_AUTOCOMPLETE;
	public String EDITOR_ID_VALIDATE = "validate";
	public String EDITOR_ID_DISABLED = ATTR_DISABLED;
	public String EDITOR_ID_DIVIDER = "divider";
	public String EDITOR_ID_ICON = ATTR_DATA_ICON;
	public String EDITOR_ID_ICON_ONLY = "icon-only";
	public String EDITOR_ID_ICON_POS = ATTR_DATA_ICONPOS;
	public String EDITOR_ID_CORNERS = ATTR_DATA_CORNERS;
	public String EDITOR_ID_COLLAPSED_ICON = ATTR_DATA_COLLAPSED_ICON;
	public String EDITOR_ID_EXPANDED_ICON = ATTR_DATA_EXPANDED_ICON;
	public String EDITOR_ID_INLINE = ATTR_DATA_INLINE;
	public String EDITOR_ID_RANGE = "range";
	public String EDITOR_ID_MIN = ATTR_DATA_MIN;
	public String EDITOR_ID_MAX = ATTR_DATA_MAX;
	public String EDITOR_ID_STEP = ATTR_DATA_STEP;
	public String EDITOR_ID_VALUE = ATTR_VALUE;
	public String EDITOR_ID_RVALUE = "right-value";
	public String EDITOR_ID_HIGHLIGHT = ATTR_DATA_HIGHLIGHT;
	public String EDITOR_ID_HIDE_LABEL = "hide-label";
	public String EDITOR_ID_ADD_HEADER = "add-header";
	public String EDITOR_ID_HEADER_TITLE = "header-title";
	public String EDITOR_ID_ADD_FOOTER = "add-footer";
	public String EDITOR_ID_FOOTER_TITLE = "footer-title";
	public String EDITOR_ID_TEXT_TYPE = "text-type";
	public String EDITOR_ID_CLEAR_INPUT = ATTR_DATA_CLEAR_BTN;
	public String EDITOR_ID_PLACEHOLDER = ATTR_PLACEHOLDER;
	public String EDITOR_ID_PATTERN = ATTR_PATTERN;
	public String EDITOR_ID_AUTOFOCUS = ATTR_AUTOFOCUS;
	public String EDITOR_ID_REQUIRED = ATTR_REQUIRED;
	public String EDITOR_ID_MAXLENGTH = ATTR_MAXLENGTH;
	public String EDITOR_ID_FIXED_POSITION = "fixed";
	public String EDITOR_ID_FULL_SCREEN = ATTR_DATA_FULL_SCREEN;
	public String EDITOR_ID_LEFT_BUTTON = "left-button";
	public String EDITOR_ID_RIGHT_BUTTON = "right-button";
	public String EDITOR_ID_BACK_BUTTON = ATTR_DATA_ADD_BACK_BUTTON;
	public String EDITOR_ID_POPUP_BUTTON = "popup-button";
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
	public String EDITOR_ID_POSITION_TO = ATTR_DATA_POSITION_TO;
	public String EDITOR_ID_INFO_STYLED = "info-styled";
	public String EDITOR_ID_SWIPE_CLOSE = ATTR_DATA_SWIPE_CLOSE;

	public String EDITOR_ID_HEADER = "header";
	public String EDITOR_ID_COLLAPSED = ATTR_DATA_COLLAPSED;
	public String EDITOR_ID_FIELD_SET = "field-set";

	public String EDITOR_ID_PANEL_POSITION = ATTR_DATA_POSITION;
	public String EDITOR_ID_DISPLAY = ATTR_DATA_DISPLAY;

	public String EDITOR_ID_MODE = ATTR_DATA_MODE;
	public String EDITOR_ID_COLUMN_NAME = "column-name";
	public String EDITOR_ID_PRIORITY = ATTR_DATA_PRIORITY;
	public String EDITOR_ID_CONTENT = "content";
	public String EDITOR_ID_RESPONSIVE = CLASS_RESPONSIVE;
	public String EDITOR_ID_STRIPES = "stripes";

	public String EDITOR_ID_ALT = ATTR_ALT;
	public String EDITOR_ID_WIDTH = ATTR_WIDTH;
	public String EDITOR_ID_HEIGHT = ATTR_HEIGHT;
	public String EDITOR_ID_ISMAP = ATTR_ISMAP;
	public String EDITOR_ID_USEMAP = ATTR_USEMAP;
	public String EDITOR_ID_CROSSORIGIN = ATTR_CROSSORIGIN;
	public String EDITOR_ID_AUTOPLAY = ATTR_AUTOPLAY;
	public String EDITOR_ID_CONTROLS = ATTR_CONTROLS;
	public String EDITOR_ID_LOOP = ATTR_LOOP;
	public String EDITOR_ID_MUTED = ATTR_MUTED;
	public String EDITOR_ID_PRELOAD = ATTR_PRELOAD;
	public String EDITOR_ID_VIDEO_TYPE = ATTR_TYPE;
	public String EDITOR_ID_AUDIO_TYPE = ATTR_TYPE;
	public String EDITOR_ID_FORM = TAG_FORM;
	public String EDITOR_ID_FOR = ATTR_FOR;

	public String EDITOR_ID_FORM_BUTTON_TYPE = ATTR_TYPE;

	public String EDITOR_ID_ADD_LIST = "add-list";
	public String EDITOR_ID_TOGGLE_KIND = "toggle-kind";
	
}
