/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.taglib.html.angular;

import static org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttributeConstants.ENUM_TRUE_FALSE;

import org.jboss.tools.jst.web.kb.internal.taglib.AbstractAttributeProvider;
import org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttribute;

/**
 * @author Alexey Kazakov
 */
public abstract class AngularAttributeProvider extends AbstractAttributeProvider {

	protected static final String[] ATTRIBUTE_PREFIXES = new String[]{"data-ng-", "ng-"}; //$NON-NLS-1$ //$NON-NLS-2$
	protected static final String CLICK = "click"; //$NON-NLS-1$

	protected static final HtmlAttribute NG_MODEL = new HtmlAttribute("ng-model", Messages.AngularAttributeProvider_NgModel); //$NON-NLS-1$
	protected static final HtmlAttribute DATA_NG_MODEL = new HtmlAttribute("data-ng-model", Messages.AngularAttributeProvider_NgModel); //$NON-NLS-1$

	protected static final HtmlAttribute NG_REQUIRED = new HtmlAttribute("ng-required", Messages.AngularAttributeProvider_NgRequired, ENUM_TRUE_FALSE); //$NON-NLS-1$
	protected static final HtmlAttribute DATA_NG_REQUIRED = new HtmlAttribute("data-ng-required", Messages.AngularAttributeProvider_NgRequired, ENUM_TRUE_FALSE); //$NON-NLS-1$

	protected static final HtmlAttribute NG_MINLENGTH = new HtmlAttribute("ng-minlength", Messages.AngularAttributeProvider_NgMinlength); //$NON-NLS-1$
	protected static final HtmlAttribute DATA_NG_MINLENGTH = new HtmlAttribute("data-ng-minlength", Messages.AngularAttributeProvider_NgMinlength); //$NON-NLS-1$

	protected static final HtmlAttribute NG_MAXLENGTH = new HtmlAttribute("ng-maxlength", Messages.AngularAttributeProvider_NgMaxlength); //$NON-NLS-1$
	protected static final HtmlAttribute DATA_NG_MAXLENGTH = new HtmlAttribute("data-ng-maxlength", Messages.AngularAttributeProvider_NgMaxlength); //$NON-NLS-1$

	protected static final HtmlAttribute NG_PATTERN = new HtmlAttribute("ng-pattern", Messages.AngularAttributeProvider_NgPattern); //$NON-NLS-1$
	protected static final HtmlAttribute DATA_NG_PATTERN = new HtmlAttribute("data-ng-pattern", Messages.AngularAttributeProvider_NgPattern); //$NON-NLS-1$

	protected static final HtmlAttribute NG_CHANGE = new HtmlAttribute("ng-change", Messages.AngularAttributeProvider_NgChange); //$NON-NLS-1$
	protected static final HtmlAttribute DATA_NG_CHANGE = new HtmlAttribute("data-ng-change", Messages.AngularAttributeProvider_NgChange); //$NON-NLS-1$

	protected boolean checkNgAttribute(String attributeName) {
		for (String prefix : ATTRIBUTE_PREFIXES) {
			if(checkAttribute(prefix + attributeName)) {
				return true;
			}
		}
		return false;
	}

	public static String[] getNgAttributes(String attributeName) {
		String[] result = new String[ATTRIBUTE_PREFIXES.length];
		for (int i = 0; i < ATTRIBUTE_PREFIXES.length; i++) {
			result[i] = ATTRIBUTE_PREFIXES[i] + attributeName;
		}
		return result;
	}
}