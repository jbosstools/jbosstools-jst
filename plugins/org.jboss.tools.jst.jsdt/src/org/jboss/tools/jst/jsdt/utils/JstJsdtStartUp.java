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

import java.io.IOException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.service.prefs.BackingStoreException;

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
		try {
			// Check Node.js install (there should be used NON-native install
			// only)
			PreferencesSupport preferencesSupport = new PreferencesSupport(
					TernNodejsCorePlugin.PLUGIN_ID, TernNodejsCorePlugin
							.getDefault().getPluginPreferences());
			String id = preferencesSupport
					.getWorkspacePreferencesValue(TernNodejsCoreConstants.NODEJS_INSTALL);

			INodejsInstall currentInstall = id == null ? null
					: TernNodejsCorePlugin.getNodejsInstallManager()
							.findNodejsInstall(id);

			if (currentInstall == null || currentInstall.isNative()) {
				// Set up NON-Native Node.js install (this will force to use
				// OOTB
				// Node.js instance)
				INodejsInstallManager installManager = TernNodejsCorePlugin
						.getNodejsInstallManager();
				INodejsInstall[] installs = installManager.getNodejsInstalls();
				for (INodejsInstall install : installs) {
					if (!install.isNative()) {
						ScopedPreferenceStore node = new ScopedPreferenceStore(
								InstanceScope.INSTANCE,
								TernNodejsCorePlugin.PLUGIN_ID);

						node.putValue(TernNodejsCoreConstants.NODEJS_INSTALL,
								install.getId());
						node.save();
						break;
					}
				}
			}
		} catch (IOException ex) {
			Platform.getLog(Platform.getBundle(TernNodejsCorePlugin.PLUGIN_ID))
					.log(new Status(IStatus.ERROR,
							TernNodejsCorePlugin.PLUGIN_ID, ex.getMessage(), ex));
		}
	}
}
