/*******************************************************************************
 * Copyright (c) 2011-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.test.ca;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.jboss.tools.test.util.ProjectImportTestSetup;

/**
 * Test case for JBIDE-6061 issue.
 * 
 * @author Victor V. Rubezhny
 *
 */
public class Jbide6061Test extends ContentAssistantTestCase {
   private static final String PROJECT_NAME = "Jbide6061Test"; //$NON-NLS-1$
   private static final String JSP_PAGE_NAME = "/WebContent/pages/jsp_page.jsp"; //$NON-NLS-1$
   private static final String XHTML_PAGE_NAME = "/WebContent/pages/xhtml_page.xhtml"; //$NON-NLS-1$
   private static final String HTML_PAGE_NAME = "/WebContent/pages/html_page.html"; //$NON-NLS-1$
   private final String[] CSSCLASS_PROPOSALS = new String[]{
           "errors", //$NON-NLS-1$
           "cls1", //$NON-NLS-1$
           "cls2", //$NON-NLS-1$
           "cls3", //$NON-NLS-1$
           "cls4", //$NON-NLS-1$
           "cls5", //$NON-NLS-1$
   };
   private static final String[] STRINGS_TO_FIND_IN_JSP = new String[] {"class=\"", "styleClass=\""};
   private static final String[] STRINGS_TO_FIND_IN_XHTML = new String[] {"class=\"", "styleClass=\""};
   private static final String[] STRINGS_TO_FIND_IN_HTML = new String [] {"class=\""};

	public void setUp() throws Exception {
		project = ProjectImportTestSetup.loadProject(PROJECT_NAME);
	}

   public void testJbide6061OnJspPage(){
	   for (String textToFind : STRINGS_TO_FIND_IN_JSP) {
		   doTheCSSClassValuesTest(JSP_PAGE_NAME, textToFind, CSSCLASS_PROPOSALS);
	   }
   }

   public void testJbide6061OnHtmlPage(){
	   for (String textToFind : STRINGS_TO_FIND_IN_HTML) {
		   doTheCSSClassValuesTest(HTML_PAGE_NAME, textToFind, CSSCLASS_PROPOSALS);
	   }
   }

   public void testJbide6061OnXhtmlPage(){
	   for (String textToFind : STRINGS_TO_FIND_IN_XHTML) {
		   doTheCSSClassValuesTest(XHTML_PAGE_NAME, textToFind, CSSCLASS_PROPOSALS);
	   }
   }
   
   protected void doTheCSSClassValuesTest(String pageName, String textToFind, String[] proposals) {
       openEditor(pageName);
       IRegion reg=null;
		try {
			reg = new FindReplaceDocumentAdapter(this.document).find(0, textToFind, true, false, false, false); //$NON-NLS-1$
		} catch (BadLocationException e) {
			fail(e.getMessage());
		}
		
		assertNotNull("Cannot find a text region to test", reg);
		
       final ICompletionProposal[] rst = checkProposals(pageName,reg.getOffset() + textToFind.length(), proposals, false);
       
       checkResult(rst,proposals);
       closeEditor();
   }
   
   /**
    * @param rst
    * @param proposals
    */
   private void checkResult(ICompletionProposal[] rst, String[] proposals) {
       for ( int i = 0 ; i < proposals.length ; i ++ ){
          assertTrue("Should be in proposals list",isInResultList(rst,proposals[i])); //$NON-NLS-1$
       }
       
   }
   /**
    * @param rst
    * @param string
    * @return
    */
   private boolean isInResultList(ICompletionProposal[] rst, String string) {
       boolean r = false;
       
       for(ICompletionProposal cp:rst){
           if(cp.getDisplayString().equals(string)){
               r = true;
               break;
           }
       }
       return r;
   }
}