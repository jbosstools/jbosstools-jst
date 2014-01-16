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

import static org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttributeConstants.INPUT;
import static org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttributeConstants.SELECT;
import static org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttributeConstants.TYPE_CHECKBOX;
import static org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileAttrConstants.DATA_INPUT_THEME_ATTRIBUTE;
import static org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileAttrConstants.DATA_MINI_ATTRIBUTE;
import static org.jboss.tools.jst.web.kb.internal.taglib.html.jq.JQueryMobileAttrConstants.DATA_ROLE_NONE_ATTRIBUTE;

import java.util.HashSet;
import java.util.Set;

import org.jboss.tools.jst.web.kb.internal.taglib.CustomTagLibAttribute;
import org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttribute;

/**
 * @author Alexey Kazakov
 */
public class FlipToggleSwitchAttributeProvider14 extends JQueryMobileAttrProvider {

	static final DataRoleAttribute FLIPSWITCH = new DataRoleAttribute("Flip switches are used for boolean style inputs like true/false or on/off in a compact UI element", "flipswitch");
	static final HtmlAttribute DATA_OFF_TEXT = new HtmlAttribute("data-off-text", "", new String[]{});
	static final HtmlAttribute DATA_ON_TEXT = new HtmlAttribute("data-on-text", "", new String[]{});

	static final HtmlAttribute[] CONDITIONAL_ATTRIBUTES = new HtmlAttribute[] {DATA_MINI_ATTRIBUTE,
		DATA_ROLE_NONE_ATTRIBUTE,
		DATA_INPUT_THEME_ATTRIBUTE};

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.attribute.AbstractAttributeProvider#checkComponent(org.jboss.tools.jst.web.kb.KbQuery)
	 */
	@Override
	protected boolean checkComponent() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.attribute.AbstractAttributeProvider#getAttributes()
	 */
	@Override
	protected CustomTagLibAttribute[] getConditionalAttributes() {
		Set<HtmlAttribute> result = new HashSet<HtmlAttribute>();
		boolean checkbox = checkAttributeForTag(TYPE_CHECKBOX, INPUT);
		if(checkbox || checkCurrentTag(SELECT)) {
			result.add(FLIPSWITCH);
		}
		if(checkDataRole(FLIPSWITCH)) {
			result.add(DATA_MINI_ATTRIBUTE);
			result.add(DATA_ROLE_NONE_ATTRIBUTE);
			result.add(DATA_INPUT_THEME_ATTRIBUTE);
			if(checkbox) {
				result.add(DATA_OFF_TEXT);
				result.add(DATA_ON_TEXT);
			}
		}
		return result.toArray(new HtmlAttribute[result.size()]);
	}
}