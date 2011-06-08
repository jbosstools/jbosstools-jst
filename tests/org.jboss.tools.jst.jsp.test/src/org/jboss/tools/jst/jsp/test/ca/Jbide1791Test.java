/*******************************************************************************
  * Copyright (c) 2007-2011 Red Hat, Inc.
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
 * Test case testing http://jira.jboss.com/jira/browse/JBIDE-1791 issue.
 * The same test case is suitable for http://jira.jboss.com/jira/browse/JBIDE-7100 issue
 * 
 * @author Eugene Stherbin
 *
 */
public class Jbide1791Test extends ContentAssistantTestCase {
    private static final String PROJECT_NAME = "JsfJbide1791Test"; //$NON-NLS-1$
    private static final String PAGE_NAME = "/WebContent/pages/jbide1791.xhtml"; //$NON-NLS-1$
    private TestProjectProvider provider = null;
    
    public static Test suite() {
        return new TestSuite(Jbide1791Test.class);
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
    
    public void testJbide1791(){
        final String[] proposals = new String[]{
                "h1", //$NON-NLS-1$
                "h2", //$NON-NLS-1$
                "h3", //$NON-NLS-1$
                "h4", //$NON-NLS-1$
                "h5", //$NON-NLS-1$
                "h6", //$NON-NLS-1$
                "hr" //$NON-NLS-1$
        };
        openEditor(PAGE_NAME);
        IRegion reg=null;
		try {
			reg = new FindReplaceDocumentAdapter(this.document).find(0, "</style>", true, false, false, false); //$NON-NLS-1$
		} catch (BadLocationException e) {
			fail(e.getMessage());
		}
		assertNotNull("Cannot find a text region to test", reg);
		
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
