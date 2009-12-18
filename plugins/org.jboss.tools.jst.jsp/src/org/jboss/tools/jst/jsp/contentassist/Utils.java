/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.jsp.contentassist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.KbQuery.Type;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * 
 * @author Victor Rubezhny
 *
 */
@SuppressWarnings("restriction")
public class Utils {
	private static final String JSFC_ATTRIBUTE_NAME = "jsfc"; //$NON-NLS-1$

	/**
	 * Creates and fills the KbQuery object
	 * 
	 * @param type
	 * @param offset
	 * @param query
	 * @param stringQuery
	 * @param prefix
	 * @param uri
	 * @param parentTags
	 * @param parent
	 * @param mask
	 * @return
	 */
	public static KbQuery createKbQuery(Type type, int offset, String query, 
			String stringQuery, String prefix, String uri, String[] parentTags, String parent, boolean mask) {
		KbQuery kbQuery = new KbQuery();

		kbQuery.setPrefix(prefix);
		kbQuery.setUri(uri);
		kbQuery.setParentTags(parentTags);
		kbQuery.setParent(parent); 
		kbQuery.setMask(mask); 
		kbQuery.setType(type);
		kbQuery.setOffset(offset);
		kbQuery.setValue(query); 
		kbQuery.setStringQuery(stringQuery);
		
		return kbQuery;
	}
	
	/**
	 * Returns the name for the tag
	 * 
	 * @param tag
	 * @param useJsfcTags
	 * @return
	 */
	public static String getTagName(Node tag, boolean useJsfcTags) {
		String tagName = tag.getNodeName();
		if(useJsfcTags) {
			// Only HTML tags
			if(tagName.indexOf(':')>0) {
				return tagName;
			}
			if (!(tag instanceof Element))
				return tagName;
			
			Element element = (Element)tag;

			NamedNodeMap attributes = element.getAttributes();
			Node jsfC = attributes.getNamedItem(JSFC_ATTRIBUTE_NAME);
			if(jsfC==null || (!(jsfC instanceof Attr))) {
				return tagName;
			}
			Attr jsfCAttribute = (Attr)jsfC;
			String jsfTagName = jsfCAttribute.getValue();
			if(jsfTagName==null || jsfTagName.indexOf(':')<1) {
				return tagName;
			}
			tagName = jsfTagName;
		}
		return tagName;
	}
	
	/**
	 * Returns name of the parent attribute/tag name
	 * 
	 * @return
	 */
	public static String getParent(IDOMNode xmlnode, boolean returnAttributeName, boolean returnThisElement, 
			boolean useJsfcAttribute) {

		Node node = xmlnode;
		while ((node != null) && (node.getNodeType() == Node.TEXT_NODE) && (node.getParentNode() != null)) {
			node = node.getParentNode();
		}

		if (!returnAttributeName && (node instanceof IDOMAttr)) {
			node = ((IDOMAttr)node).getOwnerElement();
		}
		
		if (node == null)
			return null;

		// Find the first parent tag 
		if (!(node instanceof IDOMElement)) {
			if (node instanceof IDOMAttr) {
				if (returnAttributeName) {
					String parentAttrName = node.getNodeName();
					return parentAttrName;
				}
				node = ((IDOMAttr) node).getOwnerElement();
			} else {
				node = node.getParentNode();
			}
		} else {
			if (!returnThisElement)
				node = node.getParentNode();
		}
		if (node == null)
			return null;

		return getTagName(node, useJsfcAttribute);
	}
	
	private static final String[] EMPTY_TAGS = new String[0];
	/**
	 * Returns array of the parent tags 
	 * 
	 * @return
	 */
	public static String[] getParentTags(IDOMNode xmlnode, boolean includeThisTag, boolean useJsfcAttribute) {
		List<String> parentTags = new ArrayList<String>();

		// Find the first parent tag 
		Node node = xmlnode;
		while ((node != null) && (node.getNodeType() != Node.ELEMENT_NODE) && (node.getParentNode() != null)) {
			node = node.getNodeType() == Node.ATTRIBUTE_NODE ? ((Attr)node).getOwnerElement() : node.getParentNode();
		}

		if (!includeThisTag) {
			node = node.getParentNode();
		}

		if (node == null)
			return EMPTY_TAGS;

		// Store all the parents
		while (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
			String tagName = getTagName(node, useJsfcAttribute);
			parentTags.add(0, tagName);
			node = node.getParentNode();
		}	

		return (String[])parentTags.toArray(new String[parentTags.size()]);
	}

}
