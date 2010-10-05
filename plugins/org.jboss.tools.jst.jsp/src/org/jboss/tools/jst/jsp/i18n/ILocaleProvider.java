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
package org.jboss.tools.jst.jsp.i18n;

import java.util.Locale;

import org.eclipse.ui.texteditor.ITextEditor;

/**
 * This interface is intended for creation extensions of {@code localeProvider}
 * extension point.
 *
 * @author yradtsevich
 */
public interface ILocaleProvider {
	/**
	 * Returns the locale for given {@code editor}. Implementations
	 * may return {@code null} if they can not determine the locale. 
	 */
	Locale getLocale(ITextEditor editor);

	/**
	 * Shows the string representation of the locale 
	 * returned by  #getLocale(StructuredTextEditor) method.
	 *  
	 * @return the string representation of the locale
	 */
	String getLocaleString();
}
