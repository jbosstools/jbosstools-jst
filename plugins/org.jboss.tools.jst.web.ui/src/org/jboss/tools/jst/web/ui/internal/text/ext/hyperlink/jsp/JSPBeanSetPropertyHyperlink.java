/*******************************************************************************
 * Copyright (c) 2007-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.internal.text.ext.hyperlink.jsp;

import java.text.MessageFormat;

import org.jboss.tools.common.text.ext.hyperlink.xpl.Messages;

/**
 * @author Jeremy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JSPBeanSetPropertyHyperlink extends JSPBeanGetPropertyHyperlink {

	protected String getMethodPrefix() {
		return SET_METHOD_PREFIX;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IHyperlink#getHyperlinkText()
	 */
	public String getHyperlinkText() {
		String propertyName = getPropertyName(getHyperlinkRegion());
		if (propertyName == null)
			return  MessageFormat.format(Messages.OpenA, Messages.Setter);
		
		return MessageFormat.format(Messages.OpenGetterOrSetterForProperty, Messages.Setter, propertyName);
	}
}
