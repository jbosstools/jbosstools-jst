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

import org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttribute;

/**
 * @author Alexey Kazakov
 */
public class DataRoleAttribute extends HtmlAttribute {

	public DataRoleAttribute() {
		this("data-role", "");
	}

	public DataRoleAttribute(String description) {
		super("data-role", description);
	}

	/**
	 * @param name
	 * @param description
	 * @param values
	 */
	public DataRoleAttribute(String description, String value) {
		super("data-role", "", new String[]{value}, new String[]{description});
	}
}