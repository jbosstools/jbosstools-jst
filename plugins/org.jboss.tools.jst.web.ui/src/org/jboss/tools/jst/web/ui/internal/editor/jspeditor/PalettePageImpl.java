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
package org.jboss.tools.jst.web.ui.internal.editor.jspeditor;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.part.PageBookView;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.common.model.ui.views.palette.IPaletteAdapter;
import org.jboss.tools.common.model.ui.views.palette.IPalettePageAdapter;
import org.jboss.tools.common.model.ui.views.palette.PaletteCreator;
import org.jboss.tools.jst.web.ui.palette.PaletteAdapter;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class PalettePageImpl extends Page implements PalettePage, IPalettePageAdapter, MouseListener {
	PaletteCreator paletteCreator = new PaletteCreator(this);
	PagePaletteContents contents;
	IDocument document;
	DocumentListener listener = null;
	boolean disposed = false;
	
	private FigureCanvas canvas;
	private boolean mousePressed = false;
	private boolean postponeReload = false;

	public PalettePageImpl() {}

	public PagePaletteContents getPaletteContents() {
		return contents;
	}
	public void setPaletteContents(PagePaletteContents contents) {
		this.contents = contents;		
	}

    public void init(IPageSite pageSite) {
    	super.init(pageSite);
    	paletteCreator.initActionBars();
    	getPage().addPartListener(new IPartListener(){

			@Override
			public void partActivated(IWorkbenchPart part) {
				PaletteAdapter adapter = (PaletteAdapter)getAdapter();
				if(adapter != null){
					adapter.activated();
				}
			}

			@Override
			public void partBroughtToTop(IWorkbenchPart part) {
			}

			@Override
			public void partClosed(IWorkbenchPart part) {
			}

			@Override
			public void partDeactivated(IWorkbenchPart part) {
			}

			@Override
			public void partOpened(IWorkbenchPart part) {
			}
    		
    	});
    }

    public void createControl(Composite parent) {
    	Control control = paletteCreator.createPartControlImpl(parent);
    	if(control instanceof Composite){
    		Control[] children = ((Composite) control).getChildren();
    		for(Control child : children){
    			if(child instanceof FigureCanvas){
    				canvas = (FigureCanvas)child;
    				canvas.addMouseListener(this);
    				return;
    			}
    		}
    	}
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
				if(mousePressed){
					postponeReload = true;
				}else{
					reload();
				}
			} else if(contents.computeRecognized()) {
				PaletteAdapter a = (PaletteAdapter)getAdapter();
				a.filter();
			}
		}
		
		@Override
		public void documentAboutToBeChanged(DocumentEvent event) {
		}
    }
    
    private void reload(){
    	if(canvas != null){
	    	canvas.removeMouseListener(this);
	    	canvas = null;
    	}
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

	public Control getControl() {
		return paletteCreator.getControl();
	}

	public void setFocus() {
		paletteCreator.setFocus();
	}

	public void insertIntoEditor(XModelObject macro) {
		paletteCreator.insertIntoEditor(macro);
	}
	
	@Override
	public ITextEditor getActiveTextEditor() {
		return paletteCreator.getActiveTextEditor();
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
	
	@Override
	public void mouseDoubleClick(MouseEvent e) {
	}

	@Override
	public void mouseDown(MouseEvent e) {
		mousePressed = true;
	}

	@Override
	public void mouseUp(MouseEvent e) {
		mousePressed = false;
		if(postponeReload){
			reload();
			postponeReload = false;
		}
	}
}
