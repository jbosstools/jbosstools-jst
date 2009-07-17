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

import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPTextEditor;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;

public class FormatJSPActionDelegate implements IEditorActionDelegate {
	IAction action;
	ITextEditor targetEditor;
	TextSelection textSelection;

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.action = action;
		if(targetEditor instanceof JSPMultiPageEditor) {
			targetEditor = ((JSPMultiPageEditor)targetEditor).getJspEditor();
		}
		this.targetEditor = (ITextEditor)targetEditor;
		action.setText(JstUIMessages.FormatJSPActionDelegate_Format);
	}

	public void run(IAction action) {
		JSPTextEditor te = (JSPTextEditor)targetEditor;
		IDocument document = te.getTextViewer().getDocument();
		try {
			new JSPFormatter().format(document, textSelection);
		} catch (BadLocationException e) {
			JspEditorPlugin.getPluginLog().logError(e);
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		textSelection = (selection instanceof TextSelection) ? (TextSelection)selection : null;
		action.setEnabled(textSelection != null && textSelection.getLength() > 0);
	}

}
