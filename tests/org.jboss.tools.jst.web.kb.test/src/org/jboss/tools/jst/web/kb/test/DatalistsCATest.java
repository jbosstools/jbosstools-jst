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

import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.jsp.test.ca.HTML5Test;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageProcessor;

/**
 * @author Viacheslav Kabanovich
 */
public class DatalistsCATest extends HTML5Test {

	private static final String TAGLIB_URI = "htmlFile";

	@Override
	protected String getFilePath() {
		return "WebContent/pages/datalistsCA.html";
	}

	@Override
	protected String getTaglibUri() {
		return TAGLIB_URI;
	}

	public void testInput() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("input")}, "list", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, "datalist1", "datalist2", "datalist3");
	}

}