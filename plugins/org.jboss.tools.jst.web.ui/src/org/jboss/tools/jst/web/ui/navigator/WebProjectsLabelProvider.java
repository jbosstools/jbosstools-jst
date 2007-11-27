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
package org.jboss.tools.jst.web.ui.navigator;

import org.jboss.tools.common.model.ui.navigator.NavigatorLabelProvider;

import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.*;

public class WebProjectsLabelProvider extends NavigatorLabelProvider {
	
	public String getText(Object element) {
		if(!(element instanceof XModelObject)) return "";
		XModelObject o = (XModelObject)element;
		if(o.getFileType() != XModelObject.FILE) return super.getText(element);
		String entity = o.getModelEntity().getName();
		if("FilePROPERTIES".equals(entity)) {
			XModelObject fs = o;
			while(fs != null && fs.getFileType() != XModelObject.SYSTEM) fs = fs.getParent();
			if(fs == null || !fs.getAttributeValue("name").startsWith("src")) return super.getText(element);
			String res = XModelObjectLoaderUtil.getResourcePath(o);
			if(res == null || !res.endsWith(".properties")) return super.getText(element);
			res = res.substring(1, res.length() - 11).replace('/', '.');
			return applyModification(o, res);
		} else if("FileSystemFolder".equals(entity)) {
			String s = o.getAttributeValue("location").replace('\\', '/');
			return s.substring(s.lastIndexOf('/') + 1);			
		}		
		return super.getText(element);
	}
	
}
