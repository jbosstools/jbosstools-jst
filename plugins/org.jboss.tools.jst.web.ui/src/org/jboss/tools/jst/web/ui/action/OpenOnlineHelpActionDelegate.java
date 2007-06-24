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
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import org.jboss.tools.common.reporting.ProblemReportingHelper;
import org.jboss.tools.jst.web.ui.BrowserView;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

/**
 * @author Igels
 */
public class OpenOnlineHelpActionDelegate implements IWorkbenchWindowActionDelegate {

	/**
     * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
     */
    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
    }

    public void run(IAction action) {
		try {
			WebUiPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(BrowserView.ID);
		} catch (Exception e) {
			ProblemReportingHelper.reportProblem(WebUiPlugin.PLUGIN_ID, e);
		}
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

}