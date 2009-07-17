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
package org.jboss.tools.jst.web.webapp.model;

import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.impl.*;

public class WebAppServletImpl extends OrderedObjectImpl {
	private static final long serialVersionUID = 1L;

    protected RegularChildren createChildren() {
        return new OrderedByEntityChildren();
    }

    public String name() {
        return getAttributeValue("servlet-name"); //$NON-NLS-1$
    }

    public String getPresentationString() {
        String c = getAttributeValue(WebAppConstants.SERVLET_CLASS);
        if(c.length() == 0) c = getAttributeValue(WebAppConstants.JSP_FILE);
        return "" + getAttributeValue("servlet-name") + ":" + c; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

	protected void onAttributeValueEdit(String name, String oldValue, String newValue) throws XModelException {
		if(WebAppConstants.SERVLET_CLASS.equals(name) && newValue != null && newValue.length() > 0) {
			setAttributeValue(WebAppConstants.JSP_FILE, ""); //$NON-NLS-1$
		} else if(WebAppConstants.JSP_FILE.equals(name) && newValue != null && newValue.length() > 0) {
			setAttributeValue(WebAppConstants.SERVLET_CLASS, ""); //$NON-NLS-1$
		}
	}
}

