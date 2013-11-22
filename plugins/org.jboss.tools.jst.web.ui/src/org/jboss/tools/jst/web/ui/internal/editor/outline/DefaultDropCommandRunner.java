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
package org.jboss.tools.jst.web.ui.internal.editor.outline;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.ITextEditor;

import org.jboss.tools.common.meta.action.SpecialWizard;
import org.jboss.tools.common.model.ui.editors.dnd.DropCommandFactory;
import org.jboss.tools.common.model.ui.editors.dnd.DropData;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPTextEditor;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.dnd.JSPTagProposalFactory;

public class DefaultDropCommandRunner implements SpecialWizard {
	String flavor;
	String data;
	ITextEditor textEditor;

	public boolean runDropCommand(final String flavor, final String data, ITextEditor te) {
		if(te instanceof JSPTextEditor) {
			((JSPTextEditor)te).runDropCommand(flavor, data);
			return true;
		}
		//TODO create valueProvider?
		final IEditorInput ei = te.getEditorInput();
		final ISelectionProvider sp = te.getSelectionProvider();
		final ISourceViewer sv = getSourceViewer(te);
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
//				if(parentEditor.getVisualEditor().getController()!=null ) {
					DropCommandFactory.getInstance()
					.getDropCommand(flavor, JSPTagProposalFactory.getInstance())
					.execute(
						new DropData(
							flavor,
							data,
							ei,
							sv,
							sp
						)
					);
//				}
			}
		});
		return true;
	}

	ISourceViewer getSourceViewer(ITextEditor editor) {
		if(!(editor instanceof AbstractTextEditor)) return null;
		AbstractTextEditor ae = (AbstractTextEditor)editor;
		try {
			Method m = AbstractTextEditor.class.getDeclaredMethod("getSourceViewer", new Class[0]); //$NON-NLS-1$
			m.setAccessible(true);
			return (ISourceViewer)m.invoke(ae, new Object[0]);
		} catch (NoSuchMethodException e1) {
			return null;
		} catch (InvocationTargetException e2) {
			return null;
		} catch (IllegalAccessException e2) {
			return null;
		}
	}

	public int execute() {
		boolean result = runDropCommand(flavor, data, textEditor);
		return result ? 0 : 1;
	}

	public void setObject(Object object) {
		Properties p = (Properties)object;
		flavor = p.getProperty("flavor"); //$NON-NLS-1$
		data = p.getProperty("data"); //$NON-NLS-1$
		textEditor = (ITextEditor)p.get("textEditor");		 //$NON-NLS-1$
	}

}
