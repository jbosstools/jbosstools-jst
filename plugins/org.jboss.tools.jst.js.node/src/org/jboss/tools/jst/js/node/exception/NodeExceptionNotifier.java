/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *       Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.jst.js.node.exception;

import java.text.MessageFormat;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.jboss.tools.jst.js.node.Messages;
import org.jboss.tools.jst.js.node.preference.NodePreferencePage;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class NodeExceptionNotifier {
	
	public static void nodeLocationNotDefined() {
		boolean define = MessageDialog.openQuestion(Display.getDefault().getActiveShell(),
				Messages.ErrorHandler_NodeNotDefinedTitle, Messages.ErrorHandler_NodeNotDefinedMessage);
		if (define) {
			PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(Display.getDefault().getActiveShell(),
					NodePreferencePage.PAGE_ID, null, null);
			dialog.open();
		}
	}

	public static void launchError(Exception e) {
		String message = MessageFormat.format(Messages.ErrorHandler_LaunchErrorMessage, e.getMessage());
		MessageDialog.openError(Display.getDefault().getActiveShell(), Messages.ErrorHandler_LaunchErrorTitle, message);
	}

}
