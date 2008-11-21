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
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.ChangeStyleEvent;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.ChangeStyleListener;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.StyleAttributes;

/**
 * Class for CSS editor dialog
 *
 * @author Dzmitry Sakovich (dsakovich@exadel.com)
 */
public class CSSStyleDialog extends Dialog {

    final static int MIN_HEIGHT_FOR_BROWSER = 60;

    private String previewBrowserValue = Constants.TEXT_FOR_PREVIEW;

    private Browser browser = null;
    private StyleComposite styleComposite = null;
    private StyleAttributes styleAttributes = null;
    private String oldStyle;

    public CSSStyleDialog(final Shell parentShell, String oldStyle) {
        super(parentShell);
        setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX | SWT.APPLICATION_MODAL);
        this.oldStyle = oldStyle;
        styleAttributes = new StyleAttributes();
    }

    /**
     * Getter for newStyle attribute
     *
     * @return new style string value
     */
    public String getNewStyle() {
        return (styleComposite == null) ? oldStyle : styleComposite.getNewStyle();
    }

    /**
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(Composite)
     */
    @Override
    protected Control createDialogArea(final Composite parent) {
        final Composite composite = (Composite) super.createDialogArea(parent);

        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        browser = new Browser(composite, SWT.BORDER);

        composite.setLayout(layout);

        styleAttributes.addChangeStyleListener(new ChangeStyleListener() {
                public void styleChanged(ChangeStyleEvent event) {
            		browser.setText(getTextForBrowser());
                }
            });
        styleComposite = new StyleComposite(composite, styleAttributes, oldStyle);

        GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
        gridData.minimumHeight = MIN_HEIGHT_FOR_BROWSER;
        browser.setLayoutData(gridData);
        browser.setText(getTextForBrowser());

        return composite;
    }

    /**
     * Method is used to build html body that is appropriate to browse.
     *
     * @return String html text representation
     */
    private String getTextForBrowser() {
        String styleForSpan = Constants.EMPTY;
        Set<String> keySet = styleAttributes.keySet();
        for (String key : keySet) {
            styleForSpan += (key + Constants.COLON + styleAttributes.getAttribute(key) + Constants.SEMICOLON);
        }
        String html = Constants.OPEN_DIV_TAG + styleForSpan + "\">" + previewBrowserValue + Constants.CLOSE_DIV_TAG;

        return html;
    }

    /**
     * Method for setting title for dialog
     *
     * @param newShell Shell object
     * @see org.eclipse.jface.window.Window#configureShell(Shell)
     */
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(JstUIMessages.CSS_STYLE_CLASS_DIALOG_TITLE);
    }

    /**
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        styleComposite.updateStyle();
        super.okPressed();
    }

    /**
     * @see org.eclipse.jface.dialogs.Dialog#getDialogBoundsSettings()
     */
    @Override
    protected IDialogSettings getDialogBoundsSettings() {
        return JspEditorPlugin.getDefault().getDialogSettings();
    }
}
