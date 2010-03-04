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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSNodeList;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSStyleDeclItem;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSStyleDeclaration;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSStyleRule;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.web.kb.ICSSContainerSupport;
import org.jboss.tools.jst.web.kb.PageContextFactory;
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
public class CSSJSPRecognizer {

	private JSPMultiPageEditor jspMultiPageEditor;
	
	public static final int OK = 0;
	public static final int VOID_RESULT = 1;
	private List<CSSStyleSheet> styleSheets;
	private List<CSSStyleSheetDescriptor> styleSheetDescriptors;
	private List<CSSRuleList> cssRuleLists;
	private CSSRule[] cssRules;

	public CSSJSPRecognizer(JSPMultiPageEditor jspMultiPageEditor) {
		this.jspMultiPageEditor = jspMultiPageEditor;
	}
	
	private List<CSSRuleList> extractCSSRulesLists() {
		ICSSContainerSupport cssContainerSupport = null;
		ELContext context = PageContextFactory.createPageContext(getFile());
		if (!(context instanceof ICSSContainerSupport)) {
			return null;
		}
		cssContainerSupport = (ICSSContainerSupport) context;
		java.util.List<CSSStyleSheetDescriptor> descrs = cssContainerSupport
				.getCSSStyleSheetDescriptors();
		if (descrs == null || descrs.size() == 0) {
			return null;
		}
		styleSheetDescriptors = descrs;
		java.util.List<CSSRuleList> cssRuleLists = new ArrayList<CSSRuleList>(0);
		styleSheets = new ArrayList<CSSStyleSheet>(0);
		for (int i = 0; i < descrs.size(); i++) {
			styleSheets.add(descrs.get(i).sheet);
			cssRuleLists.add(descrs.get(i).sheet.getCssRules());
		}
		if (cssRuleLists.size() == 0) {
			return null;
		}
		return cssRuleLists;
	}

	private IFile getFile() {
		StructuredModelWrapper smw = new StructuredModelWrapper();
		try {
			smw.init(getDocument());
			return smw.getFile();
		} finally {
			smw.dispose();
		}
	}

	private IDocument getDocument() {
		IDocument document = jspMultiPageEditor.getSourceEditor()
				.getTextViewer().getDocument();
		return document;
	}

	private CSSRule[] extractCSSRules(java.util.List<CSSRuleList> cssRuleLists) {
		if (cssRuleLists == null) {
			return null;
		}
		java.util.List<CSSRule> cssRules = new ArrayList<CSSRule>(0);
		for (int i = 0; i < cssRuleLists.size(); i++) {
			CSSRuleList cssRuleList = cssRuleLists.get(i);
			for (int j = 0; j < cssRuleList.getLength(); j++) {
				cssRules.add(cssRuleList.item(j));
			}
		}
		if (cssRules.size() == 0) {
			return null;
		}
		return cssRules.toArray(new CSSRule[0]);
	}
	
	public Map<String, Map<String, String>> getCSSStyleMap(
			CSSRule[] cssRules) {
		if (cssRules == null) {
			return null;
		}
		Map<String, Map<String, String>> styleMap = new LinkedHashMap<String, Map<String, String>>(
				0);
		for (int i = 0; i < cssRules.length; i++) {
			ICSSStyleRule styleRule = (ICSSStyleRule) cssRules[i];
			String styleClassSelector = styleRule.getSelectorText();
			String[] selectors = CSSSelectorUtils.parseSelectorName(styleClassSelector);
			for (int j = 0; j < selectors.length; j++) {
				String styleClassName = selectors[j];
				ICSSNodeList cssNodeList = styleRule.getChildNodes();
				if (cssNodeList == null) {
					continue;
				}
				Map<String, String> attrsMap = new LinkedHashMap<String, String>(0);
				for (int k = 0; k < cssNodeList.getLength(); k++) {
					ICSSStyleDeclaration styleDeclaration = (ICSSStyleDeclaration) cssNodeList.item(k);
					ICSSNodeList attrsList = styleDeclaration.getChildNodes();
					if (attrsList == null) {
						continue;
					}
					for (int l = 0; l < attrsList.getLength(); l++) {
						ICSSStyleDeclItem styleItem = (ICSSStyleDeclItem) attrsList.item(l);
						attrsMap.put(styleItem.getPropertyName(), styleItem.getCSSValueText());
					}
				}
				Map<String, String> attrsForCSSStyle = styleMap.get(styleClassName);
				if (attrsForCSSStyle == null) {
					styleMap.put(styleClassName, attrsMap);
				} else {
					attrsForCSSStyle.putAll(attrsMap);
					styleMap.remove(styleClassName);
					styleMap.put(styleClassName, attrsForCSSStyle);
				}
			}
		}
		return styleMap;
	}
	
	public CSSSelectorTreeModel getCssStyleClassTreeModel(CSSStyleSheetDescriptor[] cssSheets){
		if (cssSheets != null) {
			return new CSSSelectorTreeModel(cssSheets);
		}
		return null;
	}
	
	public CSSSelectorTreeModel getCssStyleClassTreeModel(){
		return getCssStyleClassTreeModel(styleSheetDescriptors.toArray(new CSSStyleSheetDescriptor[0]));
	}
	
	public Map<String, Map<String, String>> getCSSStyleMap() {
		return getCSSStyleMap(this.cssRules);
	}
	
	public int parseCSS(){
		this.cssRuleLists = extractCSSRulesLists();
		this.cssRules = extractCSSRules(this.cssRuleLists);
		if (cssRules != null && cssRules.length != 0) {
			return OK;
		}
		return VOID_RESULT;
	}
	
	public CSSStyleSheet[] getStyleSheets() {
		return styleSheets.toArray(new CSSStyleSheet[0]);
	}
	
	public CSSRule[] getCssRules() {
		return cssRules;
	}
	
}
