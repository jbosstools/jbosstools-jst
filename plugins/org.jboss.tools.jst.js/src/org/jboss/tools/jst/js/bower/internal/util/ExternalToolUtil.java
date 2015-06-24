/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.js.bower.internal.util;

import java.io.File;

import org.jboss.tools.jst.js.bower.internal.BowerConstants;
import org.jboss.tools.jst.js.bower.internal.preference.BowerPreferenceHolder;
import org.jboss.tools.jst.js.util.PlatformUtil;

/**
 * @author Ilya Buziuk (ibuziuk)
 */
public final class ExternalToolUtil {
	
	public static String getBowerExecutableLocation() {
		String bowerExecutableLocation = null;
		File bowerExecutable = new File(BowerPreferenceHolder.getBowerLocation(), BowerConstants.BOWER);
		if (bowerExecutable != null && bowerExecutable.exists()) {
			bowerExecutableLocation = bowerExecutable.getAbsolutePath();
		}
		return bowerExecutableLocation;
	}
	
	public static String getNodeExecutableLocation() {
		String nodeExecutableLocation = null;
		String nodeExecutableName = getNodeExecutableName();
		File nodeExecutable = new File(BowerPreferenceHolder.getNodeLocation(), nodeExecutableName);
		if (nodeExecutable != null && nodeExecutable.exists()) {
			nodeExecutableLocation = nodeExecutable.getAbsolutePath();
		}
		return nodeExecutableLocation;
	}
	
	public static String getNodeExecutableName() {
		return (PlatformUtil.isWindows()) ? BowerConstants.NODE_EXE : BowerConstants.NODE;
	}
	
}
