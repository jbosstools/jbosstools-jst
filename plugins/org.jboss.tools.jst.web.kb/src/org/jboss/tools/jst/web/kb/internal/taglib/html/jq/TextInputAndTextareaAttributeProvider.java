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

import static org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileAttrConstants13.*;

import org.jboss.tools.jst.web.kb.internal.taglib.CustomTagLibAttribute;
import org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttribute;

/**
 * @author Alexey Kazakov, Daniel Azarov
 */
public class TextInputAndTextareaAttributeProvider extends JQueryMobileAttrProvider {

	static final HtmlAttribute[] ATTRIBUTES = new HtmlAttribute[] {
		DATA_CLEAR_BTN_ATTRIBUTE,
		DATA_CLEAR_BTN_TEXT_ATTRIBUTE,
		DATA_MINI_ATTRIBUTE,
		DATA_ROLE_NONE_ATTRIBUTE,
		DATA_INPUT_THEME_ATTRIBUTE};

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.attribute.AbstractAttributeProvider#checkComponent(org.jboss.tools.jst.web.kb.KbQuery)
	 */
	@Override
	protected boolean checkComponent() {
		return checkCurrentTag(TEXTAREA) ||
				(checkCurrentTag(INPUT) &&
				!checkAttributeForTag(TYPE_BUTTON, INPUT) &&
				!checkAttributeForTag(TYPE_SUBMIT, INPUT) &&
				!checkAttributeForTag(TYPE_RESET, INPUT) &&
				!checkAttributeForTag(TYPE_CHECKBOX, INPUT) &&
				!checkAttributeForTag(TYPE_RADIO, INPUT) &&
				!checkAttributeForTag(TYPE_RANGE, INPUT) &&
				!checkAttributeForTag(TYPE_HIDEN, INPUT));
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