package org.jboss.tools.jst.web.ui.test;

import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileVersion;

public class InsertJSCSS14PaletteEntryTest extends InsertJSCSSPaletteEntryTest {

	protected String button(){
		return "<a href=\"\" id=\"button-1\" class=\"ui-btn ui-corner-all\">Link button</a>";
	}

	protected JQueryMobileVersion getVersion() {
		return JQueryMobileVersion.JQM_1_4;
	}

	public void testInsertSingleTagWithJcCssInEmptyFileWithNoSelection(){
		doTestWithMultipleParameters("no_tags.html", true, true, true, test_result_01);
	}

	public void testInsertSingleTagWithJcCssInHtmlFileWithNoSelection(){
		doTestWithMultipleParameters("html_tag.html", true, true, true, test_result_01);
	}

	public void testInsertSingleTagWithNoJcCssInHtmlHeadFileWithNoSelection(){
		doTestWithMultipleParameters("html_head_tags.html", true, true, false, test_result_22);
	}

	public void testInsertSingleTagWithNoJcCssInHtmlHeadBodyFileWithNoSelection(){
		doTestWithMultipleParameters("html_head_body_tags.html", true, true, false, test_result_32);
	}

	public void testInsertSingleTagWithNoJcCssInHtmlFileWithNoSelection(){
		doTestWithMultipleParameters("html_tag.html", true, true, false, test_result_12);
	}

	public void testInsertSingleTagWithJcCssInEmptyFileWithSelection(){
		doTestWithMultipleParameters("no_tags.html", true, false, true, test_result_04);
	}

	public void testInsertSingleTagWithNoJcCssInHtmlFileWithSelection(){
		doTestWithMultipleParameters("html_tag.html", true, false, false, test_result_13);
	}

	public void testInsertSingleTagWithNoJcCssInEmptyFileWithNoSelection(){
		doTestWithMultipleParameters("no_tags.html", true, true, false, test_result_02);
	}

	public void testInsertSingleTagWithJcCssInHtmlFileWithSelection(){
		doTestWithMultipleParameters("html_tag.html", true, false, true, test_result_04);
	}

	public void testInsertSingleTagWithJcCssInHtmlHeadFileWithNoSelection(){
		doTestWithMultipleParameters("html_head_tags.html", true, true, true, test_result_01);
	}

	public void testInsertSingleTagWithNoJcCssInHtmlHeadBodyFileWithSelection(){
		doTestWithMultipleParameters("html_head_body_tags.html", true, false, false, test_result_33);
	}

	public void testInsertSingleTagWithJcCssInHtmlHeadFileWithSelection(){
		doTestWithMultipleParameters("html_head_tags.html", true, false, true, test_result_04);
	}

	public void testInsertSingleTagWithJcCssInHtmlHeadBodyFileWithSelection(){
		doTestWithMultipleParameters("html_head_body_tags.html", true, false, true, test_result_04);
	}

	public void testInsertSingleTagWithNoJcCssInEmptyFileWithSelection(){
		doTestWithMultipleParameters("no_tags.html", true, false, false, test_result_03);
	}

	public void testInsertSingleTagWithJcCssInHtmlHeadBodyFileWithNoSelection(){
		doTestWithMultipleParameters("html_head_body_tags.html", true, true, true, test_result_01);
	}

	public void testInsertSingleTagWithNoJcCssInHtmlHeadFileWithSelection(){
		doTestWithMultipleParameters("html_head_tags.html", true, false, false, test_result_23);
	}

}
