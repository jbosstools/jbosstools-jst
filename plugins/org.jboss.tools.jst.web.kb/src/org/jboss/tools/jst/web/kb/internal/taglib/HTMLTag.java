/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.taglib;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Alexey Kazakov
 */
public class HTMLTag extends CustomTagLibComponent {

	private Set<String> childComponents = new HashSet<String>();

	/**
	 * @param name Child tag name
	 */
	public void addChildTagName(String name) {
		childComponents.add(name);
	}

	/**
	 * @return the childComponents
	 */
	public Set<String> getChildTags() {
		return childComponents;
	}
}