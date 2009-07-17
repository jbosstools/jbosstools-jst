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
package org.jboss.tools.jst.web.tld.model;

import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.impl.CustomizedObjectImpl;

public class TLDAttribute21Impl extends CustomizedObjectImpl {
	private static final long serialVersionUID = 1L;
	static String DEFERRED_VALUE = "deferred-value type"; //$NON-NLS-1$
	static String DEFERRED_METHOD = "deferred-method signature"; //$NON-NLS-1$

	protected void onAttributeValueEdit(String name, String oldValue, String newValue) throws XModelException {
		if(DEFERRED_VALUE.equals(name)) {
			setAttributeValue(DEFERRED_METHOD, ""); //$NON-NLS-1$
		} else if(DEFERRED_METHOD.equals(name)) {
			setAttributeValue(DEFERRED_VALUE, ""); //$NON-NLS-1$
		}
	}

}
