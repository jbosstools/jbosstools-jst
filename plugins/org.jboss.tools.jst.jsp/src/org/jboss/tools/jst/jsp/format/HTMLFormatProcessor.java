 /*******************************************************************************
  * Copyright (c) 2007 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.jst.jsp.format;

import org.eclipse.wst.html.core.internal.format.HTMLElementFormatter;
import org.eclipse.wst.html.core.internal.format.HTMLFormatProcessorImpl;
import org.eclipse.wst.sse.core.internal.format.IStructuredFormatter;
import org.w3c.dom.Node;

/**
 * This Processor formats HTML.
 * @author Alexey Kazakov
 */
public class HTMLFormatProcessor extends HTMLFormatProcessorImpl {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.html.core.internal.format.HTMLFormatProcessorImpl#getFormatter(org.w3c.dom.Node)
	 */
	protected IStructuredFormatter getFormatter(Node node) {
		IStructuredFormatter formatter = super.getFormatter(node);
		if(formatter instanceof HTMLElementFormatter) {
			formatter = new HTMLExtendedElementFormatter();
			formatter.setFormatPreferences(getFormatPreferences());
		}
		return formatter;
	}
}