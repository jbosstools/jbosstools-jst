/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.web.kb.taglib.IHTMLLibraryVersion;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;

/**
 * Recognizer for HTML5 files
 * @author Alexey Kazakov
 */
public class HTML5Recognizer extends HTMLRecognizer {

	@Override
	protected boolean recalculateResult(ITagLibrary lib, ELContext context, IFile file) {
		return FileUtil.isDoctypeHTML(file);
	}

	@Override
	public boolean isUsed(IHTMLLibraryVersion version, ELContext context) {
		return FileUtil.isDoctypeHTML(context.getResource());
	}
}