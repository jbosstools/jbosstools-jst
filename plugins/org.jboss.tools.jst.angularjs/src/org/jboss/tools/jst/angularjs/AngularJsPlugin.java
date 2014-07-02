/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.angularjs;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.common.log.BaseUIPlugin;
import org.jboss.tools.common.projecttemplates.JarVersionObserver;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.common.zip.UnzipOperation;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class AngularJsPlugin extends BaseUIPlugin {
	public static final String PLUGIN_ID = "org.jboss.tools.jst.angularjs"; //$NON-NLS-1$

	private static AngularJsPlugin INSTANCE;

	public AngularJsPlugin() {
		INSTANCE = this;
	}

	public static AngularJsPlugin getDefault() {
		if(INSTANCE == null) {
			Platform.getBundle(PLUGIN_ID);
		}
		return INSTANCE;
	}

	@Override
	public void start(BundleContext context) throws Exception {
	    super.start(context);
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

    static boolean copiedJS = false;

	public static File getJSStateRoot() {
		if(!copiedJS) {
			copiedJS = true;
			try {
				copyJS();
			} catch (IOException e) {
				getDefault().logError(e);
			}
		}
		Bundle bundle = Platform.getBundle(PLUGIN_ID);
		return Platform.getStateLocation(bundle).toFile();
	}

	static void copyJS() throws IOException {
		Bundle bundle = Platform.getBundle(PLUGIN_ID);
		File location = Platform.getStateLocation(bundle).toFile();
		File install = FileLocator.getBundleFile(bundle);
		JarVersionObserver jarVersionObserver = new JarVersionObserver(location);
		if(install.isDirectory()) {
			copy(location, install, "js"); //$NON-NLS-1$
		} else {
			UnzipOperation unzip = new UnzipOperation(install);
			unzip.execute(location, "js.*"); //$NON-NLS-1$
		}
		jarVersionObserver.execute();
	}

	static private void copy(File location, File install, String name) {
		location = new File(location, name);
		install = new File(install, name);
		location.mkdirs();
		FileUtil.copyDir(install, location, true, true, true, null);
	}
}