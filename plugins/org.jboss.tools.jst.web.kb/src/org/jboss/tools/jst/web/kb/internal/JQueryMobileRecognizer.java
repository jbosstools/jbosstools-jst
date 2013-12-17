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
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileVersion;

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

	@Override
	public String getVersion(ELContext context) {
		String version = getJSReferenceVersion(context.getResource(), getJSLibName());
		if(version==null) {
			return null;
		}
		if(version.equals(JQueryMobileVersion.JQM_1_3.toString())) {
			return JQueryMobileVersion.JQM_1_3.toString();
		}
		return JQueryMobileVersion.JQM_1_4.toString();
	}

	/**
	 * Returns the version of JQM declared in the file or null if there is no JQM declaration.
	 * Returns the default version is the declared one is unknown. 
	 * @param context
	 * @return
	 */
	public static String getVersion(IFile file) {
		String version = getJSReferenceVersion(file, JQUERY_MOBILE_JS_LIB_NAME);
		if(version==null) {
			return null;
		}
		if(version.equals(JQueryMobileVersion.JQM_1_3.toString())) {
			return JQueryMobileVersion.JQM_1_3.toString();
		}
		return JQueryMobileVersion.JQM_1_4.toString();
	}

	@Override
	protected String getJSLibName() {
		return JQUERY_MOBILE_JS_LIB_NAME;
	}
}