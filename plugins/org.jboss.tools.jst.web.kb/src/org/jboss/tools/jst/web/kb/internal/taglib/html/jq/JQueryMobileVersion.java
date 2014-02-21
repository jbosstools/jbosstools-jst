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
package org.jboss.tools.jst.web.kb.internal.taglib.html.jq;

/**
 * List of supported jQuery Mobile versions.
 * 
 * When adding a new version, we should:
 * 1. Create palette subcategory under "jQuery Mobile" 
 *    with name of that version, register in plugin.xml wizards 
 *    for its items, etc.
 * 2. Support new version in wizard preview by implementing 
 *    NewJQueryWidgetWizard.ResourceConstants and copying css/js
 *    files to org.jboss.tools.jst.web/js folder.
 * 
 * @author Viacheslav Kabanovich
 */
public enum JQueryMobileVersion {
	JQM_1_3("1.3", //$NON-NLS-1$
			"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css", //$NON-NLS-1$
			"http://code.jquery.com/jquery-1.9.1.min.js", //$NON-NLS-1$
			"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.js"), //$NON-NLS-1$

	JQM_1_4("1.4", //$NON-NLS-1$
			"http://code.jquery.com/mobile/1.4.1/jquery.mobile-1.4.1.min.css", //$NON-NLS-1$
			"http://code.jquery.com/jquery-1.10.2.min.js", //$NON-NLS-1$
			"http://code.jquery.com/mobile/1.4.1/jquery.mobile-1.4.1.min.js"); //$NON-NLS-1$

	public static final JQueryMobileVersion[] ALL_VERSIONS = {JQM_1_3, JQM_1_4};

	String version;
	String css;
	String jqjs;
	String jqmjs;

	JQueryMobileVersion(String version, String css, String jqjs, String jqmjs) {
		this.version = version;
		this.css = css;
		this.jqjs = jqjs;
		this.jqmjs = jqmjs;
	}

	@Override
	public String toString() {
		return version;
	}

	public static JQueryMobileVersion getLatestDefaultVersion() {
		return JQM_1_4;
	}

	public String getCSS() {
		return css;
	}

	public String getJQueryJS() {
		return jqjs;
	}

	public String getJQueryMobileJS() {
		return jqmjs;
	}
}