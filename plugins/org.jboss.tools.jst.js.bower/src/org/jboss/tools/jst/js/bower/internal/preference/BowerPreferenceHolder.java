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
package org.jboss.tools.jst.js.bower.internal.preference;

import org.eclipse.jface.preference.IPreferenceStore;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class BowerPreferenceHolder {
	public static final String PREF_BOWER_LOCATION = "Pref_Bower_Location"; //$NON-NLS-1$
	
	private static IPreferenceStore store;
		
	public static String getBowerLocation() {
		return store.getString(PREF_BOWER_LOCATION);
	}

	public static void setBowerLocation(String location) {
		store.setValue(PREF_BOWER_LOCATION, location);
	}

	public static IPreferenceStore getStore() {
		return store;
	}

	public static void setStore(IPreferenceStore store) {
		BowerPreferenceHolder.store = store;
	}
	
}
