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
public class NgAnyInputAttributeProvider extends AngularAttributeProvider {

	private static final HtmlAttribute NG_CHECKED = new HtmlAttribute("ng-checked", Messages.NgAnyInputAttributeProvider_NgChecked); //$NON-NLS-1$
	private static final HtmlAttribute DATA_NG_CHECKED = new HtmlAttribute("data-ng-checked", Messages.NgAnyInputAttributeProvider_NgChecked); //$NON-NLS-1$

	private static final HtmlAttribute NG_LIST = new HtmlAttribute("ng-list", Messages.NgAnyInputAttributeProvider_NgList); //$NON-NLS-1$
	private static final HtmlAttribute DATA_NG_LIST = new HtmlAttribute("data-ng-list", Messages.NgAnyInputAttributeProvider_NgList); //$NON-NLS-1$

	public static final HtmlAttribute[] ATTRIBUTES = new HtmlAttribute[] {NG_CHECKED, DATA_NG_CHECKED,
		NG_LIST, DATA_NG_LIST};

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.AbstractAttributeProvider#checkComponent()
	 */
	@Override
	protected boolean checkComponent() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.internal.taglib.AbstractAttributeProvider#getConditionalAttributes()
	 */
	@Override
	protected CustomTagLibAttribute[] getConditionalAttributes() {
		return ATTRIBUTES;
	}
}