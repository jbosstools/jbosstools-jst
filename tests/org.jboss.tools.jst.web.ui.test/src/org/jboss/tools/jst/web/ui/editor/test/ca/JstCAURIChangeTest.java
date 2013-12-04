package org.jboss.tools.jst.web.ui.editor.test.ca;

import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.jboss.tools.common.base.test.contentassist.CATestUtil;
import org.jboss.tools.jst.web.kb.KbProjectFactory;
import org.jboss.tools.jst.web.kb.taglib.INameSpaceStorage;
import org.jboss.tools.jst.web.ui.base.test.ca.ContentAssistantTestCase;
import org.jboss.tools.test.util.ProjectImportTestSetup;

public class JstCAURIChangeTest extends ContentAssistantTestCase {
	private static final String PROJECT_NAME = "Jbide6061Test"; //$NON-NLS-1$

	private static final String PAGE2_NAME = "/WebContent/pages/xhtml_page2.xhtml"; //$NON-NLS-1$
	private static final String PREF_1 = "xmlns:g=\"http://java.sun.com/jsf/html\""; //$NON-NLS-1$
	private static final String PREF_2 = "xmlns:g=\"http://java.sun.com/jsf/core\""; //$NON-NLS-1$

	public void setUp() throws Exception {
		project = ProjectImportTestSetup.loadProject(PROJECT_NAME);
	}
	
	public void testCAURIChange() {
		openEditor(PAGE2_NAME);

		String documentContent = document.get();
		int start = documentContent.indexOf("<g:s"); //$NON-NLS-1$
		assertTrue(start > 0);
		int offsetToTest = start + 4;
		
		INameSpaceStorage storage = KbProjectFactory.getKbProject(project, false).getNameSpaceStorage();
		
		try {
			List<ICompletionProposal> res = CATestUtil.collectProposals(contentAssistant, viewer, offsetToTest);
	
			assertTrue("Content Assistant returned no proposals", (res != null && res.size() > 0)); //$NON-NLS-1$
			assertEquals(7, res.size());
			checkResult(res.toArray(new ICompletionProposal[0]), new String[] {"g:selectOneMenu"});

			Set<String> uris1 = storage.getURIs("g");
			assertTrue(uris1.contains("http://java.sun.com/jsf/html"));
			assertFalse(uris1.contains("http://java.sun.com/jsf/core"));

			int p1 = documentContent.indexOf(PREF_1);
			documentContent = documentContent.substring(0, p1) + PREF_2 + documentContent.substring(p1 + PREF_1.length());
			jspTextEditor.setText(documentContent);
			jspEditor.doSave(new NullProgressMonitor());
			
			res = CATestUtil.collectProposals(contentAssistant, viewer, offsetToTest);
			Set<String> uris2 = storage.getURIs("g");
			assertTrue(uris2.contains("http://java.sun.com/jsf/core"));
			assertTrue(uris2.contains("http://java.sun.com/jsf/html"));

			assertTrue("Content Assistant returned no proposals", (res != null && res.size() > 0)); //$NON-NLS-1$
			assertEquals(3, res.size());
			checkResult(res.toArray(new ICompletionProposal[0]), new String[] {"g:selectItem"});
			
			documentContent = documentContent.substring(0, p1) + PREF_1 + documentContent.substring(p1 + PREF_2.length());
			jspTextEditor.setText(documentContent);
			jspEditor.doSave(new NullProgressMonitor());
			
			res = CATestUtil.collectProposals(contentAssistant, viewer, offsetToTest);
			assertTrue("Content Assistant returned no proposals", (res != null && res.size() > 0)); //$NON-NLS-1$
			assertEquals(7, res.size());
			checkResult(res.toArray(new ICompletionProposal[0]), new String[] {"g:selectOneMenu"});
		} finally {
			closeEditor();
		}
	}

    private void checkResult(ICompletionProposal[] rst, String[] proposals) {
        for ( int i = 0 ; i < proposals.length ; i ++ ){
           assertTrue("Should be in proposals list",isInResultList(rst,proposals[i])); //$NON-NLS-1$
        }
        
    }

    private boolean isInResultList(ICompletionProposal[] rst, String string) {
        boolean r = false;

        System.out.println("isInResultList: ");
        for(ICompletionProposal cp:rst){
        	System.out.println(">>> " + cp.getDisplayString() + " =?= " + string);
            if(cp.getDisplayString().equals(string)){
                r = true;
                break;
            }
        }
        return r;
    }

}
