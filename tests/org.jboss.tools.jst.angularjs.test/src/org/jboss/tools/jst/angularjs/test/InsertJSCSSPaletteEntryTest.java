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
package org.jboss.tools.jst.angularjs.test;

import java.util.List;

import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.IonicConstants;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.IonicVersion;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.IonicVersionPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewContentWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewContentWizardPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewIonicWidgetWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewTabWizardPage;
import org.jboss.tools.jst.jsp.test.palette.AbstractPaletteEntryTest;
import org.jboss.tools.jst.web.ui.internal.preferences.js.JSLibFactory;
import org.jboss.tools.jst.web.ui.internal.preferences.js.PreferredJSLibVersions;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizardPage;
import org.jboss.tools.jst.web.ui.palette.html.wizard.HTMLConstants;

public class InsertJSCSSPaletteEntryTest extends AbstractPaletteEntryTest implements HTMLConstants {

	static String link(String href) {
		return "<link rel=\"stylesheet\" href=\"" + href + "\" /";
	}

	static String script(String src) {
		return "<script src=\"" + src + "\"></script";
	}
	
	String[] references = getReferences();

	protected String button(){
		return "<a href=\"\" id=\"button-1\" data-role=\"button\">Link button</a>";
	}

	protected String[] test_result_1={
			"<!DOCTYPE html>",
			"<html>",
		    "<head>",
			"\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">",
			"\t"+references[0],
			"\t"+references[1],
			"</head>",
			"<body>",
			" ",
			"</body>",
			"</html>"
	};
	protected String[] test_result_1Ionic={
			"<!DOCTYPE html>",
			"<html>",
		    "<head>",
			"\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">",
			"</head>",
			"<body>",
			" ",
			"</body>",
			"</html>"
	};
	protected String[] test_result_2={
			"<!DOCTYPE html>",
			"<html>",
		    "<head>",
			"\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">",
			"\t"+references[0],
			"\t"+references[1],
			"</head>",
			"<body>",
			"\t<div data-role=\"collapsible-set\">",
			"\t\t<div data-role=\"collapsible\">",
			"\t\t\t<h3>I'm a header</h3>",
			"\t\t\t<p>I'm the collapsible content.</p>",
			"\t\t</div>",
		    "\t</div>",
		    "</body>",
			"</html>"
	};
	protected String[] test_result_2_1={
			"<!DOCTYPE html>",
			"<html>",
		    "<head>",
			"\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">",
			"\t"+references[0],
			"\t"+references[1],
			"</head>",
			"<body>",
			"\t<div data-role=\"collapsible-set\">",
			"\t\t<div data-role=\"collapsible\">",
			"\t\t\t<h3>I'm a header</h3>",
			"\t\t\t<p>I'm the collapsible content.</p>",
			"\t\t</div>",
		    "\t</div>",
		    "</body>",
			"</html>"
	};
	protected String[] test_result_3={
			"<!DOCTYPE html>",
			"<html>",
		    "<head>",
			"\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">",
			"\t"+references[0],
			"\t"+references[1],
			"</head>",
			"<body>",
			"</body>",
			"</html>"
	};

	protected String[] test_result_4={
			"<!DOCTYPE html>",
			"<html>",
		    "<head>",
		    "\t<meta name=\"viewport\">",
		    "\t<link rel=\"stylesheet\" href=\"jquery.mobile-1.7.6.css\" />",
		    "\t<script src=\"a:/jquery-2.7.1.min.js\"></script>",
		    "\t<script src=\"b:/jquery.mobile-1.9.0.min.js\"></script>",
			"</head>",
			"<body>",
			"</body>",
			"</html>"
	};
	
	protected String[] test_result_01={
			"<!DOCTYPE html>",
			"<html>",
		    "<head>",
		    "\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">",
			"\t"+references[0],
			"\t"+references[1],
			"</head>",
			"<body>",
			"\t"+button(),
			"|abcde|",
			"</body>",
			"</html>"
	};
	
	protected String[] test_result_02={
			"<!DOCTYPE html>",
			"\t"+button(),
			"|abcde|"
	};

	protected String[] test_result_12={
			"<!DOCTYPE html>",
			"<html>",
			"\t"+button(),
			"|abcde|",
			"</html>"
	};

	protected String[] test_result_22={
			"<!DOCTYPE html>",
			"<html>",
			"<head>",
			"</head>",
			""+button(),
			"|abcde|",
			"</html>"
	};

	protected String[] test_result_32={
			"<!DOCTYPE html>",
			"<html>",
			"<head>",
			"</head>",
			"<body>",
			"\t"+button(),
			"|abcde|",
			"</body>",
			"</html>"
	};

	protected String[] test_result_03={
			"<!DOCTYPE html>",
			"\t"+button()+"abcde|"
	};

	protected String[] test_result_13={
			"<!DOCTYPE html>",
			"<html>",
			"\t"+button()+"abcde|",
			"</html>"
	};

	protected String[] test_result_23={
			"<!DOCTYPE html>",
			"<html>",
			"<head>",
			"</head>",
			button()+"abcde|",
			"</html>"
	};

	protected String[] test_result_33={
			"<!DOCTYPE html>",
			"<html>",
			"<head>",
			"</head>",
			"<body>",
			"\t"+button()+"abcde|",
			"</body>",
			"</html>"
	};

	protected String[] test_result_04={
			"<!DOCTYPE html>",
			"<html>",
		    "<head>",
		    "\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">",
			"\t"+references[0],
			"\t"+references[1],
			"</head>",
			"<body>",
			"\t"+button()+"abcde|",
			"</body>",
			"</html>"
	};
	
	protected String[] test_result_05={
			"<!DOCTYPE html>",
			"<html>",
		    "<head>",
		    "\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">",
			"\t"+references[0],
			"\t"+references[1],
			"</head>",
			"<body>",
			"\t<ion-tab title=\"Title\">",
			"\t\t<!-- Tab content -->",
			"\t</ion-tab>",
			"|abcde|",
			"</body>",
			"</html>"
	};
	
	protected String[] test_result_06={
			"<!DOCTYPE html>",
			"\t<ion-tab title=\"Title\">",
			"\t\t<!-- Tab content -->",
			"\t</ion-tab>",
			"|abcde|"
	};

	protected String[] test_result_16={
			"<!DOCTYPE html>",
			"<html>",
			"\t<ion-tab title=\"Title\">",
			"\t\t<!-- Tab content -->",
			"\t</ion-tab>",
			"|abcde|",
			"</html>"
	};

	protected String[] test_result_26={
			"<!DOCTYPE html>",
			"<html>",
			"<head>",
			"</head>",
			"<ion-tab title=\"Title\">",
			"\t<!-- Tab content -->",
			"</ion-tab>",
			"|abcde|",
			"</html>"
	};

	protected String[] test_result_36={
			"<!DOCTYPE html>",
			"<html>",
			"<head>",
			"</head>",
			"<body>",
			"\t<ion-tab title=\"Title\">",
			"\t\t<!-- Tab content -->",
			"\t</ion-tab>",
			"|abcde|",
			"</body>",
			"</html>"
	};

	protected String[] test_result_07={
			"<!DOCTYPE html>",
			"\t<ion-tab title=\"Title\">",
			"\t\t<!-- Tab content -->",
			"\t\tabcde|",
			"\t</ion-tab>",
	};

	protected String[] test_result_17={
			"<!DOCTYPE html>",
			"<html>",
			"\t<ion-tab title=\"Title\">",
			"\t\t<!-- Tab content -->",
			"\t</ion-tab>",
			"</html>"
	};

	protected String[] test_result_27={
			"<!DOCTYPE html>",
			"<html>",
			"<head>",
			"</head>",
			"<ion-tab title=\"Title\">",
			"\t<!-- Tab content -->",
			"\tabcde|",
			"</ion-tab>",
			"</html>"
	};

	protected String[] test_result_37={
			"<!DOCTYPE html>",
			"<html>",
			"<head>",
			"</head>",
			"<body>",
			"\t<ion-tab title=\"Title\">",
			"\t\t<!-- Tab content -->",
			"\t</ion-tab>",
			"</body>",
			"</html>"
	};

	protected String[] test_result_08={
			"<!DOCTYPE html>",
			"<html>",
		    "<head>",
		    "\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">",
			"\t"+references[0],
			"\t"+references[1],
			"</head>",
			"<body>",
			"\t<ion-tab title=\"Title\">",
			"\t\t<!-- Tab content -->",
			"\t\tabcde|",
			"\t</ion-tab>",
			"</body>",
			"</html>"
	};
	
	protected String[] test_result_001={
			"<!DOCTYPE html>",
			"<html>",
			"<head>",
			"</head>",
			"<body>",
			"\t<div>",
			"\t\t<!--comment|-->",
			"\t\t"+button(),
			"\t</div>",
			"</body>",
			"</html>"
	};

	protected String[] test_result_002={
			"<!DOCTYPE html>",
			"<html>",
			"<head>",
			"</head>",
			"<body>",
			"\t<div>",
			"\t\t"+button(),
			"\t\t|<!--comment-->",
			"\t</div>",
			"</body>",
			"</html>"
	};

	IEditorPart editor = null;

	public InsertJSCSSPaletteEntryTest() {
	}

	String[] getReferences() {
		PreferredJSLibVersions preferredVersions = new PreferredJSLibVersions(null, getVersion());
		preferredVersions.updateLibEnablementAndSelection();
		String[][] urls = preferredVersions.getURLs(null);
		String[] references = new String[urls[0].length + urls[1].length];
		int i = 0;
		for (String s: urls[0]) {
			references[i++] = link(s) + ">";
		}
		for (String s: urls[1]) {
			references[i++] = script(s) + ">";
		}
		return references;
	}

	protected IonicVersion getVersion() {
		return IonicVersion.IONIC_1_0;
	}

	public IEditorPart openEditor(String fileName) {
		IEditorPart result = super.openEditor(fileName);
		if(getVersion() != IonicVersion.IONIC_1_0) {
			switchVersion(getVersion().toString());
		}
		return result;
	}

	@Override
	public void tearDown() {
		if(editor != null){
			editor.getSite().getPage().closeEditor(editor, false);
		}
	}
	
	public void testInsertIntoEmptyFile() {
		editor = openEditor("empty.html");
		runJSCSS();
		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		compare(text, test_result_1);
	}

	void runJSCSS() {
		IWizardPage currentPage = runToolEntry(IonicConstants.IONIC_CATEGORY, "JS/CSS", true);
		if(currentPage instanceof IonicVersionPage) {
			IWizard wizard = currentPage.getWizard();
			wizard.performFinish();
			WizardDialog dialog = (WizardDialog)wizard.getContainer();
			dialog.close();
		}
	}
	
	public void testInsertIntoEmptyFile2() {
		editor = openEditor("empty.html");
		IWizardPage currentPage = runToolEntry(IonicConstants.IONIC_CATEGORY, "JS/CSS", true);
		if(currentPage instanceof IonicVersionPage) {
			IonicVersionPage versionPage = (IonicVersionPage)currentPage;
			versionPage.getEditor("add Ionic").setValue("false");
			IWizard wizard = currentPage.getWizard();
			wizard.performFinish();
			WizardDialog dialog = (WizardDialog)wizard.getContainer();
			dialog.close();
		}
		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		compare(text, test_result_1Ionic);
	}

	public void testInsertIntoTemplate() {
		editor = openEditor("ionic.html");
		IWizardPage currentPage = runToolEntry(IonicConstants.IONIC_CATEGORY, "JS/CSS", true);
		if(currentPage instanceof IonicVersionPage) {
			IonicVersionPage versionPage = (IonicVersionPage)currentPage;
			assertEquals(Boolean.FALSE, versionPage.getEditor("add Ionic").getValue());
			versionPage.getEditor("add Ionic").setValueAsString("true");
			assertEquals("true", versionPage.getEditor("add Ionic").getValue().toString());
			IWizard wizard = currentPage.getWizard();
			wizard.performFinish();
			WizardDialog dialog = (WizardDialog)wizard.getContainer();
			dialog.close();
		}
		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		System.out.println(text);
		List<String> urls = JSLibFactory.getInstance()
				.getDefaultModel()
				.getLib(IonicConstants.IONIC_CATEGORY).getVersion(IonicVersion.getLatestDefaultVersion().toString())
				.getURLs();
		for (String url: urls) {
			assertTrue(text.indexOf(url) >= 0);
		}
	}

	public void testInsertAround(){
		editor = openEditor("insert_around.html");
		runJSCSS();
		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		compare(text, test_result_2_1);
	}
	
	public void testInsertIntoNotClosedTags(){
		editor = openEditor("not_closed_tag.html");
		runJSCSS();
		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		compare(text, test_result_3);
	}

	public void testInsertIntoNormal(){
		editor = openEditor("normal.html");
		runJSCSS();
		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		compare(text, test_result_2_1);
	}
	
//	public void testInsertIntoDifferentVersion(){
//		editor = openEditor("different_version.html");
//		runJSCSS();
//		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
//		compare(text, test_result_4);
//	}
//
	public void testInsertIntoBody(){
		editor = openEditor("body_only.html");
		runJSCSS();
		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		compare(text, test_result_2);
	}
	
	public void testAddJSCSSCheckboxSetFalse() {
		doTestAddJSCSSCheckbox("body_only.html", false, false);
	}

	public void testAddJSCSSCheckboxSetTrue() {
		doTestAddJSCSSCheckbox("body_only.html", true, true);
	}
	
	public void testAddJSCSSCheckboxSetFalseWithScript() {
		doTestAddJSCSSCheckbox("script_load.html", false, false);
	}

//TODO provide another file
//	public void testAddJSCSSCheckboxSetTrueWithScript() {
//		doTestAddJSCSSCheckbox("script_load.html", true, false);
//	}
	
//TODO implement when single-line wizard is implemented
//	// to tags file
//	// single tag (Button)
//	public void testInsertSingleTagWithJcCssInEmptyFileWithNoSelection(){
//		doTestWithMultipleParameters("no_tags.html", true, true, true, test_result_01);
//	}
//
//	public void testInsertSingleTagWithNoJcCssInEmptyFileWithNoSelection(){
//		doTestWithMultipleParameters("no_tags.html", true, true, false, test_result_02);
//	}
//
//	public void testInsertSingleTagWithNoJcCssInEmptyFileWithSelection(){
//		doTestWithMultipleParameters("no_tags.html", true, false, false, test_result_03);
//	}
//
//	public void testInsertSingleTagWithJcCssInEmptyFileWithSelection(){
//		doTestWithMultipleParameters("no_tags.html", true, false, true, test_result_04);
//	}
//
//	// to tags file
//	// multiple tag (Page)
	public void testInsertMultiTagWithJcCssInEmptyFileWithNoSelection(){
		doTestWithMultipleParameters("no_tags.html", false, true, true, test_result_05);
	}

	public void testInsertMultiTagWithNoJcCssInEmptyFileWithNoSelection(){
		doTestWithMultipleParameters("no_tags.html", false, true, false, test_result_06);
	}

	public void testInsertMultiTagWithNoJcCssInEmptyFileWithSelection(){
		doTestWithMultipleParameters("no_tags.html", false, false, false, test_result_07);
	}

	public void testInsertMultiTagWithJcCssInEmptyFileWithSelection(){
		doTestWithMultipleParameters("no_tags.html", false, false, true, test_result_08);
	}
//
//	// file with html tag
//	// single tag (Button)
//	public void testInsertSingleTagWithJcCssInHtmlFileWithNoSelection(){
//		doTestWithMultipleParameters("html_tag.html", true, true, true, test_result_01);
//	}
//
//	public void testInsertSingleTagWithNoJcCssInHtmlFileWithNoSelection(){
//		doTestWithMultipleParameters("html_tag.html", true, true, false, test_result_12);
//	}
//
//	public void testInsertSingleTagWithNoJcCssInHtmlFileWithSelection(){
//		doTestWithMultipleParameters("html_tag.html", true, false, false, test_result_13);
//	}
//
//	public void testInsertSingleTagWithJcCssInHtmlFileWithSelection(){
//		doTestWithMultipleParameters("html_tag.html", true, false, true, test_result_04);
//	}
//
//	// file with html tag
//	// multiple tag (Page)
	public void testInsertMultiTagWithJcCssInHtmlFileWithNoSelection(){
		doTestWithMultipleParameters("html_tag.html", false, true, true, test_result_05);
	}

//	public void testInsertMultiTagWithNoJcCssInHtmlFileWithNoSelection(){
//		doTestWithMultipleParameters("html_tag.html", false, true, false, test_result_16);
//	}
//
//	public void testInsertMultiTagWithNoJcCssInHtmlFileWithSelection(){
//		doTestWithMultipleParameters("html_tag.html", false, false, false, test_result_17);
//	}
//
//	public void testInsertMultiTagWithJcCssInHtmlFileWithSelection(){
//		doTestWithMultipleParameters("html_tag.html", false, false, true, test_result_08);
//	}
//
//	// file with html and head tags
//	// single tag (Button)
//	public void testInsertSingleTagWithJcCssInHtmlHeadFileWithNoSelection(){
//		doTestWithMultipleParameters("html_head_tags.html", true, true, true, test_result_01);
//	}
//
//	public void testInsertSingleTagWithNoJcCssInHtmlHeadFileWithNoSelection(){
//		doTestWithMultipleParameters("html_head_tags.html", true, true, false, test_result_22);
//	}
//
//	public void testInsertSingleTagWithNoJcCssInHtmlHeadFileWithSelection(){
//		doTestWithMultipleParameters("html_head_tags.html", true, false, false, test_result_23);
//	}
//
//	public void testInsertSingleTagWithJcCssInHtmlHeadFileWithSelection(){
//		doTestWithMultipleParameters("html_head_tags.html", true, false, true, test_result_04);
//	}
//
//	// file with html and head tags
//	// multiple tag (Page)
	public void testInsertMultiTagWithJcCssInHtmlHeadFileWithNoSelection(){
		doTestWithMultipleParameters("html_head_tags.html", false, true, true, test_result_05);
	}

	public void testInsertMultiTagWithNoJcCssInHtmlHeadFileWithNoSelection(){
		doTestWithMultipleParameters("html_head_tags.html", false, true, false, test_result_26);
	}

	public void testInsertMultiTagWithNoJcCssInHtmlHeadFileWithSelection(){
		doTestWithMultipleParameters("html_head_tags.html", false, false, false, test_result_27);
	}

	public void testInsertMultiTagWithJcCssInHtmlHeadFileWithSelection(){
		doTestWithMultipleParameters("html_head_tags.html", false, false, true, test_result_08);
	}
//	// file with html, head and body tags
//	// single tag (Button)
//	public void testInsertSingleTagWithJcCssInHtmlHeadBodyFileWithNoSelection(){
//		doTestWithMultipleParameters("html_head_body_tags.html", true, true, true, test_result_01);
//	}
//
//	public void testInsertSingleTagWithNoJcCssInHtmlHeadBodyFileWithNoSelection(){
//		doTestWithMultipleParameters("html_head_body_tags.html", true, true, false, test_result_32);
//	}
//
//	public void testInsertSingleTagWithNoJcCssInHtmlHeadBodyFileWithSelection(){
//		doTestWithMultipleParameters("html_head_body_tags.html", true, false, false, test_result_33);
//	}
//
//	public void testInsertSingleTagWithJcCssInHtmlHeadBodyFileWithSelection(){
//		doTestWithMultipleParameters("html_head_body_tags.html", true, false, true, test_result_04);
//	}
//
//	// file with html, head and body tags
//	// multiple tag (Page)
//	public void testInsertMultiTagWithJcCssInHtmlHeadBodyFileWithNoSelection(){
//		doTestWithMultipleParameters("html_head_body_tags.html", false, true, true, test_result_05);
//	}
//
//	public void testInsertMultiTagWithNoJcCssInHtmlHeadBodyFileWithNoSelection(){
//		doTestWithMultipleParameters("html_head_body_tags.html", false, true, false, test_result_36);
//	}
//
//	public void testInsertMultiTagWithNoJcCssInHtmlHeadBodyFileWithSelection(){
//		doTestWithMultipleParameters("html_head_body_tags.html", false, false, false, test_result_37);
//	}
//
//	public void testInsertMultiTagWithJcCssInHtmlHeadBodyFileWithSelection(){
//		doTestWithMultipleParameters("html_head_body_tags.html", false, false, true, test_result_08);
//	}
//
//	public void testInsertSingleTagAfterComment(){
//		doTestWithMultipleParameters("after_comment.html", true, true, false, test_result_001);
//	}
//
//	public void testInsertSingleTagBeforeComment(){
//		doTestWithMultipleParameters("before_comment.html", true, true, false, test_result_002);
//	}

	void doTestAddJSCSSCheckbox(String fileName, boolean value, boolean expected) {
		editor = openEditor(fileName);

		String sValue = value ? TRUE : FALSE;
		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertFalse(text.indexOf(link(getCSS()) + ">") > 0);

		IWizardPage currentPage = runToolEntry(IonicConstants.IONIC_CATEGORY, "Content", true);

		assertTrue(currentPage instanceof NewContentWizardPage);

		NewContentWizardPage wizardPage = (NewContentWizardPage)currentPage;
		NewContentWizard wizard = (NewContentWizard)wizardPage.getWizard();

		wizardPage.setEditorValue(AbstractNewHTMLWidgetWizardPage.ADD_JS_CSS_SETTING_NAME, sValue);
		assertEquals(sValue, wizardPage.getEditorValue(AbstractNewHTMLWidgetWizardPage.ADD_JS_CSS_SETTING_NAME));
		
		wizard.performFinish();
		WizardDialog dialog = (WizardDialog)wizard.getContainer();
		dialog.close();

		text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertEquals(expected, text.indexOf(link(getCSS()) + ">") > 0);
	}

	private void compare(String test, String[] result){
		String[] spl = test.split("[\r\n]+");
		assertEquals("Unexpected number of lines",result.length, spl.length);
		for(int i = 0; i < result.length; i++){
			String token = spl[i];
			assertEquals("Unexpected line", result[i], token);
		}
	}
	
	void doTestWithMultipleParameters(String fileName, boolean singleInsertion, boolean singleSelection, boolean insertJsCss, String[] result){
		editor = openEditor(fileName);
		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		String sValue = insertJsCss ? TRUE : FALSE;
		
		if(singleSelection){
			int offset = text.indexOf("|");
			if(offset >= 0){
				textEditor.getSelectionProvider().setSelection(new TextSelection(offset, 0));
			}else{
				fail("No selection mark | in the file: "+fileName);
			}
		}else{
			int offset = text.indexOf("|");
			if(offset >= 0){
				int offset2 = text.indexOf("|", offset+1);
				if(offset2 >= 0){
					textEditor.getSelectionProvider().setSelection(new TextSelection(offset, offset2-offset+1));
				}else{
					fail("No second selection mark | in the file: "+fileName);
				}
			}else{
				fail("No selection mark | in the file: "+fileName);
			}
		}
		IWizardPage currentPage = null;
		if(singleInsertion){
			currentPage = runToolEntry(IonicConstants.IONIC_CATEGORY, "Content", true);
			
			assertTrue(currentPage instanceof NewContentWizardPage);
		}else{
			currentPage = runToolEntry(IonicConstants.IONIC_CATEGORY, "Tab", true);
			
			assertTrue(currentPage instanceof NewTabWizardPage);
		}

		AbstractNewHTMLWidgetWizardPage wizardPage = (AbstractNewHTMLWidgetWizardPage)currentPage;
		NewIonicWidgetWizard<?> wizard = (NewIonicWidgetWizard<?>)wizardPage.getWizard();

		wizardPage.setEditorValue(AbstractNewHTMLWidgetWizardPage.ADD_JS_CSS_SETTING_NAME, sValue);
		assertEquals(sValue, wizardPage.getEditorValue(AbstractNewHTMLWidgetWizardPage.ADD_JS_CSS_SETTING_NAME));
		
		wizard.performFinish();
		WizardDialog dialog = (WizardDialog)wizard.getContainer();
		dialog.close();

		text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		compare(text, result);
	}

	String getCSS() {
		return "lib/ionic/css/ionic.css";
	}
}