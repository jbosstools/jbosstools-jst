/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.palette.internal;

import org.jboss.tools.jst.web.kb.internal.JQueryMobileRecognizer;
import org.jboss.tools.jst.web.kb.internal.taglib.html.IHTMLLibraryVersion;
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileVersion;
import org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;

/**
 * @author Alexey Kazakov
 */
public class JQMPaletteTagLib13 extends PaletteTagLibrary {

	private final static int RELEVANCE = generateUniqueRelevance();

	public JQMPaletteTagLib13() {
		super(null, "jQueryMobile", null, "jqmlpalette", true);
		IHTMLLibraryVersion version = getJQMVersion();
		this.name = "jQuery Mobile " + version + " templates";
		setPaletteLibraryVersion(version);
		setVersion(version.toString());
	}

	protected IHTMLLibraryVersion getJQMVersion() {
		return JQueryMobileVersion.JQM_1_3;
	}

	@Override
	public ITagLibRecognizer getTagLibRecognizer() {
		return new JQueryMobileRecognizer();
	}

	@Override
	protected int getRelevance() {
		return RELEVANCE;
	}

	@Override
	protected String getCategory() {
		return JQueryConstants.JQM_CATEGORY;
	}
}