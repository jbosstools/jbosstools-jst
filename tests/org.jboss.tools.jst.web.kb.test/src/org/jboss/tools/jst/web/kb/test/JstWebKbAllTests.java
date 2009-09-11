/*******************************************************************************
 * Copyright (c) 2009 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.tools.test.util.ProjectImportTestSetup;

/**
 * @author Alexey Kazakov
 */
public class JstWebKbAllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(JstWebKbAllTests.class.getName());
		suite.addTest(new ProjectImportTestSetup(new TestSuite(WebKbTest.class),
				"org.jboss.tools.jst.web.kb.test",
				new String[]{"projects/TestKbModel"},
				new String[]{"TestKbModel"}));
		suite.addTestSuite(KbModelTest.class);
		return suite;
	}
}