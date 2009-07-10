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
package org.jboss.tools.jst.web.ui.attribute.adapter;

import org.jboss.tools.common.model.*;
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;

import org.jboss.tools.common.model.ui.attribute.adapter.DefaultXAttributeListContentProvider;

public class WebRoleListContentProvider extends DefaultXAttributeListContentProvider {
	private XModel model;
//	private boolean emptyChoice = false;
//	private boolean writeOnly = false;
	
	public void setModel(XModel model) {
		this.model = model;
	}

	protected void loadTags() {
		XModelObject webxml = WebAppHelper.getWebApp(model);
		if(webxml == null) return;
		XModelObject[] os = WebAppHelper.getRoles(webxml);
		tags = new String[os.length];
		for (int i = 0; i < tags.length; i++) tags[i] = os[i].getAttributeValue("role-name"); //$NON-NLS-1$
	}

}
