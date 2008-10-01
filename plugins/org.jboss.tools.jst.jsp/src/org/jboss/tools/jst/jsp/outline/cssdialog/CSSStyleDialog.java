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
package org.jboss.tools.jst.jsp.outline.cssdialog;

import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.MessageUtil;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.ChangeStyleEvent;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.ChangeStyleListener;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.StyleAttributes;

/**
 * Class for CSS editor dialog
 * 
 * @author Dzmitry Sakovich (dsakovich@exadel.com)
 */
public class CSSStyleDialog extends Dialog {
    
   
    //TODO Dzmitry Sakovich
    private Browser browser = null;
    private StyleComposite styleComposite = null;
    private String oldStyle;
    private StyleAttributes styleAttributes = new StyleAttributes();

    
    final static int MIN_HEIGHT_FOR_BROWSER = 60; 

    public CSSStyleDialog(final Shell parentShell, String oldStyle) {
	super(parentShell);
	setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX
		| SWT.APPLICATION_MODAL);
	this.oldStyle = oldStyle;
    }

    /**
     * Getter for newStyle attribute
     * 
     * @return
     */
    public String getNewStyle() {
	return styleComposite==null?oldStyle:styleComposite.getNewStyle();
    }

    /**
     * Method for creating dialog area
     * 
     * @param parent
     */
    protected Control createDialogArea(final Composite parent) {
	
		
	final Composite composite = (Composite) super.createDialogArea(parent);
	
	GridLayout layout = new GridLayout();
	layout.numColumns = 1;
	browser = new Browser(composite,SWT.BORDER);
	
	composite.setLayout(layout);
	
	styleAttributes.addChangeStyleListener(new ChangeStyleListener() {
	    public void styleChanged(ChangeStyleEvent event) {
		String styleForSpan = "";
		String html = "";

		Set<String> keySet = styleAttributes.keySet();

		for (String key : keySet)
		    styleForSpan += key + Constants.COLON_STRING
			    + styleAttributes.getAttribute(key) + Constants.SEMICOLON_STRING;

		html = Constants.OPEN_DIV_TAG + styleForSpan
			+ Constants.TEXT_FOR_PREVIEW + Constants.CLOSE_DIV_TAG;
		browser.setText(html);
	    }
	});
	styleComposite = new StyleComposite(composite,styleAttributes,oldStyle);
	
	GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
		true);
	gridData.minimumHeight = MIN_HEIGHT_FOR_BROWSER;
	browser.setLayoutData(gridData);
	
	return composite;
    }

    /**
     * Method for setting title for dialog
     * 
     * @param newShell
     */
    protected void configureShell(Shell newShell) {
	super.configureShell(newShell);
	newShell.setText(MessageUtil.getString("CSS_STYLE_DIALOG_TITLE"));
    }

    protected void okPressed() {
	styleComposite.updateStyle();
	super.okPressed();
    }

    @Override
    protected IDialogSettings getDialogBoundsSettings() {
	return JspEditorPlugin.getDefault().getDialogSettings();
    }
}