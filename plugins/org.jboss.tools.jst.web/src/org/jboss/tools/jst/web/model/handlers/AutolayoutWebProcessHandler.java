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
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.impl.XModelImpl;
import org.jboss.tools.common.model.util.ClassLoaderUtil;
import org.jboss.tools.common.model.util.ModelFeatureFactory;
import org.jboss.tools.common.meta.action.impl.*;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.model.helpers.WebProcessStructureHelper;
import org.jboss.tools.jst.web.model.helpers.autolayout.AutoLayout;
import org.jboss.tools.jst.web.model.helpers.autolayout.Items;

public class AutolayoutWebProcessHandler extends AbstractHandler {

    public AutolayoutWebProcessHandler() {}

    public boolean isEnabled(XModelObject object) {
        return (object != null || object.isObjectEditable());
    }

    public void executeHandler(XModelObject object, Properties p) throws Exception {
        if(!isEnabled(object)) return;
        ServiceDialog d = object.getModel().getService();
        String mes = WebUIMessages.YOU_WANT_TO_REARRANGE_THE_DIAGRAM_ELEMENTS;
        int i = d.showDialog(action.getDisplayName(), mes, new String[]{WebUIMessages.OK, WebUIMessages.CANCEL}, null, ServiceDialog.QUESTION);
        if(i != 0) return;
        AutoLayout a = new AutoLayout();
        a.setItems(getItemsInstance());
        a.setOverride(true);
		WebProcessStructureHelper h = new WebProcessStructureHelper();
		h.setNodeChangeListenerLock(object, true);
		long ts = object.getTimeStamp();
		try {
    	    a.setProcess(object);
		} finally {
			h.setNodeChangeListenerLock(object, false);
			if(object.getTimeStamp() != ts) {
				object.setModified(true);
				((XModelImpl)object.getModel()).fireStructureChanged(object);
			}
		}
    }
    
    protected Items getItemsInstance() throws Exception {
    	String clsname = action.getProperty("items-class"); //$NON-NLS-1$
    	return (Items)ModelFeatureFactory.getInstance().createFeatureInstance(clsname);
    }

}
