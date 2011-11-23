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
public class CheckRoleReferenceName extends Check {
	static String ATTR = "role-name"; //$NON-NLS-1$

	public CheckRoleReferenceName(ValidationErrorManager manager, String preference, String attr) {
		super(manager, preference, attr);
	}

	public void check(XModelObject object) {
		String roleName = object.getAttributeValue(attr);
		if(roleName == null) return;
		if(roleName.length() == 0) {
			fireMessage(object, NLS.bind(WebXMLValidatorMessages.EMPTY, attr));
		} else if(!isRoleNameOk(object, roleName)) {
			fireMessage(object, NLS.bind(WebXMLValidatorMessages.ROLE_NOT_EXISTS, attr, roleName));
		}
	}
	
	boolean isRoleNameOk(XModelObject mapping, String roleName) {
		if(roleName.equalsIgnoreCase("NONE")) return true; //$NON-NLS-1$
		if("*".equals(roleName)) return true; //$NON-NLS-1$
		if(findRole(mapping, roleName) != null) return true;
		return false;
	}
	
	XModelObject findRole(XModelObject mapping, String name) {
		XModelObject webxml = WebAppHelper.getParentFile(mapping);
		if(webxml == null) return null;
		XModelObject[] cs = WebAppHelper.getRoles(webxml);
		for (int i = 0; i < cs.length; i++) {
			if(name.equals(cs[i].getAttributeValue(attr))) return cs[i];
		}
		return null;
	}

}
