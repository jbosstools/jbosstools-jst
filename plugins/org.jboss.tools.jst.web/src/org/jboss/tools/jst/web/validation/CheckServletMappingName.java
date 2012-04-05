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

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.validation.ValidationErrorManager;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;
import org.jboss.tools.jst.web.webapp.model.WebAppConstants;

/**
 * @author Viacheslav Kabanovich
 */
public class CheckServletMappingName extends Check {
	public static String JAX_RS_APPLICATION = "javax.ws.rs.core.Application"; //$NON-NLS-1$
	static String ATTR = WebAppConstants.SERVLET_NAME;
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
			fireMessage(object, WebXMLValidatorMessages.EMPTY, attr);
		} else if(findServlet(object, servletName) == null) {
			//JAX-RS
			if(servletName.equals(JAX_RS_APPLICATION)) {
				return;
			}
			IType type = CheckClass.getValidType(servletName, object);
			if(type != null) {
				try {
					new CheckServletClass(manager).check(object, servletName, type);
				} catch (JavaModelException e) {
					WebModelPlugin.getDefault().logError(e);
				}
			} else {
				fireMessage(object, WebXMLValidatorMessages.SERVLET_NOT_EXISTS, attr, servletName);
			}
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

class CheckServletClass extends CheckClass {

	public CheckServletClass(ValidationErrorManager manager) {
		super(manager, WebXMLPreferences.INVALID_SERVLET_REF, CheckServletMappingName.ATTR, false, null, CheckServletMappingName.JAX_RS_APPLICATION);
	}

	protected void check(XModelObject object, String value, IType type) throws JavaModelException {
		String mustExtend = checkExtends(object, type);
		if(mustExtend == null) {
			return;
		}
		fireExtends(object, preference, value, mustExtend);
	}
}
