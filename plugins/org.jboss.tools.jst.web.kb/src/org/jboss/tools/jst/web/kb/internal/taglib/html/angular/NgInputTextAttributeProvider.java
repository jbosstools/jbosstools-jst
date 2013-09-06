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

import org.jboss.tools.jst.web.kb.internal.taglib.CustomTagLibAttribute;
import org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttribute;

/**
 * @author Alexey Kazakov
 */
public class NgInputTextAttributeProvider extends AngularAttributeProvider {

	private static final HtmlAttribute NG_TRIM = new HtmlAttribute("ng-trim", Messages.NgInputTextAttributeProvider_NgTrim); //$NON-NLS-1$
	private static final HtmlAttribute DATA_NG_TRIM = new HtmlAttribute("data-ng-trim", Messages.NgInputTextAttributeProvider_NgTrim); //$NON-NLS-1$

	public static final HtmlAttribute[] ATTRIBUTES = new HtmlAttribute[] {NG_MODEL, DATA_NG_MODEL,
		NG_REQUIRED, DATA_NG_REQUIRED,
		NG_MINLENGTH, DATA_NG_MINLENGTH,
		NG_MAXLENGTH, DATA_NG_MAXLENGTH,
		NG_PATTERN, DATA_NG_PATTERN,
		NG_CHANGE, DATA_NG_CHANGE,
		NG_TRIM, DATA_NG_TRIM};

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.AbstractAttributeProvider#checkComponent()
	 */
	@Override
	protected boolean checkComponent() {
		return checkAttribute(TYPE_TEXT);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.AbstractAttributeProvider#getConditionalAttributes()
	 */
	@Override
	protected CustomTagLibAttribute[] getConditionalAttributes() {
		return ATTRIBUTES;
	}
}