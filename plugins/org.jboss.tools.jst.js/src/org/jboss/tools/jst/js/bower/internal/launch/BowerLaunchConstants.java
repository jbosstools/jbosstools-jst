/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *       Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.jst.js.bower.internal.launch;

import org.jboss.tools.jst.js.bower.internal.launch.type.BowerInitLaunchConfigurationDelegate;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public final class BowerLaunchConstants {

	public BowerLaunchConstants() {
	}

	public static final String LAUNCH_CONFIGURATION_TYPE_ID = BowerInitLaunchConfigurationDelegate.ID;
	public static final String ID_EXTERNAL_TOOLS_LAUNCH_GROUP = "org.eclipse.ui.externaltools.launchGroup"; //$NON-NLS-1$
	public static final String ATTR_BOWER_DIR = "ATTR_BOWER_DIR"; //$NON-NLS-1$
	public static final String ATTR_BOWER_NAME = "ATTR_BOWER_NAME"; //$NON-NLS-1$
	public static final String ATTR_BOWER_VERSION = "ATTR_BOWER_VERSION"; //$NON-NLS-1$
	public static final String ATTR_BOWER_LICENSE = "ATTR_BOWER_LICENSE"; //$NON-NLS-1$
	public static final String ATTR_BOWER_AUTHORS = "ATTR_BOWER_AUTHORS"; //$NON-NLS-1$
	public static final String ATTR_BOWER_IGNORE = "ATTR_BOWER_IGNORE"; //$NON-NLS-1$
}
