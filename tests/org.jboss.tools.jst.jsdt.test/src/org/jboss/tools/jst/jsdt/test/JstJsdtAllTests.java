/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsdt.test;

import org.jboss.tools.test.util.ProjectImportTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * 
 * JUnit Tests for Tern.java integration
 * 
 * @author Victor Rubezhny
 *
 */
public class JstJsdtAllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite(JstJsdtAllTests.class.getName());
		suite.addTest(new ProjectImportTestSetup(new TestSuite(
				JstJsdtTernCATest.class),
				"org.jboss.tools.jst.jsdt.test", "projects/JavaScriptProject", //$NON-NLS-1$ //$NON-NLS-2$
				"JavaScriptProject")); //$NON-NLS-1$
		
		return suite;
	}
}
