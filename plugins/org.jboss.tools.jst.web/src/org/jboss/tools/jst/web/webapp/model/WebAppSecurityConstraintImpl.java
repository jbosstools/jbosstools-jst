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

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.impl.*;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;

public class WebAppSecurityConstraintImpl extends OrderedObjectImpl {
	private static final long serialVersionUID = 1L;

	protected RegularChildren createChildren() {
        return new SecurityConstraintChildren();
    }

    public String name() {
        return "" + getAttributeValue(XModelObjectLoaderUtil.ATTR_ID_NAME); //$NON-NLS-1$
    }

    public String getPresentationString() {
        String dn = getAttributeValue("display-name"); //$NON-NLS-1$
        return (dn != null && dn.length() > 0) ? dn : "security-constraint"; //$NON-NLS-1$
    }

}

class SecurityConstraintChildren extends GroupOrderedChildren {
    protected int getGroupCount() {
        return 3;
    }

    protected int getGroup(XModelObject o) {
        String entity = o.getModelEntity().getName();
        if("WebAppResourceCollection".equals(entity)) return 0; //$NON-NLS-1$
        if("WebAppAuthConstraint".equals(entity)) return 1; //$NON-NLS-1$
        if("WebAppUserConstraint".equals(entity)) return 2; //$NON-NLS-1$
        return 0;
    }


}

