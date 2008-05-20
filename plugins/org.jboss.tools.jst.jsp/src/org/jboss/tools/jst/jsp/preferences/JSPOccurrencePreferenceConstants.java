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

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jst.jsp.ui.internal.JSPUIPlugin;
import org.jboss.tools.jst.jsp.preferences.xpl.PreferenceKeyGenerator;
import org.jboss.tools.jst.jsp.preferences.xpl.XMLOccurrencePreferenceConstants;

/**
 * @author Jeremy
 */
public class JSPOccurrencePreferenceConstants extends XMLOccurrencePreferenceConstants {

	/**
	 * Initializes the given preference store with the default values.
	 * @param store the preference store to be initialized
	 */
	public static void initializeDefaultValues(IPreferenceStore store) {
		// set the default values from AbstractDecoratedTextEditor

		store.setDefault(PreferenceKeyGenerator.generateKey(EDITOR_MARK_OCCURRENCES,JSPUIPlugin.ID), false);
		store.setDefault(PreferenceKeyGenerator.generateKey(EDITOR_STICKY_OCCURRENCES,JSPUIPlugin.ID), true);
		store.setDefault(PreferenceKeyGenerator.generateKey(EDITOR_OCCURRENCE_PROVIDER,JSPUIPlugin.ID), "org.jboss.tools.jst.jsp.defaultOccurenceProvider"); //$NON-NLS-1$

		store.setDefault(PreferenceKeyGenerator.generateKey(EDITOR_MARK_NODE_OCCURRENCES,JSPUIPlugin.ID), true);
		store.setDefault(PreferenceKeyGenerator.generateKey(EDITOR_MARK_ATTRIBUTE_OCCURRENCES,JSPUIPlugin.ID), true);
		store.setDefault(PreferenceKeyGenerator.generateKey(EDITOR_MARK_ATTRIBUTE_VALUE_OCCURRENCES,JSPUIPlugin.ID), true);
		store.setDefault(PreferenceKeyGenerator.generateKey(EDITOR_MARK_TEXT_OCCURRENCES,JSPUIPlugin.ID), true);
	}

	public static boolean affectsPreferences(String property) {
		return (PreferenceKeyGenerator.generateKey(EDITOR_MARK_OCCURRENCES,JSPUIPlugin.ID).equals(property) ||
			PreferenceKeyGenerator.generateKey(EDITOR_STICKY_OCCURRENCES,JSPUIPlugin.ID).equals(property) ||
			PreferenceKeyGenerator.generateKey(EDITOR_OCCURRENCE_PROVIDER,JSPUIPlugin.ID).equals(property) ||
			PreferenceKeyGenerator.generateKey(EDITOR_MARK_NODE_OCCURRENCES,JSPUIPlugin.ID).equals(property) ||
			PreferenceKeyGenerator.generateKey(EDITOR_MARK_ATTRIBUTE_OCCURRENCES,JSPUIPlugin.ID).equals(property) ||
			PreferenceKeyGenerator.generateKey(EDITOR_MARK_ATTRIBUTE_VALUE_OCCURRENCES,JSPUIPlugin.ID).equals(property) ||
			PreferenceKeyGenerator.generateKey(EDITOR_MARK_TEXT_OCCURRENCES,JSPUIPlugin.ID).equals(property));
	}
}