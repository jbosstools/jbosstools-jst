/*******************************************************************************
 * Copyright (c) 2007-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.internal.text.ext.hyperlink.jsp;

import java.text.MessageFormat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.common.text.ext.hyperlink.ClassMethodHyperlink;
import org.jboss.tools.common.text.ext.hyperlink.xpl.Messages;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper;
import org.jboss.tools.common.text.ext.util.Utils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Jeremy
 *
 */
public class JSPBeanGetPropertyHyperlink extends ClassMethodHyperlink {
	private static final String USEBEAN_TAGNAME = "jsp:useBean"; //$NON-NLS-1$
	private static final String ID_ATTRNAME = "id"; //$NON-NLS-1$
	private static final String NAME_ATTRNAME = "name"; //$NON-NLS-1$
	private static final String CLASS_ATTRNAME = "class"; //$NON-NLS-1$
	private static final String TYPE_ATTRNAME = "type"; //$NON-NLS-1$
	protected static final String GET_METHOD_PREFIX = "get"; //$NON-NLS-1$
	protected static final String SET_METHOD_PREFIX = "set"; //$NON-NLS-1$

	protected String getMethodPrefix() {
		return GET_METHOD_PREFIX;
	}
	
	private String getMethodName(String propertyName) {
		String name = propertyName.trim();
		// Capitalize first letter of the name
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		return getMethodPrefix() + name;
	}
	
	protected String getClassName(IRegion region) {
		return null;
	}
	protected String getMethodName(IRegion region) {
		return null;
	}
	
	/** 
	 * @see com.ibm.sse.editor.AbstractHyperlink#doHyperlink(org.eclipse.jface.text.IRegion)
	 */
	protected void doHyperlink(IRegion region) {
		try {
			String beanID = getBeanId(region);
			String propertyName = getPropertyName(region);
			
			Element elementByID = findElementByIDBackward(beanID, region.getOffset(), USEBEAN_TAGNAME);
			String className = getAttributeValue(elementByID, CLASS_ATTRNAME);
			String typeName = getAttributeValue(elementByID, TYPE_ATTRNAME);
			
			IJavaElement beanMethod = null;
			if (className != null && className.trim().length() > 0) {
				beanMethod = searchForClassMethod(className, getMethodName(propertyName));
			}
			if (beanMethod == null && (typeName != null && typeName.trim().length() > 0)) {
				beanMethod = searchForClassMethod(typeName, getMethodName(propertyName));
			}

			if (beanMethod != null) {
				IEditorPart part = JavaUI.openInEditor(beanMethod);
				if (part != null) {
					JavaUI.revealInEditor(part, beanMethod);
				}
				else {
					// could not open editor
					openFileFailed();
				}
			} else {
				openFileFailed();
			}
		} catch (CoreException x) {
			openFileFailed();
		}
	}

	private Element findElementByIDBackward (String id, int endOffset, String tagname) {
		StructuredModelWrapper smw = new StructuredModelWrapper();
		try {
			smw.init(getDocument());
			Document xmlDocument = smw.getDocument();
			if (xmlDocument == null) return null;

			Node n = Utils.findNodeForOffset(xmlDocument, endOffset);

			if (n == null) return null;
			if (n instanceof Attr) n = ((Attr)n).getOwnerElement();

			Element element = null;
			for (Node parent = n;parent != null && element == null; parent = parent.getParentNode()) {
				element = findElementByIDBackward(xmlDocument.getChildNodes(), id, endOffset, tagname); 
			}
			return element;
		} finally {
			smw.dispose();
		}
	}
	
	private Element findElementByIDBackward(NodeList list, String id, int endOffset, String tagName) {
		StructuredModelWrapper smw = new StructuredModelWrapper();
		try {
			smw.init(getDocument());
			Document xmlDocument = smw.getDocument();
			if (xmlDocument == null) return null;

			for (int i = list.getLength() - 1; list != null && i >= 0; i--) {
				if(!(list.item(i) instanceof Element)) continue;

				Element element = (Element)list.item(i);
				int start = Utils.getValueStart(element);
				if (start < 0 || start >= endOffset) continue;
				
				String elementName = element.getNodeName();
				if (tagName.equals(elementName)) {
					
					Attr idAttr = element.getAttributeNode(ID_ATTRNAME);
					if (idAttr != null) {
						String val = Utils.trimQuotes(idAttr.getNodeValue());
						if (id.equals(val)) {
							return element;
						}
					}
				}
				
				if (element.hasChildNodes()) {
					Element child = findElementByIDBackward(element.getChildNodes(), id, endOffset, tagName);
					if (child != null) return child;
				}
			}
		} finally {
			smw.dispose();
		}
		return null;
	}

	String getPropertyName(IRegion region) {
		try {
			return Utils.trimQuotes(getDocument().get(region.getOffset(), region.getLength()));
		} catch (BadLocationException x) {
			//ignore
			return null;
		}
	}

	private String getBeanId(IRegion region) {
		StructuredModelWrapper smw = new StructuredModelWrapper();
		try {
			smw.init(getDocument());
			Document xmlDocument = smw.getDocument();
			if (xmlDocument == null) return null;
			
			Node n = Utils.findNodeForOffset(xmlDocument, region.getOffset());

			if (n == null || !(n instanceof Attr)) return null;
			
			Node node = ((Attr)n).getOwnerElement();

			return getAttributeValue(node, NAME_ATTRNAME);
		} finally {
			smw.dispose();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IHyperlink#getHyperlinkText()
	 */
	public String getHyperlinkText() {
		String propertyName = getPropertyName(getHyperlinkRegion());
		if (propertyName == null)
			return  MessageFormat.format(Messages.OpenA, Messages.Getter);
		
		return MessageFormat.format(Messages.OpenGetterOrSetterForProperty, Messages.Getter, propertyName);
	}
}
