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

import org.jboss.tools.jst.web.kb.internal.taglib.CustomTagLibAttribute;
import org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttribute;

/**
 * @author Alexey Kazakov
 */
public class TabsAttributeProvider14  extends JQueryMobileAttrProvider {

	static final HtmlAttribute[] ATTRIBUTES = new HtmlAttribute[] {new DataRoleAttribute("", "tabs")};

	@Override
	protected boolean checkComponent() {
		return false;
	}

	@Override
	protected CustomTagLibAttribute[] getConditionalAttributes() {
		return EMPTY;
	}

	@Override
	protected CustomTagLibAttribute[] getRequiredAttributes() {
		return ATTRIBUTES;
	}
}