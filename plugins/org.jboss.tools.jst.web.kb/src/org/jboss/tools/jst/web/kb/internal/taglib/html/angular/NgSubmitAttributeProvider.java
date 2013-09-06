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
public class NgSubmitAttributeProvider extends AngularAttributeProvider {

	private static final HtmlAttribute[] CONDITINAL_ATTRIBUTES = new HtmlAttribute[] {new HtmlAttribute("ng-submit", Messages.NgSubmitAttributeProvider_NgSubmit), new HtmlAttribute("data-ng-submit", Messages.NgSubmitAttributeProvider_NgSubmit)}; //$NON-NLS-1$ //$NON-NLS-2$

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.attribute.AbstractAttributeProvider#checkComponent(org.jboss.tools.jst.web.kb.KbQuery)
	 */
	@Override
	protected boolean checkComponent() {
		return !checkNgAttribute(CLICK);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.attribute.AbstractAttributeProvider#getAttributes()
	 */
	@Override
	protected CustomTagLibAttribute[] getConditionalAttributes() {
		return CONDITINAL_ATTRIBUTES;
	}
}