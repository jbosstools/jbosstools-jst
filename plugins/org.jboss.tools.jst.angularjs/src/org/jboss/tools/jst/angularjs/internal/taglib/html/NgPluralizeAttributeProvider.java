/******************************************************************************* 
 * Copyright (c) 2013 - 2015 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.angularjs.internal.taglib.html;

import org.jboss.tools.jst.web.kb.internal.taglib.CustomTagLibAttribute;
import org.jboss.tools.jst.web.kb.internal.taglib.html.HtmlAttribute;

/**
 * @author Alexey Kazakov
 */
public class NgPluralizeAttributeProvider extends AngularAttributeProvider {

	private static final HtmlAttribute NG_PLURALIZE = new HtmlAttribute("ng-pluralize", Messages.NgPluralizeAttributeProvider_NgPluralize); //$NON-NLS-1$
	private static final HtmlAttribute DATA_NG_PLURALIZE = new HtmlAttribute("data-ng-pluralize", Messages.NgPluralizeAttributeProvider_NgPluralize); //$NON-NLS-1$

	private static final HtmlAttribute COUNT = new HtmlAttribute("count", Messages.NgPluralizeAttributeProvider_Count); //$NON-NLS-1$

	private static final HtmlAttribute WHEN = new HtmlAttribute("when", Messages.NgPluralizeAttributeProvider_When); //$NON-NLS-1$

	private static final HtmlAttribute OFFSET = new HtmlAttribute("offset", Messages.NgPluralizeAttributeProvider_Offset); //$NON-NLS-1$

	public static final HtmlAttribute[] REQUIRED_ATTRIBUTES = new HtmlAttribute[] {NG_PLURALIZE, DATA_NG_PLURALIZE};
	public static final HtmlAttribute[] CONDITIONAL_ATTRIBUTES = new HtmlAttribute[] {COUNT, WHEN, OFFSET};

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.AbstractAttributeProvider#checkComponent()
	 */
	@Override
	protected boolean checkComponent() {
		return checkNgAttribute("pluralize"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.AbstractAttributeProvider#getConditionalAttributes()
	 */
	@Override
	protected CustomTagLibAttribute[] getConditionalAttributes() {
		return CONDITIONAL_ATTRIBUTES;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.AbstractAttributeProvider#getRequiredAttributes()
	 */
	@Override
	protected CustomTagLibAttribute[] getRequiredAttributes() {
		return REQUIRED_ATTRIBUTES;
	}
}