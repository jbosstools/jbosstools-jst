/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.test;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.jst.web.kb.PageProcessor;

/**
 * @author Alexey Kazakov
 */
public class JQueryIDTest extends JQueryLibTest {

	@Override
	protected String getFilePath() {
		return "WebContent/pages/jquery/jQuery.html";
	}

	public void testHtml5WOLibs() {
		IFile file = testProject.getFile(new Path("WebContent/pages/jquery/jQueryMobileWOLibs.html"));
		ELContext context = PageContextFactory.createPageContext(file);
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("a", "button")}, "href", "#");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertEquals(0, proposals.length);
	}

	public void testIDs() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("a", "button")}, "href", "#");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, "#mainPage", "#confiramtionDialog", "#newUserDialog", "#nextPage", "#popup-1", "#panel-1");
	}

	public void testIDWithMask() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("a", "button")}, "href", "#n");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, "#newUserDialog", "#nextPage");
	}

	public void testIDWithEmptyHref() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("a", "button")}, "href", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(false, proposals, "#mainPage", "#confiramtionDialog", "#newUserDialog", "#nextPage", "#popup-1", "#panel-1");
	}

	public void testEmptyResult() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("a", "button")}, "href", "n");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertEquals(0, proposals.length);
	}
}