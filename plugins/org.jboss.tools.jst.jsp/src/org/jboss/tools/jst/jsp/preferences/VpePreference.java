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
	public static String ATT_SHOW_COMMENTS = "show comments"; //$NON-NLS-1$
	public static String ATT_SHOW_BORDER_FOR_UNKNOWN_TAGS = "show border for unknown tags"; //$NON-NLS-1$
	public static String ATT_SHOW_BORDER_FOR_ALL_TAGS = ""; //$NON-NLS-1$
	public static String ATT_USE_DETAIL_BORDER = "show border for all tags"; //$NON-NLS-1$
	public static String ATT_SHOW_RESOURCE_BUNDLES_USAGE_AS_EL = "show resource bundles usage as EL expressions"; //$NON-NLS-1$
	public static String ATT_USE_ABSOLUTE_POSITION = ""; //$NON-NLS-1$
	public static String ATT_ALWAYS_PROMPT_FOR_TAG_ATTRIBUTES_DURING_TAG_INSERT = "always prompt for tag attributes during tag insert"; //$NON-NLS-1$
	public static String ATT_OPTION_LIST = "option list"; //$NON-NLS-1$
	public static String ATT_VISUAL_SOURCE_EDITORS_SPLITTING = "Visual/Source editors splitting"; //$NON-NLS-1$
	public static String ATT_SOURCE_VISUAL_EDITORS_WEIGHTS = "Size of Visual Editor pane 0-100%"; //$NON-NLS-1$
	public static String ATT_SHOW_SELECTION_TAG_BAR = "show selection tag bar"; //$NON-NLS-1$
	public static String ATT_ALWAYS_HIDE_SELECTION_BAR_WITHOUT_PROMT="always hide selection bar without prompt"; //$NON-NLS-1$
	public static String ATT_SHOW_INVISIBLE_TAGS="show non-visual tags"; //$NON-NLS-1$
	public static String SHOW_COMMENTS_VALUE;
	public static String VPE_EDITOR_PATH = "%Options%/Struts Studio/Editors/Visual Page Editor"; //$NON-NLS-1$
	public static final Preference SHOW_COMMENTS = new VpePreference(VPE_EDITOR_PATH, ATT_SHOW_COMMENTS);
	public static final Preference SHOW_BORDER_FOR_UNKNOWN_TAGS = new VpePreference(VPE_EDITOR_PATH, ATT_SHOW_BORDER_FOR_UNKNOWN_TAGS);
	public static final Preference SHOW_BORDER_FOR_ALL_TAGS = new VpePreference(VPE_EDITOR_PATH, ATT_SHOW_BORDER_FOR_ALL_TAGS);
	public static final Preference USE_DETAIL_BORDER = new VpePreference(VPE_EDITOR_PATH, ATT_USE_DETAIL_BORDER);
	public static final Preference SHOW_RESOURCE_BUNDLES = new VpePreference(VPE_EDITOR_PATH, ATT_SHOW_RESOURCE_BUNDLES_USAGE_AS_EL);
	public static final Preference USE_ABSOLUTE_POSITION = new VpePreference(VPE_EDITOR_PATH, ATT_USE_ABSOLUTE_POSITION);
	public static final Preference ALWAYS_REQUEST_FOR_ATTRIBUTE = new VpePreference(VPE_EDITOR_PATH, ATT_ALWAYS_PROMPT_FOR_TAG_ATTRIBUTES_DURING_TAG_INSERT);
	public static final Preference EDITOR_VIEW_OPTION = new VpePreference(VPE_EDITOR_PATH, ATT_OPTION_LIST);	
	public static final Preference VISUAL_SOURCE_EDITORS_SPLITTING= new VpePreference(VPE_EDITOR_PATH, ATT_VISUAL_SOURCE_EDITORS_SPLITTING);	
	public static final Preference SOURCE_VISUAL_EDITORS_WEIGHTS= new VpePreference(VPE_EDITOR_PATH, ATT_SOURCE_VISUAL_EDITORS_WEIGHTS);	
	public static final Preference SHOW_SELECTION_TAG_BAR = new VpePreference(VPE_EDITOR_PATH,ATT_SHOW_SELECTION_TAG_BAR);
	public static final Preference ALWAYS_HIDE_SELECTION_BAR_WITHOUT_PROMT = new VpePreference(VPE_EDITOR_PATH,ATT_ALWAYS_HIDE_SELECTION_BAR_WITHOUT_PROMT);
	public static final Preference SHOW_INVISIBLE_TAGS = new VpePreference(VPE_EDITOR_PATH,ATT_SHOW_INVISIBLE_TAGS);
	
	static {
		SHOW_COMMENTS_VALUE =SHOW_COMMENTS.getValue();
	}
	protected VpePreference(String optionPath, String attributeName) {
		super(optionPath, attributeName);
	}
}