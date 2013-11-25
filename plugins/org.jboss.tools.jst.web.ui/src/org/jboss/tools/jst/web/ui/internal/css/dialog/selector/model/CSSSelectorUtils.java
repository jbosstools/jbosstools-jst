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

package org.jboss.tools.jst.web.ui.internal.css.dialog.selector.model;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.wst.css.core.internal.parser.CSSTokenizer;
import org.jboss.tools.jst.web.ui.WebUiPlugin;

/**
 * 
 * @author yzhishko
 * 
 */

@SuppressWarnings("restriction")
public class CSSSelectorUtils {

	private static String filterName(String className) {
		className = className.trim();
		if (className.indexOf(' ') > -1) {
			return null;
		}
		if (className.indexOf('.') == className.lastIndexOf('.')) {
			if (className.indexOf('.') == 0) {
				CSSTokenizer cssTokenizer = new CSSTokenizer(new StringReader(
						className + "{}")); //$NON-NLS-1$
				try {
					while (!cssTokenizer.isEOF()) {
						String token = cssTokenizer.primGetNextToken();
						if ("undefined".equalsIgnoreCase(token)) { //$NON-NLS-1$
							return null;
						}
					}
				} catch (IOException e) {
					WebUiPlugin.getDefault().logError(e);
				}
				return className.substring(className.indexOf('.') + 1);
			}
		}
		return null;
	}

	public static String[] parseSelectorName(String selectorText) {
		List<String> selectors = new ArrayList<String>(0);
		StringTokenizer tokenizer = new StringTokenizer(selectorText,
				",", false); //$NON-NLS-1$
		while (tokenizer.hasMoreTokens()) {
			String selectorName = tokenizer.nextToken();
			selectorName = filterName(selectorName);
			if (selectorName != null) {
				selectors.add(selectorName);
			}
		}
		return selectors.toArray(new String[0]);
	}
}
