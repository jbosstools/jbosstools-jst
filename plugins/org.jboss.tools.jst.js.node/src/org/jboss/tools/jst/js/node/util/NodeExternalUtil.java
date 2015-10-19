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
package org.jboss.tools.jst.js.node.util;

import java.io.File;

import org.jboss.tools.common.util.PlatformUtil;
import org.jboss.tools.jst.js.node.Constants;
import org.jboss.tools.jst.js.node.preference.NodePreferenceHolder;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public final class NodeExternalUtil {
	
	public static String getNodeExecutableLocation() {
		String nodeExecutableLocation = null;
		String nodeExecutableName = getNodeExecutableName();
		File nodeExecutable = new File(NodePreferenceHolder.getNodeLocation(), nodeExecutableName);
		
		if (nodeExecutable.exists()) {
			nodeExecutableLocation = nodeExecutable.getAbsolutePath();
		} else if (!PlatformUtil.isMacOS()) {
			
			// JBIDE-20351 Bower tooling doesn't detect node when the binary is called 'nodejs'
			// If "nodejs" is not detected try to detect "node"
			if (PlatformUtil.isLinux()) {
				nodeExecutableName = Constants.NODE;
				
			//JBIDE-20988 Preference validation fails on windows if node executable called node64.exe
			} else if (PlatformUtil.isWindows()) {
				nodeExecutableName = Constants.NODE_64_EXE;
			}
			
			nodeExecutable = new File(NodePreferenceHolder.getNodeLocation(), nodeExecutableName);
			if (nodeExecutable.exists()) {
				nodeExecutableLocation = nodeExecutable.getAbsolutePath();
			}				
		}
		return nodeExecutableLocation;
	}
	
	public static String getNodeExecutableName() {
		String name = null;
		switch(PlatformUtil.getOs()) {
			case WINDOWS:
				name = Constants.NODE_EXE;	
				break;
				
			case MACOS:
				name = Constants.NODE;
				break;
				
			case LINUX:
				name = Constants.NODE_JS;
				break;
			
			case OTHER:
				name = Constants.NODE;
				break;
		}
		return name;
	}

}
