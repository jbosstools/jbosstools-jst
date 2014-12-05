/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.test.palette;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.jst.web.kb.taglib.IHTMLLibraryVersion;

public enum TestLibraryVersion implements IHTMLLibraryVersion {
	TEST_1_0("1.0");

	String version;

	TestLibraryVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return version;
	}

	@Override
	public boolean isPreferredJSLib(IFile file, String libName) {
		return false;
	}

	@Override
	public boolean isReferencingJSLib(IFile file, String libName) {
		return false;
	}
}
