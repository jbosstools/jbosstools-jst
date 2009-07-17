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
package org.jboss.tools.jst.web.ui.navigator;

import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.navigator.CommonDropAdapter;
import org.eclipse.ui.navigator.CommonDropAdapterAssistant;
import org.eclipse.ui.views.navigator.LocalSelectionTransfer;
import org.jboss.tools.common.model.XModelBuffer;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.event.ActionDeclinedException;
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.common.model.ui.dnd.DnDUtil;
import org.jboss.tools.common.reporting.ProblemReportingHelper;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

public class XDropAdapterAssistant extends CommonDropAdapterAssistant {

	@Override
	public IStatus handleDrop(CommonDropAdapter aDropAdapter, DropTargetEvent aDropTargetEvent, Object target) {
		if(!(target instanceof XModelObject)) return Status.CANCEL_STATUS;
		XModelObject targetObject = (XModelObject)target;

		ISelection s = LocalSelectionTransfer.getTransfer().getSelection();
		if(s == null || s.isEmpty() || !(s instanceof IStructuredSelection)) return Status.CANCEL_STATUS;
		IStructuredSelection ss = (IStructuredSelection)s;

		Object o1 = ss.getFirstElement();
		if(!(o1 instanceof XModelObject)) return Status.CANCEL_STATUS;
		
		Properties p = new Properties();
		p.setProperty("isDrop", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		if(DnDUtil.isPasteEnabled(targetObject)) {
			try {
				DnDUtil.paste(targetObject, p);
			} catch (ActionDeclinedException de) {
				return null;
			} catch (XModelException e) {
				ProblemReportingHelper.reportProblem(ModelUIPlugin.PLUGIN_ID, e);
			}
		}
		return null;
	}

	@Override
	public IStatus validateDrop(Object target, int operation, TransferData transferType) {
		if(!(target instanceof XModelObject)) return Status.CANCEL_STATUS;
		XModelObject targetObject = (XModelObject)target;
		if(isSupportedType(transferType)) {
			ISelection s = LocalSelectionTransfer.getTransfer().getSelection();
			if(s == null || s.isEmpty() || !(s instanceof IStructuredSelection)) return Status.CANCEL_STATUS;
			IStructuredSelection ss = (IStructuredSelection)s;
			Object o1 = ss.getFirstElement();
			if(!(o1 instanceof XModelObject)) return Status.CANCEL_STATUS;
			XModelObject source = (XModelObject)o1;
			if(DnDUtil.getEnabledCopyAction(source, null) == null) {
				return Status.CANCEL_STATUS;
			};
			XModelBuffer b = source.getModel().getModelBuffer();
			b.clear();
			b.addSource(source);
			if(DnDUtil.getEnabledPasteAction(targetObject) == null) {
				return Status.CANCEL_STATUS;
			}
			return Status.OK_STATUS;
		}
		return Status.CANCEL_STATUS;
	}

}
