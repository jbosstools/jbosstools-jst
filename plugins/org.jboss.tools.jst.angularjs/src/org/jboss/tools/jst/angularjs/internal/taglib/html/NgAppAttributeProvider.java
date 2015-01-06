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
public class NgAppAttributeProvider extends AngularAttributeProvider {

	private static final String[] NG_APP_ATTRIBUTE_NAMES = getNgAttributes("app"); //$NON-NLS-1$

	private static final HtmlAttribute NG_APP = new HtmlAttribute("ng-app", Messages.NgAppAttributeProvider_NgApp); //$NON-NLS-1$
	private static final HtmlAttribute DATA_NG_APP = new HtmlAttribute("data-ng-app", Messages.NgAppAttributeProvider_NgApp); //$NON-NLS-1$

	public static final HtmlAttribute[] ATTRIBUTES = new HtmlAttribute[] {NG_APP, DATA_NG_APP};

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.attribute.AbstractAttributeProvider#checkComponent(org.jboss.tools.jst.web.kb.KbQuery)
	 */
	@Override
	protected boolean checkComponent() {
		return !checkAttributesInParrents(NG_APP_ATTRIBUTE_NAMES);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.attribute.AbstractAttributeProvider#getAttributes()
	 */
	@Override
	protected CustomTagLibAttribute[] getConditionalAttributes() {
		return ATTRIBUTES;
	}
}