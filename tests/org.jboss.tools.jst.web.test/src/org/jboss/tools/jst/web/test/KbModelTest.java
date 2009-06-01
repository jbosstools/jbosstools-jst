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
package org.jboss.tools.jst.web.test;

import org.eclipse.core.resources.IProject;
import org.jboss.tools.common.test.util.TestProjectProvider;

import junit.framework.TestCase;

public class KbModelTest extends TestCase {

	TestProjectProvider provider = null;
	IProject project = null;
	boolean makeCopy = true;

	public KbModelTest() {}

	public void setUp() throws Exception {

	}

	public void testXMLScanner() {
		
	}

	public void testKbProjectObjects() {
		
	}

	public void testXMLSerialization() {
		
	}

	public void testCleanBuild() {

	}

	protected void tearDown() throws Exception {
		if(provider != null) {
			provider.dispose();
		}
	}
}
