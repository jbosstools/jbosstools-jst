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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.taglib.IHTMLLibraryVersion;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Alexey Kazakov
 */
public abstract class JSRecognizer extends HTML5Recognizer {

	protected abstract String getJSPattern();
	protected abstract String getJSLibName();

	@Override
	protected boolean recalculateResult(ITagLibrary lib, ELContext context, IFile file) {
		return getJSReferenceVersion(file, getJSLibName())!=null;
	}

	public static String getJSReferenceVersion(IFile file, String jsLibName) {
		return getJSReferenceVersion(file, jsLibName, false);
	}

	@Override
	public IHTMLLibraryVersion getVersion(ELContext context) {
		return findVersion(getJSReferenceVersion(context.getResource(), getJSLibName()));
	}

	/**
	 * Returns the IHTMLLibraryVersion representing the specified string version of the library. 
	 * May return null or the default version (it's up to implementation) if the string version 
	 * is null or unknown.
	 * @param version specified string version of the library
	 * @return
	 */
	protected IHTMLLibraryVersion findVersion(String version) {
		return null;
	}

	/**
	 * Return the version number of the JS library.
	 * If the link to the JS file is found but no version defined then return an empty string.
	 * If no link found or if not an HTML5 document then return null.
	 * @param file
	 * @param jsLibName
	 * @param lookAtSrcAttributeOnly
	 * @param html5Only
	 * @return
	 */
	public static String getJSReferenceVersion(IFile file, String jsLibName, boolean lookAtSrcAttributeOnly, boolean html5Only) {
		if(html5Only) {
			String doctype = FileUtil.getDoctype(FileUtil.getContentFromEditorOrFile(file));
			if("html".equalsIgnoreCase(doctype) 
					|| (doctype == null && file.getName().endsWith(".html"))
						) {
				//ok
			} else {
				return null;
			}
		}
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
							String srcAttributeValue = getAttribute(script, "src");
							String text = srcAttributeValue;
							if(!lookAtSrcAttributeOnly) {
								String textContent = script.getTextContent();
								text = new StringBuilder(srcAttributeValue).append("\n").append(textContent).toString();
							}
							String[] lines = text.split("[\r\n]+");
							for (String line : lines) {
								String scriptText = find(line, ".*(" + jsLibName + ")(.*)(.js).*", 2);
						        if(scriptText!=null) {
						        	String version = find(scriptText, ".*?(\\d.\\d).*", 1);
						        	if(version!=null) {
						        		return version;
						        	}
									return "";
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
		return null;
	}

	/**
	 * Return the version number of the JS library.
	 * If the link to the JS file is found but no version defined then return an empty string.
	 * If no link found or if not an HTML5 document then return null.
	 * @param file
	 * @param jsLibName
	 * @param lookAtSrcAttributeOnly
	 * @return
	 */
	protected static String getJSReferenceVersion(IFile file, String jsLibName, boolean lookAtSrcAttributeOnly) {
		return getJSReferenceVersion(file, jsLibName, lookAtSrcAttributeOnly, true);
	}

	private static String find(String text, String pattern, int group) {
        String result = null;
		Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        if(m.matches()) {
        	result = m.group(group);
        }
        return result;
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