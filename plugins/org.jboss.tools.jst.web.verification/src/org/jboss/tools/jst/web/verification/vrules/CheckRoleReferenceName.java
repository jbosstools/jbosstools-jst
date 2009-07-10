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

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.verification.vrules.*;
import org.jboss.tools.common.verification.vrules.layer.VObjectImpl;
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;

public class CheckRoleReferenceName extends WebDefaultCheck {
	static String ATTR = "role-name"; //$NON-NLS-1$

	public VResult[] check(VObject object) {
		XModelObject o = ((VObjectImpl)object).getModelObject();
		String attr = rule.getProperty("attribute"); //$NON-NLS-1$
		if(attr == null || attr.length() == 0) attr = ATTR;
		String roleName = o.getAttributeValue(attr);
		if(roleName == null) return null;
		if(roleName.length() == 0) {
			return fire(object, "role.empty", attr, null); //$NON-NLS-1$
		} else if(!isRoleNameOk(o, roleName)) {
			return fire(object, "role", attr, roleName); //$NON-NLS-1$
		}
		return null;
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
			if(name.equals(cs[i].getAttributeValue(ATTR))) return cs[i];
		}
		return null;
	}

}
