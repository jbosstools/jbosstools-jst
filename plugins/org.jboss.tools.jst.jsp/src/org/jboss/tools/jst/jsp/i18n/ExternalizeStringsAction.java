/*******************************************************************************
 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.i18n;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;

public class ExternalizeStringsAction extends Action {

	protected JSPMultiPageEditor editor;
	
	public ExternalizeStringsAction() {
		super();
		editor = (JSPMultiPageEditor) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	}

	@Override
	public void run() {
		ISelection sel = editor.getSelectionProvider().getSelection();
		if (ExternalizeStringsUtils.isSelectionCorrect(sel)) {
			/*
			 * Pass null for Bundle Map that it will be created by the page itself.
			 */
			ExternalizeStringsDialog dlg = new ExternalizeStringsDialog(
					PlatformUI.getWorkbench().getDisplay().getActiveShell(),
					new ExternalizeStringsWizard(editor.getSourceEditor(), null));
			dlg.open();
		} else {
			MessageDialog.openWarning(
					PlatformUI.getWorkbench().getDisplay().getActiveShell(),
					JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE,
					JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_WRONG_SELECTION);
		}
	}

}
