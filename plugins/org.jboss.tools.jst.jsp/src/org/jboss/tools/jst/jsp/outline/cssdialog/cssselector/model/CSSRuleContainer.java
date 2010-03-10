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

import org.w3c.dom.css.CSSRule;

/**
 * 
 * @author yzhishko
 *
 */

public class CSSRuleContainer extends CSSContainer {

	private CSSRule rule;
	private String selectorName;

	public CSSRuleContainer(String selectorName, CSSRule rule,
			String styleSheetPath) {
		super(styleSheetPath);
		setRule(rule);
		setSelectorName(selectorName);
	}

	public void setRule(CSSRule rule) {
		this.rule = rule;
	}

	public CSSRule getRule() {
		return rule;
	}

	@Override
	public boolean equals(Object obj) {
		boolean eq = super.equals(obj);
		if (eq == false) {
			return false;
		}
		if (!(obj instanceof CSSRuleContainer)) {
			return false;
		}
		return eq
				&& selectorName.equals(((CSSRuleContainer) obj)
						.getSelectorName())
				&& rule.equals(((CSSRuleContainer) obj).getRule());
	}

	public void setSelectorName(String selectorName) {
		this.selectorName = selectorName;
	}

	public String getSelectorName() {
		return selectorName;
	}
}
