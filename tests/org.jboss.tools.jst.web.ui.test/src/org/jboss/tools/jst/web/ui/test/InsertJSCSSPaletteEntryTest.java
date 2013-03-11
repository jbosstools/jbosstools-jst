package org.jboss.tools.jst.web.ui.test;

import java.util.StringTokenizer;

import org.eclipse.ui.IEditorPart;

public class InsertJSCSSPaletteEntryTest extends AbstractPaletteEntryTest {
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

	public InsertJSCSSPaletteEntryTest() {}
	
	public void testInsertIntoEmptyFile() {
		IEditorPart editor=null;
		try{
			editor = openEditor("empty.html");

			runToolEntry("jQuery Mobile", "JS/CSS", false);

			String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
			
			compare(text, test_result_1);
		}finally{
			if(editor != null){
				editor.getSite().getPage().closeEditor(editor, false);
			}
		}
	}
	
	public void testInsertAround(){
		IEditorPart editor=null;
		try{
			editor = openEditor("insert_around.html");

			runToolEntry("jQuery Mobile", "JS/CSS", false);

			String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
			
			compare(text, test_result_2);
		}finally{
			if(editor != null){
				editor.getSite().getPage().closeEditor(editor, false);
			}
		}
	}
	
	public void testInsertIntoNotClosedTags(){
		IEditorPart editor=null;
		try{
			editor = openEditor("not_closed_tag.html");

			runToolEntry("jQuery Mobile", "JS/CSS", false);

			String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
			
			compare(text, test_result_3);
		}finally{
			if(editor != null){
				editor.getSite().getPage().closeEditor(editor, false);
			}
		}
	}

	public void testInsertIntoNormal(){
		IEditorPart editor=null;
		try{
			editor = openEditor("normal.html");

			runToolEntry("jQuery Mobile", "JS/CSS", false);

			String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
			
			compare(text, test_result_2);
		}finally{
			if(editor != null){
				editor.getSite().getPage().closeEditor(editor, false);
			}
		}
	}
	
	public void testInsertIntoDifferentVersion(){
		IEditorPart editor=null;
		try{
			editor = openEditor("different_version.html");

			runToolEntry("jQuery Mobile", "JS/CSS", false);

			String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
			
			compare(text, test_result_4);
		}finally{
			if(editor != null){
				editor.getSite().getPage().closeEditor(editor, false);
			}
		}
	}

	public void testInsertIntoBody(){
		IEditorPart editor=null;
		try{
			editor = openEditor("body_only.html");

			runToolEntry("jQuery Mobile", "JS/CSS", false);

			String text = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
			
			compare(text, test_result_2);
		}finally{
			if(editor != null){
				editor.getSite().getPage().closeEditor(editor, false);
			}
		}
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
