/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.jsp.jspeditor;

import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.common.model.ui.views.palette.IPaletteAdapter;
import org.jboss.tools.common.model.ui.views.palette.IPalettePageAdapter;
import org.jboss.tools.common.model.ui.views.palette.PaletteContents;
import org.jboss.tools.common.model.ui.views.palette.PaletteCreator;
import org.eclipse.ui.part.PageBookView;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class PalettePageImpl extends Page implements PalettePage, IPalettePageAdapter {
	PaletteCreator paletteCreator = new PaletteCreator(this);
	PaletteContents contents;
	IDocument document;
	DocumentListener listener = null;
	boolean disposed = false;

	public PalettePageImpl() {}

	public PaletteContents getPaletteContents() {
		return contents;
	}
	public void setPaletteContents(PaletteContents contents) {
		this.contents = contents;		
	}

    public void init(IPageSite pageSite) {
    	super.init(pageSite);
    	paletteCreator.initActionBars();
    }

    public void createControl(Composite parent) {
    	paletteCreator.createPartControlImpl(parent);
	}

    public void attach(IDocument document) {
    	this.document = document;
    	listener = new DocumentListener();
    	document.addDocumentListener(listener);
    }

    class DocumentListener implements IDocumentListener {
		@Override
		public void documentChanged(DocumentEvent event) {
			if(contents.update()) {
		    	IWorkbenchPage page = ModelUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		    	PageBookView view = (PageBookView)page.findView("org.eclipse.gef.ui.palette_view");
		    	if(view != null) {
		    		//Set 'disposed' flag. Page will be properly disposed 
		    		//by view.partClosed() in a separate job.
		    		//On view.partActivated(), editor will be requested
		    		//for page, and due to 'disposed' set to true,
		    		//new page object will be created.
		    		disposed = true; 
		    		view.partClosed(page.getActiveEditor());
		    		view.partActivated(page.getActiveEditor());
		    	}
			}
		}
		
		@Override
		public void documentAboutToBeChanged(DocumentEvent event) {
		}
    }

	public Control getControl() {
		return paletteCreator.getControl();
	}

	public void setFocus() {
		paletteCreator.setFocus();
	}

	public void insertIntoEditor(XModelObject macro) {
		paletteCreator.insertIntoEditor(macro);
	}

    public void dispose() {
    	disposed = true;
    	super.dispose();
    	paletteCreator.dispose();
    	if(document != null) {
    		document.removeDocumentListener(listener);
    		document = null;
    		listener = null;
    	}
    }

	public IActionBars getActionBars() {
		return getSite() == null ? null : getSite().getActionBars();
	}

	public boolean isEnabled() {
		return true;
	}

	public void setContentDescription(String description) {
		
	}

	public IWorkbenchPage getPage() {
		return getSite().getPage();
	}

	public IPaletteAdapter getAdapter() {
		return paletteCreator.getAdapter();
	}

	public boolean isDisposed() {
		return disposed;
	}

}
