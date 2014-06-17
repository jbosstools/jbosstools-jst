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

package org.jboss.tools.jst.web.ui.internal.editor.i18n.handlers;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.HandlerEvent;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISources;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jboss.tools.jst.web.ui.internal.editor.i18n.ExternalizeStringsDialog;
import org.jboss.tools.jst.web.ui.internal.editor.i18n.ExternalizeStringsUtils;
import org.jboss.tools.jst.web.ui.internal.editor.i18n.ExternalizeStringsWizard;
import org.w3c.dom.Attr;

/**
 * Internationalization command handler
 * @author mareshkau
 *
 */
public class I18nHandler extends AbstractHandler implements IElementUpdater {
	
	@Override
	public void setEnabled(Object evaluationContext) {
		boolean enabled=false;
		if (evaluationContext instanceof IEvaluationContext) {
			IEvaluationContext context = (IEvaluationContext) evaluationContext;
			Object activeEditor = context.getVariable(ISources.ACTIVE_EDITOR_NAME);
			if(activeEditor instanceof ITextEditor){
				ITextEditor txtEditor = (ITextEditor) activeEditor;
				ISelection selection = txtEditor.getSelectionProvider().getSelection();
				enabled = ExternalizeStringsUtils.isExternalizeStringsCommandEnabled(selection);
			} 
		}
		if (isEnabled() != enabled) {
			setBaseEnabled(enabled);
		}
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart activeEditor = HandlerUtil.getActiveEditorChecked(event);
		
		if(activeEditor instanceof ITextEditor){
			ExternalizeStringsDialog dlg = new ExternalizeStringsDialog(
					PlatformUI.getWorkbench().getDisplay().getActiveShell(),
					new ExternalizeStringsWizard((ITextEditor)activeEditor, 
							null));
			dlg.open();
		}
		return null;
	}

	public void updateElement(UIElement element, Map parameters) {
		fireHandlerChanged(new HandlerEvent(this, true, false));
	}
}
