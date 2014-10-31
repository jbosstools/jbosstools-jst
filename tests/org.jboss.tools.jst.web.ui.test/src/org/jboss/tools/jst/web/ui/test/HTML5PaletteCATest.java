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
package org.jboss.tools.jst.web.ui.test;

import java.util.Collection;

import org.jboss.tools.jst.web.kb.internal.taglib.html.HTMLVersion;
import org.jboss.tools.jst.web.ui.palette.html.wizard.HTMLConstants;
import org.jboss.tools.jst.web.ui.palette.internal.PaletteManager;
import org.jboss.tools.jst.web.ui.palette.internal.RunnablePaletteItem;

/**
 * @author Alexey Kazakov
 */
public class HTML5PaletteCATest extends PaletteCATest {

	private static final String TAGLIB_URI = "html50";

	@Override
	protected String getTaglibUri() {
		return TAGLIB_URI;
	}

	@Override
	public Collection<RunnablePaletteItem> getItems() {
		return PaletteManager.getInstance().getItems(HTMLConstants.HTML_CATEGORY, HTMLVersion.HTML_5_0);
	}

	@Override
	protected int getLibNumbers() {
		return 1;
	}
}