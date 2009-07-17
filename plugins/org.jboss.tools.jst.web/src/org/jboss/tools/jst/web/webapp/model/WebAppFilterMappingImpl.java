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

public class WebAppFilterMappingImpl extends RegularObjectImpl {
	private static final long serialVersionUID = 1L;

    public String name() {
        String url = getAttributeValue(WebAppConstants.URL_PATTERN);
        String srv = getAttributeValue(WebAppConstants.SERVLET_NAME);
        String v = (url != null && url.length() > 0) ? url : srv;
        return getAttributeValue("filter-name") + ":" + v; //$NON-NLS-1$ //$NON-NLS-2$
    }

	protected void onAttributeValueEdit(String name, String oldValue, String newValue) throws XModelException {
		if(WebAppConstants.URL_PATTERN.equals(name) && newValue != null && newValue.length() > 0) {
			setAttributeValue(WebAppConstants.SERVLET_NAME, ""); //$NON-NLS-1$
		} else if(WebAppConstants.SERVLET_NAME.equals(name) && newValue != null && newValue.length() > 0) {
			setAttributeValue(WebAppConstants.URL_PATTERN, ""); //$NON-NLS-1$
		}
	}

}

