/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal;

import org.eclipse.core.resources.IFile;

/**
 * @author Alexey Kazakov
 */
public class JQueryRecognizer extends JSRecognizer {

	private static final String JQUERY_JS_LIB_NAME = "jquery-";
	private static final String JQUERY_JS_PATTERN = ".*(" + JQUERY_JS_LIB_NAME + ").*(.js).*";

	public static boolean containsJQueryJSReference(IFile file) {
		return getJSReferenceVersion(file, JQUERY_JS_LIB_NAME) != null;
	}

	@Override
	protected String getJSPattern() {
		return JQUERY_JS_PATTERN;
	}

	@Override
	protected String getJSLibName() {
		return JQUERY_JS_LIB_NAME;
	}
}