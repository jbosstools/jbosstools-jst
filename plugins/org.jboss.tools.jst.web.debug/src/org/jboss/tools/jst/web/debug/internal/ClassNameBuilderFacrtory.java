/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.debug.internal;

import org.jboss.tools.jst.web.WebUtils;
import org.jboss.tools.jst.web.debug.internal.xpl.TomcatClassNameBuilder;

/**
 * @author igels
 */
public class ClassNameBuilderFacrtory implements IClassNameBuilderFactory {

	private static ClassNameBuilderFacrtory INSTANCE = new ClassNameBuilderFacrtory();

	private ClassNameBuilderFacrtory() {
	}

	public static ClassNameBuilderFacrtory getInstance() {
		return INSTANCE;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.debug.internal.IClassNameBuilderFactory#getClassNameBuilder(org.jboss.tools.jst.web.debug.internal.ApplicationServerName)
	 */
	public IClassNameBuilder getClassNameBuilder(String applicationServerName) {
		if(applicationServerName.startsWith(WebUtils.WEB_SERVER_TOMCAT)) {
			return new TomcatClassNameBuilder();
		}// else if(applicationServerName.startsWith(WebUtils.APPLICATION_SERVER_JBOSS)) {
//		    return new JBossClassNameBuilder();
//		}
		// Default class name builder is Tomcat class name builder. (For J2EE Application server too)
		return new TomcatClassNameBuilder();
	}
}