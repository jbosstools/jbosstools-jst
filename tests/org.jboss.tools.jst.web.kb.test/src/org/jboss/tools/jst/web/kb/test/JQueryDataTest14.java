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

import static org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttributeConstants.COLLAPSIBLE;
import static org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttributeConstants.COLLAPSIBLE_SET;
import static org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttributeConstants.CONTROLGROUP;
import static org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttributeConstants.DIALOG;
import static org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttributeConstants.ENUM_TRUE_FALSE;
import static org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttributeConstants.FIELDCONTENT;
import static org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttributeConstants.FOOTER;
import static org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttributeConstants.HEADER;
import static org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttributeConstants.LISTVIEW;
import static org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttributeConstants.PAGE;
import static org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileAttrConstants14.ENUM_ICON_VALUES;

import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageProcessor;

/**
 * @author Alexey Kazakov
 */
public class JQueryDataTest14 extends JQueryDataTest {

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.test.HTML5Test#getFilePath()
	 */
	@Override
	protected String getFilePath() {
		return "WebContent/pages/jquery/jQueryMobile14.html";
	}

	@Override
	public void testIcons() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("a", "button")}, "data-icon", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_ICON_VALUES);
	}

	public void testButtonClass() {
		assertIconClassProposal("ui-btn");
		assertIconClassProposal("ui-btn ui-corner-all");
		assertIconClassProposal("ui-btn ui-corner-all ui-btn");
		assertIconClassProposal("ui-corner-all ui-btn");
		assertIconClassProposal("ui-btndd ui-btn ui-btndd");
		assertIconClassProposal("ui-sss ui-btn");
		assertIconClassProposal(" ui-btn");
		assertIconClassProposal("ui-btn ");

		assertNoIconClassProposal("ui-btnn ui-corner-all ui-btnn");
		assertNoIconClassProposal("ui-btndd ui-btn-nnn ui-btndd");
	}

	protected void assertIconClassProposal(String classAttributeValue) {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_NAME, new KbQuery.Tag[]{createTag("a", "class", classAttributeValue)}, "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-icon", proposals);
	}

	protected void assertNoIconClassProposal(String classAttributeValue) {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_NAME, new KbQuery.Tag[]{createTag("a", "class", classAttributeValue)}, "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertNoProposal(false, proposals, "data-icon");
	}

	@Override
	public void testCollapsibleAttributeProviderIcons() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("div", COLLAPSIBLE)}, "data-expanded-icon", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_ICON_VALUES);

		query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("div", COLLAPSIBLE)}, "data-collapsed-icon", "");
		proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_ICON_VALUES);
	}

	@Override
	public void testCollapsibleSetAttributeProviderIcons() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("div", COLLAPSIBLE_SET)}, "data-expanded-icon", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_ICON_VALUES);

		query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("div", COLLAPSIBLE_SET)}, "data-collapsed-icon", "");
		proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_ICON_VALUES);
	}

	@Override
	public void testContentAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div")}, "data-rol");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-role", proposals);

		query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("div")}, "data-role", "conten");
		proposals = PageProcessor.getInstance().getProposals(query, context);
		assertEquals(0, proposals.length);
	}

	@Override
	public void testControlgroupAttributeProvider() {
		assertDataRole(false, CONTROLGROUP);

		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", CONTROLGROUP)}, "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-mini", proposals);
		assertProposal("data-type", proposals);
		assertProposal("data-theme", proposals);
		assertProposal("data-exclude-invisible", proposals);
	}

	public void testExcludeInvisible() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("div", CONTROLGROUP)}, "data-exclude-invisible", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_TRUE_FALSE);
	}

	@Override
	public void testFieldcontainAttributeProvider() {
		assertNoDataRole(FIELDCONTENT);
	}

	public void testFooterAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", FOOTER)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(false, proposals, "data-id", "data-position", "data-theme");
		assertNoProposal(false, proposals, "data-add-back-btn", "data-back-btn-text", "data-back-btn-theme");
	}

	public void testHeaderAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", HEADER)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(false, proposals, "data-id", "data-position", "data-theme", "data-add-back-btn", "data-back-btn-text", "data-back-btn-theme");
	}

	@Override
	public void testListViewAttributeProviderIcons() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("ol", LISTVIEW)}, "data-icon", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_ICON_VALUES);

		query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("ol", LISTVIEW)}, "data-split-icon", "");
		proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_ICON_VALUES);
	}

	@Override
	public void testListviewItemAttributeProviderIcons() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("ol", LISTVIEW), createTag("li")}, "data-icon", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_ICON_VALUES);
	}

	@Override
	public void testPageAttributeProvider() {
		assertDataRole(false, PAGE);

		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", PAGE)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(false, proposals, "data-dom-cache", "data-overlay-theme", "data-theme", "data-title", "data-url");
		assertNoProposal(false, proposals, "data-add-back-btn", "data-back-btn-text", "data-back-btn-theme", "data-close-btn-text");
	}

	@Override
	public void testSelectAttributeProviderIcons() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("select")}, "data-icon", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_ICON_VALUES);
	}

	@Override
	public void testDialogAttributeProvider() {
		assertNoDataRole(DIALOG);
		assertNoAttributeProposal("data-dialog");

		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("div", "page")}, "data-dialog", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("true", proposals);
	}

	public void testTabsAttributeProvider() {
		assertDataRole(false, "tabs");
	}
}