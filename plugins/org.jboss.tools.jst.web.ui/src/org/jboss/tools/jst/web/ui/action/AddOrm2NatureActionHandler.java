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
package org.jboss.tools.jst.web.ui.action;

import java.util.Properties;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.*;
import org.eclipse.ui.*;
import org.osgi.framework.Bundle;
import org.jboss.tools.common.meta.action.impl.*;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.ui.Messages;

public class AddOrm2NatureActionHandler extends AbstractHandler {
	static final String PLUGIN_ID = "TODO" ; //$NON-NLS-1$
	static final String ORM2NATURE_ID = "TODO"; //$NON-NLS-1$
	
	static final String ADD_CLASS_NAME = "TODO.view.AddOrmNatureActionDelegate"; //$NON-NLS-1$
	static final String REMOVE_CLASS_NAME = "TODO.view.RemoveOrmNatureActionDelegate"; //$NON-NLS-1$
	
	public AddOrm2NatureActionHandler() {}

    public boolean isEnabled(XModelObject object) {
    	if(object == null || EclipseResourceUtil.getProject(object) == null || Platform.getBundle(PLUGIN_ID) == null) return false;
    	boolean hasNature = EclipseResourceUtil.hasNature(object.getModel(), ORM2NATURE_ID);
    	String displayName = hasNature ? Messages.AddOrm2NatureActionHandler_Remove : Messages.AddOrm2NatureActionHandler_Add;
    	((XActionImpl)action).setDisplayName(displayName);
        return true;  
    }
    
    public boolean isEnabled(XModelObject object, XModelObject[] objects) {
        if(object != null && (objects == null || objects.length == 1)) return isEnabled(object);
        return false;
    }

    String getClassName(XModelObject object) {
    	boolean hasNature = EclipseResourceUtil.hasNature(object.getModel(), ORM2NATURE_ID);
    	return hasNature ? REMOVE_CLASS_NAME : ADD_CLASS_NAME;
    }

    public void executeHandler(XModelObject object, Properties p) throws XModelException {
    	Bundle b = Platform.getBundle(PLUGIN_ID);
    	if(b == null) return;
    	
    	Class c = null;
    	try {
    		c = b.loadClass(getClassName(object));
    	} catch (ClassNotFoundException e) {
    		throw new XModelException(e);
    	}
    	IObjectActionDelegate actionDelegate = null;
    	try {
    		actionDelegate = (IObjectActionDelegate)c.newInstance();
    	} catch (InstantiationException e1) {
    		throw new XModelException(e1);
    	} catch (IllegalAccessException e2) {
    		throw new XModelException(e2);
    	}
    	Action action = new Action() {};
    	action.setEnabled(true);
    	IWorkbenchPart part = ModelPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getPartService().getActivePart();
    	actionDelegate.setActivePart(action, part);
    	ISelection selection = new StructuredSelection(EclipseResourceUtil.getProject(object));
    	actionDelegate.selectionChanged(action, selection);
    	actionDelegate.run(action);    	
    }

}
