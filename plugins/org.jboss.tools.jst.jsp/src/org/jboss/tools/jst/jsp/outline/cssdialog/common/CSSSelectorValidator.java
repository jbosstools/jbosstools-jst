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

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import org.eclipse.jface.dialogs.IInputValidator;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleRule;

/**
 * CSS Validator
 * 
 * @author yradtsevich
 * 
 */
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
		getValidatingDocument().set(selector + "{}"); //$NON-NLS-1$

		CSSRuleList cssRules = getValidatingCSS().getCssRules();
		if (cssRules.getLength() != 1) {
			// if the selector is like 'a{} b', or it is empty, or the rule
			// cannot be created
			return false;
		}

		CSSStyleRule cssRule = (CSSStyleRule) cssRules.item(0);
		if (!selector.equals(cssRule.getSelectorText())) {
			// if the selector is like 'a{{{'
			return false;
		}

		cleanValidatingDocument();

		return true;
	}

	public String isValid(String value) {
		return isValidSelector(value) ? null
				: JstUIMessages.CSS_CLASS_NAME_NOT_VALID;
	}

}
