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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Alexey Kazakov
 */
public abstract class JSRecognizer implements ITagLibRecognizer {

	private ELContext lastUsedContext;
	private boolean lastResult;

	protected abstract String getJSPattern();

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.taglib.ITagLibRecognizer#shouldBeLoaded(org.jboss.tools.jst.web.kb.taglib.ITagLibrary, org.eclipse.core.resources.IResource)
	 */
	@Override
	public boolean shouldBeLoaded(ITagLibrary lib, ELContext context) {
		if(lastUsedContext!=null) {
			if(lastUsedContext==context) {
				// The context didn't change so we don'y need to recalculate the result.
				return lastResult;
			}
		}
		lastUsedContext = context;
		IResource resource = context.getResource();
		lastResult = false;
		if(resource instanceof IFile) {
			IFile file = (IFile) resource;
			if(FileUtil.isDoctypeHTML(file)) {
				lastResult = containsJSReference(file, getJSPattern());
			}
		}
		return lastResult;
	}

	protected static boolean containsJSReference(IFile file, String pattern) {
		return containsJSReference(file, pattern, false);
	}

	protected static boolean containsJSReference(IFile file, String pattern, boolean lookAtSrcAttributeOnly) {
		IStructuredModel model = null;
		try {
			model = StructuredModelManager.getModelManager().getModelForRead(file);
			IDOMDocument xmlDocument = (model instanceof IDOMModel) ? ((IDOMModel) model).getDocument() : null;
			if(xmlDocument != null) {
				Element htmlNode = findChildElement(xmlDocument, "html");
				if(htmlNode!=null) {
					Element headNode = findChildElement(htmlNode, "head");
					if(headNode!=null) {
						Element[] scriptNodes = findChildElements(headNode, "script");
						for (Element script : scriptNodes) {
							String text = getAttribute(script, "src");
							if(Pattern.matches(pattern, text)) {
								return true;
							}
							if(!lookAtSrcAttributeOnly) {
								text = script.getTextContent();
								String[] lines = text.split("\n");
								for (String line : lines) {
									if(Pattern.matches(pattern, line)) {
										return true;
									}
								}
							}
						}
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

	public static String getAttribute(Element element, String attributeName) {
		NamedNodeMap nodeMap = element.getAttributes();
		for (int i = 0; i < nodeMap.getLength(); i++) {
			Node attribute = nodeMap.item(i);
			if(attributeName.equalsIgnoreCase(attribute.getNodeName())) {
				return attribute.getNodeValue();
			}
		}
		return "";
	}

	public static Element findChildElement(Node parentNode, String elementName) {
		Element[] elements = findChildElements(parentNode, elementName);
		if(elements.length==0) {
			return null;
		}
		return elements[0];
	}

	public static Element[] findChildElements(Node parentNode, String elementName) {
		List<Element> elements = new ArrayList<Element>();
		NodeList list = parentNode.getChildNodes();
		for(int i = 0; i < list.getLength(); i++) {
			Node child = list.item(i);
			if(child instanceof Element&& child.getNodeName().equalsIgnoreCase(elementName)) {
				elements.add((Element)child);
			}
		}
		return elements.toArray(new Element[elements.size()]);
	}
}