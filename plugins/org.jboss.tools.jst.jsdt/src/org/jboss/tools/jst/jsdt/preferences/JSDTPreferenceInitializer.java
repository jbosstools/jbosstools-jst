/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.jst.jsdt.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.jboss.tools.jst.jsdt.JstJsdtPlugin;

public class JSDTPreferenceInitializer extends AbstractPreferenceInitializer {
	public JSDTPreferenceInitializer() {
	}

	@Override
	public void initializeDefaultPreferences() {
		IEclipsePreferences defaultPreferences = ((IScopeContext) new DefaultScope())
				.getNode(JstJsdtPlugin.PLUGIN_ID);
		defaultPreferences
				.put(JstJsdtPlugin.SHOULD_OVERRIDE_STRICT_ON_KEYWORD_USAGE_OPTION_VALUE,
						JavaScriptCore.ENABLED);
	}
}
