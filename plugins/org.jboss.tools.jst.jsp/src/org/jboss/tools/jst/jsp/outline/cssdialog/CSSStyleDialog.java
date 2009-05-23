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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.CSSConstants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Util;

/**
 * Class for CSS editor dialog
 * 
 * @author Dzmitry Sakovich (dsakovich@exadel.com)
 */
public class CSSStyleDialog extends AbstractCSSDialog {

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
}
