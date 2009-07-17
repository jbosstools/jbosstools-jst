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
import org.jboss.tools.common.meta.action.impl.AbstractHandler;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.FindObjectHelper;
import org.jboss.tools.jst.web.model.helpers.WebProcessStructureHelper;

public class JumpToTransitionTargetHandler extends AbstractHandler {

    public JumpToTransitionTargetHandler() {}

    public boolean isEnabled(XModelObject object) {
    	if(object == null || !object.isActive()) return false;
    	String shortcut = object.getAttributeValue("shortcut"); //$NON-NLS-1$
    	if(shortcut == null) return false;
        return true;
    }

    public void executeHandler(XModelObject object, Properties p) throws XModelException {
    	p = new Properties();
    	p.put("object", object); //$NON-NLS-1$
    	p.put("help", "JSFProcess_SelectItem"); //$NON-NLS-1$ //$NON-NLS-2$
    	String s = object.getAttributeValue("target"); //$NON-NLS-1$
    	XModelObject process = new WebProcessStructureHelper().getParentProcess(object);
    	if(process == null) return;
    	XModelObject selected = process.getChildByPath(s);
    	if(selected == null) return;
        FindObjectHelper.findModelObject(selected, FindObjectHelper.IN_EDITOR_ONLY);
    }

}
