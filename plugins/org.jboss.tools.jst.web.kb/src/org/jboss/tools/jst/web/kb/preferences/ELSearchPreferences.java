/*******************************************************************************
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.kb.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.jboss.tools.jst.web.kb.WebKbPlugin;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class ELSearchPreferences {
	public static final String EL_SEARCH_TIME_LIMIT = "org.jboss.tools.jst.web.kb.preferences.elSearchTimeLimit"; //$NON-NLS-1$

	public static long getElSearchTimeLimit(IProject project) {
		IEclipsePreferences p = new ProjectScope(project).getNode(WebKbPlugin.PLUGIN_ID);
		String result = (p == null) ? null : p.get(EL_SEARCH_TIME_LIMIT, null);
		if(result == null) {
			p = InstanceScope.INSTANCE.getNode(WebKbPlugin.PLUGIN_ID);
			result = (p == null) ? null : p.get(EL_SEARCH_TIME_LIMIT, null);
		}
		if(result == null) {
			result = DefaultScope.INSTANCE.getNode(WebKbPlugin.PLUGIN_ID).get(EL_SEARCH_TIME_LIMIT, "60"); //$NON-NLS-1$
		}
		try {
			return Integer.parseInt(result) * 1000;
		} catch (NumberFormatException e) {
			WebKbPlugin.getDefault().logError(e);
		}
		return 60000;
	}
}
