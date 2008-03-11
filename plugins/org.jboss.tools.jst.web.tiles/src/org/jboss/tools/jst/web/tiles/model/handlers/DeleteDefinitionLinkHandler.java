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
package org.jboss.tools.jst.web.tiles.model.handlers;

import java.util.Properties;

import org.jboss.tools.common.meta.action.impl.AbstractHandler;
import org.jboss.tools.common.model.ServiceDialog;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;

public class DeleteDefinitionLinkHandler extends AbstractHandler {

    public void executeHandler(XModelObject object, Properties p) throws Exception {
    	String s = object.getAttributeValue("extends"); //$NON-NLS-1$
        if(s == null || s.length() == 0) return;
        ServiceDialog d = object.getModel().getService();
        int i = d.showDialog(WebUIMessages.CONFIRMATION, WebUIMessages.YOU_WANT_TO_DELETE_LINK_TO + s, new String[]{WebUIMessages.OK, WebUIMessages.CANCEL}, null, ServiceDialog.QUESTION);
        if(i != 0) return;
    	object.getModel().editObjectAttribute(object, "extends", ""); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public boolean isEnabled(XModelObject object) {
    	if(object == null || !object.isObjectEditable()) return false;
    	String s = object.getAttributeValue("extends"); //$NON-NLS-1$
        return s != null && s.length() > 0;
    }

}
