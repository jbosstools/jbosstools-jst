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

/**
 * @author Alexey Kazakov
 */
public class IconAttribute extends JQueryMobileAttribute {

	/**
	 * @param parentComponent
	 * @param name
	 * @param description
	 * @param values
	 */
	public IconAttribute() {
		super("data-icon", "", new String[]{"home",
				"delete", "plus", "arrow-u", "arrow-d", "check", "gear",
				"grid", "star", "custom", "arrow-r", "arrow-l", "minus",
				"refresh", "forward", "back", "alert", "info", "search"});
	}
}