/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.angularjs.test.ca;

import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.jsp.test.ca.HTML5Test;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageProcessor;

/**
 * @author Alexey Kazakov
 */
public class IonicTagLibTest extends HTML5Test {

	@Override
	protected String getTaglibUri() {
		return "ionic";
	}

	@Override
	protected String getFilePath() {
		return "WebContent/pages/ionic/ionic.html";
	}

	public void testTabs() {
		asertComponentProposal("ion-t", "ion-tabs");
	}

	public void testTab() {
		KbQuery query = createKbQueryForTagMask("ion-t");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("<ion-tab", proposals);

		query = createKbQuery(new KbQuery.Tag[]{createTag("ion-tab")}, "");
		proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(false, proposals, "title");
	}

	public void testSideMenus() {
		asertComponentProposal("ion-side-men", "ion-side-menus", "ion-side-menu", "ion-side-menu-content");
	}

	public void testNav() {
		asertComponentProposal("ion-", "ion-nav-view", "ion-view", "ion-nav-bar", "ion-nav-buttons", "ion-nav-back-button");
	}

	public void testHeader() {
		asertComponentProposal("ion-hea", "ion-header-bar");
	}

	public void testFooter() {
		asertComponentProposal("ion-f", "ion-footer-bar");
	}

	public void testContent() {
		asertComponentProposal("ion-c", "ion-content");
		asertComponentProposal("ion-r", "ion-refresher");
		asertComponentProposal("ion-p", "ion-pane");
	}

	public void testScroll() {
		asertComponentProposal("ion-s", "ion-scroll");
		asertComponentProposal("ion-inf", "ion-infinite-scroll");
	}

	public void testFormInputs() {
		asertComponentProposal("ion-c", "ion-checkbox");
		asertComponentProposal("ion-r", "ion-radio");
		asertComponentProposal("ion-t", "ion-toggle");
	}

	public void testIonSlideBox() {
		asertComponentProposal("ion-s", "ion-slide-box");
	}

	private void asertComponentProposal(String mask, String... proposals) {
		KbQuery query = createKbQueryForTagMask(mask);
		TextProposal[] textProposals = PageProcessor.getInstance().getProposals(query, context);
		for (String proposal : proposals) {
			assertProposal("<" + proposal, textProposals);			
		}
	}

	protected KbQuery createKbQueryForTagMask(String value) {
		KbQuery kbQuery = new KbQuery();

		kbQuery.setParentTagsWithAttributes(new KbQuery.Tag[]{});
		kbQuery.setMask(true);
		kbQuery.setType(KbQuery.Type.TAG_NAME);
		int offset = value.length();
		kbQuery.setOffset(offset);
		kbQuery.setValue(value);
		kbQuery.setStringQuery(value);

		return kbQuery;
	}
}