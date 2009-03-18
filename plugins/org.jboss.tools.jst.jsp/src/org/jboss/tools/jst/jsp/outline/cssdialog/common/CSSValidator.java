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
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.eclipse.wst.css.core.internal.provisional.document.ICSSModel;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

/**
 * CSS Validator
 * 
 * @author yradtsevich
 * 
 */
@SuppressWarnings("restriction")
public class CSSValidator {
	// FIXME: this hard-coded string have to be replaced by reference
	private static final String CSS_CONTENT_TYPE_IDENTIFIER = "org.eclipse.wst.css.core.csssource"; //$NON-NLS-1$
	private final CSSStyleSheet validatingCSS;
	private final IStructuredDocument validatingDocument;
	private final LogHacker logHacker = new LogHacker();
	private static Reference<CSSValidator> instanceCache;

	public static CSSValidator getInstance() {
		CSSValidator instance = null;
		if (instanceCache != null) {
			instance = instanceCache.get();
		}
		if (instance == null) {
			instance = new CSSValidator();
			instanceCache = new SoftReference<CSSValidator>(instance);
		}
		return instance;
	}
	
	private CSSValidator() {
		IModelManager modelManager = StructuredModelManager.getModelManager();
		ICSSModel validatingModel = (ICSSModel) modelManager
				.createUnManagedStructuredModelFor(CSS_CONTENT_TYPE_IDENTIFIER);
		validatingDocument = validatingModel.getStructuredDocument();
		validatingCSS = (CSSStyleSheet) validatingModel.getDocument();
	}

	private void cleanValidatingDocument() {
		validatingDocument.set(""); //$NON-NLS-1$
	}

	/**
	 * Validates value of a CSS attribute.
	 * 
	 * @param value
	 *            the
	 * @return {@code true} if the attribute is valid, {@code false} otherwise
	 */
	public boolean isValidValue(String value) {
		logHacker.disableLogging();
		boolean valid = true;
		try {
			validatingCSS.insertRule(".testSelector {}", 0); //$NON-NLS-1$
			CSSStyleRule cssRule = ((CSSStyleRule) validatingCSS.getCssRules().item(0));
			CSSStyleDeclaration declaration = cssRule.getStyle();
			declaration.setProperty("background", value, Constants.EMPTY); //$NON-NLS-1$
		} catch (Throwable e) {
			valid = false;
		} finally {
			logHacker.enableLogging();
			cleanValidatingDocument();
		}

		return valid;
	}

	/**
	 * Validates CSS selector value.
	 * 
	 * @param selector
	 *            the selector value
	 * @return {@code true} if the selector is valid, {@code false} otherwise
	 */
	public boolean isValidSelector(String selector) {
		validatingDocument.set(selector + "{}"); //$NON-NLS-1$

		CSSRuleList cssRules = validatingCSS.getCssRules();
		if (cssRules.getLength() != 1) {
			// if the selector is like 'a{} b', or it is empty, or the rule cannot be created 
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

	/**
	 * Provides a way to temporarily delete all loggers from
	 * {@link org.eclipse.core.internal.runtime.RuntimeLog}
	 * 
	 * @author yradtsevich
	 * 
	 */
	private class LogHacker {
		private Field logListeners = null;
		private Object oldValue = null;

		public LogHacker() {
			try {
				logListeners = org.eclipse.core.internal.runtime.RuntimeLog.class
						.getDeclaredField("logListeners");
				logListeners.setAccessible(true);
			} catch (Throwable e) {
				JspEditorPlugin.getPluginLog().logError(e);
			}
		}

		public void disableLogging() {
			try {
				oldValue = logListeners.get(null);
				logListeners.set(null, new ArrayList<Object>(0));
			} catch (Throwable e) {
				JspEditorPlugin.getPluginLog().logError(e);
			}
		}

		public void enableLogging() {
			try {
				if (oldValue != null) {
					logListeners.set(null, oldValue);
				}
			} catch (Throwable e) {
				JspEditorPlugin.getPluginLog().logError(e);
			} finally {
				oldValue = null;
			}
		}
	}
}
