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
public class AButtonAttributeProvider14 extends ButtonAttributeProvider14 {

	static final HtmlAttribute[] ATTRIBUTES = new HtmlAttribute[] {new DataRoleAttribute("", "button")};

	@Override
	protected CustomTagLibAttribute[] getRequiredAttributes() {
		if(!checkAttributeForTag(DATA_ROLE_HEADER, null, true) && !checkAttributeForTag(DATA_ROLE_FOOTER, null, true)) {
			return ATTRIBUTES;
		}
		return EMPTY;
	}
}