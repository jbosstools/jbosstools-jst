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
package org.jboss.tools.jst.jsp.outline.cssdialog.tabs;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.EditorAreaHelper;
import org.eclipse.ui.internal.EditorManager;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.StyleAttributes;

/**
 * Class for creating Preview sheet tab
 */
public class TabPreviewControl extends Composite {

    /** Editor in which we open visual page. */
    protected final static String EDITOR_ID = "org.eclipse.wst.css.core.csssource.source"; //$NON-NLS-1$

    private IFile cssFile = null;
    private StructuredTextEditor editor = null;

    /**
     * Constructor for creating controls
     *
     * @param composite The parent composite for tab
     */
    public TabPreviewControl(Composite tabFolder, StyleAttributes styleAttributes) {
        super(tabFolder, SWT.NONE);
        setLayout(new FillLayout());
    }

    /**
     *
     */
    public void updateDataFile(IFile cssFile) {
    	if (cssFile != null) {
    		this.cssFile = cssFile;
    		IEditorInput input = new FileEditorInput(cssFile);

    		try {
    			WorkbenchWindow workbenchWindow = (WorkbenchWindow)PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    			EditorAreaHelper editorPresentation = new EditorAreaHelper((WorkbenchPage) workbenchWindow.getActivePage());
    			EditorManager editorManager = new EditorManager(workbenchWindow, (WorkbenchPage) workbenchWindow.getActivePage(), editorPresentation);

    			IEditorReference ref = editorManager.openEditor(EDITOR_ID, input, true, null);

    			if (ref != null) {
//        			if (getChildren() != null && getChildren().length > 0) {
        			if (editor != null) {
//        	    		editor.doRevertToSaved();
//        				editor.dispose();
        				getChildren()[0].dispose();
        			}
    				editor = (StructuredTextEditor)ref.getEditor(true);
        			editor.createPartControl(this);
        			editor.getTextViewer().setEditable(false);
    			}

    			layout();
    		} catch (PartInitException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    }

    public void closeEditor(boolean save) {
    	if (editor != null) {
    		editor.close(save);
    		editor.doRevertToSaved();
    	}
    }
}
