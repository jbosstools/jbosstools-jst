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
public class NgSwitchAttributeProvider extends AngularAttributeProvider {

	private static final HtmlAttribute NG_SWITCH = new HtmlAttribute("ng-switch", Messages.NgSwitchAttributeProvider_NgSwitch); //$NON-NLS-1$
	private static final HtmlAttribute DATA_NG_SWITCH = new HtmlAttribute("data-ng-switch", Messages.NgSwitchAttributeProvider_NgSwitch); //$NON-NLS-1$

	private static final HtmlAttribute NG_SWITCH_WHEN = new HtmlAttribute("ng-switch-when", ""); //$NON-NLS-1$ //$NON-NLS-2$
	private static final HtmlAttribute DATA_NG_SWITCH_WHEN = new HtmlAttribute("data-ng-switch-when", ""); //$NON-NLS-1$ //$NON-NLS-2$

	private static final HtmlAttribute NG_SWITCH_DEFAULT = new HtmlAttribute("ng-switch-default", ""); //$NON-NLS-1$ //$NON-NLS-2$
	private static final HtmlAttribute DATA_NG_SWITCH_DEFAULT = new HtmlAttribute("data-ng-switch-default", ""); //$NON-NLS-1$ //$NON-NLS-2$

	public static final HtmlAttribute[] SWITCH_HEADER_ATTRIBUTES = new HtmlAttribute[] {NG_SWITCH, DATA_NG_SWITCH};

	public static final HtmlAttribute[] SWITCH_BODY_ATTRIBUTES = new HtmlAttribute[] {NG_SWITCH_WHEN, DATA_NG_SWITCH_WHEN,
		NG_SWITCH_DEFAULT, DATA_NG_SWITCH_DEFAULT};

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
		return checkNgAttribute("switch")?SWITCH_BODY_ATTRIBUTES:SWITCH_HEADER_ATTRIBUTES; //$NON-NLS-1$
	}
}