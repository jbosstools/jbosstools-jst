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
* Test case testing http://jira.jboss.com/jira/browse/JBIDE-9092 issue.
* 
* @author Victor Rubezhny
*
*/
public class Jbide9092Test extends ContentAssistantTestCase {
   private static final String PROJECT_NAME = "JsfJbide1791Test"; //$NON-NLS-1$
   private static final String PAGE_NAME = "/WebContent/pages/jbide1791.xhtml"; //$NON-NLS-1$
   private TestProjectProvider provider = null;
   
   public static Test suite() {
       return new TestSuite(Jbide9092Test.class);
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
   
   public void testJbide9092(){
	   
	   // Do not use dir-attribute here because there is dir-tag proposal (the correct proposal with the same name)
       final String[] wrongProposals = new String[]{
               "jsfc", //$NON-NLS-1$
               "lang", //$NON-NLS-1$
               "media", //$NON-NLS-1$
               "title", //$NON-NLS-1$
               "type", //$NON-NLS-1$
       };
       openEditor(PAGE_NAME);
       try {
	       IRegion reg=null;
			try {
				reg = new FindReplaceDocumentAdapter(this.document).find(0, "<style", true, false, false, false); //$NON-NLS-1$
			} catch (BadLocationException e) {
				fail(e.getMessage());
			}
			assertNotNull("Cannot find a text region to test", reg);
			try {
				reg = new FindReplaceDocumentAdapter(this.document).find(reg.getOffset(), ">", true, false, false, false); //$NON-NLS-1$
			} catch (BadLocationException e) {
				fail(e.getMessage());
			}
			assertNotNull("Cannot find a text region to test", reg);
	
	       final ICompletionProposal[] rst = checkProposals(PAGE_NAME,reg.getOffset() + reg.getLength(), new String[] {}, false);
	       
	       checkNotInResult(rst,wrongProposals);
       } finally {
    	   closeEditor();
       }
   }
   /**
    * @param rst
    * @param proposals
    */
   private void checkNotInResult(ICompletionProposal[] rst, String[] proposals) {
       for ( int i = 0 ; i < proposals.length ; i ++ ){
          assertTrue("Proposal '" + proposals[i] + "' should NOT be in proposals list",isNotInResultList(rst,proposals[i])); //$NON-NLS-1$
       }
       
   }
   /**
    * @param rst
    * @param string
    * @return
    */
   private boolean isNotInResultList(ICompletionProposal[] rst, String string) {
       for(ICompletionProposal cp:rst){
           if(cp.getDisplayString().equals(string)){
               return false;
           }
       }
       return true;
   }
}
