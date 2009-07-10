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

public class CheckServletMappingName extends WebDefaultCheck {
	static String ATTR = "servlet-name"; //$NON-NLS-1$
	public VResult[] check(VObject object) {
		XModelObject o = ((VObjectImpl)object).getModelObject();
		String servletName = o.getAttributeValue(ATTR);
		if(servletName == null) return null;
		if(servletName.length() == 0) {
			if("true".equals(rule.getProperty("acceptEmpty"))) return null; //$NON-NLS-1$ //$NON-NLS-2$
			return fire(object, "servlet-mapping.empty", ATTR, null); //$NON-NLS-1$
		} else if(findServlet(o, servletName) == null) {
			return fire(object, "servlet-mapping", ATTR, servletName); //$NON-NLS-1$
		}
		return null;
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
