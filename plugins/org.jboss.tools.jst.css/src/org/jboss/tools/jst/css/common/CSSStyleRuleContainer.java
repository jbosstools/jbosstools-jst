/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.css.common;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.wst.css.core.internal.provisional.document.ICSSNode;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSNodeList;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSStyleDeclaration;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;

/**
 * @author Sergey Dzmitrovich
 * 
 */
@SuppressWarnings("restriction")
public class CSSStyleRuleContainer extends StyleContainer {

	private CSSStyleRule styleRule;

	public CSSStyleRuleContainer(CSSStyleRule styleRule) {
		this.styleRule = styleRule;
	}

	public void applyStyleAttributes(Map<String, String> attributes) {

		final CSSStyleDeclaration declaration = styleRule.getStyle();

		// set properties

		if (attributes != null) {

			if ((attributes.size() == 0) && (declaration.getLength() > 0)) {
				declaration.setCssText(Constants.EMPTY);
			} else {
				for (final Map.Entry<String, String> me : attributes.entrySet()) {
					if ((me.getValue() == null)
							|| (me.getValue().length() == 0)) {
						declaration.removeProperty(me.getKey());
					} else if (!me.getValue().equals(
							declaration.getPropertyValue(me.getKey()))) {
						declaration.setProperty(me.getKey(), me.getValue(),
								Constants.EMPTY);
					}
				}

			}
		}

	}

	public Map<String, String> getStyleAttributes() {
		CSSStyleDeclaration declaration = styleRule.getStyle();
		Map<String, String> styleMap = new HashMap<String, String>();
		for (int i = 0; i < declaration.getLength(); i++) {
			String propperty = declaration.item(i);
			String value = declaration.getPropertyValue(propperty);
			styleMap.put(propperty, value);
		}

		return styleMap;
	}

	public Object getStyleObject() {
		return styleRule;
	}

	@Override
	public void addNodeListener(INodeAdapter adapter) {
		ICSSStyleDeclaration declaration = (ICSSStyleDeclaration) styleRule
				.getStyle();
		addNodeAdapter(declaration, adapter);
		addNodeListenersForChildren(declaration, adapter);
	}
	
	/*
	 * Fixed by yzhishko. See https://jira.jboss.org/jira/browse/JBIDE-5954.
	 */
	private void addNodeListenersForChildren(Object node, INodeAdapter adapter){
		if (!(node instanceof ICSSNode)) {
			return;
		}
		ICSSNode cssNode = (ICSSNode) node;
		ICSSNodeList nodeList = cssNode.getChildNodes();
		if (nodeList == null) {
			return;
		}
		for (int i = 0; i < nodeList.getLength(); i++) {
			ICSSNode cssChildNode = nodeList.item(i);
			addNodeAdapter(cssChildNode, adapter);
			addNodeListenersForChildren(cssChildNode, adapter);
		}
	}
	
	/*
	 * Fixed by yzhishko. See https://jira.jboss.org/jira/browse/JBIDE-5954.
	 */
	private void removeNodeListenersForChildren(Object node, INodeAdapter adapter){
		if (!(node instanceof ICSSNode)) {
			return;
		}
		ICSSNode cssNode = (ICSSNode) node;
		ICSSNodeList nodeList = cssNode.getChildNodes();
		if (nodeList == null) {
			return;
		}
		for (int i = 0; i < nodeList.getLength(); i++) {
			ICSSNode cssChildNode = nodeList.item(i);
			removeNodeAdapter(cssChildNode, adapter);
			removeNodeListenersForChildren(cssChildNode, adapter);
		}
	}

	@Override
	public void removeNodelListener(INodeAdapter adapter) {

		ICSSStyleDeclaration declaration = (ICSSStyleDeclaration) styleRule
				.getStyle();
		removeNodeAdapter(declaration, adapter);
		removeNodeListenersForChildren(declaration, adapter);
	}

	@Override
	public boolean isValid() {
		final String text = styleRule.getCssText();
		if ((text != null) && (!text.endsWith(Constants.RIGHT_BRACE)))
			return false;
		return true;
	}
}
