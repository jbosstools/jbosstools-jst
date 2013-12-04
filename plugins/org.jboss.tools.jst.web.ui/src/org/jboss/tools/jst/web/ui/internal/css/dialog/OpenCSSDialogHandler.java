/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.internal.css.dialog;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;

public class OpenCSSDialogHandler extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IFile file = null;
		
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		
		IEditorPart targetEditor = HandlerUtil.getActiveEditor(event);
		if(targetEditor.getEditorInput() instanceof FileEditorInput){
			file = ((FileEditorInput) targetEditor.getEditorInput()).getFile();
		}
		CSSClassDialog dialog = new CSSClassDialog(HandlerUtil.getActiveShell(event), file, selection);
		if (dialog.open() == Window.OK) {
			dialog.getSelectorName();
		}
		return null;
	}

}
