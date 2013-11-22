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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.RGB;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

public class VpePreferencesInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IEclipsePreferences defaultPreferences = ((IScopeContext) new DefaultScope()).getNode(WebUiPlugin.PLUGIN_ID);
		defaultPreferences.putBoolean(IVpePreferencesPage.SHOW_VISUAL_TOOLBAR, false);
		defaultPreferences.putBoolean(IVpePreferencesPage.SHOW_BORDER_FOR_UNKNOWN_TAGS, true);
		defaultPreferences.putBoolean(IVpePreferencesPage.SHOW_NON_VISUAL_TAGS, false);
		defaultPreferences.putBoolean(IVpePreferencesPage.SHOW_SELECTION_TAG_BAR, true);
		defaultPreferences.putBoolean(IVpePreferencesPage.SHOW_TEXT_FORMATTING, true);
		defaultPreferences.putBoolean(IVpePreferencesPage.SHOW_RESOURCE_BUNDLES_USAGE_AS_EL, false);
		defaultPreferences.put(IVpePreferencesPage.SELECTION_VISIBLE_BORDER_COLOR, 
				StringConverter.asString(new RGB(0, 0, 255)));
		defaultPreferences.put(IVpePreferencesPage.SELECTION_HIDDEN_BORDER_COLOR, 
				StringConverter.asString(new RGB(255, 0, 0)));
		defaultPreferences.putBoolean(IVpePreferencesPage.ASK_TAG_ATTRIBUTES_ON_TAG_INSERT, true);
		defaultPreferences.putBoolean(IVpePreferencesPage.INFORM_WHEN_PROJECT_MIGHT_NOT_BE_CONFIGURED_PROPERLY_FOR_VPE, true);
		defaultPreferences.put(IVpePreferencesPage.DEFAULT_VPE_TAB, IVpePreferencesPage.DEFAULT_VPE_TAB_VISUAL_SOURCE_VALUE);
		defaultPreferences.put(IVpePreferencesPage.VISUAL_SOURCE_EDITORS_SPLITTING, IVpePreferencesPage.SPLITTING_VERT_TOP_SOURCE_VALUE);
		defaultPreferences.putBoolean(IVpePreferencesPage.SYNCHRONIZE_SCROLLING_BETWEEN_SOURCE_VISUAL_PANES, false);
		defaultPreferences.putInt(IVpePreferencesPage.VISUAL_SOURCE_EDITORS_WEIGHTS, IVpePreferencesPage.DEFAULT_VISUAL_SOURCE_EDITORS_WEIGHTS);
	}
}