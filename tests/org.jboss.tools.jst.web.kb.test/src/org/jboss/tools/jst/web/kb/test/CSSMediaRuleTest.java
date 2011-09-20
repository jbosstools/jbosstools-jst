/******************************************************************************* 
 * Copyright (c) 2009-2011 Red Hat, Inc. 
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
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.KbQuery.Type;
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.jst.web.kb.PageProcessor;

/**
 * The JUnit test case for JBIDE-9740 issue
 * 
 * @author Victor V. Rubezhny
 *
 */
public class CSSMediaRuleTest extends TestCase {

	private IProject testProject;

	protected void setUp() throws Exception {
		if(testProject==null) {
			testProject = ResourcesPlugin.getWorkspace().getRoot().getProject("TestKbModel");
		}
	}
	
	public void testCSSMediaRule() {
		assertNotNull("Can't load TestKbModel", testProject); //$NON-NLS-1$

		IFile file = testProject.getFile("WebContent/pages/cssMediaRuleTest.html");
		ELContext context = PageContextFactory.createPageContext(file);
		KbQuery query = new KbQuery();
		query.setMask(true);
		query.setOffset(266);
		query.setType(Type.ATTRIBUTE_VALUE);
		query.setValue("");
		query.setStringQuery("\"");
		query.setParentTags(new String[] {"html", "body", "p"});
		query.setParent("class");
		
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context, true);
		boolean ok = false;
		for (TextProposal proposal : proposals) {
			if("test".equals(proposal.getReplacementString())) {
				ok = true;
			}
		}
		assertTrue("Can't find 'test' Media CSS Rule proposal.", ok);
	}
}
