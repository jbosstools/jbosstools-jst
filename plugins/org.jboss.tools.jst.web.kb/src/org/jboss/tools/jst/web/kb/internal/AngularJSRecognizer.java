/******************************************************************************* 
 * Copyright (c) 2013-2014 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper.ICommand;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;
import org.w3c.dom.NodeList;

/**
 * @author Alexey Kazakov
 */
public class AngularJSRecognizer extends HTMLRecognizer {
	public static final String ANGULAR_JS_LIB_NAME = "angular";
	private static final String ANGULAR_JS_PATTERN = ".*(" + ANGULAR_JS_LIB_NAME + ").*(.js).*";
	public static final String ANGULAR_NG_ATTRIBUTE_PATTERN = "//*/@*[starts-with(name(), 'ng-')]|//*/@*[starts-with(name(), 'data-ng-')]";

	@Override
	protected boolean recalculateResult(ITagLibrary lib, ELContext context, IFile file) {
		if(super.recalculateResult(lib, context, file)) {
			final Boolean[] result = new Boolean[] {JSRecognizer.getJSReferenceVersion(file, ANGULAR_JS_LIB_NAME)!=null};
			if(!result[0]) {
				StructuredModelWrapper.execute(file, new ICommand() {
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
}