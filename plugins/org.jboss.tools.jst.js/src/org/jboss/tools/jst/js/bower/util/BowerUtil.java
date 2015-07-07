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
package org.jboss.tools.jst.js.bower.util;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.jst.js.bower.BowerJson;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public final class BowerUtil {

	private BowerUtil() {
	}

	public static void addDependencies(String bowerJsonLocation, Map<String, String> dependencies) throws CoreException {
		BowerJson bowerJson = getBowerJsonModel(bowerJsonLocation);
		Map<String, String> existingDependendecies = bowerJson.getDependencies();
		if (existingDependendecies == null) {
			bowerJson.setDependencies(dependencies);
		} else {
			existingDependendecies.putAll(dependencies);
			bowerJson.setDependencies(existingDependendecies);
		}
	}

	private static BowerJson getBowerJsonModel(String bowerJsonLocation) {
		throw new UnsupportedOperationException();
	}

}
