/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.web.validation;

import org.eclipse.osgi.util.NLS;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.validation.ValidationErrorManager;
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;

/**
 * @author Viacheslav Kabanovich
 */
public class CheckServletMappingName extends Check {
	static String ATTR = "servlet-name"; //$NON-NLS-1$
	boolean acceptEmpty = false;

	public CheckServletMappingName(ValidationErrorManager manager, String preference, boolean acceptEmpty) {
		super(manager, preference, ATTR);
		this.acceptEmpty = acceptEmpty;
	}

	public void check(XModelObject object) {
		String servletName = object.getAttributeValue(ATTR);
		if(servletName == null) return;
		if(servletName.length() == 0) {
			if(acceptEmpty) return;
			fireMessage(object, NLS.bind(WebXMLValidatorMessages.EMPTY, attr));
		} else if(findServlet(object, servletName) == null) {
			fireMessage(object, NLS.bind(WebXMLValidatorMessages.SERVLET_NOT_EXISTS, attr, servletName));
		}
	}
	
	XModelObject findServlet(XModelObject mapping, String name) {
		XModelObject webxml = WebAppHelper.getParentFile(mapping);
		XModelObject[] cs = WebAppHelper.getServlets(webxml);
		for (int i = 0; i < cs.length; i++) {
			if(name.equals(cs[i].getAttributeValue(ATTR))) return cs[i];
		}
		return null;
	}

}
