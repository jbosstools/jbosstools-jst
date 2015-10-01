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
import static org.jboss.tools.jst.js.node.Constants.*;

/**
 * @author Ilya Buziuk (ibuziuk)
 */
public final class NodeDetector {
	private static final String USR_LOCAL_BIN = "/usr/local/bin"; //$NON-NLS-1$
	private static final String USR_BIN = "/usr/bin"; //$NON-NLS-1$

	private NodeDetector() {
	}

	public static String detectNode() {
		String nodeLocation = detectFromPath(File.separator + NODE_JS);
		if (nodeLocation == null) {
			// Try to detect in "usr/local/bin" and "usr/bin" for Mac & Linux 
			if (PlatformUtil.isLinux()) {
				nodeLocation = detectNodeOnLinux();
			} else if (PlatformUtil.isMacOS()) {	
				nodeLocation = detectNodeOnMac();
			}
		}
		return nodeLocation;
	}

	/**
	 * JBIDE-20351 Bower tooling doesn't detect node when the binary is called 'nodejs'
	 * There is a naming conflict with the node package (Amateur Packet Radio Node Program), and the nodejs binary. 
	 * On Linux "nodejs" availability must be checked firstly. 
	 * 
	 * @see <a href="http://packages.ubuntu.com/trusty/node">Amateur Packet Radio Node</a>
	 * @see <a href="https://issues.jboss.org/browse/JBIDE-20351">JBIDE-20351</a>
	 */
	private static String detectNodeOnLinux() {
		String nodeLocation = null;
		File usrLocalBin = new File(USR_LOCAL_BIN);
		File usrBin = new File(USR_BIN);
		
		// detect "nodejs" binary
		if (isDetected(usrLocalBin, NODE_JS)) {
			nodeLocation = usrLocalBin.getAbsolutePath();
		} else if (isDetected(usrBin, NODE_JS)) {
			nodeLocation = usrBin.getAbsolutePath();
		}
		
		// detect "node" binary if "nodejs" was not detected 
		if (nodeLocation == null) {
			if (isDetected(usrLocalBin, NODE)) {
				nodeLocation = usrLocalBin.getAbsolutePath();
			} else if (isDetected(usrBin, NODE)) {
				nodeLocation = usrBin.getAbsolutePath();
			}
		}
		return nodeLocation;
	}

	private static String detectNodeOnMac() {
		String nodeLocation = null;
		File usrLocalBin = new File(USR_LOCAL_BIN);
		File usrBin = new File(USR_BIN);
		if (isDetected(usrLocalBin, NODE)) {
			nodeLocation = usrLocalBin.getAbsolutePath();
		} else if (isDetected(usrBin, NODE)) {
			nodeLocation = usrBin.getAbsolutePath();
		}
		return nodeLocation;
	}

	
	private static boolean isDetected(File parent, String name) {
		if (parent != null && parent.exists()) {
			File file = new File(parent, name);
			if (file != null && file.exists()) {
				return true;
			}
		}
		return false;
	}
		
	private static String detectFromPath(final String pattern) {
		String location = null;
		String path = getPath();
		String spliter = getPathSpliter();
		if (path != null) {
			String[] pathElements = path.split(spliter);
			for (String element : pathElements) {
				if (element.contains(pattern)) {
					location = element;
					break;
				}
			}
		}
		return location;
	}
	
	private static String getPath() {
		return System.getenv(PATH);
	}
	
	private static String getPathSpliter() {
		return (PlatformUtil.isWindows()) ? ";" : ":"; //$NON-NLS-1$ //$NON-NLS-2$
	}

}
