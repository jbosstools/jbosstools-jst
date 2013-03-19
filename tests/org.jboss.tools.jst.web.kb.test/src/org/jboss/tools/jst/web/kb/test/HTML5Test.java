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

import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.KbQuery.Type;
import org.jboss.tools.jst.web.kb.PageProcessor;

/**
 * @author Alexey Kazakov
 */
public class HTML5Test extends TestCase {

	protected ELContext context;

	protected void assertDataRole(String dateRoleName) {
		assertDataRole(null, dateRoleName);
	}

	protected void assertDataRole(String tagName, String dateRoleName) {
		if(tagName==null) {
			tagName = "div";
		}
		KbQuery query = createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{createTag(tagName)}, "data-role", "");
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		assertProposals(proposals, dateRoleName);
	}

	protected void assertProposals(TextProposal[] proposals, String... enums) {
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

	protected void assertProposal(String label, TextProposal[] proposals) {
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

	protected KbQuery.Tag createTag(String tagName) {
		return createTag(tagName, null);
	}

	protected KbQuery.Tag createTag(String tagName, String roleAttributeValue) {
		return createTag(tagName, roleAttributeValue==null?null:"data-role", roleAttributeValue);
	}

	protected KbQuery.Tag createTag(String tagName, String attributeName, String attributeValue) {
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