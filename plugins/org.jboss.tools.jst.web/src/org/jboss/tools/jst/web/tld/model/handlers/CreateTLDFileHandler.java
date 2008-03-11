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

import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.impl.CreateFileHandler;

public class CreateTLDFileHandler extends CreateFileHandler {

	protected XModelObject modifyCreatedObject(XModelObject o) {
/*
		String entity = getChildTagEntity(o.getModelEntity());
		XModelObject t = o.getModel().createModelObject(entity, null);
		t.setAttributeValue("name", "EnterNewTag");
		o.addChild(t);
*/
		return o;
	}
	
/*
	private String getChildTagEntity(XModelEntity entity) {
		XChild[] cs = entity.getChildren();
		for (int i = 0; i < cs.length; i++) {
			if(cs[i].getName().startsWith("TLDTag")) return cs[i].getName();
		}
		return null;
	}
*/

}
