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

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.internal.HTMLRecognizer;
import org.jboss.tools.jst.web.kb.internal.JSRecognizer;
import org.jboss.tools.jst.web.kb.internal.JspContextImpl;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;
import org.jboss.tools.jst.web.ui.palette.html.wizard.HTMLConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Alexey Kazakov
 */
public class IonicRecognizer extends HTMLRecognizer {

	private static final String JS_LIB_NAME = "ionic";

	@Override
	protected boolean recalculateResult(ITagLibrary lib, ELContext context, IFile file) {
		if(FileUtil.isDoctypeHTML(file)) {
			// HTLM5
			if(JSRecognizer.getJSReferenceVersion(file, JS_LIB_NAME)!=null) {
				// Has Ionic JS links
				return true;
			}
		} else if(context instanceof JspContextImpl && JSRecognizer.getJSReferenceVersion(file, "", false, false)==null) {
			// HTML without any JS links
			return true;
		}
		return false;
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

	/**
	 * File is Angular template if it satisfies all of the following
	 * 1. *.html
	 * 2. Has no doctype declaration
	 * 3. Is not empty and has at least one element.
	 * 4. Its first element is not <html> or <head> or <body>.
	 * @param file
	 * @return
	 */
	public static boolean isAngularTemplate(IFile file) {
		if(!file.getName().toLowerCase().endsWith(".html")) {
			return false;
		}
		String doctype = FileUtil.getDoctype(FileUtil.getContentFromEditorOrFile(file));
		if(doctype != null) {
			return false;
		}

		IStructuredModel model = null;
		try {
			model = StructuredModelManager.getModelManager().getModelForRead(file);
			IDOMDocument xmlDocument = (model instanceof IDOMModel) ? ((IDOMModel) model).getDocument() : null;
			if(xmlDocument == null) {
				return false;
			}
			NodeList list = xmlDocument.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				Node n = list.item(i);
				if(n instanceof Element) {
					String name = ((Element)n).getNodeName().toLowerCase();
					if(name.equals(HTMLConstants.TAG_HTML)
						|| name.equals(HTMLConstants.TAG_BODY)
						|| name.equals(HTMLConstants.TAG_HEAD)) {
						return false;
					} else {
						return true;
					}
				}
			}
		} catch (IOException e) {
			WebKbPlugin.getDefault().logError(e);
		} catch (CoreException e) {
			WebKbPlugin.getDefault().logError(e);
		} finally {
			if (model != null) {
				model.releaseFromRead();
			}
		}
		return false;
	}

}