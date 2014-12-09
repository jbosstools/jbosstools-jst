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

import java.io.IOException;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper.ICommand;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.web.html.HTMLConstants;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Alexey Kazakov
 */
public class AngularJSRecognizer extends HTMLRecognizer {
	private static final String ANGULAR_JS_LIB_NAME = "angular";
	private static final String ANGULAR_JS_PATTERN = ".*(" + ANGULAR_JS_LIB_NAME + ").*(.js).*";
	private static final String ANGULAR_NG_ATTRIBUTE_PATTERN = "//*/@*[starts-with(name(), 'ng-')]|//*/@*[starts-with(name(), 'data-ng-')]";

	@Override
	protected boolean recalculateResult(ITagLibrary lib, ELContext context, IFile file) {
		if(super.recalculateResult(lib, context, file)) {
			final Boolean[] result = new Boolean[] {JSRecognizer.getJSReferenceVersion(context.getResource(), ANGULAR_JS_LIB_NAME)!=null};
			if(!result[0]) {
				StructuredModelWrapper.execute(context.getResource(), new ICommand() {
					public void execute(IDOMDocument xmlDocument) {
						try {
							NodeList list = (NodeList) XPathFactory.newInstance().newXPath().compile(ANGULAR_NG_ATTRIBUTE_PATTERN).evaluate(xmlDocument,XPathConstants.NODESET);
							for (int i = 0; i < list.getLength(); i++) {
								IDOMAttr attr = ((IDOMAttr)  list.item(i));
							}
							result[0] = list.getLength()>0;
						} catch (XPathExpressionException e) {
							WebKbPlugin.getDefault().logError(e);
						}
					}
				});
			}
			return result[0];
		}
		return false;
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
		String ext = file.getFileExtension();
		if(ext==null || !ext.toLowerCase().startsWith("htm")) {
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