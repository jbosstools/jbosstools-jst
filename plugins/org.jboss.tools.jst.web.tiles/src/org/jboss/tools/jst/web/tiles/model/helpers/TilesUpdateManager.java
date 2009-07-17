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
package org.jboss.tools.jst.web.tiles.model.helpers;

import java.util.*;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.event.*;
import org.jboss.tools.jst.web.tiles.model.TilesConstants;

public class TilesUpdateManager implements XModelTreeListener, TilesConstants {

    public static synchronized TilesUpdateManager getInstance(XModel model) {
    	TilesUpdateManager instance = (TilesUpdateManager)model.getManager("org.jboss.tools.jst.web.tiles.model.helpers.TilesUpdateManager"); //$NON-NLS-1$
        if (instance == null) {
        	instance = new TilesUpdateManager();
        	model.addManager("org.jboss.tools.jst.web.tiles.model.helpers.TilesUpdateManager", instance); //$NON-NLS-1$
        	model.addModelTreeListener(instance);
        }
        return instance;
    }
    
    Set binds = new HashSet();

    public void register(String path, TilesUpdateHelper node) {
    	binds.add(node);
    }
    public void unregister(TilesUpdateHelper node) {
    	binds.remove(node);
    }

	public void nodeChanged(XModelTreeEvent event) {
		String entity = event.getModelObject().getModelEntity().getName();
		if(entity.equals(ENT_DEFINITION)) fireChange();
		else if(entity.startsWith("WebApp")) fireChange(); //$NON-NLS-1$
		
	}

	public void structureChanged(XModelTreeEvent event) {
		String sourceEntity = event.getModelObject().getModelEntity().getName();
		if(event.kind() == XModelTreeEvent.CHILD_REMOVED) {
			if(sourceEntity.equals("FileTiles")) fireChange(); //$NON-NLS-1$
			if(event.getInfo() instanceof XModelObject) {
				XModelObject o = (XModelObject)event.getInfo();
				String entity = o.getModelEntity().getName();
			}
		} else if(event.kind() == XModelTreeEvent.CHILD_ADDED) {
			XModelObject o = (XModelObject)event.getInfo();
			String entity = o.getModelEntity().getName();
			if(entity.equals(ENT_DEFINITION)) fireChange();
		}
		
	}
	
	void fireChange() {
		TilesUpdateHelper[] h = (TilesUpdateHelper[])binds.toArray(new TilesUpdateHelper[0]);
		for (int i = 0 ; i < h.length; i++) h[i].update();
	}

}
