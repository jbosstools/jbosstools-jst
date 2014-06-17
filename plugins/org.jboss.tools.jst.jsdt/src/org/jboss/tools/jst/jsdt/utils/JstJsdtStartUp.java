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
package org.jboss.tools.jst.jsdt.utils;

import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.ui.IStartup;

import tern.eclipse.ide.core.preferences.PreferencesSupport;
import tern.eclipse.ide.server.nodejs.core.INodejsInstall;
import tern.eclipse.ide.server.nodejs.core.INodejsInstallManager;
import tern.eclipse.ide.server.nodejs.core.TernNodejsCoreConstants;
import tern.eclipse.ide.server.nodejs.core.TernNodejsCorePlugin;

/**
 * 
 * @author Victor V. Rubezhny
 *
 */
public class JstJsdtStartUp implements IStartup {

	@Override
	public void earlyStartup() {
		// Check Node.js install (there should be used NON-native install only)
		PreferencesSupport preferencesSupport = new PreferencesSupport(
				TernNodejsCorePlugin.PLUGIN_ID, TernNodejsCorePlugin
						.getDefault().getPluginPreferences());
		String id = preferencesSupport
				.getWorkspacePreferencesValue(TernNodejsCoreConstants.NODEJS_INSTALL);

		INodejsInstall currentInstall = TernNodejsCorePlugin
				.getNodejsInstallManager().findNodejsInstall(id);

		if (currentInstall.isNative()) {
			// Set up NON-Native Node.js install (this will force to use OOTB
			// Node.js instance)
			INodejsInstallManager installManager = TernNodejsCorePlugin
					.getNodejsInstallManager();
			INodejsInstall[] installs = installManager.getNodejsInstalls();
			for (INodejsInstall install : installs) {
				if (!install.isNative()) {
					IEclipsePreferences node = new DefaultScope()
							.getNode(TernNodejsCorePlugin.PLUGIN_ID);

					node.put(TernNodejsCoreConstants.NODEJS_INSTALL,
							install.getId());
					break;
				}
			}
		}
	}

}
