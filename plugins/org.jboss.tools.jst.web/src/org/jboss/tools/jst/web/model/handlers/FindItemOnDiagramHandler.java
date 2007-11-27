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
import org.jboss.tools.common.model.util.FindObjectHelper;
import org.jboss.tools.common.meta.action.*;
import org.jboss.tools.common.meta.action.impl.*;
import org.jboss.tools.jst.web.model.ReferenceObject;

public class FindItemOnDiagramHandler extends AbstractHandler {

    public FindItemOnDiagramHandler() {}

    public boolean isEnabled(XModelObject object) {
        return (object != null && object.isActive());
    }

    public void executeHandler(XModelObject object, Properties p) throws Exception {
    	p = new Properties();
    	p.put("object", object);
    	String help = action.getProperty("help");
    	//"JSFProcess_SelectItem"
    	p.put("help", help);
    	String wizard = action.getProperty("wizard");
    	//"org.jboss.tools.jsf.ui.editor.wizard.SelectItemWizard"
        SpecialWizard sw = SpecialWizardFactory.createSpecialWizard(wizard);
        sw.setObject(p);
        int i = sw.execute();
        if(i != 0) return;
        XModelObject selected = (XModelObject)p.get("selected");
        if(selected == null) return;
        if(selected instanceof ReferenceObject) {
        	XModelObject r = ((ReferenceObject)selected).getReference();
        	if(r != null) FindObjectHelper.findModelObject(r, FindObjectHelper.IN_EDITOR_ONLY);
        }
        selectInEditor(selected);
    }
    
    public static void selectInEditor(XModelObject selected) {
        if(selected == null) return;
        FindObjectHelper.findModelObject(selected, FindObjectHelper.IN_EDITOR_ONLY);
    }

}
