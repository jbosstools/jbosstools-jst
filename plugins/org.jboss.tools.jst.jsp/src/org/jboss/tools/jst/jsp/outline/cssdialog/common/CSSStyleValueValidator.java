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

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;

/**
 * CSS Validator
 * 
 * @author yradtsevich
 * 
 */
@SuppressWarnings("restriction")
public class CSSStyleValueValidator extends CSSValidator implements IValidator {
	private final LogHacker logHacker = new LogHacker();
	private static Reference<CSSStyleValueValidator> instanceCache;

	public static CSSStyleValueValidator getInstance() {
		CSSStyleValueValidator instance = null;
		if (instanceCache != null) {
			instance = instanceCache.get();
		}
		if (instance == null) {
			instance = new CSSStyleValueValidator();
			instanceCache = new SoftReference<CSSStyleValueValidator>(instance);
		}
		return instance;
	}

	private CSSStyleValueValidator() {
		super();
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
			getValidatingCSS().insertRule(".testSelector {}", 0); //$NON-NLS-1$
			CSSStyleRule cssRule = ((CSSStyleRule) getValidatingCSS()
					.getCssRules().item(0));
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
	 * Provides a way to temporarily delete all loggers from
	 * {@link org.eclipse.core.internal.runtime.RuntimeLog}
	 * 
	 * @author yradtsevich
	 * 
	 */
	private static class LogHacker {
		private Field logListeners = null;
		private Object oldValue = null;

		public LogHacker() {
			try {
				logListeners = org.eclipse.core.internal.runtime.RuntimeLog.class
						.getDeclaredField("logListeners"); //$NON-NLS-1$
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

	public IStatus validate(Object value) {
		return isValidValue((String) value) ? Status.OK_STATUS : new Status(
				IStatus.ERROR, JspEditorPlugin.PLUGIN_ID,
				JstUIMessages.CSS_INVALID_STYLE_PROPERTY);
	}
}
