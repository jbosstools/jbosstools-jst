package org.jboss.tools.jst.web.ui.test;

import java.util.StringTokenizer;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewPageWizard;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewPageWizardPage;

public class InsertJSCSSPaletteEntryTest extends AbstractPaletteEntryTest implements JQueryConstants {
	private String[] test_result_1={
			"<!DOCTYPE html>",
			"<html>",
		    "<head>",
			"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">",
			"<link rel=\"stylesheet\" href=\"http://code.jquery.com/mobile/1.3.0/jquery.mobile-1.3.0.min.css\" />",
			"<script src=\"http://code.jquery.com/jquery-1.9.1.min.js\"></script>",
			"<script src=\"http://code.jquery.com/mobile/1.3.0/jquery.mobile-1.3.0.min.js\"></script>",
			"</head>",
			"<body>",
			"",
			"</body>",
			"</html>"
	};
	private String[] test_result_2={
			"<!DOCTYPE html>",
			"<html>",
		    "<head>",
			"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">",
			"<link rel=\"stylesheet\" href=\"http://code.jquery.com/mobile/1.3.0/jquery.mobile-1.3.0.min.css\" />",
			"<script src=\"http://code.jquery.com/jquery-1.9.1.min.js\"></script>",
			"<script src=\"http://code.jquery.com/mobile/1.3.0/jquery.mobile-1.3.0.min.js\"></script>",
			"</head>",
			"<body>",
			"<div data-role=\"collapsible-set\">",
			"<div data-role=\"collapsible\">",
			"<h3>I'm a header</h3>",
			"<p>I'm the collapsible content.</p>",
			"",
			"</div>",
		    "</div>",
		    "</body>",
			"</html>"
	};
	private String[] test_result_3={
			"<!DOCTYPE html>",
			"<html>",
		    "<head>",
			"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">",
			"<link rel=\"stylesheet\" href=\"http://code.jquery.com/mobile/1.3.0/jquery.mobile-1.3.0.min.css\" />",
			"<script src=\"http://code.jquery.com/jquery-1.9.1.min.js\"></script>",
			"<script src=\"http://code.jquery.com/mobile/1.3.0/jquery.mobile-1.3.0.min.js\"></script>",
			"</head>",
			"<body>",
			"</body>",
			"</html>"
	};

	private String[] test_result_4={
			"<!DOCTYPE html>",
			"<html>",
		    "<head>",
		    "<meta name=\"viewport\">",
		    "<link rel=\"stylesheet\" href=\"jquery.mobile-1.7.6.css\" />",
		    "<script src=\"a:/jquery-2.7.1.min.js\"></script>",
		    "<script src=\"b:/jquery.mobile-1.9.0.min.js\"></script>",
			"</head>",
			"<body>",
			"</body>",
			"</html>"
	};

	IEditorPart editor = null;

	public InsertJSCSSPaletteEntryTest() {}

	public void tearDown() {
		if(editor != null){
			editor.getSite().getPage().closeEditor(editor, false);
		}
	}
	
	public void testInsertIntoEmptyFile() {
		editor = openEditor("empty.html");
		runToolEntry("jQuery Mobile", "JS/CSS", false);
		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		compare(text, test_result_1);
	}
	
	public void testInsertAround(){
		editor = openEditor("insert_around.html");
		runToolEntry("jQuery Mobile", "JS/CSS", false);
		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		compare(text, test_result_2);
	}
	
	public void testInsertIntoNotClosedTags(){
		editor = openEditor("not_closed_tag.html");
		runToolEntry("jQuery Mobile", "JS/CSS", false);
		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		compare(text, test_result_3);
	}

	public void testInsertIntoNormal(){
		editor = openEditor("normal.html");
		runToolEntry("jQuery Mobile", "JS/CSS", false);
		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		compare(text, test_result_2);
	}
	
	public void testInsertIntoDifferentVersion(){
		editor = openEditor("different_version.html");
		runToolEntry("jQuery Mobile", "JS/CSS", false);
		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		compare(text, test_result_4);
	}

	public void testInsertIntoBody(){
		editor = openEditor("body_only.html");
		runToolEntry("jQuery Mobile", "JS/CSS", false);
		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		compare(text, test_result_2);
	}
	
	public void testInsertJSCSS(){
		editor = openEditor("body_only.html");
		runToolEntry("jQuery Mobile", "JS/CSS", false);
		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		compare(text, test_result_2);
	}
	
	public void testAddJSCSSCheckboxSetFalse() {
		doTestAddJSCSSCheckbox("body_only.html", false, false);
	}

	public void testAddJSCSSCheckboxSetTrue() {
		doTestAddJSCSSCheckbox("body_only.html", true, true);
	}
	
	private static final String LINK = "<link rel=\"stylesheet\" href=\"http://code.jquery.com/mobile/1.3.0/jquery.mobile-1.3.0.min.css\" />";
	
	public void testAddJSCSSCheckboxSetFalseWithScript() {
		doTestAddJSCSSCheckbox("script_load.html", false, false);
	}

	public void testAddJSCSSCheckboxSetTrueWithScript() {
		doTestAddJSCSSCheckbox("script_load.html", true, false);
	}

	void doTestAddJSCSSCheckbox(String fileName, boolean value, boolean expected) {
		editor = openEditor(fileName);

		String sValue = value ? TRUE : FALSE;
		String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertFalse(text.indexOf(LINK) > 0);

		IWizardPage currentPage = runToolEntry("jQuery Mobile", "Page", true);

		assertTrue(currentPage instanceof NewPageWizardPage);

		NewPageWizardPage wizardPage = (NewPageWizardPage)currentPage;
		NewPageWizard wizard = (NewPageWizard)wizardPage.getWizard();

		wizardPage.setEditorValue(NewPageWizardPage.ADD_JS_CSS_SETTING_NAME, sValue);
		assertEquals(sValue, wizardPage.getEditorValue(NewPageWizardPage.ADD_JS_CSS_SETTING_NAME));
		
		wizard.performFinish();
		WizardDialog dialog = (WizardDialog)wizard.getContainer();
		dialog.close();

		text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertEquals(expected, text.indexOf(LINK) > 0);
	}

	private void compare(String test, String[] result){
		StringTokenizer tokenizer = new StringTokenizer(test, "\n");
		assertEquals("Unexpected number of lines",result.length, tokenizer.countTokens());
		for(int i = 0; i < result.length; i++){
			String token = tokenizer.nextToken().trim();
			assertEquals("Unexpected line", result[i], token);
		}
	}
	
}
