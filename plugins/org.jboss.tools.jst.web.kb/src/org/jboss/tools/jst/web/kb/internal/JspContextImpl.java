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
package org.jboss.tools.jst.web.kb.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.jboss.tools.common.el.core.resolver.ELContextImpl;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.IResourceBundle;
import org.jboss.tools.jst.web.kb.taglib.INameSpace;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;

/**
 * JSP page context
 * @author Alexey Kazakov
 */
public class JspContextImpl extends ELContextImpl implements IPageContext {
	protected IDocument document;
	protected ITagLibrary[] libs;
	protected Map<IRegion, Map<String, INameSpace>> nameSpaces = new HashMap<IRegion, Map<String, INameSpace>>();
	protected IResourceBundle[] bundles;

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.IPageContext#getLibraries()
	 */
	public ITagLibrary[] getLibraries() {
		return libs;
	}

	public void setLibraries(ITagLibrary[] libs) {
		this.libs = libs;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.IPageContext#getResourceBundles()
	 */
	public IResourceBundle[] getResourceBundles() {
		return bundles;
	}

	/**
	 * Sets resource bundles
	 * @param bundles
	 */
	public void setResourceBundles(IResourceBundle[] bundles) {
		this.bundles = bundles;
	}

	/**
	 * @param document the document to set
	 */
	public void setDocument(IDocument document) {
		this.document = document;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.PageContext#getDocument()
	 */
	public IDocument getDocument() {
		return document;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.IPageContext#getNameSpaces(int)
	 */
	public Map<String, INameSpace> getNameSpaces(int offset) {
		Map<String, INameSpace> result = new HashMap<String, INameSpace>();
		Map<INameSpace, IRegion> namespaceToRegions = new HashMap<INameSpace, IRegion>();
		
		for (IRegion region : nameSpaces.keySet()) {
			if(offset>=region.getOffset() && offset<=region.getOffset() + region.getLength()) {
				Map<String, INameSpace> namespaces = nameSpaces.get(region);
				if (namespaces != null) {
					for (INameSpace ns : namespaces.values()) {
						INameSpace existingNameSpace = findNameSpaceByPrefix(namespaceToRegions.keySet(), ns.getPrefix());
						IRegion existingRegion = namespaceToRegions.get(existingNameSpace); 
						if (existingRegion != null) {
							// Perform visibility check for region
							if (region.getOffset() > existingRegion.getOffset()) {
								// Replace existingNS by this ns
								namespaceToRegions.remove(existingNameSpace);
								namespaceToRegions.put(ns, region);
							}
						} else {
							namespaceToRegions.put(ns, region);
						}
					}
				}
			}
		}

		for (INameSpace ns : namespaceToRegions.keySet()) {
			result.put(ns.getURI(), ns);
		}
	
		return result;
	}
	
	private INameSpace findNameSpaceByPrefix(Set<INameSpace> namespaces, String prefix) {
		if (namespaces != null && prefix != null) {
			for (INameSpace ns : namespaces) {
				if (prefix.equals(ns.getPrefix())) {
					return ns;
				}
			}
		}
		return null;
	}

	/**
	 * Adds new name space to the context
	 * @param region
	 * @param name space
	 */
	public void addNameSpace(IRegion region, INameSpace nameSpace) {
		if (nameSpaces.get(region) == null) {
			Map<String, INameSpace> nameSpaceMap = new HashMap<String, INameSpace>();
			nameSpaces.put(region, nameSpaceMap);
		}
		nameSpaces.get(region).put(nameSpace.getURI(), nameSpace);
	}
}