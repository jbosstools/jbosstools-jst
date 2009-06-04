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
package org.jboss.tools.jst.web.kb.internal.taglib;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.taglib.ICustomTagLibrary;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Alexey Kazakov
 */
public class CustomTagLibrary extends AbstractTagLib implements ICustomTagLibrary {

	private static final String DEFAULT_PREFIX = "defaultPrefix"; //$NON-NLS-1$
	private static final String TAG_LIB = "tag-lib"; //$NON-NLS-1$
	private static final String COMPONENT = "component"; //$NON-NLS-1$
	private static final String CLOSE_TAG = "closeTag"; //$NON-NLS-1$
	private static final String TRUE = "true"; //$NON-NLS-1$
	private static final String NAME = "name"; //$NON-NLS-1$
	private static final String ATTRIBUTE = "attribute"; //$NON-NLS-1$
	private static final String REQUIRED = "required"; //$NON-NLS-1$
	private static final String PROPOSAL = "proposal"; //$NON-NLS-1$
	private static final String TYPE = "type"; //$NON-NLS-1$
	private static final String ENUMERATION = "enumeration"; //$NON-NLS-1$
	private static final String PARAM = "param"; //$NON-NLS-1$
	private static final String VALUE = "value"; //$NON-NLS-1$
	private static final String COMPONET_EXTENSION = "componentExtension"; //$NON-NLS-1$
	private static final String DEFAULT_VALUE = "defaultValue"; //$NON-NLS-1$
	private static final String EXTENDED = "extended"; //$NON-NLS-1$
	private static final String DESCRIPTION = "description"; //$NON-NLS-1$

	protected String defaultPrefix;

	public CustomTagLibrary(File file, String uri, String version, String name) {
		Document document = null;
		try {
			DocumentBuilder builder = createDocumentBuilder(false);
			document = builder.parse(file);
		} catch (SAXException e) {
			WebKbPlugin.getDefault().logError(e);
		} catch (IOException e) {
			WebKbPlugin.getDefault().logError(e);
		} catch (ParserConfigurationException e) {
			WebKbPlugin.getDefault().logError(e);
		}
		defaultPrefix = document.getDocumentElement().getAttribute(DEFAULT_PREFIX);
		Element tagLib = document.getDocumentElement();
		NodeList children = tagLib.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if(child instanceof Element) {
				if(child.getNodeName().equals(COMPONENT)) {
					parseComponent((Element)child);
				} else if (child.getNodeName().equals(COMPONET_EXTENSION)) {
					parseComponentExtension((Element)child);
				}
			}
		}
	}

	private void parseComponent(Element component) {
		String name = component.getAttribute(NAME);
		boolean closeTag = TRUE.equalsIgnoreCase(component.getAttribute(CLOSE_TAG));
		NodeList children = component.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if(child instanceof Element) {
				if(child.getNodeName().equals(ATTRIBUTE)) {
					Element attribute = (Element)child;
					String attributeName = attribute.getAttribute(NAME);
					boolean required = TRUE.equalsIgnoreCase(attribute.getAttribute(REQUIRED));
				}
			}
		}

		//TODO
	}

	private void parseComponentExtension(Element extension) {
		//TODO
	}

	private DocumentBuilder createDocumentBuilder(boolean validate) throws ParserConfigurationException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setValidating(validate);
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

		if(!validate) {
			documentBuilder.setEntityResolver(new EntityResolver() {
				public InputSource resolveEntity(java.lang.String publicId, java.lang.String systemId) throws SAXException, java.io.IOException {
					if((systemId != null) && systemId.toLowerCase().endsWith(".dtd")) { // this deactivates DTD //$NON-NLS-1$
						return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes())); //$NON-NLS-1$
					} else {
						return null;
					}
				}
			});
		}
		return documentBuilder;
	}
}