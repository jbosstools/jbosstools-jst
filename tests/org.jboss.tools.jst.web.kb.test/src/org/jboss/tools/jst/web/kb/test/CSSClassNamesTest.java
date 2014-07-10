/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.test;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.KbQuery.Type;
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.jst.web.kb.PageProcessor;

/**
 * The JUnit test case for issue JBIDE-17676
 * 
 * @author Victor V. Rubezhny
 *
 */
public class CSSClassNamesTest extends TestCase {
	private static final String TAG_NAME = "div";
	private static final String CSS_CLASS_NAME_TEMPLATE = "bar";
	
	private IProject testProject;

	protected void setUp() throws Exception {
		if(testProject==null) {
			testProject = ResourcesPlugin.getWorkspace().getRoot().getProject("TestKbModel");
		}
	}
	
	public void testCSSClassNames() {
		assertNotNull("Can't load TestKbModel", testProject); //$NON-NLS-1$

		IFile file = testProject.getFile("WebContent/pages/cssClassNamesTest.html");
		ELContext context = PageContextFactory.createPageContext(file);
		assertTrue("Not an instance of IPageContext!", context instanceof IPageContext);
		
		String document = ((IPageContext)context).getDocument().get();
		int startIndex = document.indexOf(TAG_NAME);
		assertTrue("Tag not found in document!", (startIndex != -1));
		startIndex += TAG_NAME.length();
		startIndex = document.indexOf(CSS_CLASS_NAME_TEMPLATE, startIndex);
		assertTrue("CSS Class Name template not found in document!", (startIndex != -1));

		startIndex += CSS_CLASS_NAME_TEMPLATE.length();
		
		KbQuery query = new KbQuery();
		query.setMask(true);
		query.setOffset(startIndex);
		query.setType(Type.ATTRIBUTE_VALUE);
		query.setValue(CSS_CLASS_NAME_TEMPLATE);
		query.setStringQuery(CSS_CLASS_NAME_TEMPLATE);
		query.setParentTags(new String[] {"html", "body", "div"});
		query.setParent("class");
		
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context, true);
		assertFalse("CSS Class Name Proposals not found!", (proposals == null || proposals.length == 0));
		
		for (TextProposal proposal : proposals) {
			String value = proposal.getReplacementString();
			assertEquals("CSS Class Name contains unexpected character: '+'", -1, value.indexOf('+'));
			assertEquals("CSS Class Name contains unexpected character: ')'", -1, value.indexOf(')'));
		}
	}
}
