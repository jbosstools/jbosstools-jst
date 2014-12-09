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
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.jst.web.kb.taglib.IHTMLLibraryVersion;
import org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;

/**
 * Recognizer for HTML4 and HTML5 files
 * @author Alexey Kazakov
 */
public class HTMLRecognizer implements ITagLibRecognizer {

	protected ELContext lastUsedContext;
	protected boolean lastResult;
	protected ITagLibrary lib;

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer#shouldBeLoaded(org.jboss.tools.jst.web.kb.taglib.ITagLibrary, org.eclipse.core.resources.IResource)
	 */
	@Override
	public boolean shouldBeLoaded(ITagLibrary lib, ELContext context) {
		this.lib = lib;
		return isUsedInternal(lib, context);
	}

	protected boolean recalculateResult(ITagLibrary lib, ELContext context, IFile file) {
		return context instanceof JspContextImpl || FileUtil.isDoctypeHTML(file);
	}

	/**
	 * This is the default implementation used by methods 
	 * - shouldBeLoaded(ITagLibrary lib, ELContext context)
	 * - isUsed(IHTMLLibraryVersion version, ELContext context)
	 * - isUsed(IHTMLLibraryVersion version, IFile file)
	 * When the last two methods were defined, there was no need in a separate implementation
	 * for them in any of inherited classes. 
	 * 
	 * @param lib
	 * @param context
	 * @return
	 */
	protected final boolean isUsedInternal(ITagLibrary lib, ELContext context) {
		if(lastUsedContext!=null && lastUsedContext==context) {
			// The context has not been changed so we do not need to recalculate the result.
			return lastResult;
		}

		lastUsedContext = context;
		lastResult = recalculateResult(lib, context, context.getResource());

		return lastResult;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer#isUsed(org.jboss.tools.jst.web.kb.taglib.IHTMLLibraryVersion, org.jboss.tools.common.el.core.resolver.ELContext)
	 */
	@Override
	public boolean isUsed(IHTMLLibraryVersion version, ELContext context) {
		return isUsedInternal(null, context);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer#isUsed(org.jboss.tools.jst.web.kb.taglib.IHTMLLibraryVersion, org.eclipse.core.resources.IFile)
	 */
	@Override
	public boolean isUsed(IHTMLLibraryVersion version, IFile file) {
		return isUsed(version, PageContextFactory.createPageContext(file));
	}
}