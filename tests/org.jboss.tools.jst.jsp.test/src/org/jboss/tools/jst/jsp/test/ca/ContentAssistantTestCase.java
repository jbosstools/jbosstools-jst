package org.jboss.tools.jst.jsp.test.ca;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.jboss.tools.common.text.ext.util.Utils;
import org.jboss.tools.jst.jsp.contentassist.AutoContentAssistantProposal;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPTextEditor;
import org.jboss.tools.jst.jsp.test.TestUtil;
import org.jboss.tools.test.util.WorkbenchUtils;

public class ContentAssistantTestCase extends TestCase {
	protected IProject project = null;
	protected JSPMultiPageEditor jspEditor = null;
	protected JSPTextEditor jspTextEditor = null;
	protected StructuredTextViewer viewer = null;
	protected IContentAssistant contentAssistant = null;
	protected IDocument document = null;

	public void openEditor(String fileName) {
		IEditorPart editorPart = WorkbenchUtils.openEditor(project.getName()+"/"+ fileName);
//		System.out.println("openEditor: " + project.getName()+"/"+ fileName);
		if (editorPart instanceof JSPMultiPageEditor)
			jspEditor = (JSPMultiPageEditor) editorPart;

		jspTextEditor = jspEditor.getJspEditor();
		viewer = jspTextEditor.getTextViewer();
		document = viewer.getDocument();
		SourceViewerConfiguration config = TestUtil
				.getSourceViewerConfiguration(jspTextEditor);
		contentAssistant = (config == null ? null : config
				.getContentAssistant(viewer));

		assertTrue(
				"Cannot get the Content Assistant instance for the editor for page \""
						+ fileName + "\"", (contentAssistant != null));

		assertTrue("The IDocument is not instance of IStructuredDocument for page \""
				+ fileName + "\"",
				(document instanceof IStructuredDocument));

	}

	public ICompletionProposal[] checkProposals(String fileName, int offset, String[] proposals, boolean exactly) {
        return checkProposals(fileName, null, offset, proposals, exactly, true);
    }

	public ICompletionProposal[] checkProposals(String fileName, String substring, int offset, String[] proposals, boolean exactly) {
		return checkProposals(fileName, substring, offset, proposals, exactly, false);
	}
	public ICompletionProposal[] checkProposals(String fileName, String substring, int offset, String[] proposals, boolean exactly, boolean excludeELProposalsFromExactTest){
//		System.out.println("checkProposals >>> Enterring");
//		System.out.println("checkProposals >>> invoking openEditor() for " + fileName);
		openEditor(fileName);
//		System.out.println("checkProposals >>> openEditor() is invoked for " + fileName);

        int position = 0;
        if (substring != null) {
            String documentContent = document.get();
            position = documentContent.indexOf(substring);
        }

        ICompletionProposal[] result = null;

//		System.out.println("checkProposals >>> invoking TestUtil.getProcessor() for position " + (position + offset));
        IContentAssistProcessor p = TestUtil.getProcessor(viewer, position + offset, contentAssistant);
//		System.out.println("checkProposals >>> TestUtil.getProcessor() is invoked for " + (position + offset));
        if (p != null) {
            try {
//        		System.out.println("checkProposals >>> invoking p.computeCompletionProposals() for position " + (position + offset));
                result = p.computeCompletionProposals(viewer, position + offset);
//        		System.out.println("checkProposals >>> p.computeCompletionProposals() is invoked for " + (position + offset));
            } catch (Throwable x) {
                x.printStackTrace();
            }
        }
//		System.out.println("checkProposals >>> Performing the values check up");

        assertTrue("Content Assistant returned no proposals", (result != null && result.length > 0));

        // for (int i = 0; i < result.length; i++) {
        // System.out.println("proposal - "+result[i].getDisplayString());
        // }

        int foundCounter = 0;
        for (int i = 0; i < proposals.length; i++) {
        	boolean found = compareProposal(proposals[i], result);
        	if (found)
        		foundCounter++;
            assertTrue("Proposal " + proposals[i] + " not found!", found );
        }

        if (exactly) {
        	if (excludeELProposalsFromExactTest) {
        		assertTrue("Some other proposals were found!", foundCounter == proposals.length);
        	} else {
                assertTrue("Some other proposals were found!", result.length == proposals.length);
        	}
        }

//        System.out.println("checkProposals <<< Exiting");
        return result;
	}

	public boolean compareProposal(String proposalName, ICompletionProposal[] proposals){
		for (int i = 0; i < proposals.length; i++) {
			if (proposals[i] instanceof AutoContentAssistantProposal) {
				AutoContentAssistantProposal ap = (AutoContentAssistantProposal)proposals[i];
				String replacementString = ap.getReplacementString().toLowerCase();
				if (replacementString.equalsIgnoreCase(proposalName)) return true;
				
				// For a tag proposal there will be not only the the tag name but all others characters like default attributes, tag ending characters and so on
				String[] replacementStringParts = replacementString.split(" ");
				if (replacementStringParts != null && replacementStringParts.length > 0) {
					if (replacementStringParts[0].equalsIgnoreCase(proposalName)) return true;
				}
				
				// for an attribute proposal there will be a pare of attribute-value (i.e. attrName="attrValue")
				replacementStringParts = replacementString.split("=");
				if (replacementStringParts != null && replacementStringParts.length > 0) {
					if (replacementStringParts[0].equalsIgnoreCase(proposalName)) return true;
				}
				
				// for an Unclosed EL the closing character is appended to the proposal string (i.e. person} )
				// perform case sensitive compare operation
				replacementStringParts = replacementString.split("}");
				if (replacementStringParts != null && replacementStringParts.length > 0) {
					if (replacementStringParts[0].equals(proposalName)) return true;
				}
				
				// For an attribute value proposal there will be the quote characters
				replacementString = Utils.trimQuotes(replacementString);
				if (replacementString.equalsIgnoreCase(proposalName)) return true;
			
			} else {
				if(proposals[i].getDisplayString().toLowerCase().equals(proposalName.toLowerCase())) return true;
			}
		}
		return false;
	}

	public void closeEditor() {
		if (jspEditor != null) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().closeEditor(jspEditor, false);
			jspEditor = null;
		}
	}

	/**
	 * @return the project
	 */
	public IProject getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(IProject project) {
		this.project = project;
	}

	/**
	 * @return the jspEditor
	 */
	public JSPMultiPageEditor getJspEditor() {
		return jspEditor;
	}

	/**
	 * @param jspEditor the jspEditor to set
	 */
	public void setJspEditor(JSPMultiPageEditor jspEditor) {
		this.jspEditor = jspEditor;
	}

	/**
	 * @return the jspTextEditor
	 */
	public JSPTextEditor getJspTextEditor() {
		return jspTextEditor;
	}

	/**
	 * @param jspTextEditor the jspTextEditor to set
	 */
	public void setJspTextEditor(JSPTextEditor jspTextEditor) {
		this.jspTextEditor = jspTextEditor;
	}

	/**
	 * @return the viewer
	 */
	public StructuredTextViewer getViewer() {
		return viewer;
	}

	/**
	 * @param viewer the viewer to set
	 */
	public void setViewer(StructuredTextViewer viewer) {
		this.viewer = viewer;
	}

	/**
	 * @return the contentAssistant
	 */
	public IContentAssistant getContentAssistant() {
		return contentAssistant;
	}

	/**
	 * @param contentAssistant the contentAssistant to set
	 */
	public void setContentAssistant(IContentAssistant contentAssistant) {
		this.contentAssistant = contentAssistant;
	}

	/**
	 * @return the document
	 */
	public IDocument getDocument() {
		return document;
	}

	/**
	 * @param document the document to set
	 */
	public void setDocument(IDocument document) {
		this.document = document;
	}
}