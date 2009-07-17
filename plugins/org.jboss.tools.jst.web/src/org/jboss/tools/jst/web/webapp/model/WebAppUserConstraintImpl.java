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

import org.jboss.tools.common.model.impl.*;

public class WebAppUserConstraintImpl extends OrderedObjectImpl {
	private static final long serialVersionUID = 1L;

    public String name() {
        return "user-data-constraint"; //$NON-NLS-1$
    }

    public String getPresentationString() {
        String dn = getAttributeValue("description"); //$NON-NLS-1$
        return (dn != null && dn.length() > 0) ? dn : "user-data-constraint"; //$NON-NLS-1$
    }
}
