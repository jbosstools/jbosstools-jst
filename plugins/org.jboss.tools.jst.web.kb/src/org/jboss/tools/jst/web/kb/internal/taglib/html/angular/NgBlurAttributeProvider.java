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
public class NgBlurAttributeProvider extends AngularAttributeProvider {

	private static final HtmlAttribute NG_BLUR = new HtmlAttribute("ng-blur", Messages.NgBlurAttributeProvider_NgBlur); //$NON-NLS-1$
	private static final HtmlAttribute DATA_NG_BLUR = new HtmlAttribute("data-ng-blur", Messages.NgBlurAttributeProvider_NgBlur); //$NON-NLS-1$

	private static final HtmlAttribute NG_FOCUS = new HtmlAttribute("ng-focus", Messages.NgBlurAttributeProvider_NgFocus); //$NON-NLS-1$
	private static final HtmlAttribute DATA_NG_FOCUS = new HtmlAttribute("data-ng-focus", Messages.NgBlurAttributeProvider_NgFocus); //$NON-NLS-1$

	public static final HtmlAttribute[] ATTRIBUTES = new HtmlAttribute[] {NG_BLUR, DATA_NG_BLUR,
		NG_FOCUS, DATA_NG_FOCUS};

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.attribute.AbstractAttributeProvider#checkComponent(org.jboss.tools.jst.web.kb.KbQuery)
	 */
	@Override
	protected boolean checkComponent() {
		return true;
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