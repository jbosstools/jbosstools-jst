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

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.model.ui.action.AbstractModelActionDelegate;

public class SaveProjectAsTemplateActionDelegate extends AbstractModelActionDelegate {

	protected boolean computeEnabled() {
		if(object == null) return false;
		return true;
	}

	protected void doRun() {
		XActionInvoker.invoke("WebWorkspace", "SaveAsTemplate", object, null); //$NON-NLS-1$ //$NON-NLS-2$
	}

	protected void safeSelectionChanged(IAction action, ISelection selection) {
		if(selection == null || selection.isEmpty() 
				|| (selection instanceof IStructuredSelection && ((IStructuredSelection)selection).size() > 1)) {
			object = null;
			action.setEnabled(computeEnabled());
		} else {
			super.safeSelectionChanged(action, selection);
		}
	}

}
