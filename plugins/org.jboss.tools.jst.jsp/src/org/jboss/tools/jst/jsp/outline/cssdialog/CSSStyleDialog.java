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
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.common.model.ui.widgets.Split;
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
public class CSSStyleDialog extends Dialog implements MouseListener, FocusListener {

    final static int MIN_HEIGHT_FOR_BROWSER = 60;

    private String previewBrowserValue = Constants.TEXT_FOR_PREVIEW;

    private Composite browserContainer = null;
    private Browser browser = null;
    private Text textBrowser = null;

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

		Split split = new Split(composite, SWT.VERTICAL);

        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        composite.setLayout(layout);

        // initialize browser container
        browserContainer = getCompositeElement(split);
        createBrowserComponent();

        styleAttributes.addChangeStyleListener(new ChangeStyleListener() {
                public void styleChanged(ChangeStyleEvent event) {
            		browser.setText(getTextForBrowser());
                }
            });
        styleComposite = new StyleComposite(split, styleAttributes, oldStyle);

        split.setWeights(new int[]{15, 85});
        split.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, true));

        return composite;
    }

    /**
     * Create container that take up 2 cells and contains fontSizeCombo and extFontSizeCombo elements.
     */
    private Composite getCompositeElement(Composite parent) {
        GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
        GridLayout gridLayoutTmp = new GridLayout();
        gridLayoutTmp.marginHeight = 0;
        gridLayoutTmp.marginWidth = 0;
        Composite classComposite = new Composite(parent, SWT.FILL);
        classComposite.setLayout(gridLayoutTmp);
        classComposite.setLayoutData(gridData);

        return classComposite;
    }

    /**
     * Method is used to create browser component to display preview html.
     */
    private void createBrowserComponent() {
        GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
        browser = new Browser(browserContainer, SWT.BORDER | SWT.MOZILLA);
        browser.setText(getTextForBrowser());
        browser.addMouseListener(this);
        browser.setLayoutData(gridData);
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
        String html = Constants.OPEN_DIV_TAG + styleForSpan + "\">" + previewBrowserValue + Constants.CLOSE_DIV_TAG; //$NON-NLS-1$

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
        newShell.setText(JstUIMessages.CSS_STYLE_EDITOR_TITLE);
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

	/**
	 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDoubleClick(MouseEvent e) {
		if (e.widget == browser) {
	        browser.removeMouseListener(this);
	        browser.dispose();
	        // create Text area component instead of HTML Browser
	        GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
	        textBrowser = new Text(browserContainer, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
	        textBrowser.setText(previewBrowserValue);
	        textBrowser.addFocusListener(this);
	        textBrowser.setLayoutData(gridData);
	        textBrowser.setEditable(true);
	        textBrowser.setFocus();
		}
		browserContainer.layout();
	}

	/**
	 * @see org.eclipse.swt.events.FocusListener#focusLost(org.eclipse.swt.events.FocusEvent)
	 */
	public void focusLost(FocusEvent e) {
		if (e.widget == textBrowser) {
			previewBrowserValue = textBrowser.getText();
			textBrowser.removeFocusListener(this);
			textBrowser.dispose();
			// create Browse component instead of text area
			createBrowserComponent();
		}
		browserContainer.layout();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDown(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseUp(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.FocusListener#focusGained(org.eclipse.swt.events.FocusEvent)
	 */
	public void focusGained(FocusEvent e) {
	}
}
