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
package org.jboss.tools.jst.web.ui.action.server;

import org.eclipse.jface.action.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.ui.*;
import org.jboss.tools.common.meta.action.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

import org.eclipse.core.resources.IResource;

public class ChangeTimeStampActionDelegate implements IWorkbenchWindowActionDelegate {
	protected XModelObject object = null;
	protected IWorkbenchWindow window;
	String tooltip = null;

	protected String getActionPath() {
		return "SaveActions.ChangeTimeStamp";  //$NON-NLS-1$
	}
		
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	public void selectionChanged(IAction action, ISelection selection) {
		if(object == null && action.isEnabled()) action.setEnabled(false);
		if(tooltip == null) tooltip = action.getToolTipText();
		if(!(selection instanceof IStructuredSelection)) return;
		Object o = ((IStructuredSelection)selection).getFirstElement();
		XModelObject adapter = (o instanceof XModelObject) ? (XModelObject)o : null;
		if(adapter == null) return;
		object = adapter;
		action.setEnabled(computeEnabled());
		if(object == null) {
			action.setToolTipText(tooltip);	
		} else {
			IResource r = EclipseResourceUtil.getResource(object);
			if(r != null && r.exists()) action.setToolTipText(WebUIMessages.CHANGE_TIME_STAMP + r.getLocation().toString());
		}
	}
	
	protected boolean computeEnabled() {
		object = (object == null) ? null : WebAppHelper.getWebApp(object.getModel());
		return object != null;
	}

	public void run(IAction action) {
		try {
			doRun();
		} catch (Exception e) {
			WebUiPlugin.getPluginLog().logError(e);
		}
	}
	
	protected void doRun() throws Exception {
		XActionInvoker.invoke(getActionPath(), object, null);
	}

	public void dispose() {}
	
}
