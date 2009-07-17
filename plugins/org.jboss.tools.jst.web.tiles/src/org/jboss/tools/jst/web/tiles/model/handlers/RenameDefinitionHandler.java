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
package org.jboss.tools.jst.web.tiles.model.handlers;

import java.util.*;
import org.jboss.tools.common.meta.action.impl.handlers.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.jst.web.tiles.model.helpers.TilesDefinitionSet;

public class RenameDefinitionHandler extends DefaultEditHandler {

    public boolean isEnabled(XModelObject object) {
        return object != null && object.isObjectEditable();
    }
    
    public boolean isEnabled(XModelObject object, XModelObject[] objects) {
    	if(objects != null && objects.length > 1) return false;
    	return true;
    }

    public void executeHandler(XModelObject object, Properties p) throws XModelException {
    	if(!isEnabled(object)) return;
    	int q = DefaultRenameSupport.run(object, data[0], p);
    	if(q != 0) return;
    	p = DefaultCreateHandler.extractProperties(data[0]);
    	boolean updateReferences = "true".equals(p.getProperty("update references")); //$NON-NLS-1$ //$NON-NLS-2$
    	String name = p.getProperty("name"); //$NON-NLS-1$
    	String oldName = object.getAttributeValue("name"); //$NON-NLS-1$
    	XModel model = object.getModel();
    	Map map = TilesDefinitionSet.getInstance(model).getDefinitions();
    	XModelObject[] ds = (XModelObject[])map.values().toArray(new XModelObject[0]);
    	model.editObjectAttribute(object, "name", name); //$NON-NLS-1$
    	if(updateReferences) {
    		for (int i = 0; i < ds.length; i++) {
    			if(!ds[i].isObjectEditable()) continue;
    			String ext = ds[i].getAttributeValue("extends"); //$NON-NLS-1$
    			if(!oldName.equals(ext)) continue;
    			model.editObjectAttribute(ds[i], "extends", name); //$NON-NLS-1$
    		}
    	}
    }

}
