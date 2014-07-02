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
package org.jboss.tools.jst.angularjs.test.ca;

import org.jboss.tools.jst.jsp.test.ca.CAMobilePaletteTemplatesTest;

/**
 * @author Alexey Kazakov
 */
public class CAMobilePaletteIonicTemplatesTest extends CAMobilePaletteTemplatesTest {

	@Override
	protected String getPageName() {
		return "ionic.html";
	}

	public void testContent() throws Exception {
		doTestTemplate("", "content", decorateDisplay("Content"), 0);
	}

	public void testHeader() throws Exception {
		doTestTemplate("", "header", decorateDisplay("Header Bar"), 0);
	}

	public void testScroll() throws Exception {
		doTestTemplate("", "scroll", decorateDisplay("Scroll"), 0);
	}

	@Override
	protected String decorateDisplay(String display) {
		return display + " - Ionic 1.0";
	}
}