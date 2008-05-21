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
import org.jboss.tools.common.model.ui.action.file.ProjectRootActionDelegate;

public class ServerXmlActionDelegate extends ProjectRootActionDelegate {
	protected String textTemplate = null;
	
	public void selectionChanged(IAction action, ISelection selection) {
		super.selectionChanged(action, selection);
		if(textTemplate == null) {
			textTemplate = action.getText();
		}
		if(textTemplate == null) return;
		String t = textTemplate;
		int i = t.indexOf("server.xml");
		if(i >= 0) {
			t = t.substring(0, i) + "Server" + t.substring(i + "server.xml".length());
			action.setText(t);
		}
	}

}
