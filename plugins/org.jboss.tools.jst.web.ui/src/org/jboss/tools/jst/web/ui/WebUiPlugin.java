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
package org.jboss.tools.jst.web.ui;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.common.log.BaseUIPlugin;
import org.jboss.tools.common.log.IPluginLog;
import org.osgi.framework.BundleContext;

public class WebUiPlugin extends BaseUIPlugin {
	public static final String PLUGIN_ID = "org.jboss.tools.jst.web.ui"; //$NON-NLS-1$
	static WebUiPlugin INSTANCE;
	
	public WebUiPlugin() {
		INSTANCE = this;
	}

	public static WebUiPlugin getDefault() {
		if(INSTANCE == null) {
			Platform.getBundle(PLUGIN_ID);
		}
		return INSTANCE;
	}
	
	public void start(BundleContext context) throws Exception {
	    super.start(context);
	}
	
	public static boolean isDebugEnabled() {
		return INSTANCE != null && INSTANCE.isDebugging();
	}
	
	public static Shell getShell() {
		return (INSTANCE == null) ? null : INSTANCE.getWorkbench().getActiveWorkbenchWindow().getShell();
	}
	
	/**
	 * @return IPluginLog object
	 */
	public static IPluginLog getPluginLog() {
		return getDefault();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.log.BaseUIPlugin#getId()
	 */
	@Override
	public String getId() {
		return PLUGIN_ID;
	}
}