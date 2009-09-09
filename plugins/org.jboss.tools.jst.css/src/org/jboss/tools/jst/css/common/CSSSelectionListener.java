/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.css.common;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.jst.css.CssPlugin;

public class CSSSelectionListener implements ISelectionListener {

	private static CSSSelectionListener instance;

	private ListenerList listeners = new ListenerList();

	CSSStyleManager styleManager = new CSSStyleManager();

	private CSSSelectionListener() {
	}

	public synchronized static CSSSelectionListener getInstance() {

		if (instance == null) {
			instance = new CSSSelectionListener();
		}
		return instance;
	}

	public void addSelectionListener(ISelectionListener listener) {

		// if added the first listener start listing
		if (listeners.size() == 0)
			startListening();

		listeners.add(listener);
	}

	public void removeSelectionListener(ISelectionListener listener) {
		listeners.remove(listener);

		// if removed last listener start listing
		if (listeners.size() == 0)
			stopListening();
	}

	private void startListening() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getSelectionService().addPostSelectionListener(this);

	}

	private void stopListening() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getSelectionService().removePostSelectionListener(this);

	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {

		StyleContainer styleContainer = styleManager
				.recognizeCSSStyle(selection);

		ISelection selectionToLiteners = null;

		if (styleContainer != null) {
			selectionToLiteners = new StructuredSelection(styleContainer);
		} else {
			selectionToLiteners = StructuredSelection.EMPTY;
		}

		Object[] array = listeners.getListeners();
		for (int i = 0; i < array.length; i++) {
			final ISelectionListener l = (ISelectionListener) array[i];
			if ((part != null && selection != null)
					|| l instanceof INullSelectionListener) {

				try {
					l.selectionChanged(part, selectionToLiteners);
				} catch (Exception e) {
					CssPlugin.log(e.getLocalizedMessage());
				}
			}

		}

	}
}
