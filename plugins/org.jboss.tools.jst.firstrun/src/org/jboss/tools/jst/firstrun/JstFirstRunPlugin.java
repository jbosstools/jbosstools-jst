/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 

package org.jboss.tools.jst.firstrun;

import org.jboss.tools.common.log.BaseUIPlugin;
import org.jboss.tools.common.log.IPluginLog;
import org.osgi.framework.BundleContext;

/**
 * @author eskimo
 *
 */
public class JstFirstRunPlugin extends BaseUIPlugin {

	static private JstFirstRunPlugin instance;
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;
	}
	
	/**
	 * @return
	 */
	public static IPluginLog getPluginLog() {
		return (IPluginLog)instance;
	}
		
	/**
	 * 
	 * @return
	 */
	public static JstFirstRunPlugin getDefault() {
		return instance;
	}
}
