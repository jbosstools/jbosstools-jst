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
public class ButtonAttributeProvider extends JQueryMobileAttrProvider {

	static final HtmlAttribute[] ATTRIBUTES = new HtmlAttribute[] {DATA_CORNERS_ATTRIBUTE,
		DATA_ICON_ATTRIBUTE,
		DATA_ICONPOS_ATTRIBUTE,
		DATA_ICONSHADOW_ATTRIBUTE,
		DATA_INLINE_ATTRIBUTE,
		DATA_MINI_ATTRIBUTE,
		DATA_SHADOW_ATTRIBUTE,
		DATA_THEME_ATTRIBUTE};

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.attribute.AbstractAttributeProvider#checkComponent(org.jboss.tools.jst.web.kb.KbQuery)
	 */
	@Override
	protected boolean checkComponent() {
		return checkDataRole(BUTTON) ||
				checkCurrentTag(BUTTON) ||
				checkAttributeForTag(TYPE_BUTTON, INPUT) ||
				checkAttributeForTag(TYPE_SUBMIT, INPUT) ||
				checkAttributeForTag(TYPE_RESET, INPUT) ||
				checkAttributeForTag(DATA_ROLE_HEADER, null, true) ||
				checkAttributeForTag(DATA_ROLE_FOOTER, null, true);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.attribute.AbstractAttributeProvider#getAttributes()
	 */
	@Override
	protected CustomTagLibAttribute[] getConditionalAttributes() {
		return ATTRIBUTES;
	}
}