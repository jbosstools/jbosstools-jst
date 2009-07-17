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
package org.jboss.tools.jst.jsp.support.kb;

import org.eclipse.ui.IEditorInput;

import org.jboss.tools.common.kb.KbDinamicResource;
import org.jboss.tools.jst.web.project.list.IWebPromptingProvider;

public class WTPKbdManagedBeanNameResource extends WTPKbdBeanPropertyResource {

	public WTPKbdManagedBeanNameResource(IEditorInput editorInput) {
		super(editorInput);
	}

	private static String[][] MANAGED_BEAN_NAME_WRAPPERS = {{"", ""}}; //$NON-NLS-1$ //$NON-NLS-2$
	
	protected String[][] getWrappers() {
		return MANAGED_BEAN_NAME_WRAPPERS;
	}

	public String getType() {
		return KbDinamicResource.MANAGED_BEAN_NAME_TYPE;
	}

	public void setConstraint(String name, String value) {
		super.setConstraint(name, value);
		if(IWebPromptingProvider.PROPERTY_BEAN_ONLY.equals(name)) {
			if(value == null) {
				type.remove(IWebPromptingProvider.PROPERTY_BEAN_ONLY);
			} else {
				type.setProperty(IWebPromptingProvider.PROPERTY_BEAN_ONLY, value);
			}
		}
	}
}
