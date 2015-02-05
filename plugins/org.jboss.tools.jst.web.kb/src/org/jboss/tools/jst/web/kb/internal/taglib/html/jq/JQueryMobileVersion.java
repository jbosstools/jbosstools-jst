/******************************************************************************* 
 * Copyright (c) 2013-2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.taglib.html.jq;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.jst.web.kb.internal.JQueryMobileRecognizer;
import org.jboss.tools.jst.web.kb.internal.JQueryRecognizer;
import org.jboss.tools.jst.web.kb.internal.taglib.html.IHTMLLibraryVersion;

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
public enum JQueryMobileVersion implements IHTMLLibraryVersion {
	JQM_1_3("1.3", //$NON-NLS-1$
			"1.3.2", //$NON-NLS-1$
			"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css", //$NON-NLS-1$
			"http://code.jquery.com/jquery-1.9.1.min.js", //$NON-NLS-1$
			"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.js"), //$NON-NLS-1$

	JQM_1_4("1.4", //$NON-NLS-1$
			"1.4.5", //$NON-NLS-1$
			"http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css", //$NON-NLS-1$
			"http://code.jquery.com/jquery-1.11.2.min.js", //$NON-NLS-1$
			"http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"); //$NON-NLS-1$

	public static final JQueryMobileVersion[] ALL_VERSIONS = {JQM_1_3, JQM_1_4};

	public static final String JQ_CATEGORY = "jQuery";
	public static final String JQM_CATEGORY = "jQuery Mobile";

	String version;
	String fullDefaultVersion;
	String css;
	String jqjs;
	String jqmjs;

	JQueryMobileVersion(String version, String fullDefaultVersion, String css, String jqjs, String jqmjs) {
		this.version = version;
		this.fullDefaultVersion = fullDefaultVersion;
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

	public String getFullDefaultVersion() {
		return fullDefaultVersion;
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

	@Override
	public boolean isPreferredJSLib(IFile file, String libName) {
		return JQ_CATEGORY.equals(libName) || JQM_CATEGORY.equals(libName);
	}

	@Override
	public boolean isReferencingJSLib(IFile file, String libName) {
		if(JQ_CATEGORY.equals(libName)) {
			return JQueryRecognizer.containsJQueryJSReference(file);
		} else if(JQM_CATEGORY.equals(libName)) {
			return JQueryMobileRecognizer.getVersion(file) != null;
		}
		return false;
	}

}