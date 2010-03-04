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

	public CSSSelectorTreeModel(CSSStyleSheet[] cssSheets) {
	}

	public CSSSelectorTreeModel(CSSStyleSheetDescriptor[] cssSheets) {
		this.cssSheets = cssSheets;
		setInvisibleRoot(new CSSTreeNode("")); //$NON-NLS-1$
		initModel(this.cssSheets);
	}

	private void initModel(CSSStyleSheetDescriptor[] cssStyleSheets){
		for (int i = 0; i < cssStyleSheets.length; i++) {
			CSSStyleSheet styleSheet = cssStyleSheets[i].sheet;
			CSSTreeNode parentSheet = new CSSTreeNode(cssStyleSheets[i].source);
			parentSheet.setStyleSheetSource(parentSheet.toString());
			invisibleRoot.addChild(parentSheet);
			parentSheet.setCssResource(styleSheet);
			CSSRuleList cssRuleList = styleSheet.getCssRules();
			for (int j = 0; j < cssRuleList.getLength(); j++) {
				CSSRule cssRule = cssRuleList.item(j);
				String[] selectors = CSSSelectorUtils.parseSelectorName(((ICSSStyleRule)cssRule).getSelectorText());
				for (int k = 0; k < selectors.length; k++) {
					CSSTreeNode ruleNode = new CSSTreeNode(selectors[k]);
					ruleNode.setCssResource(cssRule);
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
	
	
}
