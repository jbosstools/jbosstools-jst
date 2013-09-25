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
package org.jboss.tools.jst.web.kb.internal.taglib.html.jq;

import org.jboss.tools.jst.web.kb.internal.taglib.CustomTagLibAttribute;
import org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttribute;

/**
 * @author Alexey Kazakov
 */
public class ListViewAttributeProvider extends JQueryMobileAttrProvider {

	static final HtmlAttribute[] REQUIRED_ATTRIBUTES = new HtmlAttribute[] {new DataRoleAttribute("", LISTVIEW)};

	static final HtmlAttribute[] CONDITIONAL_ATTRIBUTES = new HtmlAttribute[] {DATA_AUTODIVIDERS_ATTRIBUTE,
		DATA_COUNT_THEME_ATTRIBUTE,
		DATA_DIVIDER_THEME_ATTRIBUTE,
		DATA_FILTER_ATTRIBUTE,
		DATA_FILTER_PLACEHOLDER_ATTRIBUTE,
		DATA_FILTER_THEME_ATTRIBUTE,
		DATA_HEADER_THEME_ATTRIBUTE,
		DATA_ICON_ATTRIBUTE,
		DATA_INSET_ATTRIBUTE,
		DATA_SPLIT_ICON_ATTRIBUTE,
		DATA_SPLIT_THEME_ATTRIBUTE,
		DATA_THEME_ATTRIBUTE};

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.attribute.AbstractAttributeProvider#checkComponent(org.jboss.tools.jst.web.kb.KbQuery)
	 */
	@Override
	protected boolean checkComponent() {
		return checkDataRole(LISTVIEW);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.attribute.AbstractAttributeProvider#getAttributes()
	 */
	@Override
	protected CustomTagLibAttribute[] getConditionalAttributes() {
		return CONDITIONAL_ATTRIBUTES;
	}

	@Override
	protected CustomTagLibAttribute[] getRequiredAttributes() {
		return REQUIRED_ATTRIBUTES;
	}
}