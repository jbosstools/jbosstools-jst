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
package org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.jst.angularjs.internal.ionic.IonicRecognizer;
import org.jboss.tools.jst.web.kb.internal.AngularJSRecognizer;
import org.jboss.tools.jst.web.kb.taglib.IHTMLLibraryVersion;

/**
 * List of Ionic versions supported by Palette.
 * When adding a new version, we should create palette subcategory 
 * under "Ionic" with name of that version, register in plugin.xml wizards 
 *    for its items, etc.

 * @author Viacheslav Kabanovich
 *
 */
public enum IonicVersion implements IHTMLLibraryVersion {
	IONIC_1_0("1.0");

	public static final IonicVersion[] ALL_VERSIONS = {IONIC_1_0};

	String version;

	IonicVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return version;
	}

	public static IonicVersion getLatestDefaultVersion() {
		return IONIC_1_0;
	}

	@Override
	public boolean isPreferredJSLib(IFile file, String libName) {
		if(IonicConstants.IONIC_CATEGORY.equals(libName)) {
			return file == null || !AngularJSRecognizer.isAngularTemplate(file);
		}
		return false;
	}

	@Override
	public boolean isReferencingJSLib(IFile file, String libName) {
		if(IonicConstants.IONIC_CATEGORY.equals(libName)) {
			return IonicRecognizer.isReferencingIonicLib(file);
		}
		return false;
	}

}