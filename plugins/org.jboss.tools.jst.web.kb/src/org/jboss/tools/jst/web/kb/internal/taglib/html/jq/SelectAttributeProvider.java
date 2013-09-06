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
 * @author Alexey Kazakov, Daniel Azarov
 */
public class SelectAttributeProvider extends JQueryMobileAttrProvider {

	static final HtmlAttribute[] ATTRIBUTES = new HtmlAttribute[] {
		DATA_SELECT_DIVIDER_THEME_ATTRIBUTE,
		DATA_ICON_ATTRIBUTE,
		DATA_ICONPOS_ATTRIBUTE,
		DATA_INLINE_ATTRIBUTE,
		DATA_MINI_ATTRIBUTE,
		DATA_NATIVE_MENU_ATTRIBUTE,
		DATA_OVERLAY_THEME_ATTRIBUTE,
		DATA_PLACEHOLDER_ATTRIBUTE,
		DATA_ROLE_NONE_ATTRIBUTE,
		DATA_INPUT_THEME_ATTRIBUTE};

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.attribute.AbstractAttributeProvider#checkComponent(org.jboss.tools.jst.web.kb.KbQuery)
	 */
	@Override
	protected boolean checkComponent() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.attribute.AbstractAttributeProvider#getAttributes()
	 */
	@Override
	protected CustomTagLibAttribute[] getConditionalAttributes() {
		return EMPTY;
	}

	@Override
	protected CustomTagLibAttribute[] getRequiredAttributes() {
		return ATTRIBUTES;
	}
}