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
package org.jboss.tools.jst.web.ui.internal.editor.outline;

import java.util.HashSet;
import java.util.Set;

import org.jboss.tools.jst.web.ui.internal.editor.outline.JSPPropertySourceAdapter.ICategoryFilter;
import org.jboss.tools.jst.web.html.HTMLConstants;
import org.jboss.tools.jst.web.html.JQueryHTMLConstants;
import org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileAttrProvider;
import org.jboss.tools.jst.web.kb.taglib.IAttribute;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class JQueryCategoryFilter implements ICategoryFilter {
	static final String CATEGORY_JQM = "jQuery";
	Set<String> jQueryAttrs = new HashSet<String>();

	static final Set<String> STRUCTURAL_ATTRIBUTES = new HashSet<String>();
	static {
		STRUCTURAL_ATTRIBUTES.add(HTMLConstants.ATTR_TYPE);
		STRUCTURAL_ATTRIBUTES.add(JQueryHTMLConstants.ATTR_DATA_ROLE);
		STRUCTURAL_ATTRIBUTES.add(HTMLConstants.ATTR_CLASS);
	}

	public JQueryCategoryFilter() {
	}

	@Override
	public void setAttributes(IAttribute[] attributes) {
		jQueryAttrs.clear();
		Set<String> all = JQueryMobileAttrProvider.getAllAttributes();
		for (IAttribute a: attributes) {
			if(all.contains(a.getName())) {
				jQueryAttrs.add(a.getName());
			}
		}
	}

	@Override
	public String getCategory(String attributeName) {
		return (jQueryAttrs.contains(attributeName)) ? CATEGORY_JQM : null;
	}

	@Override
	public Set<String> getStructuralAttributes(String nodeName) {
		return STRUCTURAL_ATTRIBUTES;
	}
}
