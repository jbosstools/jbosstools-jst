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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jboss.tools.common.model.project.ext.store.XMLStoreConstants;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.jst.web.kb.taglib.INameSpaceStorage;
import org.w3c.dom.Element;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NameSpaceStorage implements INameSpaceStorage {
	KbProject project;
	private Map<String, Set<String>> urisByPrefix = new HashMap<String, Set<String>>();

	public NameSpaceStorage(KbProject project) {
		this.project = project;
	}

	public synchronized void add(String prefix, String uri) {
		Set<String> uris = urisByPrefix.get(prefix);
		if(uris == null || !uris.contains(uri)) {
			//new uri, check that it exists
			if(project.getTagLibraries(uri).length == 0) {
				return;
			}
		}
		if(uris == null) {
			uris = new HashSet<String>();
			urisByPrefix.put(prefix, uris);
		}
		uris.add(uri);
	}

	public synchronized Set<String> getURIs(String prefix) {
		Set<String> result = new HashSet<String>();
		Set<String> urls = urisByPrefix.get(prefix);
		if(urls != null) {
			result.addAll(urls);
		}
		return result;
	}

	public synchronized Set<String> getPrefixes(String prefixMask) {
		Set<String> result = new HashSet<String>();
		for (String prefix: urisByPrefix.keySet()) {
			if(prefix.startsWith(prefixMask)) {
				Set<String> urls = urisByPrefix.get(prefix);
				if(!urls.isEmpty()) {
					result.add(prefix);
				}
			}
		}
		return result;
	}

	static final String ELEMENT_URIS = "uris";
	static final String ELEMENT_URI = "uri";
	static final String ELEMENT_PREFIX = "prefix";

	public synchronized void store(Element root) {
		Element urisElement = XMLUtilities.createElement(root, ELEMENT_URIS);
		Map<String, Set<String>> uris = revert();
		for (String uri: uris.keySet()) {
			Element uriElement = XMLUtilities.createElement(urisElement, ELEMENT_URI);
			uriElement.setAttribute(XMLStoreConstants.ATTR_VALUE, uri);
			Set<String> prefixes = uris.get(uri);
			for (String prefix: prefixes) {
				Element prefixElement = XMLUtilities.createElement(uriElement, ELEMENT_PREFIX);
				prefixElement.setTextContent(prefix);
			}
		}
	}

	public synchronized void load(Element root) {
		Element urisElement = XMLUtilities.getUniqueChild(root, ELEMENT_URIS);
		if(urisElement != null) {
			for (Element uriElement: XMLUtilities.getChildren(urisElement, ELEMENT_URI)) {
				String uri = uriElement.getAttribute(XMLStoreConstants.ATTR_VALUE);
				for (Element prefixElement: XMLUtilities.getChildren(uriElement, ELEMENT_PREFIX)) {
					String prefix = prefixElement.getTextContent();
					if(prefix != null && uri != null && prefix.length() > 0 && uri.length() > 0) {
						add(prefix, uri);
					}
				}
			}
		}
	}

	private Map<String, Set<String>> revert() {
		Map<String, Set<String>> result = new HashMap<String, Set<String>>();
		for (String prefix: urisByPrefix.keySet()) {
			Set<String> uris = urisByPrefix.get(prefix);
			for (String uri: uris) {
				Set<String> prefixes = result.get(uri);
				if(prefixes == null) {
					prefixes = new HashSet<String>();
					result.put(uri, prefixes);
				}
				prefixes.add(prefix);
			}
		}
		return result;
	}

}
