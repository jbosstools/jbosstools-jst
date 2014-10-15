/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *       Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.jst.jsdt;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.osgi.framework.BundleContext;

public class JstJsdtPlugin extends AbstractUIPlugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.jst.jsdt"; //$NON-NLS-1$

	public static final String SHOULD_OVERRIDE_STRICT_ON_KEYWORD_USAGE_OPTION_VALUE = "shouldOverrideStrictOnKeywordUsage";
	
	// Copy the preference name here due not to depend on new JSDT Core version
	public static final String JSDT_COMPILER_STRICT_ON_KEYWORD_USAGE_OPTION = "strictOnKeywordUsage";

	// The shared instance
	private static JstJsdtPlugin plugin;

	/**
	 * The constructor
	 */
	public JstJsdtPlugin() {
		plugin = this;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static JstJsdtPlugin getDefault() {
		return plugin;
	}

	boolean shouldOverrideStrictOnKeywordUsageOptionValue = true;
	Preferences.IPropertyChangeListener propertyListener;
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		IEclipsePreferences preferences = ((IScopeContext) new InstanceScope()).getNode(JstJsdtPlugin.PLUGIN_ID);
		String value = preferences.get(SHOULD_OVERRIDE_STRICT_ON_KEYWORD_USAGE_OPTION_VALUE, JavaScriptCore.ENABLED);
		if (!JavaScriptCore.DISABLED.equals(value)) {
			// Listen to preference changes
			propertyListener = new Preferences.IPropertyChangeListener() {
				public void propertyChange(Preferences.PropertyChangeEvent event) {
					if (JSDT_COMPILER_STRICT_ON_KEYWORD_USAGE_OPTION.equals(event.getProperty())) {
						// Stop listening
						JavaScriptCore.getPlugin().getPluginPreferences().removePropertyChangeListener(propertyListener);
						propertyListener = null;
						
						// And stop overriding
						IEclipsePreferences preferences = ((IScopeContext) new InstanceScope()).getNode(JstJsdtPlugin.PLUGIN_ID);
						preferences.put(SHOULD_OVERRIDE_STRICT_ON_KEYWORD_USAGE_OPTION_VALUE, JavaScriptCore.DISABLED);
					}
				}
			};
			
			// Do override the value of JSDT 'strict keyword validation' option
			IEclipsePreferences jsdtPreferences = ((IScopeContext) new InstanceScope()).getNode(JavaScriptCore.PLUGIN_ID);
			jsdtPreferences.put(JSDT_COMPILER_STRICT_ON_KEYWORD_USAGE_OPTION, JavaScriptCore.DISABLED);
			
			// Start listening on JSDT preferences
			JavaScriptCore.getPlugin().getPluginPreferences().addPropertyChangeListener(propertyListener);
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (propertyListener != null) {
			JavaScriptCore.getPlugin().getPluginPreferences().removePropertyChangeListener(propertyListener);
		}
		super.stop(context);
	}
}
