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

import java.util.Properties;

import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.impl.*;

public class WebAppErrorPageImpl extends RegularObjectImpl {
	private static final long serialVersionUID = 1L;

    public String name() {
        String code = getAttributeValue(WebAppConstants.ERROR_CODE);
        return (code != null && code.length() > 0) ? code : getAttributeValue(WebAppConstants.EXCEPTION_TYPE);
    }

	protected void onAttributeValueEdit(String name, String oldValue, String newValue) throws XModelException {
		if(WebAppConstants.ERROR_CODE.equals(name) && newValue != null && newValue.length() > 0) {
			if(getAttributeValue(WebAppConstants.EXCEPTION_TYPE).length() > 0) {
				Properties p = new Properties();
				p.setProperty("focusAttribute", WebAppConstants.ERROR_CODE); //$NON-NLS-1$
				XActionInvoker.invoke("EditActions.Edit", this, p); //$NON-NLS-1$
				if(!"true".equals(p.getProperty("done"))) { //$NON-NLS-1$ //$NON-NLS-2$
					if(oldValue == null) oldValue = ""; //$NON-NLS-1$
					setAttributeValue(WebAppConstants.ERROR_CODE, oldValue);
				}
			}
		} else if(WebAppConstants.EXCEPTION_TYPE.equals(name) && newValue != null && newValue.length() > 0) {
			if(getAttributeValue(WebAppConstants.ERROR_CODE).length() > 0) {
				Properties p = new Properties();
				p.setProperty("focusAttribute", WebAppConstants.EXCEPTION_TYPE); //$NON-NLS-1$
				XActionInvoker.invoke("EditActions.Edit", this, p); //$NON-NLS-1$
				if(!"true".equals(p.getProperty("done"))) { //$NON-NLS-1$ //$NON-NLS-2$
					if(oldValue == null) oldValue = ""; //$NON-NLS-1$
					setAttributeValue(WebAppConstants.EXCEPTION_TYPE, oldValue);
				}
			}
		}
	}

}

