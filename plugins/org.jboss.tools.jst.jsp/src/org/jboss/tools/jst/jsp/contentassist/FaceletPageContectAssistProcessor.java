/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.jsp.contentassist;

import java.util.Map;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.jboss.tools.jst.web.kb.IFaceletPageContext;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.internal.FaceletPageContextImpl;
import org.jboss.tools.jst.web.kb.taglib.INameSpace;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FaceletPageContectAssistProcessor extends JspContentAssistProcessor {

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.jsp.contentassist.JspContentAssistProcessor#createContext()
	 */
	@Override
	protected IPageContext createContext() {
		IPageContext superContext = super.createContext();
		
		
		FaceletPageContextImpl context = new FaceletPageContextImpl();
		context.setResource(superContext.getResource());
		context.setElResolvers(superContext.getElResolvers());
		setVars(context);

		context.setResourceBundles(superContext.getResourceBundles());
		context.setDocument(getDocument());
		setNameSpaces(superContext, context);
		context.setLibraries(getTagLibraries(context));
		
//		IFaceletPageContext getParentContext();
//		Map<String, String> getParams();

		return context;
	}

	protected void setNameSpaces(IPageContext superContext, FaceletPageContextImpl context) {
		IStructuredModel sModel = StructuredModelManager
									.getModelManager()
									.getExistingModelForRead(getDocument());
		if (superContext != null) {
			IRegion region = new Region (0, getDocument().getLength());
			Map<String, INameSpace> nameSpaces = superContext.getNameSpaces(getOffset());
			for (String prefix : nameSpaces.keySet()) {
				context.addNameSpace(region, nameSpaces.get(prefix));
			}
		}
			
		try {
			if (sModel == null)
				return;

			Document xmlDocument = (sModel instanceof IDOMModel) ? ((IDOMModel) sModel)
					.getDocument()
					: null;

			if (xmlDocument == null)
				return;

			Node n = findNodeForOffset(xmlDocument, getOffset());
			while (n != null) {
				if (!(n instanceof Element)) {
					if (n instanceof Attr) {
						n = ((Attr) n).getOwnerElement();
					} else {
						n = n.getParentNode();
					}
					continue;
				}

				NamedNodeMap attrs = n.getAttributes();
				for (int j = 0; attrs != null && j < attrs.getLength(); j++) {
					Attr a = (Attr) attrs.item(j);
					String name = a.getName();
					if (name.startsWith("xmlns:")) {
						final String prefix = name.substring("xmlns:".length());
						final String uri = a.getValue();
						if (prefix != null && prefix.trim().length() > 0 &&
								uri != null && uri.trim().length() > 0) {
								
							// TODO: Check the IRegion instance creation
							IRegion region = new Region(
									((IndexedRegion)n).getStartOffset(),
									((IndexedRegion)n).getLength());
							
							INameSpace nameSpace = new INameSpace(){
							
								public String getURI() {
									return uri.trim();
								}
							
								public String getPrefix() {
									return prefix.trim();
								}
							};
							context.addNameSpace(region, nameSpace);
						}
					}
				}

				n = n.getParentNode();
			}

			return;
		} finally {
			if (sModel != null) {
				sModel.releaseFromRead();
			}
		}
	}
	
	
	@Override
	protected IFaceletPageContext getContext() {
		return (IFaceletPageContext)super.getContext();
	}
}
