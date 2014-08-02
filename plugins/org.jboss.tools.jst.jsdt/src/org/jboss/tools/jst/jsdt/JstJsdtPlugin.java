/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.jsdt;

import org.jboss.tools.common.log.BaseUIPlugin;

public class JstJsdtPlugin extends BaseUIPlugin {
	
	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.jst.jsdt"; //$NON-NLS-1$

	// The shared instance
	private static JstJsdtPlugin plugin;

	/**
	 * The constructor
	 */
	public JstJsdtPlugin() {
		plugin = this;
	}

	public static JstJsdtPlugin getDefault() {
		return plugin; 
	}

}
