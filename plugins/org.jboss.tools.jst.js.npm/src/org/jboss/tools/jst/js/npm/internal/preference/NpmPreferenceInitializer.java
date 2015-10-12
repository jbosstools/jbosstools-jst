/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *       Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.jst.js.npm.internal.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.jboss.tools.jst.js.npm.NpmPlugin;
import org.jboss.tools.jst.js.npm.internal.util.NpmDetector;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class NpmPreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = NpmPlugin.getDefault().getPreferenceStore();
		String npm = NpmDetector.detectNpm();
		store.setDefault(NpmPreferenceHolder.PREF_NPM_LOCATION, ((npm != null) ? npm : "")); //$NON-NLS-1$
	}

}
