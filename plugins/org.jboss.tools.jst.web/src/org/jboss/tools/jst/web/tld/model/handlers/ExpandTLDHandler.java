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
package org.jboss.tools.jst.web.tld.model.handlers;

import java.util.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.impl.*;
import org.jboss.tools.common.meta.XModelEntity;
import org.jboss.tools.common.meta.action.XAction;
import org.jboss.tools.common.meta.action.impl.*;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;

public class ExpandTLDHandler extends AbstractHandler {

    public ExpandTLDHandler() {}

    public boolean isEnabled(XModelObject object) {
        if("yes".equals(object.get("isIncorrect"))) return false; //$NON-NLS-1$ //$NON-NLS-2$
        String d = ("true".equals(object.get("expanded"))) ? WebUIMessages.ExpandTLDHandler_Collapse : WebUIMessages.ExpandTLDHandler_Expand; //$NON-NLS-1$ //$NON-NLS-2$
        ((XActionImpl)action).setDisplayName(d);
        return true;
    }

    protected boolean ignoreDifferentParents(String entity1, String entity2) {
        return true;
    }

    public void executeHandler(XModelObject object, Properties p) throws XModelException {
        if(!isEnabled(object)) return;
        String v = ("true".equals(object.get("expanded"))) ? "false" : "true"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        object.setAttributeValue("expanded", v); //$NON-NLS-1$
        XModelImpl m = (XModelImpl)object.getModel();
        m.fireStructureChanged(object);
    }

    public void executeHandler(XModelObject object, XModelObject[] objects, java.util.Properties p) throws XModelException {
        if(!isEnabled(object, objects)) return;
        if(object == null || objects == null || objects.length < 2) {
            executeHandler(object, p);
            return;
        }
        
        boolean leadingObjectExpanded = "true".equals(object.get("expanded")); //$NON-NLS-1$ //$NON-NLS-2$
        
        String path = action.getPath();
        XModelEntity entity = object.getModelEntity();
        for (int i = 0; i < objects.length; i++) {
            if(leadingObjectExpanded != "true".equals(objects[i].get("expanded"))) { //$NON-NLS-1$ //$NON-NLS-2$
            	continue;
            }
            XModelEntity ent = objects[i].getModelEntity();
            if(ent == entity) {
                executeHandler(objects[i], p);
                continue;
            }
            XAction a = (XAction)((XActionListImpl) ent.getActionList()).getByPath(path);
            if(a == null || !a.isEnabled(objects[i])) continue;
            
            mergeEntityData(data, a.getEntityData(objects[i]));
            a.executeHandler(objects[i], p);
        }
    }

}
