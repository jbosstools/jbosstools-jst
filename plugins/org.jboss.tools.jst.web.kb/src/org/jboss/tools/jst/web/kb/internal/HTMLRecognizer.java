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
import org.eclipse.core.resources.IResource;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;

/**
 * Recognizer for HTML4 and HTML5 files
 * @author Alexey Kazakov
 */
public class HTMLRecognizer implements ITagLibRecognizer {

	protected ELContext lastUsedContext;
	protected boolean lastResult;

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer#shouldBeLoaded(org.jboss.tools.jst.web.kb.taglib.ITagLibrary, org.eclipse.core.resources.IResource)
	 */
	@Override
	public boolean shouldBeLoaded(ITagLibrary lib, ELContext context) {
		if(lastUsedContext!=null && lastUsedContext==context) {
			// The context has not been changed so we do not need to recalculate the result.
			return lastResult;
		}

		lastUsedContext = context;
		IResource resource = context.getResource();
		lastResult = false;
		if(resource instanceof IFile) {
			IFile file = (IFile) resource;
			lastResult = recalculateResult(lib, context, file);
		}

		return lastResult;
	}

	protected boolean recalculateResult(ITagLibrary lib, ELContext context, IFile file) {
		return context instanceof JspContextImpl || FileUtil.isDoctypeHTML(file);
	}
}