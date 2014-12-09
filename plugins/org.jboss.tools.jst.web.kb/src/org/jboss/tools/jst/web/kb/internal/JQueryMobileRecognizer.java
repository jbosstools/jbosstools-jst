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

import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileVersion;
import org.jboss.tools.jst.web.kb.taglib.IHTMLLibraryVersion;

/**
 * @author Alexey Kazakov
 */
public class JQueryMobileRecognizer extends JSRecognizer {

	private static final String JQUERY_MOBILE_JS_LIB_NAME = "jquery.mobile-";
	private static final String JQUERY_MOBILE_JS_PATTERN_START = ".*(" + JQUERY_MOBILE_JS_LIB_NAME;
	private static final String JQUERY_MOBILE_JS_PATTERN_END = ").*(.js).*";

	@Override
	protected String getJSPattern() {
		return JQUERY_MOBILE_JS_PATTERN_START + (lib.getVersion()!=null?lib.getVersion():"") + JQUERY_MOBILE_JS_PATTERN_END;
	}

	/**
	 * Returns the version of JQM representing the specified  version of the library.
	 * Returns null if string version is null.
	 * Returns the default version is the string version is unknown. 
	 * @param context
	 * @return
	 */
	@Override
	protected IHTMLLibraryVersion findVersion(String version) {
		if(version == null) {
			return null;
		}
		if(version.equals(JQueryMobileVersion.JQM_1_3.toString())) {
			return JQueryMobileVersion.JQM_1_3;
		}
		return JQueryMobileVersion.JQM_1_4;
	}

	@Override
	protected String getJSLibName() {
		return JQUERY_MOBILE_JS_LIB_NAME;
	}
}