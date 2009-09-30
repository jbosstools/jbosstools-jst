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
package org.jboss.tools.jst.web.tld.model.handlers;

import java.util.Properties;

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.meta.*;
import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.meta.action.impl.handlers.*;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.tld.model.*;
import org.jboss.tools.jst.web.tld.model.helpers.*;

public class PaletteAdopt implements XAdoptManager {
    TLDToPaletteHelper helper = new TLDToPaletteHelper();

    public PaletteAdopt() {}

    public boolean isAdoptable(XModelObject target, XModelObject object) {
        return isAdoptableTag(target, object) 
        	|| isAdoptableTaglib(target, object)
        	|| isAdoptableFaceletTaglib(target, object);
    }

    public void adopt(XModelObject target, XModelObject object, java.util.Properties p) throws XModelException {
        if(isAdoptableTag(target, object)) adoptTag(target, object);
        else if(isAdoptableTaglib(target, object)) adoptTaglib(target, object);
        else if(isAdoptableFaceletTaglib(target, object)) adoptFaceletTaglib(target, object);
    }

    protected boolean isAdoptableTag(XModelObject target, XModelObject object) {
        if(!TLDUtil.isTag(object)) return false;
        if(!isPaletteObject(target, true)) return false;
        return true;
    }

    private boolean isPaletteObject(XModelObject target, boolean excludeRoot) {
    	String te = target.getModelEntity().getName();
    	if(te.startsWith("SharablePageTab")) return true; //$NON-NLS-1$
    	if(te.startsWith("SharableGroup")) return true; //$NON-NLS-1$
    	if(te.startsWith("SharableMacro")) return true; //$NON-NLS-1$
    	if(!excludeRoot && te.startsWith("SharablePalette")) return true; //$NON-NLS-1$
    	return false;
    }
    protected boolean isAdoptableTaglib(XModelObject target, XModelObject object) {
        if(!TLDUtil.isTaglib(object)) return false;
        if(!isPaletteObject(target, false)) return false;
        return true;
    }

    protected boolean isAdoptableFaceletTaglib(XModelObject target, XModelObject object) {
    	if(!TLDUtil.isFaceletTaglib(object)) return false;
        if(!isPaletteObject(target, false)) return false;
        return true;
    }

    public void adoptTag(XModelObject target, XModelObject object) throws XModelException {
    	if(target.getModelEntity().getName().startsWith("SharableMacro")) target = target.getParent(); //$NON-NLS-1$
        add(target, object, helper.createMacroByTag(object, target.getModel()));
    }

    public void adoptTaglib(XModelObject target, XModelObject object) {
    	if(target.getModelEntity().getName().startsWith("SharablePalette")) { //$NON-NLS-1$
    		adoptTaglib2(target, object);
    	} else {
    		target = getTab(target);
    		if(target != null) {
        		adoptTaglib2(target, object);
    		}
    	}
    }
    
    public void adoptTaglib2(XModelObject target, XModelObject object) {
    	XModelObject paletteRoot = target;
    	while(paletteRoot != null && !paletteRoot.getModelEntity().getName().startsWith("SharablePalette")) { //$NON-NLS-1$
    		paletteRoot = paletteRoot.getParent();
    	}
    	if(paletteRoot == null) return;
    	Properties p = new Properties();
    	p.put("initialSelection", object); //$NON-NLS-1$
    	if(paletteRoot != target) {
    		p.put("target", target); //$NON-NLS-1$
    	}
		XActionInvoker.invoke("ImportTLDToPaletteWizard", "CreateActions.ImportTLD", paletteRoot, p); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public void adoptFaceletTaglib(XModelObject target, XModelObject object) {
    	adoptTaglib(target, object);
    }

    static void add(XModelObject target, XModelObject object, XModelObject created) throws XModelException {
        if(created == null) return;
        XModelObject old = target.getChildByPath(created.getPathPart());
        if(old != null) {
            ServiceDialog d = object.getModel().getService();
            String mes = DefaultCreateHandler.title(target, true) + " contains " + DefaultCreateHandler.title(old, false); //$NON-NLS-1$
            d.showDialog(WebUIMessages.WARNING, mes, new String[]{WebUIMessages.OK}, null, ServiceDialog.WARNING);
        } else {
            DefaultCreateHandler.addCreatedObject(target, created, -1);
        }
    }

    private XModelObject getTab(XModelObject target) {
        while(target != null && !target.getModelEntity().getName().equals("SharablePageTabHTML")) target = target.getParent(); //$NON-NLS-1$
        return target;
    }

}
