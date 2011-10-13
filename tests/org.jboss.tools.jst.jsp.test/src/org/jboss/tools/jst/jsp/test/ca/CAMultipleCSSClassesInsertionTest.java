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

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.jboss.tools.common.base.test.contentassist.CATestUtil;
import org.jboss.tools.jst.jsp.contentassist.AutoContentAssistantProposal;
import org.jboss.tools.test.util.JobUtils;

/**
 * The JUnit test case for JBIDE-9752 issue
 * 
 * @author Victor V. Rubezhny
 *
 */
public class CAMultipleCSSClassesInsertionTest extends ContentAssistantTestCase {
   private static final String PROJECT_NAME = "Jbide6061Test"; //$NON-NLS-1$
   private static final String PAGE_NAME = "/WebContent/pages/jsp_page.jsp"; //$NON-NLS-1$
   
   private final String[] CSSCLASS_PROPOSALS = new String[]{
       "errors", //$NON-NLS-1$
       "cls1", //$NON-NLS-1$
       "cls2", //$NON-NLS-1$
       "cls3", //$NON-NLS-1$
       "cls4", //$NON-NLS-1$
       "cls5" //$NON-NLS-1$
   };
   
   private static final String STRING_TO_FIND = "class=\"";
   private static final String FILTERING_INITIAL_VALUE = "errors cls1";
   private static final String[] FILTERING_CSSCLASS_PROPOSALS = new String[]{
	       "errors", //$NON-NLS-1$
	       "cls1", //$NON-NLS-1$
	   };
   private static final int FILTER_LENGTH = 3;

   public static Test suite() {
       return new TestSuite(CAMultipleCSSClassesInsertionTest.class);
   }
   
   public void setUp() throws Exception {
       project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
   }

   public void testCAMultipleCSSClassesInsertion(){
	   openEditor(PAGE_NAME);
	   try {
		   for (int i = 0; i < CSSCLASS_PROPOSALS.length; i++) {
			   doCAMultipleCSSClassesInsertionTest(CSSCLASS_PROPOSALS[i]);
		   }
	   } finally {
		   closeEditor();
	   }
   }

   public void testCAMultipleCSSClassesInsertionWithFilter(){
	   openEditor(PAGE_NAME);
	   try {
		   for (int i = 0; i < FILTERING_CSSCLASS_PROPOSALS.length; i++) {
			   doCAMultipleCSSClassesInsertionWithFilterTest(FILTERING_CSSCLASS_PROPOSALS[i]);
		   }
	   } finally {
		   closeEditor();
	   }
   }

   
   @SuppressWarnings("restriction")
   protected void doCAMultipleCSSClassesInsertionTest(String proposalToApply) {
	   String documentContent = document.get();
	   assertNotNull("Document must not be null", document);

	   IRegion reg=null;
	   try {
		   reg = new FindReplaceDocumentAdapter(this.document).find(0, STRING_TO_FIND, true, false, false, false); //$NON-NLS-1$
	   } catch (BadLocationException e) {
		   fail("Cannot find start of value text: " + e.getLocalizedMessage());
	   }
	   assertNotNull("Cannot find a text region to test", reg);
	   int start = reg.getOffset() + STRING_TO_FIND.length();
	   
	   try {
		   reg = new FindReplaceDocumentAdapter(this.document).find(start, "\"", true, false, false, false); //$NON-NLS-1$
	   } catch (BadLocationException e) {
		   fail("Cannot find end of value text: " + e.getLocalizedMessage());
	   }
	   assertNotNull("Cannot find a text region to test", reg);
	   int end = reg.getOffset();

	   // insert space character if it's not first value
	   if (documentContent.substring(start, end).trim().length() > 0) {
		   documentContent = documentContent.substring(0, end) + ' ' + 
					documentContent.substring(end);
		   document.set(documentContent);
		   end++;
		   JobUtils.waitForIdle();
	   }
		
	   String documentContentToCompare = documentContent.substring(0, end) + proposalToApply + 
				documentContent.substring(end);
			
	   List<ICompletionProposal> res = CATestUtil.collectProposals(contentAssistant, viewer, end);

	   assertTrue("Content Assistant returned no proposals", (res != null && res.size() > 0));

	   boolean bPropoosalToApplyFound = false;
	   for (ICompletionProposal p : res) {
		   if (!(p instanceof AutoContentAssistantProposal)) 
			   continue;
		   AutoContentAssistantProposal proposal = (AutoContentAssistantProposal)p;
		   String proposalString = proposal.getDisplayString();

		   if (proposalToApply.equals(proposalString)) {
			   bPropoosalToApplyFound = true;
			   proposal.apply(document);
			   JobUtils.waitForIdle();
			   break;
		   }
	   }
	   assertTrue("The proposal to apply not found.", bPropoosalToApplyFound);

	   String documentUpdatedContent = document.get();
	   assertTrue("The proposal replacement is failed.", documentContentToCompare.equals(documentUpdatedContent));
   }
   
   protected void doCAMultipleCSSClassesInsertionWithFilterTest(String proposalToApply){
	   String documentContent = document.get();
	   assertNotNull("Document must not be null", document);

	   IRegion reg=null;
	   try {
		   reg = new FindReplaceDocumentAdapter(this.document).find(0, STRING_TO_FIND, true, false, false, false); //$NON-NLS-1$
	   } catch (BadLocationException e) {
		   fail("Cannot find start of value text: " + e.getLocalizedMessage());
	   }
	   assertNotNull("Cannot find a text region to test", reg);
	   int start = reg.getOffset() + STRING_TO_FIND.length();
	   
	   try {
		   reg = new FindReplaceDocumentAdapter(this.document).find(start, "\"", true, false, false, false); //$NON-NLS-1$
	   } catch (BadLocationException e) {
		   fail("Cannot find end of value text: " + e.getLocalizedMessage());
	   }
	   assertNotNull("Cannot find a text region to test", reg);
	   int end = reg.getOffset();

	   // Make initial value of CSS Class attribute
	   documentContent = documentContent.substring(0, start) + FILTERING_INITIAL_VALUE + 
				documentContent.substring(end);
	   document.set(documentContent);
	   JobUtils.waitForIdle();
	   
	   // Find end of value again after the document modification
	   try {
		   reg = new FindReplaceDocumentAdapter(this.document).find(start, "\"", true, false, false, false); //$NON-NLS-1$
	   } catch (BadLocationException e) {
		   fail("Cannot find end of value text: " + e.getLocalizedMessage());
	   }
	   assertNotNull("Cannot find a text region to test", reg);
	   end = reg.getOffset();
	   
	   // Find start position in attribute value part which equals to the proposal
	   try {
		   reg = new FindReplaceDocumentAdapter(this.document).find(start, proposalToApply, true, false, false, false); //$NON-NLS-1$
	   } catch (BadLocationException e) {
		   fail("Cannot find end of value text: " + e.getLocalizedMessage());
	   }
	   int templateStart = reg.getOffset();
			   
	   String documentContentToCompare = documentContent.substring(0, templateStart + FILTER_LENGTH) + proposalToApply.substring(FILTER_LENGTH) + 
			   documentContent.substring(templateStart + FILTER_LENGTH);
			
	   List<ICompletionProposal> res = CATestUtil.collectProposals(contentAssistant, viewer, templateStart + FILTER_LENGTH);

	   assertTrue("Content Assistant returned no proposals", (res != null && res.size() > 0));

		boolean bPropoosalToApplyFound = false;
		for (ICompletionProposal p : res) {
			if (!(p instanceof AutoContentAssistantProposal)) 
				continue;
			AutoContentAssistantProposal proposal = (AutoContentAssistantProposal)p;
			String proposalString = proposal.getDisplayString();

			if (proposalToApply.equals(proposalString)) {
				bPropoosalToApplyFound = true;
				proposal.apply(document);
				JobUtils.waitForIdle();
				break;
			}
		}
		assertTrue("The proposal to apply not found.", bPropoosalToApplyFound);

		String documentUpdatedContent = document.get();
		assertTrue("The proposal replacement is failed.", documentContentToCompare.equals(documentUpdatedContent));
   }
}