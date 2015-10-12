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
package org.jboss.tools.jst.js.npm.internal.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.jboss.tools.jst.js.npm.internal.Messages;
import org.jboss.tools.jst.js.npm.internal.preference.NpmPreferencePage;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class NpmExceptionNotifier {

	public static void npmLocationNotDefined() {
		boolean define = MessageDialog.openQuestion(Display.getDefault().getActiveShell(),
				Messages.NpmErrorHandler_NpmNotDefinedTitle, Messages.NpmErrorHandler_NpmNotDefinedMessage);
		if (define) {
			PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(Display.getDefault().getActiveShell(),
					NpmPreferencePage.PAGE_ID, null, null);
			dialog.open();
		}
	}

}
