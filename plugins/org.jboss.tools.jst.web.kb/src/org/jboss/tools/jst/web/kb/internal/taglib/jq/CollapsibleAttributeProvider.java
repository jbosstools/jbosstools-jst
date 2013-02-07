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
public class CollapsibleAttributeProvider extends JQueryMobileAttrProvider {

	private static final JQueryMobileAttribute[] requiredAttributes = new JQueryMobileAttribute[] {new DataRoleAttribute("", COLLAPSIBLE)};

	private static final JQueryMobileAttribute[] conditionalAttributes = new JQueryMobileAttribute[] {DATA_COLLAPSED_ATTRIBUTE,
		DATA_COLLAPSED_ICON_ATTRIBUTE,
		DATA_CONTENT_THEME_ATTRIBUTE,
		DATA_EXPANDED_ICON_ATTRIBUTE,
		DATA_ICONPOS_ATTRIBUTE,
		DATA_INSET_ATTRIBUTE,
		DATA_MINI_ATTRIBUTE,
		DATA_THEME_ATTRIBUTE};

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.attribute.AbstractAttributeProvider#checkComponent(org.jboss.tools.jst.web.kb.KbQuery)
	 */
	@Override
	protected boolean checkComponent() {
		return checkDataRole(COLLAPSIBLE);
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