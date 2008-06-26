/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.debug;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.jdt.internal.debug.ui.JDIModelPresentation;

/**
 * @author igels
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class WebDebugModelPresentation extends JDIModelPresentation {

	protected String getBreakpointText(IBreakpoint breakpoint) {

		if (breakpoint instanceof IBreakpointPresentation) {
			return ((IBreakpointPresentation)breakpoint).getLabelText();
		}
		return super.getBreakpointText(breakpoint);
	}
}