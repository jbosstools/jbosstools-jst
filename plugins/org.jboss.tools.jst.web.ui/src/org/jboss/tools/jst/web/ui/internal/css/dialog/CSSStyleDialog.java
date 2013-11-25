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
package org.jboss.tools.jst.web.ui.internal.css.dialog;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.jst.web.ui.internal.css.dialog.common.CSSConstants;
import org.jboss.tools.jst.web.ui.internal.css.dialog.common.Util;
import org.jboss.tools.jst.web.ui.internal.editor.messages.JstUIMessages;
import org.jboss.tools.jst.web.ui.internal.editor.outline.css.CSSStyleDialogInterface;
import org.jboss.tools.jst.web.ui.internal.editor.util.Constants;

/**
 * Class for CSS editor dialog
 * 
 * @author Dzmitry Sakovich (dsakovich@exadel.com)
 */
public class CSSStyleDialog extends AbstractCSSDialog implements CSSStyleDialogInterface{

	public CSSStyleDialog(final Shell parentShell, String style) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX
				| SWT.APPLICATION_MODAL);

		getStyleAttributes().setStyleProperties(parseStyle(style));

	}
	
	public String getStyle() {
		return getStyleAttributes().getStyle();
	}

	protected Map<String, String> parseStyle(String styleString) {

		Map<String, String> properties = new HashMap<String, String>();

		if ((styleString != null) && (styleString.length() > 0)) {

			String[] styles = styleString.split(Constants.SEMICOLON);
			for (String styleElement : styles) {
				String[] styleElementParts = styleElement.trim().split(
						Constants.COLON);
				if ((styleElementParts != null)
						&& (styleElementParts.length == 2)
						&& Util.searchInElement(styleElementParts[0],
								CSSConstants.CSS_STYLES_MAP)) {

					properties.put(styleElementParts[0], styleElementParts[1]);
				}
			}

		}
		return properties;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(JstUIMessages.CSS_STYLE_EDITOR_TITLE);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle(JstUIMessages.CSS_STYLE_EDITOR_TITLE);
		return super.createDialogArea(parent);
	}
}
