/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.attribute.adapter;

import org.jboss.tools.common.model.*;
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;

import org.jboss.tools.common.model.ui.attribute.adapter.DefaultXAttributeListContentProvider;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class ServletNameListContentProvider extends DefaultXAttributeListContentProvider {
	private XModel model;
	private XModelObject object;
	
	public void setModel(XModel model, XModelObject context) {
		this.model = model;
		object = context;
	}

	protected void loadTags() {
		XModelObject webxml = null;
		if(object != null) {
			XModelObject f = object;
			while(f != null && f.getFileType() != XModelObject.FILE) f = f.getParent();
			if(f != null) webxml = f;
		}
		if(webxml == null) webxml = WebAppHelper.getWebApp(model);
		if(webxml == null) return;
		XModelObject[] os = WebAppHelper.getServlets(webxml);
		tags = new String[os.length];
		for (int i = 0; i < tags.length; i++) tags[i] = os[i].getAttributeValue("servlet-name"); //$NON-NLS-1$
	}

}
