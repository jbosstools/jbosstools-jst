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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.jst.web.kb.ICSSContainerSupport;
import org.jboss.tools.jst.web.kb.IIncludedContextSupport;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.IResourceBundle;
import org.jboss.tools.jst.web.kb.PageContextFactory.CSSStyleSheetDescriptor;
import org.jboss.tools.jst.web.kb.internal.taglib.NameSpace;
import org.jboss.tools.jst.web.kb.taglib.INameSpace;
import org.jboss.tools.jst.web.kb.taglib.INameSpaceExtended;
import org.jboss.tools.jst.web.kb.taglib.ITagLibrary;

/**
 * JSP page context
 * @author Alexey Kazakov
 */
public class JspContextImpl extends XmlContextImpl implements IPageContext, IIncludedContextSupport, ICSSContainerSupport {
	protected List<IResourceBundle> bundles = new ArrayList<IResourceBundle>();

	protected List<ELContext> fIncludedContexts = new ArrayList<ELContext>();
	protected List<CSSStyleSheetDescriptor> fCSSStyleSheetDescriptors = new ArrayList<CSSStyleSheetDescriptor>();;

	public void addIncludedContext(ELContext includedContext) {
		fIncludedContexts.add(includedContext);
	}

	public List<ELContext> getIncludedContexts() {
		return fIncludedContexts;
	}

	@Override
	public Map<String, List<INameSpace>> getNameSpaces(int offset) {
		Map<String, List<INameSpace>> superNameSpaces = super.getNameSpaces(offset);
		
		List<INameSpace> fakeForHtmlNS = new ArrayList<INameSpace>();
		fakeForHtmlNS.add(new NameSpace("", "")); //$NON-NLS-1$ //$NON-NLS-2$
		superNameSpaces.put("", fakeForHtmlNS); //$NON-NLS-1$
		
		return superNameSpaces;
	}

	/*
	 * The method is commented due the following reasons:
	 * 1. https://jira.jboss.org/jira/browse/JBIDE-5753. 
	 * 2. Wrong way of gathering Var-s from the included contexts if any. There are at least two ways 
	 * of how the code may be included into the page, but anyway we cannot use offset within the page 
	 * to search for Var-s in other pages.
	 * 3. Because of ##1-2 this method should be modified in future, but for now we're excluding the included 
	 * contexts from the account. So, super method does this job well.
	 * 
	 * DO NOT uncomment this until the #2 will be solved
	 * 
	@Override
	public Var[] getVars(int offset) {
		Var[] thisVars = super.getVars(offset);
		
		List<Var> includedVars = new ArrayList<Var>();
		List<ELContext> includedContexts = getIncludedContexts();
		if (includedContexts != null) {
			for (ELContext includedContext : includedContexts) {
				if (!(includedContext instanceof IXmlContext))
					continue;
				
				Var[] vars = ((IXmlContext)includedContext).getVars(offset);
				if (vars != null) {
					for (Var b : vars) {
						includedVars.add(b);
					}
				}
			}
		}
		
		Var[] result = new Var[thisVars == null ? 0 : thisVars.length + includedVars.size()];
		if (thisVars != null && thisVars.length > 0) {
			System.arraycopy(thisVars, 0, result, 0, thisVars.length);
		}
		if (!includedVars.isEmpty()) {
			System.arraycopy(includedVars.toArray(new Var[includedVars.size()]), 0, 
					result, thisVars == null ? 0 : thisVars.length, includedVars.size());
		}
		return result;
	}
	*/
	
	public ITagLibrary[] getLibraries() {
		Set<ITagLibrary> libraries = new HashSet<ITagLibrary>();

		for (Map<String, INameSpace> nsMap : nameSpaces.values()) {
			for (INameSpace ns : nsMap.values()) {
				if (ns instanceof INameSpaceExtended) {
					ITagLibrary[] libs = ((INameSpaceExtended)ns).getTagLibraries();
					for(ITagLibrary lib : libs) {
						libraries.add(lib);
					}
				}
			}
		}

		for (ELContext includedContext : this.fIncludedContexts) {
			if (includedContext instanceof IPageContext) { 
				ITagLibrary[] includedLibraries = ((IPageContext)includedContext).getLibraries();
				for (ITagLibrary lib : includedLibraries) {
					libraries.add(lib);
				}
			}
		}
		
		return libraries.toArray(new ITagLibrary[libraries.size()]);
	}

	/**
	 * Adds resource bundle to the context
	 * 
	 * @param bundle
	 */
	public void addResourceBundle(IResourceBundle bundle) {
		bundles.add(bundle);
	}

	public IResourceBundle[] getResourceBundles() {
		Set<IResourceBundle> resourceBundles = new HashSet<IResourceBundle>();
		resourceBundles.addAll(bundles);

		for (ELContext includedContext : this.fIncludedContexts) {
			if (includedContext instanceof IPageContext) {
				for (IResourceBundle b : ((IPageContext)includedContext).getResourceBundles()) {
					resourceBundles.add(b);
				}
			}
		}

		return (IResourceBundle[])resourceBundles.toArray(new IResourceBundle[resourceBundles.size()]);
	}

	public void addCSSStyleSheetDescriptor(CSSStyleSheetDescriptor cssStyleSheetDescriptor) {
		fCSSStyleSheetDescriptors.add(cssStyleSheetDescriptor);
	}

	public List<CSSStyleSheetDescriptor> getCSSStyleSheetDescriptors() {
		List<CSSStyleSheetDescriptor> descrs = new ArrayList<CSSStyleSheetDescriptor>();
		
		descrs.addAll(fCSSStyleSheetDescriptors);
		
		for (ELContext includedContext : this.fIncludedContexts) {
			if (includedContext instanceof ICSSContainerSupport) {
				descrs.addAll(((ICSSContainerSupport)includedContext).getCSSStyleSheetDescriptors());
			}
		}
		
		return descrs;
	}	
}