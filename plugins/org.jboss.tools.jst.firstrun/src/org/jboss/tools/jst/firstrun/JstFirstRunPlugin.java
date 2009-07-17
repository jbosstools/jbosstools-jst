/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.firstrun;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author eskimo
 *
 */
public class JstFirstRunPlugin extends AbstractUIPlugin {
	
	public static final String PLUGIN_ID = "org.jboss.tools.jst.firstrun"; //$NON-NLS-1$

	static private JstFirstRunPlugin instance;
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;
	}
	
	/**
	 * @return
	 */
	public static ILog getPluginLog() {
		return instance.getLog();
	}
		
	/**
	 * 
	 * @return
	 */
	public static JstFirstRunPlugin getDefault() {
		return instance;
	}
}
