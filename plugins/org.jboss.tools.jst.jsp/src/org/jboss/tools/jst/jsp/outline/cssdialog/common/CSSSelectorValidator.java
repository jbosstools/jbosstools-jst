/*******************************************************************************
 * Copyright (c) 2007-2009 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.outline.cssdialog.common;

import java.io.IOException;
import java.io.StringReader;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.wst.css.core.internal.parser.CSSTokenizer;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.w3c.dom.css.CSSRuleList;

/**
 * CSS Validator
 * 
 * @author yradtsevich
 * 
 */
@SuppressWarnings("restriction")
public class CSSSelectorValidator extends CSSValidator implements
		IInputValidator {
	private static Reference<CSSSelectorValidator> instanceCache;

	public static CSSSelectorValidator getInstance() {
		CSSSelectorValidator instance = null;
		if (instanceCache != null) {
			instance = instanceCache.get();
		}
		if (instance == null) {
			instance = new CSSSelectorValidator();
			instanceCache = new SoftReference<CSSSelectorValidator>(instance);
		}
		return instance;
	}

	private CSSSelectorValidator() {
		super();
	}

	/**
	 * Validates CSS selector value.
	 * 
	 * @param selector
	 *            the selector value
	 * @return {@code true} if the selector is valid, {@code false} otherwise
	 */
	public boolean isValidSelector(String selector) {
		cleanValidatingDocument();

		getValidatingDocument().set(selector + "{}"); //$NON-NLS-1$

		CSSRuleList cssRules = getValidatingCSS().getCssRules();
		if (cssRules.getLength() != 1) {
			// if the selector is like 'a{} b', or it is empty, or the rule
			// cannot be created
			return false;
		}

		//https://jira.jboss.org/jira/browse/JBIDE-5994 fix
		
		CSSTokenizer cssTokenizer = new CSSTokenizer(new StringReader(selector+"{}")); //$NON-NLS-1$
		try {
			while (!cssTokenizer.isEOF()) {
				String token = cssTokenizer.primGetNextToken();
				if ("undefined".equalsIgnoreCase(token)) { //$NON-NLS-1$
					return false;
				}
			}
		} catch (IOException e) {
			JspEditorPlugin.getPluginLog().logError(e);
		}
		
		cleanValidatingDocument();
		
		return true;
	}

	public String isValid(String value) {
		return isValidSelector(value) ? null
				: JstUIMessages.CSS_CLASS_NAME_NOT_VALID;
	}

}
