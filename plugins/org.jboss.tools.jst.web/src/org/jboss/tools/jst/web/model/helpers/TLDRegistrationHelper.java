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
package org.jboss.tools.jst.web.model.helpers;

import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.meta.action.impl.handlers.DefaultRemoveHandler;
import org.jboss.tools.common.model.*;
import org.jboss.tools.jst.web.project.WebProject;

public class TLDRegistrationHelper {

	public boolean isRegistered(XModel model, XModelObject file) {
		return getRegisteredObject(file) != null;
	}

	public void unregister(XModel model, String oldPath) {
		XModelObject t = getRegisteredObject(model, oldPath);
		if(t != null) {
			DefaultRemoveHandler.removeFromParent(t);
			XModelObject webxml = WebAppHelper.getWebApp(model);
			XActionInvoker.invoke("SaveActions.Save", webxml, null); //$NON-NLS-1$
		}
	}

	private XModelObject getRegisteredObject(XModelObject file) {
		if(file == null) return null;
		String path = WebProject.getInstance(file.getModel()).getPathInWebRoot(file);
		return getRegisteredObject(file.getModel(), path);
	}

	private XModelObject getRegisteredObject(XModel model, String path) {
		if(path == null) return null;
		XModelObject webxml = WebAppHelper.getWebApp(model);
		if(webxml == null) return null;
        XModelObject[] sz = WebAppHelper.getTaglibs(webxml);
        for (int i = 0; i < sz.length; i++) {
            String location = sz[i].getAttributeValue("taglib-location").replace('\\', '/'); //$NON-NLS-1$
            if(path.equalsIgnoreCase(location)) return sz[i];
        }
        return null;
	}
	

}
