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
package org.jboss.tools.jst.web.kb.internal.taglib.html;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.jst.web.html.HTMLConstants;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.internal.taglib.CustomTagLibAttribute;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class DatalistsAttributeProvider extends DynamicAttributeValueProvider {
	private static final String TAG_NAME = "datalist";
	private static final String ATTRIBUTE_NAME = "list";

	@Override
	protected CustomTagLibAttribute getAttribute() {
		IFile file = context.getResource();
		IStructuredModel model = null;
		Set<String> datalists = new HashSet<String>();
		try {
			model = StructuredModelManager.getModelManager().getModelForRead(file);
			IDOMDocument xmlDocument = (model instanceof IDOMModel) ? ((IDOMModel) model).getDocument() : null;
			if (xmlDocument == null) {
				return null;
			}
			getDatalists(xmlDocument, datalists);
		} catch (IOException e) {
			WebKbPlugin.getDefault().logError(e);
		} catch (CoreException e) {
			WebKbPlugin.getDefault().logError(e);
		} finally {
			if (model != null) {
				model.releaseFromRead();
			}
		}
		return new HtmlAttribute(ATTRIBUTE_NAME, "", datalists.toArray(new String[0]));
	}

	private void getDatalists(Node node, Set<String> list) {
		if(TAG_NAME.equalsIgnoreCase(node.getNodeName()) && node instanceof Element) {
			String id = ((Element)node).getAttribute(HTMLConstants.ATTR_ID);
			if(id.length() > 0) {
				list.add(id);
			}
			return;
		}
		NodeList ns = node.getChildNodes();
		for (int i = 0; i < ns.getLength(); i++) {
			Node c = ns.item(i);
			if(c.getNodeType() == Node.ELEMENT_NODE) {
				getDatalists(c, list);
			}
		}		
	}
}
