/*******************************************************************************
 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.outline.cssdialog.cssselector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.common.model.ui.widgets.Split;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.outline.cssdialog.AbstractCSSDialog;

/**
 * 
 * @author yzhishko
 * 
 */

public class CSSStyleClassSelector extends AbstractCSSDialog {

	private String currentCSSStyleClass;
	private CSSSelectorPartComposite cssClassComposite;
	private String selectesCSSStylesClasses;
	private CSSSelectorPreview preview;

	public CSSStyleClassSelector(Shell shell) {
		super(shell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX
				| SWT.APPLICATION_MODAL);
	}

	@Override
	protected Composite createControlComposite(Composite parent) {
		// Create down splitter container
		Split controlsContainer = new Split(parent, SWT.VERTICAL);
		controlsContainer.setLayout(new GridLayout());
		controlsContainer.setLayoutData(new GridData(GridData.FILL,
				GridData.FILL, true, true));
		cssClassComposite = createCSSClassComposite(controlsContainer);
		preview = createPreviewComposite(controlsContainer);
		cssClassComposite.addCSSClassSelectionChangedListener(preview);
		return controlsContainer;
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = (Composite) super.createContents(parent);
		return composite;
	}

	private CSSSelectorPartComposite createCSSClassComposite(Composite parent) {
		return new CSSSelectorPartComposite(getStyleAttributes(), parent,
				currentCSSStyleClass);
	}

	public void setCurrentStyleClass(String value) {
		currentCSSStyleClass = value;
	}

	public String getCSSStyleClasses() {
		return selectesCSSStylesClasses;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == OK) {
			selectesCSSStylesClasses = cssClassComposite.getCSSStyleClasses();
		}
		super.buttonPressed(buttonId);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(JstUIMessages.CSS_SELECTOR_TITLE);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle(JstUIMessages.CSS_SELECTOR_TITLE);
		return super.createDialogArea(parent);
	}

	private CSSSelectorPreview createPreviewComposite(Composite parent) {
		return new CSSSelectorPreview(parent);
	}
	
}
