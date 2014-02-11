/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.editor.test.quickassist;

import java.text.MessageFormat;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.quickassist.IQuickAssistAssistant;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.TextInvocationContext;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.html.core.internal.HTMLCoreMessages;
import org.eclipse.wst.sse.ui.internal.reconcile.TemporaryAnnotation;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPTextEditor;
import org.jboss.tools.test.util.ProjectImportTestSetup;

public class JstJspQuickAssistTest extends TestCase {
	public static final String PROBLEM_TYPE = "org.jboss.tools.cdi.core.cdiproblem"; //$NON-NLS-1$
	private static final String PROJECT_NAME = "StaticWebProject";
	private static final String PAGE_NAME = "WebContent/quickassist/html5.html";
	private static final String TEST_STRING = "ng-tro-lo-lo";
	private static final String TEST_QUICKQIX_CLASSNAME = "org.eclipse.wst.html.ui.internal.text.correction.IgnoreAttributeNameCompletionProposal";
	private IProject project;

	public void setUp() throws Exception {
		project = ProjectImportTestSetup.loadProject(PROJECT_NAME);
	}

	/**
	 * Test case for the following issues:
	 * (JBIDE-16440) Standard HTML quickfixes aren't shown in JBoss Tools HTML/JSP editors
	 * (JBIDE-14597) warning about ng-* attributes used by angular js should be possible to ignore
	 *
	 * @throws CoreException
	 */
	public void testWSTQuickAssistProcessors() throws CoreException {
		checkProposalExistance(project, PAGE_NAME, TEST_STRING, 0, 
				TEST_QUICKQIX_CLASSNAME);
	}
	
	
	private void checkProposalExistance(IProject project, String fileName, String str, int id, 
					String proposalClassName) throws CoreException {
		IFile file = project.getFile(fileName);
		assertTrue("File '"+file.getFullPath()+"' not found!", file.exists());

		IEditorInput input = new FileEditorInput(file);
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input,	"org.jboss.tools.jst.jsp.jspeditor.HTMLTextEditor", true);
		ISourceViewer viewer = getViewer(editor);
		
		try{
			IDocument document = viewer.getDocument();
			
			String text = document.get();

			
			int offset = text.indexOf(str);
			
			assertTrue("String - "+str+" not found", offset > 0);
			
			int length = str.length();
			Annotation annotation = createAnnotation(document, id, offset, length);
			
			Position position = new Position(offset, length);
			
			IQuickAssistAssistant assiatant = ((SourceViewer)viewer).getQuickAssistAssistant();
			TextInvocationContext ctx = new TextInvocationContext(viewer, offset, length);
			ICompletionProposal[] proposals = assiatant.getQuickAssistProcessor().computeQuickAssistProposals(ctx);
			
			for(ICompletionProposal proposal : proposals){
				if (proposal.getClass().getName().equals(proposalClassName)) {
					return;
				}
			}
			
			fail("Quick fix: "+proposalClassName+" not found");
		}finally{
			Workbench.getInstance().getActiveWorkbenchWindow().getActivePage().closeEditor(editor, false);
		}
	}

	private ISourceViewer getViewer(IEditorPart editor){
		 if(editor instanceof JSPMultiPageEditor){
			IEditorPart ed = ((JSPMultiPageEditor)editor).getSourceEditor();
			
			if(ed instanceof JSPTextEditor){
				return ((JSPTextEditor)ed).getTextViewer();
			}else {
				fail("Editor must be JSPTextEditor, but was "+ed.getClass());
			}
		}else{
			fail("editor must be instanceof EditorPartWrapper, but was "+editor.getClass());
		}
		return null;
	}

	protected Annotation createAnnotation(IDocument doc, int quickFixId, int offset, int length){
		String text = "";
		try {
			text = MessageFormat.format(HTMLCoreMessages.Undefined_attribute_name___ERROR_, doc.get(offset, length));
		} catch (BadLocationException e) {
			fail(e.getMessage());
		}
		@SuppressWarnings("restriction")
		TemporaryAnnotation annotation = new TemporaryAnnotation(
				new Position(offset, length), 
				TemporaryAnnotation.ANNOT_WARNING, 
				text, 
				null, 
				quickFixId);
		
		return annotation;
	}
}
