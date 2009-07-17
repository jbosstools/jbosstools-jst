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
package org.jboss.tools.jst.web.tiles.model;

import org.jboss.tools.common.model.*;

public class TilesConfigFilteredTreeConstraint implements XFilteredTreeConstraint, TilesConstants {

	public void update(XModel model) {		
	}
	
	public boolean isHidingAllChildren(XModelObject object) {
		return false;
	}
	
	static String HIDING_SOME_CHILDREN_ENTITIES = "." + ENT_FILE + ".";  //$NON-NLS-1$ //$NON-NLS-2$

	public boolean isHidingSomeChildren(XModelObject object) {
		String entity = object.getModelEntity().getName();
		return (HIDING_SOME_CHILDREN_ENTITIES.indexOf("." + entity + ".") >= 0); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	static String HIDDEN_CHILDREN_ENTITIES = "." + ENT_PROCESS + "."; //$NON-NLS-1$ //$NON-NLS-2$

	public boolean accepts(XModelObject object) {
		String entity = object.getModelEntity().getName();
		if(HIDDEN_CHILDREN_ENTITIES.indexOf("." + entity + ".") >= 0) return false; //$NON-NLS-1$ //$NON-NLS-2$
		return true;
	}

}
