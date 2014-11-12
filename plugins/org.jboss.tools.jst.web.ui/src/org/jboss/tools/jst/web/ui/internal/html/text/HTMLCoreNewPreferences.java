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
package org.jboss.tools.jst.web.ui.internal.html.text;

import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.html.core.internal.HTMLCorePlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

public class HTMLCoreNewPreferences {
	/*
	 * The constants are duplicated in order to make them compatible with any release of WTP 3.6.x
	 */
	public static final boolean IGNORE_ELEMENT_NAMES_DEFAULT = false;
	public static final String ELEMENT_NAMES_TO_IGNORE_DEFAULT = ""; //$NON-NLS-1$
	public static final String IGNORE_ELEMENT_NAMES = "ignoreElementNames";//$NON-NLS-1$
	public static final String ELEMENT_NAMES_TO_IGNORE = "elementNamesToIgnore";//$NON-NLS-1$

	private static final String MINIMUM_REQUIRED_WTP_CORE_VERSION = "1.1.801";//$NON-NLS-1$
	
	public static boolean hasRequiredAPI() {
		Bundle wstHtmlCoreBundle = Platform.getBundle(HTMLCorePlugin.ID);
		Version version = wstHtmlCoreBundle.getVersion();
		Version minRequiredVersion = Version.parseVersion(MINIMUM_REQUIRED_WTP_CORE_VERSION);
		return minRequiredVersion.compareTo(version) <= 0;
	}
}
