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
package org.jboss.tools.jst.js.bower.internal;

import java.util.ArrayList;
import java.util.Arrays;

import org.jboss.tools.jst.js.bower.BowerJsonModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public final class BowerJsonGenerator {
	private static final String NAME = "Bower"; //$NON-NLS-1$
	private static final String VERSION = "0.0.0"; //$NON-NLS-1$
	private static final String[] AUTHORS = { "JBoss Tools <tools@jboss.org>" }; //$NON-NLS-1$
	private static final String LICENSE = "MIT"; //$NON-NLS-1$
	private static final String[] IGNORE = { "**/.*", "node_modules", "bower_components", "test", "tests" }; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

	public static String generateDefault(String projectName) {
		BowerJsonModel model = new BowerJsonModel();
		model.setName(projectName != null ? projectName : NAME);
		model.setVersion(VERSION);
		model.setAuthors(new ArrayList<String>(Arrays.asList(AUTHORS)));
		model.setLicense(LICENSE);
		model.setIgnore(new ArrayList<String>(Arrays.asList(IGNORE)));
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		String json = gson.toJson(model);
		return json;
	}
	
}
