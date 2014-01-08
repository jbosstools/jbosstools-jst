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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
import org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttribute;
import org.jboss.tools.jst.web.kb.taglib.CustomTagLibManager;
import org.jboss.tools.jst.web.kb.taglib.ICustomTagLibrary;

/**
 * @author Alexey Kazakov
 */
public abstract class HTML5Test extends TestCase {

	protected IProject testProject;
	protected ELContext context;

	protected abstract String getTaglibUri();
	protected abstract String getFilePath();

	@Override
	protected void setUp() throws Exception {
		if(testProject==null) {
			testProject = ResourcesPlugin.getWorkspace().getRoot().getProject("TestKbModel");
			assertNotNull("Can't load TestKbModel", testProject); //$NON-NLS-1$
			IFile file = testProject.getFile(new Path(getFilePath()));
			context = PageContextFactory.createPageContext(file);
			assertNotNull(context);
		}
	}

	public void testCustomTagLibs() {
		ICustomTagLibrary[] libs = CustomTagLibManager.getInstance().getLibraries();
		boolean found = false;
		for (ICustomTagLibrary lib : libs) {
			if(getTaglibUri().equals(lib.getURI())) {
				found = true;
				break;
			}
		}
		assertTrue("Custom tag librarty " + getTaglibUri() + "is not loaded.", found);
	}

	protected void assertProposals(TextProposal[] proposals, HtmlAttribute... attributes) {
		assertProposals(true, proposals, attributes);
	}

	protected void assertProposals(boolean strict, TextProposal[] proposals, HtmlAttribute... attributes) {
		Set<String> names = new HashSet<String>();
		for (HtmlAttribute attribute : attributes) {
			names.add(attribute.getName());
		}
		assertProposals(strict, proposals, names.toArray(new String[names.size()]));
	}

	protected void assertProposals(TextProposal[] proposals, String... enums) {
		assertProposals(true, proposals, enums);
	}

	protected void assertNoProposal(boolean strict, TextProposal[] proposals, String... enums) {
		Set<String> uniqueProposals = new HashSet<String>();

		StringBuilder prp = new StringBuilder("[");
		for (TextProposal proposal : proposals) {
			if(!uniqueProposals.contains(proposal.getLabel())) {
				prp.append(proposal.getLabel()).append(",");
				uniqueProposals.add(proposal.getLabel());
			}
		}
		prp.replace(prp.length()-1, prp.length(), "]");

		StringBuilder enm = new StringBuilder("[");
		for (String e : enums) {
			enm.append(e).append(", ");
		}
		enm.replace(enm.length()-2, enm.length(), "]");

		if(strict) {
			assertEquals("The are some proposals (" + prp +") instead of an empty list. ", 0, proposals.length);
		}

		for (String enumItem : enums) {
			for (String proposal : uniqueProposals) {
				if(enumItem.equals(proposal)) {
					fail("There is a wrong proposal \"" + enumItem + "\" among the found ones: " + prp);
				}
			}
		}
	}

	protected void assertProposals(boolean strict, TextProposal[] proposals, String... enums) {
		assertTrue("No proposals found", proposals.length>0);
		Set<String> uniqueProposals = new HashSet<String>();

		StringBuilder prp = new StringBuilder("[");
		for (TextProposal proposal : proposals) {
			if(!uniqueProposals.contains(proposal.getLabel())) {
				prp.append(proposal.getLabel()).append(",");
				uniqueProposals.add(proposal.getLabel());
			}
		}
		prp.replace(prp.length()-1, prp.length(), "]");

		StringBuilder enm = new StringBuilder("[");
		for (String e : enums) {
			enm.append(e).append(", ");
		}
		enm.replace(enm.length()-2, enm.length(), "]");

		if(strict) {
			assertEquals("The number of expected proposals (" + enm + ") doesn't match the actual (" + prp +"). ", enums.length, uniqueProposals.size());
		}

		for (String enumItem : enums) {
			for (String proposal : uniqueProposals) {
				if(enumItem.equals(proposal)) {
					return;
				}
			}
			fail("There is no proposal \"" + enumItem + "\" among the found proposals: " + prp);
		}
	}

	protected void assertEqualDescription(TextProposal[] proposals, String proposalText, String descriptionText) {
		assertTrue("No proposals found", proposals.length>0);
		Map<String, TextProposal> uniqueProposals = new HashMap<String, TextProposal>();

		StringBuilder prp = new StringBuilder("[");
		for (TextProposal proposal : proposals) {
			if(!uniqueProposals.containsKey(proposal.getLabel())) {
				prp.append(proposal.getLabel()).append(",");
				uniqueProposals.put(proposal.getLabel(), proposal);
			}
		}
		prp.replace(prp.length()-1, prp.length(), "]");
		TextProposal proposal = uniqueProposals.get(proposalText);
		assertNotNull("There is no proposal \"" + proposalText + "\" among the found proposals: " + prp, proposal);
		assertEquals("Wrong description for \"" + proposalText + "\" proposal.", descriptionText, proposal.getContextInfo());
	}

	protected void assertProposal(String label, TextProposal[] proposals) {
		assertTrue("No proposals found", proposals.length>0);
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

	protected KbQuery createKbQuery(String tagName, String... attributes) {
		Map<String, String> attrs = new HashMap<String, String>();
		String value = "";
		for (int i = 0; i < attributes.length; i=i+2) {
			attrs.put(attributes[i], attributes[i+1]);
			value = attributes[i+1];
		}
		KbQuery.Tag tag = new KbQuery.Tag(tagName, attrs);
		return createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{tag}, null, value);
	}

	protected KbQuery createKbQuery(String tagName, String attributeName, String attributeValue) {
		KbQuery.Tag tag = createTag(tagName, attributeName, attributeValue);
		return createKbQuery(KbQuery.Type.ATTRIBUTE_VALUE, new KbQuery.Tag[]{tag}, null, attributeValue);
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
		kbQuery.setParent(parent);
		kbQuery.setMask(true);
		kbQuery.setType(type);
		int offset = value.length();
		for (KbQuery.Tag tag : parentTags) {
			offset+=tag.getName().length();
		}
		kbQuery.setOffset(offset);
		kbQuery.setValue(value);
		kbQuery.setStringQuery(value);
		if(attributes==null) {
			attributes = parentTags[parentTags.length-1].getAttributes();
		}
		if(parent == null) {
			if(type == KbQuery.Type.ATTRIBUTE_VALUE) {
				for (Map.Entry<String, String> attribute : attributes.entrySet()) {
					if(attribute.getValue().equals(value)) {
						parent = attribute.getKey();
						break;
					}
				}
			} else {
				parent = parentTags[parentTags.length-1].getName();
			}
		}
		kbQuery.setParent(parent);
		kbQuery.setAttributes(attributes);

		return kbQuery;
	}
}