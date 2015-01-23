/******************************************************************************* 
 * Copyright (c) 2015 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.angularjs.internal;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author Alexey Kazakov
 *
 */
public class AngularMessages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.jst.angularjs.internal.messages"; //$NON-NLS-1$

	public static String dataLoading;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, AngularMessages.class);
	}

	private AngularMessages() {
	}
}