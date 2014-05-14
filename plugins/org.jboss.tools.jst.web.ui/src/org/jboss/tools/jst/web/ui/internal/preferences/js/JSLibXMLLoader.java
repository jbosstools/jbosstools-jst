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
package org.jboss.tools.jst.web.ui.internal.preferences.js;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import org.jboss.tools.common.xml.XMLUtilities;
import org.w3c.dom.Element;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class JSLibXMLLoader {
	static String TAG_LIBS = "libs";
	static String TAG_LIB = "lib";
	static String TAG_VERSION = "version";
	static String ATTR_NAME = "name";
	static String TAG_URL = "url";

	private  JSLibXMLLoader() {}

	public static JSLibModel load(String s) {
		StringReader reader = new StringReader(s);
		Element root = XMLUtilities.getElement(reader, XMLUtilities.EMPTY_RESOLVER);
		return root != null ? load(root) : null;
	}

	public static JSLibModel load(Element root) {
		JSLibModel result = new JSLibModel();
		Element[] libElements = XMLUtilities.getChildren(root, TAG_LIB);
		for (Element libElement: libElements) {
			JSLib lib = result.getOrCreateLib(libElement.getAttribute(ATTR_NAME));
			Element[] versionElements = XMLUtilities.getChildren(libElement, TAG_VERSION);
			for (Element versionElement: versionElements) {
				JSLibVersion version = lib.getOrCreateVersion(versionElement.getAttribute(ATTR_NAME));
				Element[] urlElements = XMLUtilities.getChildren(versionElement, TAG_URL);
				for (Element urlElement: urlElements) {
					version.getURLs().add(XMLUtilities.getCDATA(urlElement, true));
				}
			}
		}
		return result;
	}

	public static String saveToString(JSLibModel model) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		try {
			XMLUtilities.serialize(save(model), b);
		} catch (IOException e) {
			//never happens with ByteArrayOutputStream
		}
		return b.toString();
	}

	public static Element save(JSLibModel model) {
		synchronized (model) {
			Element libsElement = XMLUtilities.createDocumentElement(TAG_LIBS);
			for (JSLib lib: model.getSortedLibs()) {
				Element libElement = XMLUtilities.createElement(libsElement, TAG_LIB);
				libElement.setAttribute(ATTR_NAME, lib.getName());
				for (JSLibVersion version: lib.getSortedVersions()) {
					Element versionElement = XMLUtilities.createElement(libElement, TAG_VERSION);
					versionElement.setAttribute(ATTR_NAME, version.getVersion());
					for (String url: version.getSortedUrls()) {
						Element urlElement = XMLUtilities.createElement(versionElement, TAG_URL);
						XMLUtilities.setText(urlElement, url);
					}
				}
			}
			return libsElement;
		}
	}

}
