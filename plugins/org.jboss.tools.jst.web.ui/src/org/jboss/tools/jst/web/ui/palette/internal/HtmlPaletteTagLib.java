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

import java.util.Collection;

import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.jst.web.kb.internal.HTML5Recognizer;
import org.jboss.tools.jst.web.kb.internal.taglib.html.HTMLVersion;
import org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.palette.html.wizard.HTMLConstants;

/**
 * @author Alexey Kazakov
 */
public class HtmlPaletteTagLib extends PaletteTagLibrary {

	private static final ImageDescriptor IMAGE = ImageDescriptor.createFromFile(WebUiPlugin.class, "html5.png");

	public HtmlPaletteTagLib() {
		super(null, "html50", null, "html50palette", true);
		String version = HTMLVersion.HTML_5_0.toString();
		this.name = "jQuery Mobile " + version + " templates";
		setVersion(version);
	}

	@Override
	public int getRelevance() {
		return HTML_GROUP_RELEVANCE;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.ui.palette.internal.PaletteTagLibrary#getImage()
	 */
	@Override
	public ImageDescriptor getImage() {
		return IMAGE;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.ui.palette.internal.PaletteTagLibrary#getItems()
	 */
	@Override
	public Collection<RunnablePaletteItem> getItems() {
		return PaletteManager.getInstance().getItems(HTMLConstants.HTML_CATEGORY, getVersion());
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.ui.palette.internal.PaletteTagLibrary#getTagLibRecognizer()
	 */
	@Override
	public ITagLibRecognizer getTagLibRecognizer() {
		return new HTML5Recognizer();
	}
}