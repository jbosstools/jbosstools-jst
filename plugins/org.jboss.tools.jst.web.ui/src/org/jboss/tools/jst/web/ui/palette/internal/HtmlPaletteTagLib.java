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

import org.jboss.tools.jst.web.kb.internal.HTML5Recognizer;
import org.jboss.tools.jst.web.kb.internal.taglib.html.HTMLVersion;
import org.jboss.tools.jst.web.kb.internal.taglib.html.IHTMLLibraryVersion;
import org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer;
import org.jboss.tools.jst.web.ui.palette.html.wizard.HTMLConstants;

/**
 * @author Alexey Kazakov
 */
public class HtmlPaletteTagLib extends PaletteTagLibrary {

	public HtmlPaletteTagLib() {
		super(null, "html50", null, "html50palette", true);
		IHTMLLibraryVersion version = HTMLVersion.HTML_5_0;
		this.name = "HTML " + version + " templates";
		setPaletteLibraryVersion(version);
		setVersion(version.toString());
	}

	@Override
	public int getRelevance() {
		return HTML_GROUP_RELEVANCE;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.ui.palette.internal.PaletteTagLibrary#getTagLibRecognizer()
	 */
	@Override
	public ITagLibRecognizer getTagLibRecognizer() {
		return new HTML5Recognizer();
	}

	@Override
	protected String getCategory() {
		return HTMLConstants.HTML_CATEGORY;
	}
}