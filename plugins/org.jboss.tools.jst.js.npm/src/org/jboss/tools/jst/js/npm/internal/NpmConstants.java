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
package org.jboss.tools.jst.js.npm.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public final class NpmConstants {

	private NpmConstants() {
	}

	public static final String BIN = "bin"; //$NON-NLS-1$
	public static final String NPM_CLI_JS = "npm-cli.js"; //$NON-NLS-1$
	public static final String PACKAGE_JSON = "package.json"; //$NON-NLS-1$
	public static final String NODE_MODULES = "node_modules"; //$NON-NLS-1$
	public static final String NPM = "npm"; //$NON-NLS-1$
	public static final String PATH = "PATH"; //$NON-NLS-1$
	public static final String UTF_8 = "UTF-8"; //$NON-NLS-1$
	
	// Default package.json values
	public static final String DEFAULT_NAME = "js"; //$NON-NLS-1$
	public static final String DEFAULT_VERSION = "0.0.0"; //$NON-NLS-1$
	public static final String DEFAULT_DESCRIPTION = "Generated with JBoss npm Tools"; //$NON-NLS-1$
	public static final String DEFAULT_MAIN = "index.js"; //$NON-NLS-1$
	public static final String DEFAULT_AUTHOR = ""; //$NON-NLS-1$
	public static final String DEFAULT_LICENSE = "ISC"; //$NON-NLS-1$
	
	@SuppressWarnings("serial")
	public static final Map<String, String> DEFAULT_SCRIPTS = Collections.unmodifiableMap(new HashMap<String, String>() {{ 
        put("test", "echo 'Error: no test specified' && exit 1");  //$NON-NLS-1$//$NON-NLS-2$
    }});

}
