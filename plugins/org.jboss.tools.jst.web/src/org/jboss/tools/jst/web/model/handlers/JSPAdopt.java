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
package org.jboss.tools.jst.web.model.handlers;

import java.util.*;
import org.jboss.tools.common.meta.XAdoptManager;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;

public class JSPAdopt implements XAdoptManager {

	public boolean isAdoptable(XModelObject target, XModelObject object) {
		if(!isAcceptableTarget(target)) return false;
		return isAdoptableBundle(object);
	}

	public void adopt(XModelObject target, XModelObject object, java.util.Properties p) {
		if(!isAcceptableTarget(target)) return;
		if(isAdoptableBundle(object)) adoptBundle(target, object, p);
	}
	
	static String PAGE = ".FileJSP.FileHTML.";

	private boolean isAcceptableTarget(XModelObject target) {
		String entity = "." + target.getModelEntity().getName() + ".";
		return PAGE.indexOf(entity) >= 0;
	}

	protected boolean isAdoptableBundle(XModelObject object) {
		return "FilePROPERTIES".equals(object.getModelEntity().getName());
	}
    
	public void adoptBundle(XModelObject target, XModelObject object, Properties p) {
		if(p == null) return;
		String res = XModelObjectLoaderUtil.getResourcePath(object);
		if(res == null || !res.endsWith(".properties")) res = object.getPresentationString();
		else res = res.substring(1, res.length() - 11).replace('/', '.');
		p.setProperty("start text", res);
		p.setProperty("end text", "");
	}
}
