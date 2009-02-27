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
package org.jboss.tools.jst.jsp.test;

import org.jboss.tools.jst.jsp.preferences.JSPOccurrencesPreferencePage;
import org.jboss.tools.tests.PreferencePageTest;

import junit.framework.TestCase;

/**
 * @author eskimo
 *
 */
public class JspPreferencesPageTest extends PreferencePageTest {
	
	public void testJSPOccurrencesPreferencePage() {
		doDefaultTest("org.eclipse.wst.sse.ui.preferences.jsp.occurrences",JSPOccurrencesPreferencePage.class);
	}
}
