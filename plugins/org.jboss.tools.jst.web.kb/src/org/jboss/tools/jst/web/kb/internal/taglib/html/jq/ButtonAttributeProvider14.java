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
public class ButtonAttributeProvider14 extends ButtonAttributeProvider {

	static final HtmlAttribute[] ATTRIBUTES = new HtmlAttribute[] {DATA_CORNERS_ATTRIBUTE,
		DATA_ICON_ATTRIBUTE,
		DATA_ICONPOS_ATTRIBUTE,
		DATA_ICONSHADOW_ATTRIBUTE,
		DATA_INLINE_ATTRIBUTE,
		DATA_MINI_ATTRIBUTE,
		DATA_SHADOW_ATTRIBUTE,
		DATA_THEME_ATTRIBUTE};

	@Override
	protected boolean checkComponent() {
		return checkDataRole(BUTTON) ||
				checkCurrentTag(BUTTON) ||
				checkAttributeForTag(TYPE_BUTTON, INPUT) ||
				checkAttributeForTag(TYPE_SUBMIT, INPUT) ||
				checkAttributeForTag(TYPE_RESET, INPUT) ||
				checkAttributeForTag(DATA_ROLE_HEADER, null, true) ||
				checkAttributeForTag(DATA_ROLE_FOOTER, null, true) ||
				checkClassAttribute(UI_BTN_CLASS);
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