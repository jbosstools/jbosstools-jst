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
package org.jboss.tools.jst.jsp.test.ca;

import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageProcessor;

/**
 * @author Alexey Kazakov
 */
public abstract class JQueryLibTest extends HTML5Test {

	private static final String TAGLIB_URI = "jQueryMobile";

	@Override
	protected String getTaglibUri() {
		return TAGLIB_URI;
	}

	protected void assertDataRole(String dateRoleName) {
		assertDataRole(true, dateRoleName);
	}

	protected void assertDataRole(boolean strict, String dateRoleName) {
		assertDataRole(strict, null, dateRoleName);
	}

	protected void assertDataRole(String tagName, String dateRoleName) {
		assertDataRole(true, tagName, dateRoleName);
	}

	protected void assertNoDataRole(String dateRoleName) {
		String tagName = "div";
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag(tagName)}, "data-role", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertNoProposal(false, proposals, dateRoleName);
	}

	protected void assertDataRole(boolean strict, String tagName, String dateRoleName) {
		if(tagName==null) {
			tagName = "div";
		}
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag(tagName)}, "data-role", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(strict, proposals, dateRoleName);
	}
}