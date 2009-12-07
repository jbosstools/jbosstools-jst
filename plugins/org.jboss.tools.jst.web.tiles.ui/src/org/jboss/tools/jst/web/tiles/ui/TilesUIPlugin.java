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
package org.jboss.tools.jst.web.tiles.ui;

import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.common.log.BaseUIPlugin;
import org.jboss.tools.common.log.IPluginLog;
import org.osgi.framework.BundleContext;


public class TilesUIPlugin extends BaseUIPlugin {
	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}
	
	public static final String PLUGIN_ID = "org.jboss.tools.jst.web.tiles.ui"; //$NON-NLS-1$
	private static TilesUIPlugin plugin;
	//public static ShowSequence rs;

	public TilesUIPlugin() {
		super();
		plugin = this;
	}

	public Shell getShell() {
		return TilesUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	public static TilesUIPlugin getDefault() {
		return plugin;
	}

	public boolean isDebugEnabled() {
		return plugin.isDebugging();
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