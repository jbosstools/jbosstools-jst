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
package org.jboss.tools.jst.web.kb.internal.taglib.jq;

import org.jboss.tools.jst.web.kb.internal.taglib.CustomTagLibAttribute;

/**
 * @author Alexey Kazakov
 */
public class FixedToolbarAttributeProvider extends JQueryMobileAttrProvider {

	private static final JQueryMobileAttribute[] conditionalAttributes = new JQueryMobileAttribute[] {DATA_DISABLED_PAGE_ZOOM_ATTRIBUTE,
		DATA_FULLSCREEN_ATTRIBUTE,
		DATA_TAP_TOGGLE_ATTRIBUTE,
		FIXED_TOOLBAR_DATA_TRANSITION_ATTRIBUTE,
		DATA_UPDATE_PAGE_PADDING_ATTRIBUTE,
		DATA_VISIBLE_ON_PAGE_SHOW_ATTRIBUTE};

	private static final JQueryMobileAttribute[] requiredAttributes = new JQueryMobileAttribute[] {new DataRoleAttribute("", HEADER),
		new DataRoleAttribute("", FOOTER),
		DATA_POSITION_ATTRIBUTE};

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.attribute.AbstractAttributeProvider#checkComponent(org.jboss.tools.jst.web.kb.KbQuery)
	 */
	@Override
	protected boolean checkComponent() {
		return (checkDataRole(HEADER) || checkDataRole(FOOTER)) &&
				checkAttribute(DATA_POSITION_FIXED);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.attribute.AbstractAttributeProvider#getAttributes()
	 */
	@Override
	protected CustomTagLibAttribute[] getConditionalAttributes() {
		return conditionalAttributes;
	}

	@Override
	protected CustomTagLibAttribute[] getRequiredAttributes() {
		return requiredAttributes;
	}
}