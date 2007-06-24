/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.jsp.preferences;

import org.jboss.tools.common.model.options.Preference;

public class VpePreference extends Preference {
	public static String ATT_SHOW_COMMENTS = "show comments";
	public static String ATT_SHOW_BORDER_FOR_UNKNOWN_TAGS = "show border for unknown tags";
	public static String ATT_SHOW_BORDER_FOR_ALL_TAGS = "";
	public static String ATT_USE_DETAIL_BORDER = "show border for all tags";
	public static String ATT_SHOW_RESOURCE_BUNDLES_USAGE_AS_EL = "show resource bundles usage as EL expressions";
	public static String ATT_USE_ABSOLUTE_POSITION = "";
	public static String ATT_ALWAYS_PROMPT_FOR_TAG_ATTRIBUTES_DURING_TAG_INSERT = "always prompt for tag attributes during tag insert";
	public static String ATT_OPTION_LIST = "option list";

	public static String VPE_EDITOR_PATH = "%Options%/Struts Studio/Editors/Visual Page Editor"; //$NON-NLS-1$
	public static final Preference SHOW_COMMENTS = new VpePreference(VPE_EDITOR_PATH, ATT_SHOW_COMMENTS);
	public static final Preference SHOW_BORDER_FOR_UNKNOWN_TAGS = new VpePreference(VPE_EDITOR_PATH, ATT_SHOW_BORDER_FOR_UNKNOWN_TAGS);
	public static final Preference SHOW_BORDER_FOR_ALL_TAGS = new VpePreference(VPE_EDITOR_PATH, ATT_SHOW_BORDER_FOR_ALL_TAGS);
	public static final Preference USE_DETAIL_BORDER = new VpePreference(VPE_EDITOR_PATH, ATT_USE_DETAIL_BORDER);
	public static final Preference SHOW_RESOURCE_BUNDLES = new VpePreference(VPE_EDITOR_PATH, ATT_SHOW_RESOURCE_BUNDLES_USAGE_AS_EL);
	public static final Preference USE_ABSOLUTE_POSITION = new VpePreference(VPE_EDITOR_PATH, ATT_USE_ABSOLUTE_POSITION);
	public static final Preference ALWAYS_REQUEST_FOR_ATTRIBUTE = new VpePreference(VPE_EDITOR_PATH, ATT_ALWAYS_PROMPT_FOR_TAG_ATTRIBUTES_DURING_TAG_INSERT);
	public static final Preference EDITOR_VIEW_OPTION = new VpePreference(VPE_EDITOR_PATH, ATT_OPTION_LIST);	

	protected VpePreference(String optionPath, String attributeName) {
		super(optionPath, attributeName);
	}
}