/*******************************************************************************
 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.model;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSStyleRule;
import org.jboss.tools.jst.web.kb.PageContextFactory.CSSStyleSheetDescriptor;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

/**
 * 
 * @author yzhishko
 * 
 */

@SuppressWarnings("restriction")
public class CSSSelectorTreeModel {

	private CSSStyleSheetDescriptor[] cssSheets;
	private CSSTreeNode invisibleRoot;

	public CSSSelectorTreeModel(CSSStyleSheetDescriptor[] cssSheets) {
		this.cssSheets = cssSheets;
		setInvisibleRoot(new CSSTreeNode("")); //$NON-NLS-1$
		initModel(this.cssSheets);
	}

	private void initModel(CSSStyleSheetDescriptor[] cssStyleSheets) {
		for (int i = 0; i < cssStyleSheets.length; i++) {
			CSSStyleSheet styleSheet = cssStyleSheets[i].sheet;
			CSSTreeNode parentSheet = new CSSTreeNode(cssStyleSheets[i].source);
			parentSheet.setStyleSheetSource(parentSheet.toString());
			invisibleRoot.addChild(parentSheet);
			parentSheet.setCSSContainer(new CSSStyleSheetContainer(styleSheet,
					cssStyleSheets[i].source));
			CSSRuleList cssRuleList = styleSheet.getCssRules();
			for (int j = 0; j < cssRuleList.getLength(); j++) {
				CSSRule cssRule = cssRuleList.item(j);
				String[] selectors = CSSSelectorUtils
						.parseSelectorName(((ICSSStyleRule) cssRule)
								.getSelectorText());
				for (int k = 0; k < selectors.length; k++) {
					CSSTreeNode ruleNode = new CSSTreeNode(selectors[k]);
					ruleNode.setCSSContainer(new CSSRuleContainer(selectors[k], cssRule,
							cssStyleSheets[i].source));
					ruleNode.setStyleSheetSource(cssStyleSheets[i].source);
					parentSheet.addChild(ruleNode);
				}
			}
		}
	}

	public void setInvisibleRoot(CSSTreeNode invisibleRoot) {
		this.invisibleRoot = invisibleRoot;
	}

	public CSSTreeNode getInvisibleRoot() {
		return invisibleRoot;
	}

	public CSSTreeNode[] findCSSNodesByName(String name) {
		List<CSSTreeNode> treeNodes = new ArrayList<CSSTreeNode>(0);
		findCSSNodesRecursivly(invisibleRoot, name, treeNodes);
		return treeNodes.toArray(new CSSTreeNode[0]);
	}

	private void findCSSNodesRecursivly(CSSTreeNode parentNode, String name,
			List<CSSTreeNode> nodeCollection) {
		if (parentNode == null) {
			return;
		}
		List<CSSTreeNode> treeNodes = parentNode.getChildren();
		if (treeNodes == null) {
			return;
		}
		for (int i = 0; i < treeNodes.size(); i++) {
			CSSTreeNode node = treeNodes.get(i);
			if (name.equals(node.toString())) {
				nodeCollection.add(node);
			}
			findCSSNodesRecursivly(node, name, nodeCollection);
		}
	}

}
