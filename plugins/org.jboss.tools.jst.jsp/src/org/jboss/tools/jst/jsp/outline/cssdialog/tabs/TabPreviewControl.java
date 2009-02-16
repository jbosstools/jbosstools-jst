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

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.css.ui.StructuredTextViewerConfigurationCSS;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.StructuredTextViewerConfiguration;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.CSSModel;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.StyleAttributes;

/**
 * Class for creating Preview sheet tab
 */
public class TabPreviewControl extends Composite {

    /** Editor in which we open visual page. */
    SourceViewer viewer = null;
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
			for (Control control : getChildren()) {
				control.dispose();
			}

			StructuredTextViewerConfiguration baseConfiguration = new StructuredTextViewerConfigurationCSS();

			viewer = new StructuredTextViewer(this, null, null, false,
					SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
			((StructuredTextViewer) viewer).getTextWidget().setFont(
					JFaceResources.getFont("org.eclipse.wst.sse.ui.textfont")); //$NON-NLS-1$

			viewer.configure(baseConfiguration);
			viewer.setDocument(cssModel.getStructuredDocument());
			viewer.setEditable(false);

			layout();
		}
	}

    public void doRevertToSaved() {

    	cssModel.reload();

	}
    
    /**
     * TODO redesign method : remove "index" param
     * Method is used to select area that corresponds to specific selector.
     *
     * @param selector the selector that should be selected in editor area
     * @param index if CSS file contains more then one elements with the same selector name,
     * 		then index is serial number of this selector
     */
    public void selectEditorArea(String selector, int index) {
		if (cssModel != null) {
			IndexedRegion indexedRegion = cssModel.getSelectorRegion(selector,
					index);
			if (/* editor */viewer != null) {
				if (indexedRegion != null) {
					// editor.selectAndReveal(indexedRegion.getStartOffset(),
					// indexedRegion.getLength());
					viewer.setSelectedRange(indexedRegion.getStartOffset(),
							indexedRegion.getLength());
					viewer.revealRange(indexedRegion.getStartOffset(),
							indexedRegion.getLength());
				} else {
					// editor.selectAndReveal(0, 0);
					viewer.setSelectedRange(0, 0);
					viewer.revealRange(0,0);
				}
			}
		}
	}

}
