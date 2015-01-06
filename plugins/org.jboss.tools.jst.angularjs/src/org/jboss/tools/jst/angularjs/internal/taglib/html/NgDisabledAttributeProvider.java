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
public class NgDisabledAttributeProvider extends AngularAttributeProvider {

	protected static final HtmlAttribute NG_DISABLED = new HtmlAttribute("ng-disabled", Messages.NgDisabledAttributeProvider_NgDisabled); //$NON-NLS-1$
	protected static final HtmlAttribute DATA_NG_DISABLED = new HtmlAttribute("data-ng-disabled", Messages.NgDisabledAttributeProvider_NgDisabled); //$NON-NLS-1$

	public static final HtmlAttribute[] ATTRIBUTES = new HtmlAttribute[] {NG_DISABLED, DATA_NG_DISABLED};

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