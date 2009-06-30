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

package org.jboss.tools.jst.css.view;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.wst.css.core.internal.provisional.document.ICSSNode;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class CSSViewUtil {

	public static CSSStyleRule getStyleRule(ICSSNode node) {

		while (node != null) {

			if (node instanceof CSSStyleRule)
				return (CSSStyleRule) node;

			node = node.getParentNode();
		}

		return null;
	}

	public static Map<String, String> getStyleAttributes(CSSStyleRule styleRule) {

		CSSStyleDeclaration declaration = styleRule.getStyle();
		Map<String, String> styleMap = new HashMap<String, String>();
		for (int i = 0; i < declaration.getLength(); i++) {
			String propperty = declaration.item(i);
			String value = declaration.getPropertyValue(propperty);
			styleMap.put(propperty, value);
		}

		return styleMap;

	}

}
