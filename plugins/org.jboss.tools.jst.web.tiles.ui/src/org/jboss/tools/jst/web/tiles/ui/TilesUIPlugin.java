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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


public class TilesUIPlugin extends AbstractUIPlugin {
	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}
	
	public static final String PLUGIN_ID = "org.jboss.tools.jst.web.tiles.ui";
	private static TilesUIPlugin plugin;
	//public static ShowSequence rs;

	public TilesUIPlugin() {
		super();
		plugin = this;
	}

	public Shell getShell() {
		try {
			return TilesUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
		} catch(Exception e){
			log(new Status(Status.OK, PLUGIN_ID, Status.OK, "Exception:", e));
			return null;
		}
	}

	public static TilesUIPlugin getDefault() {
		return plugin;
	}

	public boolean isDebugEnabled() {
		return plugin.isDebugging();
	}
	
	public void log(String msg) {
		if(isDebugEnabled()) plugin.getLog().log(new Status(Status.INFO, PLUGIN_ID, Status.OK, msg, null));		
	}
	
	public void log(IStatus status) {
		if(isDebugEnabled() || !status.isOK()) plugin.getLog().log(status);
	}
	
	public void log(String message, Exception exception) {
		getLog().log(new Status(Status.ERROR, PLUGIN_ID, Status.OK, message, exception));		
	}
	
	public void log(Exception ex) {
		getLog().log(new Status(Status.ERROR, PLUGIN_ID, Status.OK, "No message", ex));
	}
}
