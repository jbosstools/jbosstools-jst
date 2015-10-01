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
package org.jboss.tools.jst.js.node.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.jboss.tools.jst.js.node.Activator;
import org.jboss.tools.jst.js.node.util.NodeDetector;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class NodePreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String nodeLocation = NodeDetector.detectNode();
		store.setDefault(NodePreferenceHolder.PREF_NODE_LOCATION, ((nodeLocation != null) ? nodeLocation : "")); //$NON-NLS-1$
	}

}
