/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.test;

import java.io.File;

import junit.framework.TestCase;

import org.jboss.tools.jst.web.kb.taglib.TagLibraryManager;

/**
 * @author Alexey Kazakov
 */
public class XMLCatalogTest extends TestCase {

	public void testJSFLibs() {
		File file = TagLibraryManager.getStaticTLD("http://java.sun.com/jsf/html");
		assertNotNull(file);
		assertTrue(file.exists());
		file = TagLibraryManager.getStaticTLD("http://java.sun.com/jsf/core");
		assertNotNull(file);
		assertTrue(file.exists());
	}

	public void testRichFacesLibs() {
		File file = TagLibraryManager.getStaticTLD("http://richfaces.org/rich");
		assertNotNull(file);
		assertTrue(file.exists());
		file = TagLibraryManager.getStaticTLD("http://richfaces.org/a4j");
		assertNotNull(file);
		assertTrue(file.exists());
	}
}