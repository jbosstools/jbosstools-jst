/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.taglib.html.jq;

import static org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileAttrConstants14.*;

import org.jboss.tools.jst.web.kb.internal.taglib.CustomTagLibAttribute;
import org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttribute;

/**
 * @author Alexey Kazakov
 */
public class ListViewAttributeProvider14 extends ListViewAttributeProvider {

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
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.attribute.AbstractAttributeProvider#getAttributes()
	 */
	@Override
	protected CustomTagLibAttribute[] getConditionalAttributes() {
		return CONDITIONAL_ATTRIBUTES;
	}
}