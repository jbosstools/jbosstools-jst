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
package org.jboss.tools.jst.web.ui.internal.editor.jspeditor;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jst.jsp.core.internal.provisional.contenttype.ContentTypeIdForJSP;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.MultiPageEditorSite;

/**
 * 
 * @author eskimo(dgolovin@exadel.com)
 * 
 */
public abstract class JSPMultiPageEditorSite extends MultiPageEditorSite {

	private ISelectionChangedListener fSelChangeListener = null;
	
	public JSPMultiPageEditorSite(JSPMultiPageEditorPart multiPageEditor,
			IEditorPart editor) {
		super(multiPageEditor, editor);
	}

	public void dispose() {
		super.dispose();
		
		if (fSelChangeListener != null) {
			getSelectionProvider().removeSelectionChangedListener(fSelChangeListener);
			fSelChangeListener = null;
		}
	}

	public String getId() {
		return ContentTypeIdForJSP.ContentTypeID_JSP + ".source"; //$NON-NLS-1$; 
	}

	protected ISelectionChangedListener getSelectionChangedListener() {
		if (fSelChangeListener == null) {
			fSelChangeListener = new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent event) {
					JSPMultiPageEditorSite.this.handleSelectionChanged(event);
				}
			};
		}
		return fSelChangeListener;
	}

	public IWorkbenchWindow getWorkbenchWindow() {
		if(getMultiPageEditor().getSite() == null) // fix JBIDE-2218
			return null;						   // fix JBIDE-2218
		return getMultiPageEditor().getSite().getWorkbenchWindow();
	}

	protected void handleSelectionChanged(SelectionChangedEvent event) {
		ISelectionProvider parentProvider = getMultiPageEditor().getSite()
				.getSelectionProvider();
		if (parentProvider instanceof JSPMultiPageSelectionProvider) {
			SelectionChangedEvent newEvent = new SelectionChangedEvent(
					parentProvider, event.getSelection());
			((JSPMultiPageSelectionProvider) parentProvider)
					.fireSelectionChanged(newEvent);
		}	
	}

	protected void handlePostSelectionChanged(SelectionChangedEvent event) {
		ISelectionProvider parentProvider = getMultiPageEditor().getSite()
				.getSelectionProvider();
		if (parentProvider instanceof JSPMultiPageSelectionProvider) {
			SelectionChangedEvent newEvent = new SelectionChangedEvent(
					parentProvider, event.getSelection());
			((JSPMultiPageSelectionProvider) parentProvider)
					.firePostSelectionChanged(newEvent);
		}
	}
	
	public void progressEnd(Job job) {
	}

	public void progressStart(Job job) {
	}
}
