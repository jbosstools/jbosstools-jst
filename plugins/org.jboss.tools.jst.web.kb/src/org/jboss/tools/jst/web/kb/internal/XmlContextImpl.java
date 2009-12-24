package org.jboss.tools.jst.web.kb.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
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

public class XmlContextImpl extends ELContextImpl implements IXmlContext {
	protected IDocument document;

	// Fix for JBIDE-5097: It must be a map of <IRegion to Map of <NS-Prefix to NS>> 
	protected Map<IRegion, Map<String, INameSpace>> nameSpaces = new HashMap<IRegion, Map<String, INameSpace>>();
	
	/**
	 * Sets up the context resource and retrieves the document for the specified resource
	 */
	@Override
	public void setResource(IFile resource) {
		super.setResource(resource);
		
		FileEditorInput editorInput = null;
		try {
			editorInput = new FileEditorInput(resource);
			document = getConnectedDocument(editorInput);
		} finally {
			releaseConnectedDocument(editorInput);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.PageContext#getDocument()
	 */
	public IDocument getDocument() {
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
		if (nameSpaces.get(region) == null) {
			Map<String, INameSpace> nameSpaceMap = new HashMap<String, INameSpace>();
			nameSpaces.put(region, nameSpaceMap);
		}
		nameSpaces.get(region).put(nameSpace.getPrefix(), nameSpace); 	// Fix for JBIDE-5097
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