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
public class PanelAttributeProvider extends JQueryMobileAttrProvider {

	private static final JQueryMobileAttribute[] requiredAttributes = new JQueryMobileAttribute[] {new DataRoleAttribute("", PANEL)};

	private static final JQueryMobileAttribute[] conditionalAttributes = new JQueryMobileAttribute[] {PANEL_DATA_POSITION_ATTRIBUTE,
		DATA_DISPLAY_ATTRIBUTE,
		PANEL_DATA_DISMISSIBLE_ATTRIBUTE,
		DATA_POSITION_FIXED_ATTRIBUTE,
		DATA_SWIPE_CLOSE_ATTRIBUTE,
		DATA_THEME_ATTRIBUTE};

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.attribute.AbstractAttributeProvider#checkComponent(org.jboss.tools.jst.web.kb.KbQuery)
	 */
	@Override
	protected boolean checkComponent() {
		return checkDataRole(PANEL);
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