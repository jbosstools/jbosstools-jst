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

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.ui.internal.view.servers.StopAction;
import org.jboss.tools.common.model.ServiceDialog;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.server.ServerManager;

public class StopServerActionDelegate extends AbstractServerActionDelegate {
	protected StopAction delegate;

	public void init(IWorkbenchWindow window) {
		super.init(window);
		update();
		delegate = new StopAction(window.getShell(), getSelectionProvider());
	}

	protected void doRun() {
		IServer server = ServerManager.getInstance().getSelectedServer();
		if(server == null) return;
		if( delegate.accept(server))
			delegate.perform(server);
	}

	protected boolean isActionEnabled() {
		IServer selected = ServerManager.getInstance().getSelectedServer();
		return (selected != null && 
				(selected.getServerState() == IServer.STATE_STARTED
				|| selected.getServerState() == IServer.STATE_STARTING));
	}	
		
	protected String computeToolTip() {
		IServer selected = ServerManager.getInstance().getSelectedServer();
		String name = selected == null ? "" : selected.getName(); //$NON-NLS-1$
		return WebUIMessages.STOP + name;
	}	

}
