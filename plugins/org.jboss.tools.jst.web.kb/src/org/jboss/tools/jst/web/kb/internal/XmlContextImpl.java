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
import java.util.Collections;
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
	protected Set<String> uris = new HashSet<String>();

	protected class RegionNameSpaces {
		protected RegionNameSpaces parent;
		protected IRegion region;
		protected Map<String, INameSpace> nameSpacesByPrefix = new HashMap<String, INameSpace>();
		protected Map<String, List<INameSpace>> nameSpacesByUri = null;
		protected List<RegionNameSpaces> children = null;

		public void addNameSpace(IRegion region, INameSpace nameSpace) {
			if(this.region == null || this.region == region || areEqual(this.region, region)) {
				this.region = region;
				nameSpacesByPrefix.put(nameSpace.getPrefix(), nameSpace);
				return;
			} else if(children != null) {
				for (RegionNameSpaces c: children) {
					if(contains(c.region, region)) {
						c.addNameSpace(region, nameSpace);
						return;
					}
				}
			}
			if(children == null) {
				children = new ArrayList<RegionNameSpaces>();
			}
			RegionNameSpaces c = new RegionNameSpaces();
			c.parent = this;
			c.region = region;
			children.add(c);
			c.addNameSpace(region, nameSpace);
		}

		public RegionNameSpaces find(int offset) {
			if(children != null) {
				for (RegionNameSpaces c: children) {
					if(c.region.getOffset() <= offset && c.region.getOffset() + c.region.getLength() >= offset) {
						return c.find(offset);
					}
				}
			}
			if(this.region == null) {
				return this;
			} else if(region.getOffset() <= offset && region.getOffset() + region.getLength() >= offset) {
				return this;
			}
			return null;
		}

		/**
		 * Returns map where key is URI, and value is list of namespaces, prefixes
		 * being unique in the entire map. When several namespaces with the same prefix are 
		 * visible from the current region, the most inner one is used.
		 * 
		 * @return
		 */
		public Map<String, List<INameSpace>> getNameSpacesByUri() {
			if(nameSpacesByUri == null) {
				nameSpacesByUri = new HashMap<String, List<INameSpace>>();
				Set<String> prefixes = new HashSet<String>();
				RegionNameSpaces s = this;
				while(s != null) {
					for (String prefix: s.nameSpacesByPrefix.keySet()) {
						if(prefixes.contains(prefix)) continue;
						INameSpace n = s.nameSpacesByPrefix.get(prefix);
						prefixes.add(prefix);
						String uri = n.getURI();
						List<INameSpace> list = nameSpacesByUri.get(uri);
						if(list == null) {
							list = new ArrayList<INameSpace>();
							nameSpacesByUri.put(uri, list);
						}
						list.add(n);
					}
					s = s.parent;
				}
				modifyNameSpacesByUri(this);
			}
			return nameSpacesByUri;
		}
		
	}

	boolean areEqual(IRegion r1, IRegion r2) {
		return r1.getOffset() == r2.getOffset() && r1.getLength() == r2.getLength();
	}

	boolean contains(IRegion r1, IRegion r2) {
		return r2.getOffset() >= r1.getOffset() && r1.getOffset() + r1.getLength() >= r2.getOffset() + r2.getLength();
	}

	/**
	 * Overriden by jsp context to add global namespace with empty prefix.
	 * @param s
	 */
	protected void modifyNameSpacesByUri(RegionNameSpaces s) {		
	}

	RegionNameSpaces root = new RegionNameSpaces();

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

	static final Map<String, List<INameSpace>> EMPTY_NAME_SPACES = Collections.emptyMap();

	/* 
	 * TODO: the visibility must differ between 'include'-like and 'template'-like inclusion
	 * 
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.IPageContext#getNameSpaces(int)
	 */
	public Map<String, List<INameSpace>> getNameSpaces(int offset) {
		RegionNameSpaces n = root.find(offset);
		return (n != null) ? n.getNameSpacesByUri() : EMPTY_NAME_SPACES;
	}

	@Override
	public Map<String, List<INameSpace>> getRootNameSpaces() {
		return root.getNameSpacesByUri();
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
		root.addNameSpace(region, nameSpace);

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