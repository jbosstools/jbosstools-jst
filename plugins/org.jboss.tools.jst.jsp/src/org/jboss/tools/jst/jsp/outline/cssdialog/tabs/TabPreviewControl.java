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
import org.eclipse.wst.css.ui.StructuredTextViewerConfigurationCSS;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.StructuredTextViewerConfiguration;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.ICSSDialogModel;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.StyleAttributes;

/**
 * Class for creating Preview sheet tab
 */
public class TabPreviewControl extends Composite implements ICSSTabControl {

	/** Editor in which we open visual page. */
	SourceViewer viewer = null;
	private ICSSDialogModel cssModel = null;
	private String selector = null;
	private StyleAttributes styleAttributes;

	public void setSelector(String selector) {
		this.selector = selector;
	}

	/**
	 * Constructor for creating controls
	 * 
	 * @param composite
	 *            The parent composite for tab
	 * @param styleAttributes
	 *            the StyleAttributes object
	 */
	public TabPreviewControl(Composite tabFolder,
			StyleAttributes styleAttributes, ICSSDialogModel cssModel) {
		super(tabFolder, SWT.NONE);
		setLayout(new FillLayout());
		this.cssModel = cssModel;
		this.styleAttributes = styleAttributes;
		initPreview(cssModel);
	}

	/**
	 * Method update preview tab with information from the CSS file passed by
	 * parameter.
	 * 
	 * @param cssFile
	 *            CSS file to be displayed in preview area
	 */
	public void initPreview(ICSSDialogModel cssModel) {
		this.cssModel = cssModel;
		if (cssModel != null) {

			StructuredTextViewerConfiguration baseConfiguration = new StructuredTextViewerConfigurationCSS();

			viewer = new StructuredTextViewer(this, null, null, false,
					SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
			((StructuredTextViewer) viewer).getTextWidget().setFont(
					JFaceResources.getFont("org.eclipse.wst.sse.ui.textfont")); //$NON-NLS-1$

			viewer.configure(baseConfiguration);
			viewer.setDocument(cssModel.getDocument());
			viewer.setEditable(false);

			layout();
		}
	}

	public void reinit(ICSSDialogModel cssModel) {
		this.cssModel = cssModel;
		viewer.setDocument(cssModel.getDocument());
	}

	public void releaseModel() {
		viewer.setDocument(null);
		cssModel = null;
	}

	/**
	 * 
	 * @param selector
	 */
	public void selectClass(String selector) {
		if (cssModel != null) {
			IndexedRegion indexedRegion = cssModel.getIndexedRegion(selector);
			if (viewer != null) {
				if (indexedRegion != null) {
					viewer.setSelectedRange(indexedRegion.getStartOffset(),
							indexedRegion.getLength());
					viewer.revealRange(indexedRegion.getStartOffset(),
							indexedRegion.getLength());
				} else {
					viewer.setSelectedRange(0, 0);
					viewer.revealRange(0, 0);
				}
			}
		}
	}

	public void update() {
		cssModel.updateCSSStyle(selector, styleAttributes);
		selectClass(selector);

	}

}
