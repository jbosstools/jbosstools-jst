/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;

import junit.framework.TestCase;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class StyleRangeInPaletteWizardPreviewTest extends TestCase {

	public StyleRangeInPaletteWizardPreviewTest() {}
	
	public void testScript() {
		String text = "<script>\n" +
					  "var a = 1;\n" +
					  "//comments var\n" +
					  "var b = 2;\n" +
					  "</script>";
		StyleRange[] ranges = AbstractNewHTMLWidgetWizardPage.getRanges(text);
		assertEquals(6, ranges.length);
		assertRange(ranges[0], 0, 7, false, false);		/*<script*/
		assertRange(ranges[1], 7, 1, false, false);		/*>*/
		assertRange(ranges[2], 9, 3, false, true);		/*var*/
		assertRange(ranges[3], 20, 14, false, false);	/*//comments var*/
		assertRange(ranges[4], 35, 3, false, true);		/*var*/
		assertRange(ranges[5], 46, 9, false, false);	/*</script>*/
	}

	public void testScript2() {
		String text = "<script>var a = false;var b = 'abstract';</script>";
		StyleRange[] ranges = AbstractNewHTMLWidgetWizardPage.getRanges(text);
		assertEquals(7, ranges.length);
		assertRange(ranges[0], 0, 7, false, false);		/*<script*/
		assertRange(ranges[1], 7, 1, false, false);		/*>*/
		assertRange(ranges[2], 8, 3, false, true);		/*var*/
		assertRange(ranges[3], 16, 5, false, true);		/*false*/
		assertRange(ranges[4], 22, 3, false, true);		/*var*/
		assertRange(ranges[5], 30, 10, false, false);	/*'abstract'*/
		assertRange(ranges[6], 41, 9, false, false);	/*</script>*/
	}

	public void testScript3() {
		String text = "<script>var a = 0;/*var b = 'abstract';\nvar c = 1;*/var d = 2;</script>";
		StyleRange[] ranges = AbstractNewHTMLWidgetWizardPage.getRanges(text);
		assertEquals(6, ranges.length);
		assertRange(ranges[0], 0, 7, false, false);		/*<script*/
		assertRange(ranges[1], 7, 1, false, false);		/*>*/
		assertRange(ranges[2], 8, 3, false, true);		/*var*/
		assertRange(ranges[3], 18, 34, false, false);	/*/*var b = 'abstract';\nvar c = 1;^/*/
		assertRange(ranges[4], 52, 3, false, true);		/*var*/
		assertRange(ranges[5], 62, 9, false, false);	/*</script>*/
	}

	void assertRange(StyleRange range, int start, int length, boolean italic, boolean bold) {
		assertEquals(start, range.start);
		assertEquals(length, range.length);
		assertEquals(italic, (range.fontStyle & SWT.ITALIC) != 0);
		assertEquals(bold, (range.fontStyle & SWT.BOLD) != 0);
	}
}
