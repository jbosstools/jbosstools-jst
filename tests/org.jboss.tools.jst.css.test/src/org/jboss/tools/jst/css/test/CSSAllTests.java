/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.css.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.tools.test.util.ProjectImportTestSetup;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class CSSAllTests {

	public static final String RESOURCE_PATH = "resources/"; //$NON-NLS-1$

	public static Test suite() {

		TestSuite suite = new TestSuite("Tests for CSS views"); //$NON-NLS-1$
		// $JUnit-BEGIN$
		suite.addTestSuite(CSSViewTest.class);
		// $JUnit-END$

		return new ProjectImportTestSetup(
				suite,
				CSSTestPlugin.PLUGIN_ID,
				new String[] { RESOURCE_PATH + CSSViewTest.IMPORT_PROJECT_NAME },
				new String[] { CSSViewTest.IMPORT_PROJECT_NAME });
	}
}
