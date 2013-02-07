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

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.KbQuery.Type;
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.jst.web.kb.PageProcessor;
import org.jboss.tools.jst.web.kb.internal.taglib.jq.JQueryMobileAttrProvider;
import org.jboss.tools.jst.web.kb.taglib.CustomTagLibManager;
import org.jboss.tools.jst.web.kb.taglib.ICustomTagLibrary;

/**
 * @author Alexey Kazakov
 */
public class JQueryDataTest extends TestCase {

	private static final String JQUERY_MOBILE_URI = "jQueryMobile";

	private IProject testProject;
	private ELContext context;

	@Override
	protected void setUp() throws Exception {
		if(testProject==null) {
			testProject = ResourcesPlugin.getWorkspace().getRoot().getProject("TestKbModel");
			assertNotNull("Can't load TestKbModel", testProject); //$NON-NLS-1$
			IFile file = testProject.getFile(new Path("WebContent/pages/params/jQueryMobile.html"));
			context = PageContextFactory.createPageContext(file);
			assertNotNull(context);
		}
	}

	public void testCustomTagLibs() {
		ICustomTagLibrary[] libs = CustomTagLibManager.getInstance().getLibraries();
		boolean found = false;
		for (ICustomTagLibrary lib : libs) {
			if(JQUERY_MOBILE_URI.equals(lib.getURI())) {
				found = true;
				break;
			}
		}
		assertTrue("Custom tag jQueryMobile is not loaded.", found);
	}

	public void testHtml4() {
		IFile file = testProject.getFile(new Path("WebContent/pages/params/jQueryMobileHTML4.html"));
		ELContext context = PageContextFactory.createPageContext(file);
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("a", JQueryMobileAttrProvider.BUTTON)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertEquals(0, proposals.length);
	}

	public void testHtml5WOLibs() {
		IFile file = testProject.getFile(new Path("WebContent/pages/params/jQueryMobileWOLibs.html"));
		ELContext context = PageContextFactory.createPageContext(file);
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("a", JQueryMobileAttrProvider.BUTTON)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertEquals(0, proposals.length);
	}

	public void testIcons() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("a", "button")}, "data-icon", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(JQueryMobileAttrProvider.ENUM_ICON_VALUES, proposals);
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
		assertDataRole("a", JQueryMobileAttrProvider.BUTTON);
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
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", JQueryMobileAttrProvider.COLLAPSIBLE)}, "data-collapsed-ico");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-collapsed-icon", proposals);
	}

	public void testCollapsibleSetAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", JQueryMobileAttrProvider.COLLAPSIBLE_SET)}, "data-expanded-ico");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-expanded-icon", proposals);
	}

	public void testContentAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div")}, "data-rol");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-role", proposals);

		query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("div")}, "data-role", "");
		proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(new String[]{"content"}, proposals);
	}

	public void testControlgroupAttributeProvider() {
		assertDataRole(JQueryMobileAttrProvider.CONTROLGROUP);

		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", JQueryMobileAttrProvider.CONTROLGROUP)}, "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-mini", proposals);
		assertProposal("data-type", proposals);
	}

	public void testDialogAttributeProvider() {
		assertDataRole(JQueryMobileAttrProvider.DIALOG);

		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", JQueryMobileAttrProvider.DIALOG)}, "");
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
		assertProposals(JQueryMobileAttrProvider.ENUM_TRUE_FALSE, proposals);
	}

	public void testAjaxAttribute() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div")}, "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-ajax", proposals);

		query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("div")}, "data-ajax", "");
		proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(JQueryMobileAttrProvider.ENUM_TRUE_FALSE, proposals);
	}

	public void testFieldcontainAttributeProvider() {
		assertDataRole(JQueryMobileAttrProvider.FIELDCONTENT);
	}

	public void testFixedToolbarAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", JQueryMobileAttrProvider.HEADER)}, "data-");
		query.getAttributes().put("data-position", "fixed");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-disable-page-zoom", proposals);
	}

	public void testFlipToggleSwitchAttributeProvider() {
		assertDataRole("select", JQueryMobileAttrProvider.SLIDER);
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("select", JQueryMobileAttrProvider.SLIDER)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-mini", proposals);
	}

	public void testFooterHeaderAttributeProvider() {
		assertDataRole(JQueryMobileAttrProvider.HEADER);
		assertDataRole(JQueryMobileAttrProvider.FOOTER);

		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", JQueryMobileAttrProvider.HEADER)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-id", proposals);
	}

	public void testLinkAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("a", JQueryMobileAttrProvider.BUTTON)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-rel", proposals);
	}

	public void testListViewAttributeProvider() {
		assertDataRole("ol", JQueryMobileAttrProvider.LISTVIEW);
		assertDataRole("ul", JQueryMobileAttrProvider.LISTVIEW);

		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("ol", JQueryMobileAttrProvider.LISTVIEW)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-autodividers", proposals);
	}

	public void testListviewItemAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("ol", JQueryMobileAttrProvider.LISTVIEW), createTag("li")}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-icon", proposals);
	}

	public void testNavbarAttributeProvider() {
		assertDataRole("div", JQueryMobileAttrProvider.NAVBAR);

		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", JQueryMobileAttrProvider.NAVBAR)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-iconpos", proposals);
	}

	public void testPageAttributeProvider() {
		assertDataRole(JQueryMobileAttrProvider.PAGE);

		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", JQueryMobileAttrProvider.PAGE)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-add-back-btn", proposals);
	}

	public void testPopupAnchorAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("a", "data-rel", "popup")}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-position-to", proposals);
	}

	public void testDataRel() {
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag("a")}, "data-rel", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(JQueryMobileAttrProvider.DATA_REL_ENUM, proposals);
	}

	public void testPopupAttributeProvider() {
		assertDataRole(JQueryMobileAttrProvider.POPUP);

		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("div", JQueryMobileAttrProvider.POPUP)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-corners", proposals);
	}

	public void testRadioButtonAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag(JQueryMobileAttrProvider.INPUT, JQueryMobileAttrProvider.TYPE, JQueryMobileAttrProvider.RADIO)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-mini", proposals);
	}

	public void testSelectAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag("select")}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-placeholder", proposals);
	}

	public void testSliderAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag(JQueryMobileAttrProvider.INPUT, JQueryMobileAttrProvider.TYPE, JQueryMobileAttrProvider.RANGE)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-track-theme", proposals);
		assertProposal("data-highlight", proposals);
	}

	public void testTextInputAndTextareaAttributeProvider() {
		KbQuery query = createKbQuery(new KbQuery.Tag[]{createTag(JQueryMobileAttrProvider.TEXTAREA)}, "data-");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposal("data-clear-btn-text", proposals);
	}

	private void assertDataRole(String dateRoleName) {
		assertDataRole(null, dateRoleName);
	}

	private void assertDataRole(String tagName, String dateRoleName) {
		if(tagName==null) {
			tagName = "div";
		}
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag(tagName)}, "data-role", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(new String[]{dateRoleName}, proposals);
	}

	private void assertProposals(String[] enums, TextProposal[] proposals) {
		assertTrue(proposals.length>0);
		for (String enumItem : enums) {
			StringBuffer sb = new StringBuffer("There is no proposal \"" + enumItem + "\" among found proposals: [");
			for (TextProposal proposal : proposals) {
				sb.append(proposal.getLabel()).append(",");
				if(enumItem.equals(proposal.getLabel())) {
					return;
				}
			}
			sb.replace(sb.length()-1, sb.length(), "]");
			fail(sb.toString());
		}
	}

	private void assertProposal(String label, TextProposal[] proposals) {
		assertTrue(proposals.length>0);
		StringBuffer sb = new StringBuffer("There is no proposal \"" + label + "\" among found proposals: [");
		for (TextProposal proposal : proposals) {
			sb.append(proposal.getLabel()).append(",");
			if(proposal.getLabel().equals(label)) {
				return;
			}
		}
		sb.replace(sb.length()-1, sb.length(), "]");
		fail(sb.toString());
	}

	private KbQuery.Tag createTag(String tagName) {
		return createTag(tagName, null);
	}

	private KbQuery.Tag createTag(String tagName, String roleAttributeValue) {
		return createTag(tagName, roleAttributeValue==null?null:"data-role", roleAttributeValue);
	}

	private KbQuery.Tag createTag(String tagName, String attributeName, String attributeValue) {
		Map<String, String> attributes = new HashMap<String, String>();
		if(attributeName!=null) {
			attributes.put(attributeName, attributeValue);
		}
		KbQuery.Tag tag = new KbQuery.Tag(tagName, attributes);
		return tag;
	}

	protected KbQuery createKbQuery(KbQuery.Tag[] parentTags, String value) {
		return createKbQuery(null, parentTags, null, value, null);
	}

	protected KbQuery createKbQuery(Type type, KbQuery.Tag[] parentTags, String value) {
		return createKbQuery(type, parentTags, null, value, null);
	}

	protected KbQuery createKbQuery(Type type, KbQuery.Tag[] parentTags, String parent, String value) {
		return createKbQuery(type, parentTags, parent, value, null);
	}

	protected KbQuery createKbQuery(Type type, KbQuery.Tag[] parentTags, String parent, String value, Map<String, String> attributes) {
		if(type==null) {
			type = KbQuery.Type.ATTRIBUTE_NAME;
		}
		KbQuery kbQuery = new KbQuery();

		kbQuery.setParentTagsWithAttributes(parentTags);
		if(parent==null) {
			parentTags[parentTags.length-1].getName();
		}
		kbQuery.setParent(parent);
		kbQuery.setMask(true);
		kbQuery.setType(type);
		int offset = value.length();
		for (KbQuery.Tag tag : parentTags) {
			offset+=tag.getName().length();
		}
		kbQuery.setOffset(offset);
		kbQuery.setValue(value);
		if(attributes==null) {
			attributes = parentTags[parentTags.length-1].getAttributes();
		}
		kbQuery.setAttributes(attributes);

		return kbQuery;
	}
}