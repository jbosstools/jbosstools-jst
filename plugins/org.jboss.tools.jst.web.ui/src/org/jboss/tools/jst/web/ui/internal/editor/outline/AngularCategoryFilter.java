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
import org.jboss.tools.jst.web.kb.taglib.IAttribute;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class AngularCategoryFilter implements ICategoryFilter {
	static final String CATEGORY_ANGULAR = "AngularJS";
	Set<String> angularAttrs = new HashSet<String>();

	static final Set<String> STRUCTURAL_ATTRIBUTES = new HashSet<String>();
	static {
		STRUCTURAL_ATTRIBUTES.add(HTMLConstants.ATTR_TYPE);
	}

	public AngularCategoryFilter() {}

	@Override
	public void setAttributes(IAttribute[] attributes) {
		angularAttrs.clear();
		for (IAttribute a: attributes) {
			//many attrs in angularJS do not belong to a component, and we cannot get library.
			if(a.getName().indexOf("ng-") >= 0) {
				angularAttrs.add(a.getName());
			}
		}
	}

	@Override
	public String getCategory(String attributeName) {
		if(attributeName.indexOf("ng-") < 0) {
			return null;
		}
		return (angularAttrs.contains(attributeName)
				|| (!angularAttrs.isEmpty() && "ng-app".equals(attributeName))) ? CATEGORY_ANGULAR : null;
	}

	@Override
	public Set<String> getStructuralAttributes(String nodeName) {
		return STRUCTURAL_ATTRIBUTES;
	}
}
