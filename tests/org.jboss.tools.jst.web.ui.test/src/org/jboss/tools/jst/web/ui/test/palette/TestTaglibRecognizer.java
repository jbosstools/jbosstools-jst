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
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;

public class TestTaglibRecognizer implements ITagLibRecognizer {

	@Override
	public boolean shouldBeLoaded(ITagLibrary lib, ELContext context) {
		return false;
	}

	@Override
	public boolean isUsed(ELContext context) {
		return false;
	}

	@Override
	public boolean isUsed(IFile file) {
		return false;
	}
}