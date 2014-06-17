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

import static org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttributeConstants.TYPE_CHECKBOX;

import org.jboss.tools.jst.web.kb.internal.taglib.CustomTagLibAttribute;
import org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttribute;

/**
 * @author Alexey Kazakov
 */
public class NgInputCheckboxAttributeProvider extends AngularAttributeProvider {

	private static final HtmlAttribute NG_TRUE_VALUE = new HtmlAttribute("ng-true-value", Messages.NgInputCheckboxAttributeProvider_NgTrueValue); //$NON-NLS-1$
	private static final HtmlAttribute DATA_NG_TRUE_VALUE = new HtmlAttribute("data-ng-true-value", Messages.NgInputCheckboxAttributeProvider_NgTrueValue); //$NON-NLS-1$

	private static final HtmlAttribute NG_FALSE_VALUE = new HtmlAttribute("ng-false-value", Messages.NgInputCheckboxAttributeProvider_NgFalseValue); //$NON-NLS-1$
	private static final HtmlAttribute DATA_NG_FALSE_VALUE = new HtmlAttribute("data-ng-false-value", Messages.NgInputCheckboxAttributeProvider_NgFalseValue); //$NON-NLS-1$

	public static final HtmlAttribute[] ATTRIBUTES = new HtmlAttribute[] {NG_MODEL, DATA_NG_MODEL,
		NG_TRUE_VALUE, DATA_NG_TRUE_VALUE,
		NG_FALSE_VALUE, DATA_NG_FALSE_VALUE,
		NG_CHANGE, DATA_NG_CHANGE};

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.AbstractAttributeProvider#checkComponent()
	 */
	@Override
	protected boolean checkComponent() {
		return checkAttribute(TYPE_CHECKBOX);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.AbstractAttributeProvider#getConditionalAttributes()
	 */
	@Override
	protected CustomTagLibAttribute[] getConditionalAttributes() {
		return ATTRIBUTES;
	}
}