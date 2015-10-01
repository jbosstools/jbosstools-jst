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
		} else if (PlatformUtil.isLinux()) {
			// JBIDE-20351 Bower tooling doesn't detect node when the binary is called 'nodejs'
			// If "nodejs" is not detected try to detect "node"
			nodeExecutableName = Constants.NODE; 
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
