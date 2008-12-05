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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.EditorAreaHelper;
import org.eclipse.ui.internal.EditorManager;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.CSSModel;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.StyleAttributes;

/**
 * Class for creating Preview sheet tab
 */
public class TabPreviewControl extends Composite {

    /** Editor in which we open visual page. */
    protected final static String EDITOR_ID = "org.eclipse.wst.css.core.csssource.source"; //$NON-NLS-1$

    private StructuredTextEditor editor = null;
    private CSSModel cssModel = null;

    /**
     * Constructor for creating controls
     *
     * @param composite The parent composite for tab
     * @param styleAttributes the StyleAttributes object
     */
    public TabPreviewControl(Composite tabFolder, StyleAttributes styleAttributes) {
        super(tabFolder, SWT.NONE);
        setLayout(new FillLayout());
        Label label = new Label(this, SWT.CENTER);
        label.setText(JstUIMessages.DEFAULT_PREVIEW_TEXT);
    }

    /**
     * Method update preview tab with information from the CSS file passed by parameter.
     *
     * @param cssFile CSS file to be displayed in preview area
     */
    public void initPreview(CSSModel cssModel) {
    	this.cssModel = cssModel;
    	if (cssModel != null) {
    		IEditorInput input = new FileEditorInput(cssModel.getStyleFile());
    		try {
    			WorkbenchWindow workbenchWindow = (WorkbenchWindow)PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    			EditorAreaHelper editorPresentation = new EditorAreaHelper((WorkbenchPage) workbenchWindow.getActivePage());
    			EditorManager editorManager = new EditorManager(workbenchWindow, (WorkbenchPage) workbenchWindow.getActivePage(), editorPresentation);

    			IEditorReference ref = editorManager.openEditor(EDITOR_ID, input, true, null);
    			if (ref != null) {
    				// all preview tab editors should be disposed before adding new editor compoment
       				for (Control control : getChildren()) {
       					control.dispose();
        			}
       				if (editor != null) {
       					editor.doRevertToSaved();
       					editor.close(false);
       				}
    				editor = (StructuredTextEditor)ref.getEditor(true);
        			editor.createPartControl(this);
        			editor.getTextViewer().setEditable(false);
    			}

    			layout();
    		} catch (PartInitException e) {
    			e.printStackTrace();
    		}
    	}
    }

    public void doRevertToSaved() {
    	if (editor != null) {
    		editor.doRevertToSaved();
    	}
    }
    
    /**
     * Method is used to select area that corresponds to specific selector.
     *
     * @param selector the selector that should be selected in editor area
     * @param index if CSS file contains more then one elements with the same selector name,
     * 		then index is serial number of this selector
     */
    public void selectEditorArea(String selector, int index) {
    	if (cssModel != null) {
    		IndexedRegion indexedRegion = cssModel.getSelectorRegion(selector, index);
    		if (editor != null) {
    			if (indexedRegion != null) {
    				editor.selectAndReveal(indexedRegion.getStartOffset(), indexedRegion.getLength());
    			} else {
    				editor.selectAndReveal(0, 0);
    			}
    		}
    	}
    }

    /**
     * Method is used to close CSS file editor correctly.
     *
     * @param save true if close editor with closure operation; false - otherwise
     */
    public void closeEditor(boolean save) {
    	if (editor != null) {
    		editor.doRevertToSaved();
    		editor.close(save);
    		editor = null;
    	}
    	if (cssModel != null) {
    		cssModel.releaseModel();
    	}
    }
}
