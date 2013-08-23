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
public class ControlgroupAttributeProvider extends JQueryMobileAttrProvider {

	private static final HtmlAttribute[] requiredAttributes = new HtmlAttribute[] {new DataRoleAttribute("Visually integrate multiple button-styled inputs of a single type (checkboxes, link-based buttons, radio buttons, selects) into a group. For grouping form checkboxes and radio buttons, the fieldset container is recommended inside a div container with the data-role=\"fieldcontain\", to improve label styling", CONTROLGROUP)};

	private static final HtmlAttribute[] conditionalAttributes = new HtmlAttribute[] {DATA_MINI_ATTRIBUTE, DATA_TYPE_ATTRIBUTE};

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.attribute.AbstractAttributeProvider#checkComponent(org.jboss.tools.jst.web.kb.KbQuery)
	 */
	@Override
	protected boolean checkComponent() {
		return checkDataRole(CONTROLGROUP);
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