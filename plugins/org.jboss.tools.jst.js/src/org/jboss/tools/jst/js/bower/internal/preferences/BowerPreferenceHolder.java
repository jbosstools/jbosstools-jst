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
package org.jboss.tools.jst.js.bower.internal.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class BowerPreferenceHolder {
	public static final String PREF_NPM_LOCATION = "Pref_npm_Location"; //$NON-NLS-1$
	private static IPreferenceStore store;
	
	public static String getNpmLocation() {
		return store.getString(PREF_NPM_LOCATION);
	}

	public static void setNpmLocation(String location) {
		store.setValue(PREF_NPM_LOCATION, location);
	}

	public static IPreferenceStore getStore() {
		return store;
	}

	public static void setStore(IPreferenceStore store) {
		BowerPreferenceHolder.store = store;
	}

	public static void load(PropertyChangeEvent event) {
		if (event.getProperty().equals(PREF_NPM_LOCATION)) {
			String value = event.getNewValue().toString();
			setNpmLocation(value);
		}
	}

}
