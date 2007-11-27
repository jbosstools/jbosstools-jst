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

import org.jboss.tools.common.meta.action.SpecialWizard;
import org.jboss.tools.common.meta.action.SpecialWizardFactory;
import org.jboss.tools.common.meta.action.impl.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.model.helpers.WebProcessStructureHelper;

public class FindReferringItemHandler extends AbstractHandler {
	WebProcessStructureHelper helper = new WebProcessStructureHelper();

	public boolean isEnabled(XModelObject object) {
		return (object != null && object.isActive());
	}

	public void executeHandler(XModelObject object, Properties p) throws Exception {
		if(!isEnabled(object)) return;
		XModelObject[] os = getReferringObjects(object);
		if(os.length == 0) {
			ServiceDialog d = object.getModel().getService();
			d.showDialog(WebUIMessages.WARNING, WebUIMessages.ITEM_ISNOT_REFERENCED, new String[]{WebUIMessages.OK, WebUIMessages.CANCEL}, null, ServiceDialog.WARNING);
			return;
		} else if(os.length == 1) {
			selectObject(os[0]);
		} else {
	    	p = new Properties();
	    	p.put("object", object); //$NON-NLS-1$
	    	String help = action.getProperty("help"); //$NON-NLS-1$
	    	p.put("help", help); //$NON-NLS-1$
	    	p.put("items", os); //$NON-NLS-1$
	    	String wizard = action.getProperty("wizard"); //$NON-NLS-1$
	        SpecialWizard sw = SpecialWizardFactory.createSpecialWizard(wizard);
	        sw.setObject(p);
	        int i = sw.execute();
	        XModelObject selected = (i != 0) ? null : (XModelObject)p.get("selected"); //$NON-NLS-1$
	        selectObject(selected);
		}
	}
	
	public void selectObject(XModelObject selected) {
		if(selected == null) return;
		XModelObject process = helper.getProcess(selected);
		XModelObject parent = selected.getParent();
		if(parent != process) selectObject(parent);
		FindItemOnDiagramHandler.selectInEditor(selected);
	}
	
	private XModelObject[] getReferringObjects(XModelObject object) {
		XModelObject process = helper.getParentProcess(object);
		if(process == null) return new XModelObject[0];
		List<XModelObject> list = getTargetReferers(process, object.getPathPart());
		return list.toArray(new XModelObject[0]);
	}

    public List<XModelObject> getTargetReferers(XModelObject root, String target) {
        List<XModelObject> list = new ArrayList<XModelObject>();
        fillTargetReferers(list, root, target);
        return list;
    }

    private void fillTargetReferers(List<XModelObject> list, XModelObject obj, String target) {
        XModelObject[] children = obj.getChildren();
        for (int i = 0; i < children.length; i++) {
            if (target.equals(children[i].getAttributeValue("target"))) list.add(children[i]); //$NON-NLS-1$
            fillTargetReferers(list, children[i], target);
        }
    }
}
