/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.angularjs.test;

import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.IonicConstants;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.IonicVersion;
import org.jboss.tools.jst.web.ui.internal.preferences.js.JSLib;
import org.jboss.tools.jst.web.ui.internal.preferences.js.JSLibFactory;
import org.jboss.tools.jst.web.ui.internal.preferences.js.JSLibModel;
import org.jboss.tools.jst.web.ui.internal.preferences.js.JSLibVersion;

import junit.framework.TestCase;

public class DefaultJSLibsText extends TestCase {

	public DefaultJSLibsText() {
	}

	public void testIonicDefaultLib() {
		JSLibModel model = JSLibFactory.getInstance().getDefaultModel();
		assertNotNull(model);
		JSLib lib = model.getLib(IonicConstants.IONIC_CATEGORY);
		assertNotNull(lib);
		JSLibVersion v = lib.getVersion(IonicVersion.IONIC_1_0.toString());
		assertNotNull(v);
		String[] urls = v.getSortedUrls();
		assertEquals(2, urls.length);
	}
}
