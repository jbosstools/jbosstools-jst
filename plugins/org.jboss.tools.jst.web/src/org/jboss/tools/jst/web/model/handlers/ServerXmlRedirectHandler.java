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

import org.jboss.tools.common.meta.action.impl.XActionImpl;
import org.jboss.tools.common.meta.action.impl.handlers.DefaultRedirectHandler;
import org.jboss.tools.common.model.XModelObject;

public class ServerXmlRedirectHandler extends DefaultRedirectHandler {
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
		return super.isEnabled(object);
	}

	protected XModelObject getTrueSource(XModelObject source) {
		return source.getModel().getByPath("FileSystems"); //$NON-NLS-1$
	}

}
