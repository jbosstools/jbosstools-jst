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
public class JQueryMobileRecognizer extends JSRecognizer {

	public static final String JQUERY_MOBILE_JS_PATTERN = ".*(jquery.mobile-).*(.js).*";

	public static boolean containsJQueryMobileJSReference(IFile file) {
		return containsJSReference(file, JQUERY_MOBILE_JS_PATTERN);
	}

	@Override
	protected String getJSPattern() {
		return JQUERY_MOBILE_JS_PATTERN;
	}
}