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
 * @author Alexey Kazakov
 */
public class FlipToggleSwitchAttributeProvider extends JQueryMobileAttrProvider {

	static final HtmlAttribute[] REQUIRED_ATTRIBUTES = new HtmlAttribute[] {new DataRoleAttribute("", SLIDER)};

	static final HtmlAttribute[] CONDITIONAL_ATTRIBUTES = new HtmlAttribute[] {DATA_MINI_ATTRIBUTE,
		DATA_ROLE_NONE_ATTRIBUTE,
		DATA_INPUT_THEME_ATTRIBUTE,
		DATA_TRACK_THEME_ATTRIBUTE};

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.attribute.AbstractAttributeProvider#checkComponent(org.jboss.tools.jst.web.kb.KbQuery)
	 */
	@Override
	protected boolean checkComponent() {
		return checkDataRole(SLIDER);
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