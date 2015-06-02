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
import org.jboss.tools.jst.js.internal.util.PlatformUtil;

/**
 * @author Ilya Buziuk (ibuziuk)
 */
public final class ExternalToolDetector {

	private ExternalToolDetector() {
	}

	public static String detectNpm() {
		return detectNpmFromPathVariable();
	}

	private static String detectNpmFromPathVariable() {
		String npmLocation = null;
		if (PlatformUtil.isWindows()) {
			String path = System.getenv(BowerConstants.PATH);
			String[] split = path.split(";"); //$NON-NLS-1$
			for (String pathElement : split) {
				if (pathElement.endsWith(File.separator + BowerConstants.NPM)) {
					npmLocation = pathElement;
					break;
				}
			}
		}
		return npmLocation;
	}

}
