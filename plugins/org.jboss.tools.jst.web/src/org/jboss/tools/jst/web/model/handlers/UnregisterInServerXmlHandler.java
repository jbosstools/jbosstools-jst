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

import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.jboss.tools.common.meta.action.impl.AbstractHandler;
import org.jboss.tools.common.meta.action.impl.XActionImpl;
import org.jboss.tools.common.model.ServiceDialog;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.server.RegistrationHelper;

public class UnregisterInServerXmlHandler extends AbstractHandler {
	String textTemplate = null;

	public boolean isEnabled(XModelObject object) {
		if(textTemplate == null) {
			textTemplate = action.getDisplayName();
		}
		if(textTemplate != null) {
			String t = textTemplate;
			int i = t.indexOf("server.xml"); //$NON-NLS-1$
			if(i >= 0) {
				t = t.substring(0, i) + "Server"/*ServerXmlHelper.getDefaultServer(2)*/ + t.substring(i + "server.xml".length()); //$NON-NLS-1$ //$NON-NLS-2$
				((XActionImpl)action).setDisplayName(t);
			}
		}
		return object != null && isRegistered(object);
	}

    public boolean isEnabled(XModelObject object, XModelObject[] objects) {
        if(object != null && (objects == null || objects.length == 1)) return isEnabled(object);
        return false;
    }

    public void executeHandler(XModelObject object, Properties p) throws XModelException {
		String name = object.getAttributeValue("application name"); //$NON-NLS-1$
		if(p != null && "true".equals(p.getProperty("unregisterFromAllServers"))) { //$NON-NLS-1$ //$NON-NLS-2$
			unregisterFromAllServers(object);
			return;
		}
		boolean result = RegistrationHelper.unregister(EclipseResourceUtil.getProject(object));
		if(result) {
			ServiceDialog d = object.getModel().getService();
			String server = RegistrationHelper.getSelectedServer().getName();
			String mes = NLS.bind(WebUIMessages.APPLICATION_HAS_BEEN_UNREGISTERED_FROM, name, server);
			d.showDialog(WebUIMessages.MESSAGE, mes, new String[]{WebUIMessages.CLOSE}, null, ServiceDialog.MESSAGE);
		}
	}
	
	boolean isRegistered(XModelObject object) {
///		String name =  object.getAttributeValue("application name"); //$NON-NLS-1$
		return RegistrationHelper.isRegistered(EclipseResourceUtil.getProject(object));
	}
	
	void unregisterFromAllServers(XModelObject object) throws XModelException {
		IProject project = EclipseResourceUtil.getProject(object);
		IServer[] ss = ServerCore.getServers();
		for (int i = 0; i < ss.length; i++) {
			if(RegistrationHelper.isRegistered(project, ss[i])) {
				RegistrationHelper.unregister(project, ss[i]);
			}
		}
	}
	
}
