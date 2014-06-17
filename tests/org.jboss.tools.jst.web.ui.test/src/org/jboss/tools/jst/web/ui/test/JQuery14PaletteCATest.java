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

import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileVersion;

/**
 * @author Alexey Kazakov
 */
public class JQuery14PaletteCATest extends JQueryPaletteCATest {

	@Override
	protected String getFilePath() {
		return "WebContent/pages/jquery/jQueryMobile14.html";
	}

	@Override
	public JQueryMobileVersion getVersion() {
		return JQueryMobileVersion.JQM_1_4;
	}
}