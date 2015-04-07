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

import org.jboss.tools.jst.web.ui.palette.html.wizard.HTMLConstants;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public interface IonicConstants extends HTMLConstants {

	/**
	 * Palette category id.
	 */
	public String IONIC_CATEGORY = "Ionic";

	public String TAG_ION_CHECKBOX = "ion-checkbox";
	public String TAG_ION_CONTENT = "ion-content";
	public String TAG_ION_DELETE_BUTTON = "ion-delete-button";
	public String TAG_ION_FOOTER_BAR = "ion-footer-bar";
	public String TAG_ION_HEADER_BAR = "ion-header-bar";
	public String TAG_ION_ITEM = "ion-item";
	public String TAG_ION_LIST = "ion-list";
	public String TAG_ION_NAV_BACK_BUTTON = "ion-nav-back-button";
	public String TAG_ION_NAV_BAR = "ion-nav-bar";
	public String TAG_ION_NAV_BUTTONS = "ion-nav-buttons";
	public String TAG_ION_NAV_VIEW = "ion-nav-view";
	public String TAG_ION_OPTION_BUTTON = "ion-option-button";
	public String TAG_ION_RADIO = "ion-radio";
	public String TAG_ION_REFRESHER = "ion-refresher";
	public String TAG_ION_REORDER_BUTTON = "ion-reorder-button";
	public String TAG_ION_SCROLL = "ion-scroll";
	public String TAG_ION_SIDE_MENUS = "ion-side-menus";
	public String TAG_ION_SIDE_MENU = "ion-side-menu";
	public String TAG_ION_SIDE_MENU_CONTENT = "ion-side-menu-content";
	public String TAG_ION_SLIDE_BOX = "ion-slide-box";
	public String TAG_ION_SLIDE = "ion-slide";
	public String TAG_ION_SPINNER = "ion-spinner";
	public String TAG_ION_TABS = "ion-tabs";
	public String TAG_ION_TAB = "ion-tab";
	public String TAG_ION_TOGGLE = "ion-toggle";
	public String TAG_ION_VIEW = "ion-view";

	public String ATTR_ACTIVE_SLIDE = "active-slide";
	public String ATTR_ALIGN_TITLE = "align-title";
	public String ATTR_ANIMATION = "animation";
	public String ATTR_AUTO_PLAY = "auto-play";
	public String ATTR_BADGE = "badge";
	public String ATTR_BADGE_STYLE = "badge-style";
	public String ATTR_CAN_SWIPE = "can-swipe";
	public String ATTR_DELEGATE_HANDLE = "delegate-handle";
	public String ATTR_DIRECTION = "direction";
	public String ATTR_DOES_CONTINUE = "does-continue";
	public String ATTR_DRAG_CONTENT = "drag-content";
	public String ATTR_EDGE_DRAG_THRESHOLD = "edge-drag-threshold";
	public String ATTR_HAS_BOUNCING = "has-bouncing";
	public String ATTR_HIDE_BACK_BUTTON = "hide-back-button";
	public String ATTR_ICON = "icon";
	public String ATTR_ICON_OFF = "icon-off";
	public String ATTR_ICON_ON = "icon-on";
	public String ATTR_IS_ENABLED = "is-enabled";
	public String ATTR_MAX_ZOOM = "max-zoom";
	public String ATTR_MENU_TOGGLE = "menu-toggle";
	public String ATTR_MIN_ZOOM = "min-zoom";
	public String ATTR_NG_CHANGE = "ng-change";
	public String ATTR_NG_CHECKED = "ng-checked";
	public String ATTR_NG_CLICK = "ng-click";
	public String ATTR_NG_DISABLED = "ng-disabled";
	public String ATTR_NG_FALSE_VALUE = "ng-false-value";
	public String ATTR_NG_MODEL = "ng-model";
	public String ATTR_NG_PATTERN = "ng-pattern";
	public String ATTR_NG_TRUE_VALUE = "ng-true-value";
	public String ATTR_NG_VALUE = "ng-value";
	public String ATTR_NO_TAP_SCROLL = "no-tap-scroll";
	public String ATTR_ON_PULLING = "on-pulling";
	public String ATTR_ON_REFRESH = "on-refresh";
	public String ATTR_ON_REORDER = "on-reorder";
	public String ATTR_ON_SCROLL = "on-scroll";
	public String ATTR_ON_SCROLL_COMPLETE = "on-scroll-complete";
	public String ATTR_ON_SELECT = "on-select";
	public String ATTR_ON_SLIDE_CHANGED = "on-slide-changed";
	public String ATTR_ON_DESELECT = "on-deselect";
	public String ATTR_OVERFLOW_SCROLL = "overflow-scroll";
	public String ATTR_PADDING = "padding";
	public String ATTR_PAGER_CLICK = "pager-click";
	public String ATTR_PAGING = "paging";
	public String ATTR_PULLING_ICON = "pulling-icon";
	public String ATTR_PULLING_TEXT = "pulling-text";
	public String ATTR_REFRESHING_ICON = "refreshing-icon";
	public String ATTR_REFRESHING_TEXT = "refreshing-text";
	public String ATTR_SCROLL = "scroll";
	public String ATTR_SCROLLBAR_X = "scrollbar-x";
	public String ATTR_SCROLLBAR_Y = "scrollbar-y";
	public String ATTR_SHOW_DELETE = "show-delete";
	public String ATTR_SHOW_PAGER = "show-pager";
	public String ATTR_SHOW_REORDER = "show-reorder";
	public String ATTR_SIDE = "side";
	public String ATTR_SLIDE_INTERVAL = "slide-interval";
	public String ATTR_START_Y = "start-y";
	public String ATTR_TOGGLE_CLASS = "toggle-class";
	public String ATTR_ZOOMING = "zooming";

	public String CLASS_BAR_POSITIVE = "bar-positive";
	public String CLASS_BAR_SUBFOOTER = "bar-subfooter";
	public String CLASS_BAR_SUBHEADER = "bar-subheader";
	public String CLASS_BUTTON = "button";
	public String CLASS_BUTTONS = "buttons";
	public String CLASS_ITEM_AVATAR = "item-avatar";
	public String CLASS_ITEM_BODY = "item-body";
	public String CLASS_ITEM_DIVIDER = "item-divider";
	public String CLASS_ITEM_ICON_LEFT = "item-icon-left";
	public String CLASS_ITEM_PLACEHOLDER = "item-placeholder";
	public String CLASS_ITEM_THUMBNAIL_LEFT = "item-thumbnail-left";
	public String CLASS_ITEM_THUMBNAIL_RIGHT = "item-thumbnail-right";
	public String CLASS_ICON = "icon";
	public String CLASS_TABS_ITEM_HIDE = "tabs-item-hide";
	public String CLASS_TITLE = "title";

	public String SIDE_LEFT = "left";
	public String SIDE_RIGHT = "right";
	
	public String SPINNER_ICON_DEFAULT = "default";
	public String SPINNER_ICON_ANDROID = "android";
	public String SPINNER_ICON_BUBBLES = "bubbles";
	public String SPINNER_ICON_CIRCLES = "circles";
	public String SPINNER_ICON_CRESCENT = "crescent";
	public String SPINNER_ICON_DOTS = "dots";
	public String SPINNER_ICON_IOS = "ios";
	public String SPINNER_ICON_IOS_SMALL = "ios-small";
	public String SPINNER_ICON_LINES = "lines";
	public String SPINNER_ICON_RIPPLE = "ripple";
	public String SPINNER_ICON_SPIRAL = "spiral";

	/**
	 * All named spinner icons. Default constant is not included.
	 */
	public String[] SPINNER_ICONS = {
		SPINNER_ICON_ANDROID, SPINNER_ICON_BUBBLES, SPINNER_ICON_CIRCLES, SPINNER_ICON_CRESCENT, SPINNER_ICON_DOTS,
		SPINNER_ICON_IOS, SPINNER_ICON_IOS_SMALL, SPINNER_ICON_LINES, SPINNER_ICON_RIPPLE, SPINNER_ICON_SPIRAL
	};

	public String EDITOR_ID_BAR_COLOR = "bar-color";
	public String EDITOR_ID_LEFT_BUTTON_CLICK = "left-button-click";
	public String EDITOR_ID_RIGHT_BUTTON_CLICK = "right-button-click";
	public String EDITOR_ID_TABS_COLOR = "tabs-color";
	public String EDITOR_ID_IS_NG_VALUE = "is-ng-value";
	public String EDITOR_ID_LEFT_MENU = "left-menu";
	public String EDITOR_ID_RIGHT_MENU = "right-menu";
	public String EDITOR_ID_LEFT_MENU_TITLE = "left-menu-title";
	public String EDITOR_ID_LEFT_ADD_MENU_TOGGLE = "left-menu-toggle";
	public String EDITOR_ID_LEFT_WIDTH = "left-menu-width";
	public String EDITOR_ID_LEFT_IS_ENABLED = "left-menu-enabled";
	public String EDITOR_ID_LIST_ITEM_LABEL = "list-item-label";
	public String EDITOR_ID_RIGHT_MENU_TITLE = "right-menu-title";
	public String EDITOR_ID_RIGHT_ADD_MENU_TOGGLE = "right-menu-toggle";
	public String EDITOR_ID_RIGHT_WIDTH = "right-menu-width";
	public String EDITOR_ID_RIGHT_IS_ENABLED = "right-menu-enabled";
	public String EDITOR_ID_DELETE_BUTTON = "delete-button";
	public String EDITOR_ID_OPTION_BUTTON = "option-button";
	public String EDITOR_ID_REORDER_BUTTON = "reorder-button";
	public String EDITOR_ID_ITEM_STYLE = "item-style";
	public String EDITOR_ID_BUTTON_SIZE = "button-size";
	public String EDITOR_ID_BUTTON_TYPE = "button-type";
	public String EDITOR_ID_BUTTON_WIDTH = "button-width";
	public String EDITOR_ID_BUTTON_FILL = "button-fill";
	public String EDITOR_ID_INPUT_LABEL_STYLE = "input-label-style";
	public String EDITOR_ID_NAV_BAR_ANIMATION = "nav-bar-animation";
	public String EDITOR_ID_NAV_VIEW_ANIMATION = "nav-view-animation";
			
	
}