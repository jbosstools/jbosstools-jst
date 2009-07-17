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

import org.jboss.tools.common.model.*;

public class EditorTreeConstraint implements XFilteredTreeConstraint {

    public EditorTreeConstraint() {}

    public void update(XModel model) {}

    public boolean accepts(XModelObject object) {
        return true;
    }
    
    static String entities = ".TLDTag12.TLDTag.TLDValidator."; //TLDTag21 has more rich attributes //$NON-NLS-1$

    public boolean isHidingAllChildren(XModelObject object) {
        String entity = "." + object.getModelEntity().getName() + "."; //$NON-NLS-1$ //$NON-NLS-2$
        if(entities.indexOf(entity) >= 0) return true;
        return false;
    }

    public boolean isHidingSomeChildren(XModelObject object) {
        return false;
    }

}
