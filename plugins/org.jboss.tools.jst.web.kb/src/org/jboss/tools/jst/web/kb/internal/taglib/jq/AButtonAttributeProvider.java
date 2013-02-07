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
public class AButtonAttributeProvider extends ButtonAttributeProvider {

	private static final JQueryMobileAttribute[] requiredAttributes = new JQueryMobileAttribute[] {new DataRoleAttribute("", "button")};

	@Override
	protected CustomTagLibAttribute[] getRequiredAttributes() {
		if(!checkAttributeForTag(DATA_ROLE_HEADER, null, true) && !checkAttributeForTag(DATA_ROLE_FOOTER, null, true)) {
			return requiredAttributes;
		}
		return EMPTY;
	}
}