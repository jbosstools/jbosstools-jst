/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.jspeditor;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.INestableKeyBindingService;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.common.core.resources.XModelObjectEditorInput;
import org.jboss.tools.jst.jsp.selection.bar.SelectionBar;

/**
 * 
 */
public abstract class JSPMultiPageEditorPart extends MultiPageEditorPart {

	private ArrayList<IEditorPart> nestedEditors = new ArrayList<IEditorPart>(3);
	
	private SelectionBar selectionBar;
	

	protected JSPMultiPageEditorPart() {
		super();
	}

	/**
	 * 
	 */
	Composite ppp = null;
	/**
	 * 
	 * @param editor
	 * @param input
	 * @return
	 * @throws PartInitException
	 */

	public int addPage(IEditorPart editor, IEditorInput input, StructuredTextEditor sourcePart)
			throws PartInitException {
		Composite parent2;
		if (ppp == null) {
			IEditorSite site = createSite(editor);
			editor.init(site, input);
			parent2 = new Composite(getContainer(), SWT.NONE);
			ppp = parent2;
			GridLayout gridLayout = new GridLayout(2, false);
			gridLayout.marginHeight = 0;
			gridLayout.marginWidth = 0;
			gridLayout.horizontalSpacing = 0;
			gridLayout.verticalSpacing = 0;
			parent2.setLayout(gridLayout);
			
			if(editor==sourcePart){
				/*
				 * In the case when only the source part is available
				 */
				Composite editorComp = new Composite(parent2, SWT.NONE);
				GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
				gd.horizontalSpan = 2;
				editorComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
				editorComp.setLayout(new FillLayout(SWT.VERTICAL));
				editor.createPartControl(editorComp);
			}else {
				editor.createPartControl(parent2);
			}
			/*
			 * Create Selection Bar Composite and set its layout
			 */
			selectionBar = new SelectionBar(sourcePart, parent2, SWT.NONE);
			selectionBar.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
			
			editor.addPropertyListener(new IPropertyListener() {
				public void propertyChanged(Object source, int propertyId) {
					JSPMultiPageEditorPart.this
							.handlePropertyChange(propertyId);
				}
			});
			nestedEditors.add(editor);
		} else {
			parent2 = ppp;
		}
		Item item = createItem(parent2);
		item.setData(editor);
		return getPageCount() - 1;
	}

	/*
	 * 
	 */
	private CTabItem createItem(Control control) {
		CTabItem item = new CTabItem((CTabFolder)getContainer(), SWT.NONE);
		item.setControl(control);
		return item;
	}

	protected abstract IEditorSite createSite(IEditorPart editor);

	public void dispose() {
		if(selectionBar!=null) {
			selectionBar.dispose();
		}
		getSite().setSelectionProvider(null);
		for (int i = 0; i < nestedEditors.size(); ++i) {
			IEditorPart editor = nestedEditors.get(i);
			disposePart(editor);
		}
		nestedEditors.clear();
	}
	
	protected IEditorPart getActiveEditor() {
		return super.getActiveEditor();
	}

	public SelectionBar getSelectionBar() {
		return selectionBar;
	}

	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(XModelObjectEditorInput.checkInput(input));
		site.setSelectionProvider(new JSPMultiPageSelectionProvider(this));
	}

	protected abstract void pageChange(int newPageIndex);

	private void disposePart(final IWorkbenchPart part) {
		SafeRunner.run(new SafeRunnable() {
			public void run() {
				if (part.getSite() instanceof MultiPageEditorSite) {
					MultiPageEditorSite partSite = (MultiPageEditorSite) part
							.getSite();
					partSite.dispose();
				}
				part.dispose();
			}

			public void handleException(Throwable e) {
			}
		});
	}

	public void setFocus() {
		setFocus(getActivePage());
	}

	private void setFocus(int pageIndex) {
		final IKeyBindingService service = getSite().getKeyBindingService();
		if (pageIndex < 0 || pageIndex >= getPageCount()) {
			if (service instanceof INestableKeyBindingService) {
				final INestableKeyBindingService nestableService = (INestableKeyBindingService) service;
				nestableService.activateKeyBindingService(null);
			}
			return;
		}

		IEditorPart editor = getEditor(pageIndex);
		if (editor != null) {
			editor.setFocus();
			if (service instanceof INestableKeyBindingService) {
				INestableKeyBindingService nestableService = (INestableKeyBindingService) service;
				if (editor != null) {
					nestableService.activateKeyBindingService(editor
							.getEditorSite());
				} else {
					nestableService.activateKeyBindingService(null);
				}
			}
		} else {
			if (service instanceof INestableKeyBindingService) {
				INestableKeyBindingService nestableService = (INestableKeyBindingService) service;
				nestableService.activateKeyBindingService(null);
			}

			Control control = getControl(pageIndex);
			if (control != null) {
				control.setFocus();
			}
		}
	}

	public boolean isDirty() {
		// use nestedEditors to avoid SWT requests; see bug 12996
		for (Iterator<IEditorPart> i = nestedEditors.iterator(); i.hasNext();) {
			IEditorPart editor = i.next();
			if (editor.isDirty()) {
				return true;
			}
		}
		return false;
	}

}
