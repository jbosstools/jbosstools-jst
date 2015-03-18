/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.DocumentProviderRegistry;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.jboss.tools.common.el.core.resolver.ELContextImpl;
import org.jboss.tools.jst.web.kb.IXmlContext;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.jboss.tools.jst.web.kb.taglib.INameSpace;

/**
 * 
 * @author Victor V. Rubezhny
 *
 */
public class XmlContextImpl extends ELContextImpl implements IXmlContext {

	// Fix for JBIDE-5097: It must be a map of <IRegion to Map of <NS-Prefix to NS>> 
	protected Map<IRegion, Map<String, INameSpace>> nameSpaces = new HashMap<IRegion, Map<String, INameSpace>>();
	protected Set<String> uris = new HashSet<String>();

	public IDocument getDocument() {
		IDocument document = null;
		if (resource != null) {
			FileEditorInput editorInput = null;
			try {
				editorInput = new FileEditorInput(resource);
				document = getConnectedDocument(editorInput);
			} finally {
				releaseConnectedDocument(editorInput);
			}
		}
		return document;
	}
	
	/* 
	 * TODO: the visibility must differ between 'include'-like and 'template'-like inclusion
	 * 
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.IPageContext#getNameSpaces(int)
	 */
	public Map<String, List<INameSpace>> getNameSpaces(int offset) {
		Map<String, List<INameSpace>> result = new HashMap<String, List<INameSpace>>();
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
			List<INameSpace> list = result.get(ns.getURI());
			if(list==null) {
				list = new ArrayList<INameSpace>();
			}
			list.add(ns);
			result.put(ns.getURI(), list);
		}

		return result;
	}

	@Override
	public Map<String, List<INameSpace>> getRootNameSpaces() {
		int offset = Integer.MAX_VALUE;
		for (IRegion region : nameSpaces.keySet()) {
			if(offset > region.getOffset()) {
				offset = region.getOffset();
			}
		}
		return getNameSpaces(offset);
	}


	public INameSpace findNameSpaceByPrefix(Set<INameSpace> namespaces, String prefix) {
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
		Map<String, INameSpace> nameSpaceMap = nameSpaces.get(region);

		if (nameSpaceMap == null) {
			nameSpaceMap = new HashMap<String, INameSpace>();
			nameSpaces.put(region, nameSpaceMap);
		}

		nameSpaceMap.put(nameSpace.getPrefix(), nameSpace); 	// Fix for JBIDE-5097
		
		String uri = nameSpace.getURI();
		if(!uri.isEmpty()) {
			uris.add(uri);
		}
	}

	public Set<String> getURIs() {
		return uris;
	}

	private IDocument getConnectedDocument(IEditorInput input) {
		IDocumentProvider provider= DocumentProviderRegistry.getDefault().getDocumentProvider(input);
		try {
			provider.connect(input);
		} catch (CoreException e) {
			WebKbPlugin.getDefault().logError(e);
		}
		return provider.getDocument(input);
	}
	
	private void releaseConnectedDocument(IEditorInput input) {
		IDocumentProvider provider= DocumentProviderRegistry.getDefault().getDocumentProvider(input);
		provider.disconnect(input);
	}
}