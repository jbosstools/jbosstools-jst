/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.angularjs.internal.ionic;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.IonicVersion;
import org.jboss.tools.jst.web.kb.internal.AngularJSRecognizer;
import org.jboss.tools.jst.web.kb.internal.HTMLRecognizer;
import org.jboss.tools.jst.web.kb.internal.JSRecognizer;
import org.jboss.tools.jst.web.kb.internal.JspContextImpl;
import org.jboss.tools.jst.web.kb.taglib.IHTMLLibraryVersion;
import org.jboss.tools.jst.web.kb.taglib.ITagLibVersionRecognizer;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;

/**
 * @author Alexey Kazakov
 */
public class IonicRecognizer extends HTMLRecognizer implements ITagLibVersionRecognizer {

	private static final String JS_LIB_NAME = "ionic";

	@Override
	protected boolean recalculateResult(ITagLibrary lib, ELContext context, IFile file) {
		if(context instanceof JspContextImpl && AngularJSRecognizer.isAngularTemplate(file)) {
		    return true;
		}
		if(FileUtil.isDoctypeHTML(file)) {
			return (JSRecognizer.getJSReferenceVersion(file, JS_LIB_NAME)!=null);
		}
		return false;
	}

	@Override
	public IHTMLLibraryVersion getVersion(ELContext context) {
		return isUsed(context) ? IonicVersion.IONIC_1_0 : null;
	}
	/**
	 * Returns true if file has link to *.js or *.css resource 
	 * with occurance of 'ionic' in the name of link.
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isReferencingIonicLib(IFile file) {
		return JSRecognizer.getJSReferenceVersion(file,  JS_LIB_NAME) != null;
	}
}