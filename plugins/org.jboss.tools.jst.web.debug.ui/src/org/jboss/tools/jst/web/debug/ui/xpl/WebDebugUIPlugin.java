/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc.
 *     Red Hat, Inc.
 *******************************************************************************/
package org.jboss.tools.jst.web.debug.ui.xpl;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import org.jboss.tools.jst.web.debug.ui.internal.views.properties.xpl.WebDataProperties;

/**
 * @author Jeremy
 *
 */
public class WebDebugUIPlugin extends AbstractUIPlugin {
	//Resource bundle.
	private static ResourceBundle resourceBundle;

	public static final String PLUGIN_ID = "org.jboss.tools.jst.web.debug.ui"; 

	static WebDebugUIPlugin INSTANCE = null;
	
	/**
	 * The constructor.
	 */
	public WebDebugUIPlugin() {
		super();
		INSTANCE = this;
	}
	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		try {
			return getResourceBundle().getString(key);
		} catch (MissingResourceException e) {
			//ignore
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public static ResourceBundle getResourceBundle() {
		if (resourceBundle==null) {
			try {
				resourceBundle= ResourceBundle.getBundle("org.jboss.tools.jst.web.debug.ui.xpl.WebDebugUIResources");
			} catch (Exception x) {
				//ignore
				resourceBundle = null;
			}
		}
		return resourceBundle;
	}

	public void startup() throws CoreException {
		super.startup();
		getPluginPreferences();	
	}


	/**
	 * Returns the shared instance.
	 */
	public static WebDebugUIPlugin getDefault() {
		return INSTANCE;
	}

	public static boolean isDebugEnabled() {
		return INSTANCE.isDebugging();
	}

	public static void out(String msg) {
		if(isDebugEnabled()) System.out.println(msg);		
	}

	protected void initializeDefaultPreferences(IPreferenceStore store) {
		WebDataProperties.initializeDefaultValues(store);
	}
	
}
