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

package org.jboss.tools.jst.css.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.jboss.tools.jst.css.browser.CSSBrowser;
import org.jboss.tools.jst.css.common.CSSStyleListener;
import org.jboss.tools.jst.css.common.ICSSViewListner;
import org.jboss.tools.jst.css.common.StyleContainer;
import org.jboss.tools.jst.css.messages.CSSUIMessages;
import org.jboss.tools.jst.jsp.util.Constants;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class CSSPreview extends ViewPart implements ICSSViewListner {

	private CSSBrowser browser;

	private Text previewText;

	private String previewContent = CSSUIMessages.CSSPreview_DefaultBrowserText;

	private String currentStyle = new String();

	private Map<String, String> styleAttributes = new HashMap<String, String>();

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		CSSStyleListener.getInstance().addSelectionListener(this);
	}

	@Override
	public void dispose() {
		CSSStyleListener.getInstance().removeSelectionListener(this);
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		final SashForm previewComposite = new SashForm(parent, SWT.None);
		previewComposite.setLayout(new GridLayout());
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		previewComposite.setLayoutData(gridData);

		browser = CSSBrowser.createCSSBrowser(previewComposite, SWT.BORDER);
		browser.setLayoutData(gridData);
		browser.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				if (browser.isBrowserEvent(e)) {
					browser.setEnabled(false);
					previewComposite.setMaximizedControl(previewText);
					previewText.setFocus();
				}
			}
		});

		updateBrowser();

		previewText = new Text(previewComposite, SWT.NONE | SWT.H_SCROLL);
		previewText.setLayoutData(gridData);
		previewText.setText(getPreviewContent());
		previewText.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				if (e.widget == previewText) {
					String text = previewText.getText();
					if (!getPreviewContent().equals(text)) {
						if (text.equals(Constants.EMPTY)) {
							setPreviewContent(CSSUIMessages.CSSPreview_DefaultBrowserText);
						} else {
							setPreviewContent(text);
						}
						updateBrowser();
					}
					browser.setEnabled(true);
					previewComposite.setMaximizedControl(browser);
				}
			}
		});
		previewComposite.setMaximizedControl(browser);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		browser.setFocus();
	}

	/**
	 * /** Method is used to build html body that is appropriate to browse.
	 * 
	 * @return String html text representation
	 */
	public String generateBrowserPage() {
		return CSSPreviewPageCreator.createPreviewHtml(getCurrentStyle(), getPreviewContent());
	}

	public String getPreviewContent() {
		return previewContent;
	}

	public void setPreviewContent(String previewContent) {
		this.previewContent = previewContent;
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			Map<String, String> newAttributes;
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.getFirstElement() instanceof StyleContainer) {
				newAttributes = ((StyleContainer) structuredSelection
						.getFirstElement()).getStyleAttributes();
			} else {
				newAttributes = Collections.emptyMap();
			}

			updateView(newAttributes);
		}
	}

	public void updateBrowser() {
		browser.setText(generateBrowserPage());
	}

	protected String getStyle(Map<String, String> styleAttributes) {
		StringBuilder style = new StringBuilder();
		for (Map.Entry<String, String> styleItem : styleAttributes.entrySet()) {
			style.append(styleItem.getKey() + Constants.COLON
					+ styleItem.getValue() + Constants.SEMICOLON);
		}
		return style.toString();
	}

	public String getCurrentStyle() {
		return currentStyle;
	}

	public void styleChanged(StyleContainer styleContainer) {
		updateView(styleContainer.getStyleAttributes());
	}

	protected void updateView(Map<String, String> attributes) {
		this.styleAttributes = attributes;
		this.currentStyle = getStyle(attributes);
		updateBrowser();
	}
}
