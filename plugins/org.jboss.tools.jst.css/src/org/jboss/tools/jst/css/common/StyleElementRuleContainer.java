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

import org.w3c.dom.Node;
import org.w3c.dom.css.CSSStyleRule;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class StyleElementRuleContainer extends CSSStyleRuleContainer {

	private Node styleNode;

	public StyleElementRuleContainer(Node styleNode, CSSStyleRule styleRule) {
		super(styleRule);
		this.styleNode = styleNode;
	}

	public Object getStyleObject() {
		return styleNode;
	}

}
