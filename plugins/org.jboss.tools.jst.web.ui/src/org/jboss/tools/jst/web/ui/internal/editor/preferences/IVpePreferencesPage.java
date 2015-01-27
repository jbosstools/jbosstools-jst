/*******************************************************************************
 * Copyright (c) 2007-2009 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.internal.editor.preferences;

public interface IVpePreferencesPage {

	static final String SHOW_BORDER_FOR_UNKNOWN_TAGS = "Show Border for Unknown Tags"; //$NON-NLS-1$
	static final String SHOW_NON_VISUAL_TAGS = "Show non-visual tags"; //$NON-NLS-1$
	static final String SHOW_SELECTION_TAG_BAR = "Show Selection Tag Bar"; //$NON-NLS-1$
	static final String SHOW_TEXT_FORMATTING = "Show Text Formatting bar"; //$NON-NLS-1$
	static final String SHOW_RESOURCE_BUNDLES_USAGE_AS_EL = "Show Resource Bundles Usage as EL Expressions"; //$NON-NLS-1$
	static final String SELECTION_VISIBLE_BORDER_COLOR = "Selection border color for visible element"; //$NON-NLS-1$
	static final String SELECTION_HIDDEN_BORDER_COLOR = "Selection border color for hidden element"; //$NON-NLS-1$
	static final String ASK_TAG_ATTRIBUTES_ON_TAG_INSERT = "Ask for tag attributes during tag insert"; //$NON-NLS-1$
	static final String INFORM_WHEN_PROJECT_MIGHT_NOT_BE_CONFIGURED_PROPERLY_FOR_VPE = "Inform when a project might not be configured properly for Visual Page Editor"; //$NON-NLS-1$
	static final String DEFAULT_VPE_TAB = "Default VPE Tab"; //$NON-NLS-1$
	static final String VISUAL_SOURCE_EDITORS_SPLITTING = "Visual/Source Editors Splitting"; //$NON-NLS-1$
	static final String VISUAL_SOURCE_EDITORS_WEIGHTS = "Size of Visual Editor Pane 0-100%"; //$NON-NLS-1$
	static final String SYNCHRONIZE_SCROLLING_BETWEEN_SOURCE_VISUAL_PANES = "Synchronize scrolling between source and visual panes"; //$NON-NLS-1$
	static final String SHOW_VISUAL_TOOLBAR = "Show VPE Toolbar"; //$NON-NLS-1$
	
	static final String USE_VISUAL_EDITOR_FOR_HTML5 = "Use visual editor for html5 editing"; //$NON-NLS-1$
	static final String REMEMBER_VISUAL_EDITOR_ENGINE = "Remember visual editor engine"; //$NON-NLS-1$
	
	static final String DEFAULT_VPE_TAB_VISUAL_SOURCE_VALUE = "0"; //$NON-NLS-1$
	static final String DEFAULT_VPE_TAB_SOURCE_VALUE = "1"; //$NON-NLS-1$
	static final String DEFAULT_VPE_TAB_PREVIEW_VALUE = "2"; //$NON-NLS-1$
	
	static final String SPLITTING_VERT_TOP_SOURCE_VALUE = "1"; //$NON-NLS-1$
	static final String SPLITTING_VERT_TOP_VISUAL_VALUE = "2"; //$NON-NLS-1$
	static final String SPLITTING_HORIZ_LEFT_SOURCE_VALUE = "3"; //$NON-NLS-1$
	static final String SPLITTING_HORIZ_LEFT_VISUAL_VALUE = "4"; //$NON-NLS-1$
	
	static final int DEFAULT_VISUAL_SOURCE_EDITORS_WEIGHTS = 500;
}
