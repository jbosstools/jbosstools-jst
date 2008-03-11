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
package org.jboss.tools.jst.jsp.ui.action;

import org.jboss.tools.jst.jsp.jspeditor.JSPTextEditor;

public class ExtendedFormatAction implements IExtendedAction {
	JSPTextEditor editor;
	String actionID;
	
	public ExtendedFormatAction(JSPTextEditor editor, String actionID) {
		this.editor = editor;
		this.actionID = actionID;
	}

	public void preRun() {
//		editor.getVPEController().preAction(actionID);
	}

	public void postRun() {
//		editor.getVPEController().postAction();
	}

}
