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

import java.io.InputStream;
import java.util.*;
import org.eclipse.ui.IEditorInput;
import org.jboss.tools.common.kb.*;
import org.jboss.tools.jst.web.project.list.IWebPromptingProvider;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;

/**
 * @author Jeremy
 */
public class WTPKbdBeanMethodResource extends WTPKbdBeanPropertyResource {
	public static String SUPPORTED_ID = WebPromptingProvider.JSF_BEAN_METHODS;
	
	public WTPKbdBeanMethodResource(IEditorInput editorInput) {
		super(editorInput);
	}
	
	public boolean isReadyToUse() {
		return (fProvider != null && fXModel != null);
	}
	
	public String getType() { 
		return KbDinamicResource.BEAN_METHOD_BY_SYGNATURE_TYPE;
	}

	public InputStream getInputStream() {
		return null;
	}
	
	public String toString () {
		return "WTPKbdBeanMethodResource"; //$NON-NLS-1$
	}

	public String getSupportedID () {
		return SUPPORTED_ID;
	}

	public void setConstraint(String name, String value) {
		if (name == null) return;
		if (value == null || value.trim().length() == 0) return;

		if ("paramType".equalsIgnoreCase(name)) { //$NON-NLS-1$
			String[] types = (String[])type.get(IWebPromptingProvider.PARAMETER_TYPES);
			List<String> aTypes = new ArrayList<String>();
			for (int i = 0; types != null && i < types.length; i++) {
				aTypes.add(types[i]);	
			}
			aTypes.add(value);
			types = new String[(aTypes == null ? 0 : aTypes.size())];
			for (int i = 0; aTypes != null && i < aTypes.size(); i++) {
				types[i] = (String)aTypes.get(i);
			}
			
			type.put(IWebPromptingProvider.PARAMETER_TYPES, types);
		} else if ("returnType".equalsIgnoreCase(name)) { //$NON-NLS-1$
			type.put(IWebPromptingProvider.RETURN_TYPE, value);
		}
	}

	public void clearConstraints() {
		if (type != null) type.clear();
		type.put(IWebPromptingProvider.PARAMETER_TYPES, new String[0]);
		type.put(IWebPromptingProvider.RETURN_TYPE, "void"); //$NON-NLS-1$
	}

	public Collection queryProposal(String query, String tail) {
		throw new RuntimeException("Not implemented"); //$NON-NLS-1$
	}

}