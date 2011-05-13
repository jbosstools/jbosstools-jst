/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.test.ca;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.jboss.tools.test.util.TestProjectProvider;

/**
 * Test cast testing http://jira.jboss.com/jira/browse/JBIDE-6061 issue.
 * 
 * @author Victor V. Rubezhny
 *
 */
public class Jbide6061Test extends ContentAssistantTestCase {
   private static final String PROJECT_NAME = "Jbide6061Test"; //$NON-NLS-1$
   private static final String JSP_PAGE_NAME = "/WebContent/pages/jsp_page.jsp"; //$NON-NLS-1$
   private static final String XHTML_PAGE_NAME = "/WebContent/pages/xhtml_page.xhtml"; //$NON-NLS-1$
   private TestProjectProvider provider = null;
   private final String[] CSSCLASS_PROPOSALS = new String[]{
           "errors", //$NON-NLS-1$
           "cls1", //$NON-NLS-1$
           "cls2", //$NON-NLS-1$
           "cls3", //$NON-NLS-1$
           "cls4", //$NON-NLS-1$
           "cls5", //$NON-NLS-1$
   };
   private static final String STRING_TO_FIND_IN_JSP = "styleClass=\"";
   private static final String STRING_TO_FIND_IN_XHTML = "class=\"";

   public static Test suite() {
       return new TestSuite(Jbide6061Test.class);
   }
   
   public void setUp() throws Exception {
       provider = new TestProjectProvider("org.jboss.tools.jst.jsp.test", null, PROJECT_NAME,false);  //$NON-NLS-1$
       project = provider.getProject();
   }

   protected void tearDown() throws Exception {
       if(provider != null) {
           provider.dispose();
       }
   }
   
   public void testJbide6061OnJspPage(){
       openEditor(JSP_PAGE_NAME);
       IRegion reg=null;
		try {
			reg = new FindReplaceDocumentAdapter(this.document).find(0, STRING_TO_FIND_IN_JSP, true, false, false, false); //$NON-NLS-1$
		} catch (BadLocationException e) {
			fail(e.getMessage());
		}
		
		assertNotNull("Cannot find a text region to test", reg);
		
       final ICompletionProposal[] rst = checkProposals(JSP_PAGE_NAME,reg.getOffset() + STRING_TO_FIND_IN_JSP.length(), CSSCLASS_PROPOSALS, false);
       
       checkResult(rst,CSSCLASS_PROPOSALS);
       closeEditor();
   }

   public void testJbide6061OnXhtmlPage(){
       openEditor(XHTML_PAGE_NAME);
       IRegion reg=null;
		try {
			reg = new FindReplaceDocumentAdapter(this.document).find(0, STRING_TO_FIND_IN_XHTML, true, false, false, false); //$NON-NLS-1$
		} catch (BadLocationException e) {
			fail(e.getMessage());
		}
		
		assertNotNull("Cannot find a text region to test", reg);
		
       final ICompletionProposal[] rst = checkProposals(XHTML_PAGE_NAME,reg.getOffset() + STRING_TO_FIND_IN_XHTML.length(), CSSCLASS_PROPOSALS, false);
       
       checkResult(rst,CSSCLASS_PROPOSALS);
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
