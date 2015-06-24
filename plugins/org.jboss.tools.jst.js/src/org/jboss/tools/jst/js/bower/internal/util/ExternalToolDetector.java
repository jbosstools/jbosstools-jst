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

import org.jboss.tools.jst.js.util.PlatformUtil;

import static org.jboss.tools.jst.js.bower.internal.BowerConstants.*;

/**
 * @author Ilya Buziuk (ibuziuk)
 */
public final class ExternalToolDetector {
	private static final String USR_LOCAL_BIN = "/usr/local/bin"; //$NON-NLS-1$

	private ExternalToolDetector() {
	}

	public static String detectNode() {
		String nodeLocation = null;
		if (PlatformUtil.isWindows()) {
			nodeLocation = detectFromPath(File.separator + NODE_JS);
		} else {
			// Detecting Node for Mac & Linux
			File usrLocalBin = new File(USR_LOCAL_BIN);
			if (usrLocalBin != null && usrLocalBin.exists()) {
				File nodeExecutable = new File(usrLocalBin, NODE);
				if (nodeExecutable != null && nodeExecutable.exists()) {
					nodeLocation = usrLocalBin.getAbsolutePath();
				}
			}
		}
		return nodeLocation;
	}
	
	public static String detectBower() {
		String bowerLocation = null;
		if (PlatformUtil.isWindows()) {
			String npmLocation = detectNpmFromPath();
			if (npmLocation != null) {
				String separator = File.separator;
				if (!npmLocation.endsWith(separator)) {
					npmLocation = npmLocation + separator;
				}
				
				File bowerHome =  new File(npmLocation, NODE_MODULES + separator + BOWER + separator + BIN);
				if (bowerHome != null && bowerHome.exists()) {
					bowerLocation = bowerHome.getAbsolutePath();
				}
			}
		} else {
			// Detecting Bower for Mac & Linux
			File usrLocalBin = new File(USR_LOCAL_BIN);
			if (usrLocalBin != null && usrLocalBin.exists()) {
				File bowerExecutable = new File(usrLocalBin, BOWER);
				if (bowerExecutable != null && bowerExecutable.exists()) {
					bowerLocation = usrLocalBin.getAbsolutePath();
				}
			}
		}
		return bowerLocation;
	}
	
	private static String detectNpmFromPath() {
		return detectFromPath(File.separator + NPM);
	}
		
	private static String detectFromPath(final String pattern) {
		String nodeLocation = null;
		String path = getPath();
		String spliter = getPathSpliter();
		if (path != null) {
			String[] pathElements = path.split(spliter);
			for (String element : pathElements) {
				if (element.contains(pattern)) {
					nodeLocation = element;
					break;
				}
			}
		}
		return nodeLocation;
	}
	
	private static String getPath() {
		return System.getenv(PATH);
	}
	
	private static String getPathSpliter() {
		return (PlatformUtil.isWindows()) ? ";" : ":"; //$NON-NLS-1$ //$NON-NLS-2$
	}

}
