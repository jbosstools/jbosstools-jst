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
package org.jboss.tools.jst.web.ui.internal.editor.support.kb;

public class WTPKbJsfValuesResource {
	@SuppressWarnings("nls")
	private static final String[] fixedJsfValues = {
		"header", "headerValues", "param", "paramValues",
		"cookie", "initParam", "requestScope",
		"sessionScope", "applicationScope",
		"facesContext", "view"};

	/**
     * 
     * @return
     */
    public static String[] getJsfValues() {
    	return fixedJsfValues;
    }
}