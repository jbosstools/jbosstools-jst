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

import static org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileAttrConstants13.*;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.jsp.test.ca.JQueryLibTest;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.jst.web.kb.PageProcessor;

/**
 * @author Alexey Kazakov
 */
public class JQueryDataTest extends JQueryLibTest {

	@Override
	protected String getFilePath() {
		return "WebContent/pages/jquery/jQueryMobile.html";
	}

	public void testFileWithNoDoctype() {
		IFile file = testProject.getFile(new Path("WebContent/pages/jquery/jQueryMobileHTML4.html"));
		ELContext context = PageContextFactory.createPageContext(file);
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("a", BUTTON)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertTrue(proposals.length > 0);
	}

	public void testFileWithWrongDoctype() {
		IFile file = testProject.getFile(new Path("WebContent/pages/jquery/jQueryMobileWithWrongDoctype.html"));
		ELContext context = PageContextFactory.createPageContext(file);
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("a", BUTTON)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertEquals(0, proposals.length);
	}

	public void testHtml5WithoutLibs() {
		IFile file = testProject.getFile(new Path("WebContent/pages/jquery/jQueryMobileWOLibs.html"));
		ELContext context = PageContextFactory.createPageContext(file);
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("a", BUTTON)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertEquals(0, proposals.length);
	}

	public void testJQMLoadedViaJs() {
		IFile file = testProject.getFile(new Path("WebContent/pages/jquery/jQueryMobileLoadedViaJS.html"));
		ELContext context = PageContextFactory.createPageContext(file);
		
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("article")}, "data-role", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(false, proposals, PAGE);

		query = createKbQuery(new KbQuery.Tag[]{createTag("aside")}, "data-");
		proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-role", proposals);
	}

	public void testIcons() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("a", "button")}, "data-icon", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_ICON_VALUES);
	}

	public void testAButton() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("a", "button")}, "data-ico");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-icon", proposals);

		query = createKbQuery(new KbQuery.Tag[]{createTag("a")}, "data-ico");
		proposals = PageProcessor.getInstance().getProposals(query, context);
		assertTrue(proposals.length==0);
	}

	public void testButton() {
		assertDataRole("a", BUTTON);
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", "header"), createTag("a")}, "data-ico");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-icon", proposals);

		query = createKbQuery(new KbQuery.Tag[]{createTag("div"), createTag("a")}, "data-ico");
		proposals = PageProcessor.getInstance().getProposals(query, context);
		assertTrue(proposals.length==0);
	}

	public void testCheckBox() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("input", "type", "checkbox")}, "data-min");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-mini", proposals);
	}

	public void testCollapsibleAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", COLLAPSIBLE)}, "data-collapsed-ico");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-collapsed-icon", proposals);
	}

	public void testCollapsibleSetAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", COLLAPSIBLE_SET)}, "data-expanded-ico");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-expanded-icon", proposals);
	}

	public void testCollapsibleAttributeProviderIcons() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("div", COLLAPSIBLE)}, "data-expanded-icon", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_ICON_VALUES);

		query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("div", COLLAPSIBLE)}, "data-collapsed-icon", "");
		proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_ICON_VALUES);
	}

	public void testCollapsibleSetAttributeProviderIcons() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("div", COLLAPSIBLE_SET)}, "data-expanded-icon", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_ICON_VALUES);

		query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("div", COLLAPSIBLE_SET)}, "data-collapsed-icon", "");
		proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_ICON_VALUES);
	}

	public void testContentAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div")}, "data-rol");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-role", proposals);

		query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("div")}, "data-role", "");
		proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(false, proposals, "content");
	}

	public void testControlgroupAttributeProvider() {
		assertDataRole(false, CONTROLGROUP);

		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", CONTROLGROUP)}, "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-mini", proposals);
		assertProposal("data-type", proposals);

		assertNoProposal(false, proposals, "data-theme", "data-exclude-invisible");
	}

	public void testDialogAttributeProvider() {
		assertDataRole(false, DIALOG);

		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", DIALOG)}, "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-close-btn-text", proposals);
		assertProposal("data-dom-cache", proposals);
	}

	public void testEnhancementAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div")}, "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-enhance", proposals);

		query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("div")}, "data-enhance", "");
		proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_TRUE_FALSE);
	}

	public void testAjaxAttribute() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div")}, "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-ajax", proposals);

		query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("div")}, "data-ajax", "");
		proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_TRUE_FALSE);
	}

	public void testFieldcontainAttributeProvider() {
		assertDataRole(false, FIELDCONTENT);
	}

	public void testFixedToolbarAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", HEADER)}, "data-");
		query.getAttributes().put("data-position", "fixed");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-disable-page-zoom", proposals);
	}

	public void testFlipToggleSwitchAttributeProvider() {
		assertDataRole("select", SLIDER);
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("select", SLIDER)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-mini", proposals);
		assertProposal("data-theme", proposals);
		assertProposal("data-track-theme", proposals);
	}

	public void testFooterHeaderAttributeProvider() {
		assertDataRole(false, HEADER);
		assertDataRole(false, FOOTER);

		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", HEADER)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-id", proposals);
	}

	public void testLinkAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("a", BUTTON)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-rel", proposals);
	}

	public void testListViewAttributeProvider() {
		assertDataRole("ol", LISTVIEW);
		assertDataRole("ul", LISTVIEW);

		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("ol", LISTVIEW)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-autodividers", proposals);
	}

	public void testListViewAttributeProviderIcons() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("ol", LISTVIEW)}, "data-icon", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_ICON_VALUES);

		query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("ol", LISTVIEW)}, "data-split-icon", "");
		proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_ICON_VALUES);
	}

	public void testListviewItemAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("ol", LISTVIEW), createTag("li")}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-icon", proposals);
	}

	public void testListviewItemAttributeProviderIcons() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("ol", LISTVIEW), createTag("li")}, "data-icon", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_ICON_VALUES);
	}

	public void testNavbarAttributeProvider() {
		assertDataRole(false, "div", NAVBAR);

		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", NAVBAR)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-iconpos", proposals);
	}

	public void testPageAttributeProvider() {
		assertDataRole(false, PAGE);

		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", PAGE)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(false, proposals, "data-add-back-btn", "data-back-btn-text", "data-back-btn-theme", "data-close-btn-text", "data-dom-cache", "data-overlay-theme", "data-theme", "data-title", "data-url");
	}

	public void testPopupAnchorAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("a", "data-rel", "popup")}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-position-to", proposals);
	}

	public void testDataRel() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("a")}, "data-rel", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, DATA_REL_ENUM);
	}

	public void testPopupAttributeProvider() {
		assertDataRole(false, POPUP);

		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", POPUP)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-corners", proposals);
	}

	public void testRadioButtonAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag(INPUT, TYPE, RADIO)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-mini", proposals);
	}

	public void testSelectAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("select")}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-placeholder", proposals);
	}

	public void testSelectAttributeProviderIcons() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("select")}, "data-icon", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_ICON_VALUES);
	}

	public void testSliderAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag(INPUT, TYPE, RANGE)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-track-theme", proposals);
		assertProposal("data-highlight", proposals);
	}

	public void testTextInputAndTextareaAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag(TEXTAREA)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-clear-btn-text", proposals);
	}

	public void testTableAttributeProvider() {
		assertDataRole(TABLE, TABLE);

		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag(TABLE, TABLE)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, "data-role", "data-mode", "data-column-btn-text", "data-column-btn-theme", "data-column-popup-theme");
	}

	public void testDataMode() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag(TABLE, TABLE)}, "data-mode", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, DATA_MODE_ENUM);
	}

	public void testDataColumnBtnTheme() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag(TABLE, TABLE)}, "data-column-btn-theme", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_THEME);
	}

	public void testDataColumnPopupTheme() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag(TABLE, TABLE)}, "data-column-popup-theme", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_THEME);
	}

	public void testThAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag(TABLE, TABLE), createTag("th")}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-priority", proposals);

		query = createKbQuery(new KbQuery.Tag[]{createTag(TABLE), createTag("th")}, "data-");
		proposals = PageProcessor.getInstance().getProposals(query, context);
		assertTrue(proposals.length==0);
	}

	public void testDataPriotity() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag(TABLE, TABLE), createTag("th")}, "data-priority", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, DATA_PRIORITY_ENUM);
	}

	public void testPanelAttributeProvider() {
		assertDataRole(false, PANEL);

		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", PANEL)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(false, proposals, "data-position", "data-display", "data-dismissible", "data-position-fixed", "data-swipe-close", "data-theme");
	}

	public void testPanelDataPosition() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("div", PANEL)}, "data-position", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, DATA_POSITION_ENUM);
	}

	public void testPanelDataDisplay() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("div", PANEL)}, "data-display", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, DATA_DISPLAY_ENUM);
	}

	public void testPanelDataDismissible() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("div", PANEL)}, "data-dismissible", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_TRUE_FALSE);
	}

	public void testPanelDataPositionFixed() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("div", PANEL)}, "data-position-fixed", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_TRUE_FALSE);
	}

	public void testPanelDataSwipeClose() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("div", PANEL)}, "data-swipe-close", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_TRUE_FALSE);
	}

	public void testPanelDataTheme() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("div", PANEL)}, "data-theme", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, ENUM_THEME);
	}
}