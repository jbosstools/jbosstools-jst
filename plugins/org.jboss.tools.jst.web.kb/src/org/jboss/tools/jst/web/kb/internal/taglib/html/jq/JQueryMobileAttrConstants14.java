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
package org.jboss.tools.jst.web.kb.internal.taglib.html.jq;

import org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttribute;

/**
 * @author Alexey Kazakov
 */
public class JQueryMobileAttrConstants14 extends JQueryMobileAttrConstants {

	public static final String[] ENUM_ICON_VALUES = new String[] {
		"action", "alert", "arrow-d", "arrow-d-l", "arrow-d-r", "arrow-l", "arrow-r", "arrow-u", "arrow-u-l", "arrow-u-r",
		"audio", "back", "bars", "bullets", "calendar", "camera", "carat-d", "carat-l", "carat-r",  "carat-u", 
		"check", "clock", "cloud", "comment", "custom", "delete", "edit", "eye", "forbidden", "forward", 
		"gear", "grid", "heart", "home", "info", "location", "lock", "mail", "minus", "navigation", 
		"phone", "plus", "power", "recycle", "refresh", "search", "shop", "star", "tag", "user", "video"
	};

	public static final HtmlAttribute DATA_ICON_ATTRIBUTE = new HtmlAttribute("data-icon", "", ENUM_ICON_VALUES);
	public static final HtmlAttribute DATA_COLLAPSED_ICON_ATTRIBUTE = new HtmlAttribute("data-collapsed-icon", "", ENUM_ICON_VALUES);
	public static final HtmlAttribute DATA_EXPANDED_ICON_ATTRIBUTE = new HtmlAttribute("data-expanded-icon", "", ENUM_ICON_VALUES);
	public static final HtmlAttribute DATA_SPLIT_ICON_ATTRIBUTE = new HtmlAttribute("data-split-icon", "", ENUM_ICON_VALUES);
	public static final HtmlAttribute DATA_DIALOG_ATTRIBUTE = new HtmlAttribute("data-dialog", "", new String[]{"true"});

	public static final HtmlAttribute DATA_EXCLUDE_INVISIBLE_ATTRIBUTE = new HtmlAttribute("data-exclude-invisible", "Sets whether to exclude invisible children in the assignment of rounded corners", ENUM_TRUE_FALSE);

	public static final String UI_BTN_CLASS = "ui-btn";

	protected JQueryMobileAttrConstants14() {
	}
}