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
public class NgImageAttributeProvider extends AngularAttributeProvider {

	private static final HtmlAttribute NG_SRC = new HtmlAttribute("ng-src", Messages.NgImageAttributeProvider_NgSrc); //$NON-NLS-1$
	private static final HtmlAttribute DATA_NG_SRC = new HtmlAttribute("data-ng-src", Messages.NgImageAttributeProvider_NgSrc); //$NON-NLS-1$

	private static final HtmlAttribute NG_SRCSET = new HtmlAttribute("ng-srcset", Messages.NgImageAttributeProvider_NgSrcset); //$NON-NLS-1$
	private static final HtmlAttribute DATA_NG_SRCSET = new HtmlAttribute("data-ng-srcset", Messages.NgImageAttributeProvider_NgSrcset); //$NON-NLS-1$

	public static final HtmlAttribute[] ATTRIBUTES = new HtmlAttribute[] {NG_SRC, DATA_NG_SRC,
		NG_SRCSET, DATA_NG_SRCSET};

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