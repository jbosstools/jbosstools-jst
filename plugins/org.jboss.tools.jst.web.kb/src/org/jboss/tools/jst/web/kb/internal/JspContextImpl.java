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
package org.jboss.tools.jst.web.kb.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.tools.jst.web.kb.ICSSContainerSupport;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.internal.taglib.NameSpace;
import org.jboss.tools.jst.web.kb.taglib.INameSpace;
import org.w3c.dom.css.CSSStyleSheet;

/**
 * JSP page context
 * @author Alexey Kazakov
 */
public class JspContextImpl extends XmlContextImpl implements ICSSContainerSupport {
	protected List<IPageContext> fIncludedContexts = null;
	protected List<CSSStyleSheet> fCSSStyleSheets = null;
	
	
	@Override
	public void addIncludedContext(IPageContext includedContext) {
		if (fIncludedContexts == null) {
			fIncludedContexts = new ArrayList<IPageContext>();
		}
		fIncludedContexts.add(includedContext);
		// Fix for JBIDE-5083 >>>
		includedContext.setParent(this);
		// Fix for JBIDE-5083 <<<
	}

	@Override
	public List<IPageContext> getIncludedContexts() {
		return fIncludedContexts;
	}

	@Override
	public Map<String, List<INameSpace>> getNameSpaces(int offset) {
		Map<String, List<INameSpace>> superNameSpaces = super.getNameSpaces(offset);
		
		List<INameSpace> fakeForHtmlNS = new ArrayList<INameSpace>();
		fakeForHtmlNS.add(new NameSpace("", "")); //$NON-NLS-1$ //$NON-NLS-2$
		superNameSpaces.put("", fakeForHtmlNS); //$NON-NLS-1$
		
		return superNameSpaces;
	}

	public void addCSSStyleSheet(CSSStyleSheet cssStyleSheet) {
		if (fCSSStyleSheets == null) {
			fCSSStyleSheets = new ArrayList<CSSStyleSheet>();
		}
		fCSSStyleSheets.add(cssStyleSheet);
	}

	public List<CSSStyleSheet> getCSSStyleSheets() {
		List<CSSStyleSheet> sheets = new ArrayList<CSSStyleSheet>();
		
		if (fCSSStyleSheets != null) {
			for (CSSStyleSheet sheet : fCSSStyleSheets) {
				sheets.add(sheet);
			}
		}
		
		List<IPageContext> includedContexts = getIncludedContexts();
		if (includedContexts != null) {
			for (IPageContext includedContext : includedContexts) {
				if (includedContext instanceof ICSSContainerSupport) {
					List<CSSStyleSheet> includedSheets = ((ICSSContainerSupport)includedContext).getCSSStyleSheets();
					if (includedSheets != null) {
						sheets.addAll(includedSheets);
					}
				}
			}
		}
		
		return sheets;

	}
	
}