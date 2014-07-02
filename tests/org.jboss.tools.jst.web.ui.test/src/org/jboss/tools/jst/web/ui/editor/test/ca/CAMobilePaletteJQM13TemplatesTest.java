/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.editor.test.ca;

import org.jboss.tools.jst.jsp.test.ca.CAMobilePaletteTemplatesTest;

public class CAMobilePaletteJQM13TemplatesTest extends CAMobilePaletteTemplatesTest {
	private static final String PAGE_NAME = "ca_13_templates.html"; //$NON-NLS-1$

	public CAMobilePaletteJQM13TemplatesTest() {}

	public void testPage() throws Exception {
		doTestTemplate("", "page", decorateDisplay("Page"), 0);
	}

	public void testPopup() throws Exception {
		doTestTemplate("", "popup", decorateDisplay("Popup"), 0);
	}

	public void testGrid() throws Exception {
		doTestTemplate("", "grid", decorateDisplay("Grid"), 0);
	}

	public void testPanel() throws Exception {
		doTestTemplate("", "panel", decorateDisplay("Panel"), 0);
	}

	public void testTable() throws Exception {
		doTestTemplate("", "table", decorateDisplay("Table"), 0);
	}

	public void testCollapsible() throws Exception {
		doTestTemplate("", "collapsible", decorateDisplay("Collapsible Content Block"), 0);
	}

	public void testCollapsible1() throws Exception {
		doTestTemplate("", "collapsible", decorateDisplay("Collapsible Set"), 1);
	}

	public void testHeader() throws Exception {
		doTestTemplate("", "header", decorateDisplay("Header Bar"), 0);
	}

	public void testFooter() throws Exception {
		doTestTemplate("", "footer", decorateDisplay("Footer Bar"), 0);
	}

	public void testBar() throws Exception {
		doTestTemplate("", "bar", decorateDisplay("Footer Bar"), 0);
	}

	public void testBar1() throws Exception {
		doTestTemplate("", "bar", decorateDisplay("Header Bar"), 1);
	}

	public void testNavbar() throws Exception {
		doTestTemplate("", "navbar", decorateDisplay("Navbar"), 0);
	}

	public void testSelect() throws Exception {
		doTestTemplate("", "select", decorateDisplay("Select Menu"), 0);
	}

	public void testListview() throws Exception {
		doTestTemplate("", "listview", decorateDisplay("Listview"), 0);
	}

	public void testButton() throws Exception {
		doTestTemplate("", "button", decorateDisplay("Button"), 0);
	}

	public void testForm() throws Exception {
		doTestTemplate("", "form", decorateDisplay("Form Button"), 1);
	}

	public void testLink() throws Exception {
		doTestTemplate(".", "link", decorateDisplay("Link"), 0);
	}

	public void testToggle() throws Exception {
		doTestTemplate("", "toggle", decorateDisplay("Flip Toggle Switch"), 0);
	}

	public void testRadio() throws Exception {
		doTestTemplate("", "radio", decorateDisplay("Radio Button"), 0);
	}

	public void testCheckbox() throws Exception {
		doTestTemplate("", "checkbox", decorateDisplay("Checkbox"), 0);
	}

	public void testCheckbox1() throws Exception {
		doTestTemplate("", "checkbox", decorateDisplay("Grouped Checkboxes"), 1);
	}

	public void testGrouped() throws Exception {
		doTestTemplate("", "grouped", decorateDisplay("Grouped Buttons"), 0);
	}

	public void testGrouped1() throws Exception {
		doTestTemplate("", "grouped", decorateDisplay("Grouped Checkboxes"), 1);
	}

	public void testSlider() throws Exception {
		doTestTemplate("", "slider", decorateDisplay("Range Slider"), 0);
	}

	public void testText() throws Exception {
		doTestTemplate("", "text", decorateDisplay("Text Input"), 0);
	}

	public void testInput() throws Exception {
		doTestTemplate("", "input", decorateDisplay("Text Input"), 6);
	}

	@Override
	protected String getPageName() {
		return PAGE_NAME;
	}

	@Override
	protected String decorateDisplay(String display) {
		return display + " - jQuery Mobile 1.3";
	}
}