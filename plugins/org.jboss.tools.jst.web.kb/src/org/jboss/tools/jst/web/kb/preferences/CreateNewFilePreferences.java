/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.kb.preferences;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.jboss.tools.jst.web.kb.WebKbPlugin;

public class CreateNewFilePreferences {
	public static final String CREATE_NEW_FILE_USE_WIZARD = "org.jboss.tools.jst.web.kb.preferences.useWizard"; //$NON-NLS-1$

	public static boolean isWizardUsed() {
		IEclipsePreferences p = InstanceScope.INSTANCE.getNode(WebKbPlugin.PLUGIN_ID);
		boolean result = (p == null) ? true : p.getBoolean(CREATE_NEW_FILE_USE_WIZARD, true);
		return result;
	}
	
	public static void setWizardUsed(boolean useWizard) {
		IEclipsePreferences p = InstanceScope.INSTANCE.getNode(WebKbPlugin.PLUGIN_ID);
		if(p != null){
			p.putBoolean(CREATE_NEW_FILE_USE_WIZARD, useWizard);
		}
	}
}
