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

import java.text.MessageFormat;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.jboss.tools.common.model.ui.action.file.ProjectRootActionDelegate;
import org.jboss.tools.jst.web.ui.Messages;

public class ServerXmlActionDelegate extends ProjectRootActionDelegate {
	protected String textTemplate = null;
	
	public void selectionChanged(IAction action, ISelection selection) {
		super.selectionChanged(action, selection);
		if(textTemplate == null) {
			textTemplate = action.getText();
		}
		if(textTemplate == null) return;
		String t = textTemplate;
		int i = t.indexOf("server.xml"); //$NON-NLS-1$
		if(i >= 0) {
			String prefix = t.substring(0, i);
			String suffix = t.substring(i + "server.xml".length()); //$NON-NLS-1$
			t = MessageFormat.format(Messages.ServerXmlActionDelegate_PrefixServerSuffix, prefix, suffix);
			action.setText(t);
		}
	}

}
