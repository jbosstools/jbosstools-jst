/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.internal.text.ext.hyperlink;

import org.eclipse.osgi.service.runnable.ParameterizedRunnable;
import org.jboss.tools.foundation.ui.util.BrowserUtility;

public class OpenWithBrowserLauncher implements ParameterizedRunnable {

	@Override
	public Object run(Object context) throws Exception {
		new BrowserUtility().openExternalBrowser((String)context);
		return null;
	}

}
