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

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public final class BowerConstants {

	private BowerConstants() {
	}

	public static final String BIN = "bin"; //$NON-NLS-1$
	public static final String BOWER = "bower"; //$NON-NLS-1$
	public static final String BOWER_CMD = "bower.cmd"; //$NON-NLS-1$
	public static final String BOWER_JSON = "bower.json"; //$NON-NLS-1$
	public static final String NODE = "node"; //$NON-NLS-1$
	public static final String NODE_EXE = "node.exe"; //$NON-NLS-1$
	public static final String NODE_JS = "nodejs"; //$NON-NLS-1$
	public static final String NODE_MODULES = "node_modules"; //$NON-NLS-1$
	public static final String NPM = "npm"; //$NON-NLS-1$
	public static final String PATH = "PATH"; //$NON-NLS-1$
	
	// Default bower.json values
	public static final String DEFAULT_NAME = "Bower"; //$NON-NLS-1$
	public static final String DEFAULT_VERSION = "0.0.0"; //$NON-NLS-1$
	public static final String[] DEFAULT_AUTHORS = { "JBoss Tools <tools@jboss.org>" }; //$NON-NLS-1$
	public static final String DEFAULT_LICENSE = "MIT"; //$NON-NLS-1$
	public static final String[] DEFAULT_IGNORE = { "**/.*", "node_modules", "bower_components", "test", "tests" }; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$


}
