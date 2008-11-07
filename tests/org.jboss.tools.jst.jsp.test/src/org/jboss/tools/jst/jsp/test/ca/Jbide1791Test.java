/*******************************************************************************
  * Copyright (c) 2007-2008 Red Hat, Inc.
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
import org.jboss.tools.common.test.util.TestProjectProvider;

/**
 * Test cast testing http://jira.jboss.com/jira/browse/JBIDE-1791 issue.
 * 
 * @author Eugene Stherbin
 *
 */
public class Jbide1791Test extends ContentAssistantTestCase {
    private static final String PROJECT_NAME = "JsfJbide1791Test";
    private static final String PAGE_NAME = "/WebContent/pages/jbide1791.xhtml";
    private TestProjectProvider provider = null;
    
    public static Test suite() {
        return new TestSuite(Jbide1791Test.class);
    }
    
    public void setUp() throws Exception {
        provider = new TestProjectProvider("org.jboss.tools.jst.jsp.test", null, PROJECT_NAME,false); 
        project = provider.getProject();
        Throwable exception = null;
    }

    protected void tearDown() throws Exception {
        if(provider != null) {
            provider.dispose();
        }
    }
    public void testJbide1791(){
        final String[] proposals = new String[]{
                "h1",
                "h2",
                "h3",
                "h4",
                "h5",
                "h6",
                "hr"
        };
        openEditor(PAGE_NAME);
        IRegion reg=null;
		try {
			reg = new FindReplaceDocumentAdapter(this.document).find(0, "</style>", true, false, false, false);
		} catch (BadLocationException e) {
			fail(e.getMessage());
		}
        final ICompletionProposal[] rst = checkProposals(PAGE_NAME,reg.getOffset(), proposals, false);
        
        checkResult(rst,proposals);
        closeEditor();
    }
    /**
     * @param rst
     * @param proposals
     */
    private void checkResult(ICompletionProposal[] rst, String[] proposals) {
        for ( int i = 0 ; i < proposals.length ; i ++ ){
           assertTrue("Should be in proposals list",isInResultList(rst,proposals[i]));
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
