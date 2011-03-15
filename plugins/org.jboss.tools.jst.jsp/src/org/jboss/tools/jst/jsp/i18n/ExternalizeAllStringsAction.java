/*******************************************************************************
 * Copyright (c) 2007-2011 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.i18n;

import org.eclipse.ui.PlatformUI;

public class ExternalizeAllStringsAction extends ExternalizeStringsAction {

	@Override
	public void run() {
		/*
		 * Pass null for Bundle Map that it will be created by the page itself.
		 */
		ExternalizeStringsDialog dlg = new ExternalizeStringsDialog(
				PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				new ExternalizeAllStringsWizard(editor.getSourceEditor(), null));
		dlg.open();
	}
}
