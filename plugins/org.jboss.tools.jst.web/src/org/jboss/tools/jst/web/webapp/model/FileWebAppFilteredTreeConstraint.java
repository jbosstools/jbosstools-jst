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

import org.jboss.tools.common.meta.XChild;
import org.jboss.tools.common.meta.XModelEntity;
import org.jboss.tools.common.model.XFilteredTreeConstraint;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;

public class FileWebAppFilteredTreeConstraint implements XFilteredTreeConstraint {

	public void update(XModel model) {		
	}

	public boolean isHidingAllChildren(XModelObject object) {
		return false;
	}
	
	static Properties checkedEntities = new Properties();
	
	boolean checkEntity(XModelEntity entity) {
		String res = checkedEntities.getProperty(entity.getName());
		if(res != null) return "true".equals(res); //$NON-NLS-1$
		XChild[] cs = entity.getChildren();
		for (int i = 0; i < cs.length; i++) {
			String n = cs[i].getName();
			if(n.startsWith("WebAppFolder")) { //$NON-NLS-1$
				checkedEntities.setProperty(n, "true"); //$NON-NLS-1$
				return true;
			}
		}
		checkedEntities.setProperty(entity.getName(), "false"); //$NON-NLS-1$
		return false;		
	}
	
	public boolean isHidingSomeChildren(XModelObject object) {
		///if(true) return false;
		return checkEntity(object.getModelEntity());
	}

	public boolean accepts(XModelObject object) {
		String entity = object.getModelEntity().getName();
		if(entity.startsWith("WebAppFolder") && object.getChildren().length == 0) return false; //$NON-NLS-1$
		return true;
	}

}
