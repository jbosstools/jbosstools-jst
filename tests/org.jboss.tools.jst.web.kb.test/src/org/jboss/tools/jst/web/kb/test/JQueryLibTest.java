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
package org.jboss.tools.jst.web.kb.test;

/**
 * @author Alexey Kazakov
 */
public abstract class JQueryLibTest extends HTML5Test {

	private static final String TAGLIB_URI = "jQueryMobile";

	@Override
	protected String getTaglibUri() {
		return TAGLIB_URI;
	}
}