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
package org.jboss.tools.jst.web.debug;

import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class WebDebugPlugin extends AbstractUIPlugin {
	static WebDebugPlugin INSTANCE = null;

	public static final String PLUGIN_ID = "org.jboss.tools.jst.web.debug";

	public WebDebugPlugin(IPluginDescriptor descriptor)	{
		super(descriptor);
		INSTANCE = this;
	}

	public static WebDebugPlugin getDefault() {
		return INSTANCE;
	}

    public static boolean isDebugEnabled() {
        return getDefault().isDebugging();
    }

    public static void printStackTrace(Throwable throwable) {
        if (isDebugEnabled() && throwable != null) throwable.printStackTrace();
    }

    public static void println(String str) {
        if (isDebugEnabled()) {
            if (str != null) System.out.println(str);
            else System.out.println();
        }
    }

	public static Shell getShell() {
		return WebDebugPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
	}
}