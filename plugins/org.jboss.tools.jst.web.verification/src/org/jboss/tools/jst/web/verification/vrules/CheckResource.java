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
package org.jboss.tools.jst.web.verification.vrules;

import java.util.List;
import java.util.StringTokenizer;

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.impl.XModelImpl;
import org.jboss.tools.common.verification.vrules.*;
import org.jboss.tools.jst.web.project.list.IWebPromptingProvider;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;

public class CheckResource extends WebDefaultCheck {

	protected boolean isRelevant(VObject object) {
		return object.getParent() != null;
	}

	public VResult[] check(VObject object) {
		String attribute = getAttribute();
		String value = (String)object.getAttribute(attribute);
		XModel model = getXModel(object);
		XModelObject webRoot = model == null ? null : model.getByPath("FileSystems/WEB-ROOT"); //$NON-NLS-1$
		if(webRoot == null) return null;
		
		List list = WebPromptingProvider.getInstance().getList(model, IWebPromptingProvider.JSF_CONVERT_URL_TO_PATH, value, null);
		if(list != null && list.size() > 0) {
			value = list.get(0).toString();
		}
		
		if(value == null || value.trim().length() == 0) {
			if("true".equals(rule.getProperty("acceptEmpty"))) return null; //$NON-NLS-1$ //$NON-NLS-2$
			return fire(object, attribute + ".empty", attribute, null); //$NON-NLS-1$
		}
		String value1 = !value.startsWith("/") ? "/" + value : value; //$NON-NLS-1$ //$NON-NLS-2$
		XModelObject tld = XModelImpl.getByRelativePath(model, value1);
		if(tld == null) {
			return fire(object, attribute + ".exists", attribute, value); //$NON-NLS-1$
		}
		String value2 = value.startsWith("/") ? value.substring(1) : value; //$NON-NLS-1$
		XModelObject tld2 = webRoot.getChildByPath(value2);
		if(tld2 == null) {
			return fire(object, attribute + ".exists", attribute, value); //$NON-NLS-1$
		}
		if(!checkExtensions(value)) {
			return fire(object, attribute + ".extension", attribute, value); //$NON-NLS-1$
		}
		return null;
		
	}

	protected String getAttribute() {
		return rule.getProperty("attribute"); //$NON-NLS-1$
	}
	
	boolean checkExtensions(String value) {
		value = value.toLowerCase();
		String extensions = rule.getProperty("extensions"); //$NON-NLS-1$
		if(extensions == null || extensions.trim().length() == 0) return true;
		StringTokenizer st = new StringTokenizer(extensions, " "); //$NON-NLS-1$
		while(st.hasMoreTokens()) {
			String ext = st.nextToken();
			if(value.endsWith(ext)) return true;
		}
		return false;
	}
	
}
