/*******************************************************************************
 * Copyright (c) 2007-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.jsp.jspeditor;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.part.MultiPageSelectionProvider;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.FileSystemsHelper;
import org.jboss.tools.common.model.filesystems.impl.FileAnyImpl;
import org.jboss.tools.common.model.util.PositionSearcher;

/**
 * 
 * @author eskimo(dgolovin@exadel.com)
 *
 */
public class JSPMultiPageSelectionProvider extends MultiPageSelectionProvider {
	JSPMultiPageEditorPart multiPageEditor;
	
	public JSPMultiPageSelectionProvider(JSPMultiPageEditorPart multiPageEditor) {
		super(multiPageEditor);
		this.multiPageEditor = multiPageEditor;
	}

	public JSPMultiPageEditorPart getMultiPageEditor() {
		return multiPageEditor;
	}

	public void setSelection(ISelection selection) {
		selection = convertObjectSelection(selection);
		if (!isAppropriateSelected(selection))
			return;
		if (isFiringSelection)
			return;
		isFiringSelection = true;
		try {
			super.setSelection(selection);
		} finally {
			isFiringSelection = false;
		}
	}

	private ISelection convertObjectSelection(ISelection selection) {
		if(selection instanceof IStructuredSelection && !selection.isEmpty()
				&& ((IStructuredSelection)selection).getFirstElement() instanceof XModelObject) {
			XModelObject o = (XModelObject)((IStructuredSelection)selection).getFirstElement();
			XModelObject f = FileSystemsHelper.getFile(o);
			if(((JSPMultiPageEditor)multiPageEditor).getModelObject() == f) {
				String text = ((FileAnyImpl)f).getAsText();
				PositionSearcher searcher = new PositionSearcher();
				searcher.init(text, o, null);
				searcher.execute();
				int bp = searcher.getStartPosition();
				int ep = searcher.getEndPosition();
				if(bp >= 0 && ep >= bp) {
					selection = new TextSelection(bp,  ep - bp);
				}
			}
		}
		return selection;
	}

	private boolean isAppropriateSelected(ISelection selection) {
		if (!(selection instanceof IStructuredSelection))
			return true;
		if (selection.isEmpty())
			return true;
		Object o = ((IStructuredSelection) selection).getFirstElement();
		return (o instanceof IndexedRegion);
	}

	boolean isFiringSelection = false;

	public boolean isFiringSelection() {
		return isFiringSelection;
	}

	public void fireSelectionChanged(final SelectionChangedEvent event,
			ListenerList listenerList) {
		if (isFiringSelection)
			return;
		isFiringSelection = true;
		super.fireSelectionChanged(event);
		isFiringSelection = false;
	}

	public void firePostSelectionChanged(final SelectionChangedEvent event,
			ListenerList listenerList) {
		if (isFiringSelection)
			return;
		isFiringSelection = true;
		super.firePostSelectionChanged(event);
		isFiringSelection = false;
	}
}
